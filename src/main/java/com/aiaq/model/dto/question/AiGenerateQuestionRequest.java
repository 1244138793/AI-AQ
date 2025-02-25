package com.aiaq.model.dto.question;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/30 18:40
 * @Description AI生成题目请求
 */
@Data
public class AiGenerateQuestionRequest {
    /**
     * 应用 id
     */
    @NotNull(message = "应用id不能为空")
    private Long appId;

    /**
     * 题目数
     */
    int questionNumber = 10;

    /**
     * 选项数
     */
    int optionNumber = 2;
}
