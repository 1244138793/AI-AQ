package com.aiaq.model.dto.question;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/12 17:36
 * @Description 题目添加请求
 */
@Data
public class QuestionAddRequest {

    /**
     * 应用id
     */
    @NotNull(message = "应用id不能为空")
    private Long appId;

    /**
     * 题目
     */
    @NotNull(message = "题目不能为空")
    @Valid
    private List<QuestionContent> questionContents;
}
