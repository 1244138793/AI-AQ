package com.aiaq.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/27 17:08
 * @Description 用户答题记录视图对象
 */
@Data
public class UserAnswerVO {

    /**
     *
     */
    private Long id;

    /**
     * 应用 id
     */
    private Long appId;

    @JsonIgnore
    private Integer appTypeCode;
    /**
     * 应用类型（0-得分类，1-角色测评类）
     */
    private String appType;

    @JsonIgnore
    private Integer scoringStrategyCode;
    /**
     * 评分策略（0-自定义，1-AI）
     */
    private String scoringStrategy;

    /**
     * 用户答案（JSON 数组）
     */
    private String choices;

    /**
     * 结果名称，如物流师
     */
    private String resultName;

    /**
     * 结果描述
     */
    private String resultDesc;

    /**
     * 得分
     */
    private Integer resultScore;

    /**
     * 创建时间
     */
    private String createTime;

    private Long userId;
}
