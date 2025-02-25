package com.aiaq.model.dto.question;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/15 10:12
 * @Description 题目编辑请求
 */
@Data
public class QuestionEditRequest {

    /**
     * 题目 ID
     */
    @NotNull(message = "题目ID不能为空")
    private Long id;

    /**
     * 题目
     */
    @NotNull(message = "题目不能为空")
    @Valid
    private List<QuestionContent> questionContents;
}
