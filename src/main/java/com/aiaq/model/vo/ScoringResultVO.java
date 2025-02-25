package com.aiaq.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/29 11:49
 * @Description 评分结果详情视图
 */
@Data
public class ScoringResultVO {

    /**
     * id
     */
    private Long id;

    /**
     * 结果名称，如物流师
     */
    private String resultName;

    /**
     * 结果描述
     */
    private String resultDesc;

    /**
     * 结果属性集合 JSON
     */
    private String resultPropStr;

    /**
     * 结果属性集合
     */
    private List<String> resultProp;

    /**
     * 结果得分范围
     */
    private Integer resultScoreRange;

    /**
     * 应用 id
     */
    private Long appId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建用户名称
     */
    private String userName;

    /**
     * 创建时间
     */
    private String createTime;
}
