package com.aiaq.model.dto.answer;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/27 16:39
 * @Description 用户答题记录添加请求
 */
@Data
public class UserAnswerAddRequest {

    /**
     * 应用 id
     */
    @NotNull(message = "应用id不能为空")
    private Long appId;

    /**
     * 用户答案（JSON 数组）
     */
    @NotEmpty(message = "用户答案不能为空")
    private List<String> choices;
}
