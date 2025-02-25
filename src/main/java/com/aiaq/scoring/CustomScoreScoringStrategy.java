package com.aiaq.scoring;

import com.aiaq.common.ErrorCode;
import com.aiaq.exception.BusinessException;
import com.aiaq.exception.ThrowUtils;
import com.aiaq.model.dto.question.QuestionContent;
import com.aiaq.model.entity.App;
import com.aiaq.model.entity.Question;
import com.aiaq.model.entity.ScoringResult;
import com.aiaq.model.entity.UserAnswer;
import com.aiaq.model.vo.QuestionVO;
import com.aiaq.service.QuestionService;
import com.aiaq.service.ScoringResultService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/28 18:24
 * @Description 自定义打分类应用评分策略
 */
@ScoringStrategyConfig(appType = 0, scoringStrategy = 0)
public class CustomScoreScoringStrategy implements ScoringStrategy {

    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        // 获取题目
        Question question = questionService.getOne(Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, app.getId()));
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        ThrowUtils.throwIf(sra == null, ErrorCode.SYSTEM_ERROR, "无法获取请求");
        HttpServletRequest request = sra.getRequest();
        QuestionVO questionVO = questionService.detailQuestion(question.getAppId(), request);
        List<QuestionContent> questionContent = questionVO.getQuestionContent();
        // 获取得分
        int totalScore = 0;
        if (choices.size() != questionContent.size()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案数量与题目数量不匹配");
        }

        for (int i = 0; i < questionContent.size(); i++) {
            QuestionContent questionCon = questionContent.get(i);
            String choice = choices.get(i);
            // 遍历题目中的选项
            for (QuestionContent.Option option : questionCon.getOptions()) {
                // 如果答案和选项的key匹配
                if (option.getKey().equals(choice)) {
                    int score = Optional.of(option.getScore()).orElse(0);
                    totalScore += score;
                }
            }
        }
        // 获取得分结果
        List<ScoringResult> scoringResultList = scoringResultService.list(Wrappers.lambdaQuery(ScoringResult.class)
                .eq(ScoringResult::getAppId, app.getId())
                .orderByDesc(ScoringResult::getResultScoreRange));
        ScoringResult result = null;
        for (ScoringResult scoringResult : scoringResultList) {
            if (totalScore >= scoringResult.getResultScoreRange()) {
                result = scoringResult;
                break;
            }
        }
        ThrowUtils.throwIf(result == null, ErrorCode.NOT_FOUND_ERROR);
        // 更新用户答题记录信息
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(app.getId());
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(0);
        userAnswer.setResultId(result.getId());
        userAnswer.setResultName(result.getResultName());
        userAnswer.setResultDesc(result.getResultDesc());
        userAnswer.setResultScore(totalScore);
        return userAnswer;
    }
}
