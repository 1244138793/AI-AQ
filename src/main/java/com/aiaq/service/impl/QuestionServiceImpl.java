package com.aiaq.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aiaq.common.ErrorCode;
import com.aiaq.constant.AiPromptConstant;
import com.aiaq.constant.CommonConstant;
import com.aiaq.exception.BusinessException;
import com.aiaq.exception.ThrowUtils;
import com.aiaq.manager.AiManager;
import com.aiaq.mapper.QuestionMapper;
import com.aiaq.model.dto.question.AiGenerateQuestionRequest;
import com.aiaq.model.dto.question.QuestionAddRequest;
import com.aiaq.model.dto.question.QuestionContent;
import com.aiaq.model.dto.question.QuestionEditRequest;
import com.aiaq.model.entity.App;
import com.aiaq.model.entity.Question;
import com.aiaq.model.entity.User;
import com.aiaq.model.enums.AppTypeEnum;
import com.aiaq.model.enums.ReviewStatusEnum;
import com.aiaq.model.vo.QuestionVO;
import com.aiaq.service.AppService;
import com.aiaq.service.QuestionService;
import com.aiaq.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhipu.oapi.service.v4.model.ModelData;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
* @author 最紧要开心
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2024-09-12 17:07:22
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService {

    @Resource
    private UserService userService;

    @Resource
    private AppService appService;

    @Resource
    private AiManager aiManager;

    @Override
    public Long addQuestion(QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        // 应用是否存在
        ThrowUtils.throwIf(appService.getById(questionAddRequest.getAppId()) == null, ErrorCode.NOT_FOUND_ERROR);
        // 检查是否已有题目
        this.checkHasQuestion(questionAddRequest.getAppId());
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        // 创建题目实体
        Question question = new Question();
        question.setQuestionContent(JSONUtil.toJsonStr(questionAddRequest.getQuestionContents()));
        question.setAppId(questionAddRequest.getAppId());
        question.setUserId(loginUser.getId());
        // 保存题目
        boolean result = this.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return question.getId();
    }

    @Override
    public void editQuestion(QuestionEditRequest questionEditRequest, HttpServletRequest request) {
        // 题目是否存在
        Question question = this.getById(questionEditRequest.getId());
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        // 创建人或者管理员才能编辑
        if (!loginUser.getId().equals(question.getUserId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 更新题目
        question.setQuestionContent(JSONUtil.toJsonStr(questionEditRequest.getQuestionContents()));
        boolean result = this.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }

    @Override
    public void deleteQuestion(Long questionId, HttpServletRequest request) {
        // 题目是否存在
        Question question = this.getById(questionId);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        // 创建人或者管理员才能删除
        if (!loginUser.getId().equals(question.getUserId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 删除题目
        boolean result = this.removeById(questionId);
    }

    @Override
    public QuestionVO detailQuestion(Long appId, HttpServletRequest request) {
        // 题目是否存在
        Question question = this.lambdaQuery().eq(Question::getAppId, appId).eq(Question::getIsDelete, CommonConstant.NOT_DELETED).one();
        if (question == null) {
            return null;
        }
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        // 应用是否过审
        App app = appService.getById(question.getAppId());
        if (!app.getReviewStatus().equals(ReviewStatusEnum.PASS.getValue()) && !loginUser.getId().equals(question.getUserId())
                && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 返回题目详情
        QuestionVO questionVO = this.baseMapper.detailQuestion(question.getId());
        questionVO.setQuestionContent(JSONUtil.toBean(questionVO.getQuestionContentStr(), new TypeReference<List<QuestionContent>>() {}, false));
        return questionVO;
    }

    @Override
    public List<QuestionContent> aiGenerateQuestion(AiGenerateQuestionRequest aiGenerateQuestionRequest) {
        // 获取参数
        Long appId = aiGenerateQuestionRequest.getAppId();
        int questionNumber = aiGenerateQuestionRequest.getQuestionNumber();
        int optionNumber = aiGenerateQuestionRequest.getOptionNumber();
        // 获取应用信息
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        // 封装 Prompt
        String userMessage = getGenerateQuestionUserMessage(app, questionNumber, optionNumber);
        // AI 生成
        String result = aiManager.doSyncUnstableRequest(AiPromptConstant.GENERATE_QUESTION_PROMPT, userMessage);
        // 截取需要的 JSON 信息
        int start = result.indexOf("[");
        int end = result.lastIndexOf("]");
        String json = result.substring(start, end + 1);
        List<QuestionContent> questionContents = JSONUtil.toList(json, QuestionContent.class);
        return questionContents;
    }

    @Override
    public SseEmitter aiGenerateQuestionBySSE(AiGenerateQuestionRequest aiGenerateQuestionRequest) {
        ThrowUtils.throwIf(aiGenerateQuestionRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取参数
        Long appId = aiGenerateQuestionRequest.getAppId();
        int questionNumber = aiGenerateQuestionRequest.getQuestionNumber();
        int optionNumber = aiGenerateQuestionRequest.getOptionNumber();
        // 获取应用信息
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        // 封装 Prompt
        String userMessage = getGenerateQuestionUserMessage(app, questionNumber, optionNumber);
        // 建立 SSE 连接对象，0 表示永不超时
        SseEmitter sseEmitter = new SseEmitter(0L);
        // AI 生成，SSE 流式返回
        Flowable<ModelData> modelDataFlowable = aiManager.doStreamRequest(AiPromptConstant.GENERATE_QUESTION_PROMPT, userMessage, null);
        // 左括号计数器，除了默认值外，当回归为 0 时，表示左括号等于右括号，可以截取
        AtomicInteger counter = new AtomicInteger(0);
        // 拼接完整题目
        StringBuilder stringBuilder = new StringBuilder();

        // 默认全局线程池
        Scheduler scheduler = Schedulers.io();
        modelDataFlowable
                .observeOn(scheduler)
                .map(modelData -> modelData.getChoices().get(0).getDelta().getContent())
                .map(message -> message.replaceAll("\\s", ""))
                .filter(StrUtil::isNotBlank)
                .flatMap(message -> {
                    List<Character> characterList = new ArrayList<>();
                    for (char c : message.toCharArray()) {
                        characterList.add(c);
                    }
                    return Flowable.fromIterable(characterList);
                })
                .doOnNext(c -> {
                    // 如果是 '{'，计数器 + 1
                    if (c == '{') {
                        counter.addAndGet(1);
                    }
                    if (counter.get() > 0) {
                        stringBuilder.append(c);
                    }
                    if (c == '}') {
                        counter.addAndGet(-1);
                        if (counter.get() == 0) {
                            // 可以拼接题目，并且通过 SSE 返回给前端
                            sseEmitter.send(JSONUtil.toJsonStr(stringBuilder.toString()));
                            // 重置，准备拼接下一道题
                            stringBuilder.setLength(0);
                        }
                    }
                })
                .doOnError((e) -> log.error("sse error", e))
                .doOnComplete(sseEmitter::complete)
                .subscribe();
        return sseEmitter;
    }

    /**
     * 检查应用是否已有题目
     * @param appId 应用ID
     */
    private void checkHasQuestion(Long appId) {
        Long count = this.lambdaQuery()
                .eq(Question::getAppId, appId)
                .eq(Question::getIsDelete, CommonConstant.NOT_DELETED)
                .count();
        ThrowUtils.throwIf(count > 0, ErrorCode.OPERATION_ERROR, "该应用已有题目");
    }

    /**
     * 生成题目的用户消息
     * @param app 应用
     * @param questionNumber 题目数量
     * @param optionNumber 选项数量
     * @return 用户消息
     */
    private String getGenerateQuestionUserMessage(App app, int questionNumber, int optionNumber) {
        AppTypeEnum appTypeEnum = AppTypeEnum.getEnumByValue(app.getAppType());
        ThrowUtils.throwIf(appTypeEnum == null, ErrorCode.NOT_FOUND_ERROR, "应用类型不存在");
        StringBuilder userMessage = new StringBuilder();
        userMessage.append(app.getAppName()).append("\n");
        userMessage.append(app.getAppDesc()).append("\n");
        userMessage.append(appTypeEnum.getText()).append("类").append("\n");
        userMessage.append(questionNumber).append("\n");
        userMessage.append(optionNumber);
        return userMessage.toString();
    }
}
