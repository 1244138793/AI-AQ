package com.aiaq.controller;

import cn.hutool.json.JSONUtil;
import com.aiaq.common.BaseResponse;
import com.aiaq.common.ResultUtils;
import com.aiaq.model.dto.question.AiGenerateQuestionRequest;
import com.aiaq.model.dto.question.QuestionAddRequest;
import com.aiaq.model.dto.question.QuestionContent;
import com.aiaq.model.dto.question.QuestionEditRequest;
import com.aiaq.model.vo.QuestionVO;
import com.aiaq.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/12 17:11
 * @Description 题目控制器
 */
@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {

    @Resource
    private QuestionService questionService;

    /**
     * 添加题目
     * @param questionAddRequest 题目添加请求
     * @return 题目 ID
     */
    @PostMapping("/add")
    private BaseResponse<Long> addQuestion(@Valid @RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        log.info("添加题目入参={}", JSONUtil.toJsonStr(questionAddRequest));
        return ResultUtils.success(questionService.addQuestion(questionAddRequest, request));
    }

    /**
     * 编辑题目
     * @param questionEditRequest 题目编辑请求
     * @return 是否编辑成功
     */
    @PostMapping("/edit")
    private BaseResponse<Boolean> editQuestion(@Valid @RequestBody QuestionEditRequest questionEditRequest, HttpServletRequest request) {
        log.info("编辑题目入参={}", JSONUtil.toJsonStr(questionEditRequest));
        questionService.editQuestion(questionEditRequest, request);
        return ResultUtils.success();
    }

    /**
     * 删除题目
     * @param questionId 题目 ID
     * @return 是否删除成功
     */
    @GetMapping("/delete/{questionId}")
    private BaseResponse<Boolean> deleteQuestion(@PathVariable("questionId") Long questionId, HttpServletRequest request) {
        log.info("删除题目入参={}", questionId);
        questionService.deleteQuestion(questionId, request);
        return ResultUtils.success();
    }

    /**
     * 查看题目详情
     * @param appId 应用 ID
     * @return 题目详情
     */
    @GetMapping("/detail/{appId}")
    private BaseResponse<QuestionVO> detailQuestion(@PathVariable("appId") Long appId, HttpServletRequest request) {
        log.info("查询题目详情入参={}", appId);
        return ResultUtils.success(questionService.detailQuestion(appId, request));
    }

    /**
     * AI生成题目
     * @param aiGenerateQuestionRequest AI生成题目请求
     * @return 题目列表
     */
    @PostMapping("/ai_generate")
    public BaseResponse<List<QuestionContent>> aiGenerateQuestion(@RequestBody AiGenerateQuestionRequest aiGenerateQuestionRequest) {
        log.info("AI生成题目入参={}", JSONUtil.toJsonStr(aiGenerateQuestionRequest));
        return ResultUtils.success(questionService.aiGenerateQuestion(aiGenerateQuestionRequest));
    }

    @GetMapping("/ai_generate/sse")
    public SseEmitter aiGenerateQuestionSSE(AiGenerateQuestionRequest aiGenerateQuestionRequest) {
        log.info("AI生成题目SSE入参={}", JSONUtil.toJsonStr(aiGenerateQuestionRequest));
        return questionService.aiGenerateQuestionBySSE(aiGenerateQuestionRequest);
    }
}
