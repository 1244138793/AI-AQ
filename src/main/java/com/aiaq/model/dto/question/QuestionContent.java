package com.aiaq.model.dto.question;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/12 17:15
 * @Description 题目内容
 */
@Data
public class QuestionContent {

    /**
     * 问题
     */
    @NotBlank(message = "问题不能为空")
    private String title;

    /**
     * 选项
     */
    @NotEmpty(message = "选项不能为空")
    private List<Option> options;

    @Data
    public static class Option {
        private String key;
        private String value;
        private int score;
        private String result;
    }
}
