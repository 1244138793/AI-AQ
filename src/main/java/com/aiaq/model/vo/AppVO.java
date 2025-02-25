package com.aiaq.model.vo;

import lombok.Data;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/11 16:42
 * @Description 应用试图
 */
@Data
public class AppVO {

    /**
     * 应用id
     */
    private Long id;

    /**
     * 应用名
     */
    private String appName;

    /**
     * 应用描述
     */
    private String appDesc;

    /**
     * 应用类型（0-得分类，1-测评类）
     */
    private Integer appType;

    /**
     * 应用类型名称
     */
    private String appTypeName;

    /**
     * 评分策略（0-自定义，1-AI）
     */
    private Integer scoringStrategy;

    /**
     * 评分策略名称
     */
    private String scoringStrategyName;

    /**
     * 审核状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核状态名称
     */
    private String reviewStatusName;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 创建时间
     */
    private String createTime;

    private String reviewMessage;

    private String reviewerId;

    private String reviewTime;

    private String userId;
}
