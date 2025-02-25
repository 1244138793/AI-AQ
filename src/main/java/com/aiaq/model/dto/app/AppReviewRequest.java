package com.aiaq.model.dto.app;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/12 16:53
 * @Description 应用审核请求
 */
@Data
public class AppReviewRequest {

    /**
     * 应用 id
     */
    @NotNull(message = "应用 id 不能为空")
    private Long id;

    /**
     * 审核状态：0-待审核, 1-通过, 2-拒绝
     */
    @NotNull(message = "审核状态不能为空")
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    @Length(max = 500, message = "审核信息长度不能超过500个字符")
    private String reviewMessage;
}
