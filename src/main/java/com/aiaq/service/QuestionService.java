package com.aiaq.service;

import com.aiaq.model.dto.question.AiGenerateQuestionRequest;
import com.aiaq.model.dto.question.QuestionAddRequest;
import com.aiaq.model.dto.question.QuestionContent;
import com.aiaq.model.dto.question.QuestionEditRequest;
import com.aiaq.model.entity.Question;
import com.aiaq.model.vo.QuestionVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 最紧要开心
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2024-09-12 17:07:22
*/
public interface QuestionService extends IService<Question> {

    /**
     * 添加题目
     * @param questionAddRequest 题目添加请求
     * @return 题目ID
     */
    Long addQuestion(QuestionAddRequest questionAddRequest, HttpServletRequest request);

    /**
     * 编辑题目
     * @param questionEditRequest 题目编辑请求
     */
    void editQuestion(QuestionEditRequest questionEditRequest, HttpServletRequest request);

    /**
     * 删除题目
     * @param questionId 题目ID
     */
    void deleteQuestion(Long questionId, HttpServletRequest request);

    /**
     * 查看题目详情
     * @param appId 应用
     * @return 题目详情
     */
    QuestionVO detailQuestion(Long appId, HttpServletRequest request);

    /**
     * AI生成题目
     * @param aiGenerateQuestionRequest AI生成题目请求
     * @return 题目列表
     */
    List<QuestionContent> aiGenerateQuestion(AiGenerateQuestionRequest aiGenerateQuestionRequest);

    SseEmitter aiGenerateQuestionBySSE(AiGenerateQuestionRequest aiGenerateQuestionRequest);
}
