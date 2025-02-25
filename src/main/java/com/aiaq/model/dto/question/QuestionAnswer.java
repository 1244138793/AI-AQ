package com.aiaq.model.dto.question;

import lombok.Data;

/**
 * 题目答案封装类（用于 AI 评分）
 */
@Data
public class QuestionAnswer {

    /**
     * 题目
     */
    private QuestionContent title;

    /**
     * 用户答案
     */
    private String userAnswer;
}
