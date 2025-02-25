package com.aiaq.model.dto.app;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/11 11:59
 * @Description 应用更新请求
 */
@Data
public class AppUpdateRequest {

    /**
     * 应用id
     */
    @NotNull(message = "应用id不能为空")
    private Long id;

    /**
     * 应用名
     */
    @NotBlank(message = "应用名不能为空")
    @Length(max = 100, message = "应用名长度不能超过100个字符")
    private String appName;

    /**
     * 应用描述
     */
    @Length(max = 2000, message = "应用描述长度不能超过2000个字符")
    private String appDesc;

    /**
     * 应用图标
     */
    private String appIcon;

    /**
     * 应用类型（0-得分类，1-测评类）
     */
    @NotNull(message = "应用类型不能为空")
    private Integer appType;

    /**
     * 评分策略（0-自定义，1-AI）
     */
    @NotNull(message = "评分策略不能为空")
    private Integer scoringStrategy;
}
