package com.aiaq.model.dto.app;

import com.aiaq.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/11 17:23
 * @Description 应用查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppQueryRequest extends PageRequest {

    /**
     * 应用名
     */
    private String appName;

    /**
     * 应用类型（0-得分类，1-测评类）
     */
    private List<Integer> appType;

    /**
     * 评分策略（0-自定义，1-AI）
     */
    private List<Integer> scoringStrategy;

    /**
     * 审核状态：0-待审核, 1-通过, 2-拒绝
     */
    private List<Integer> reviewStatus;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 创建开始时间
     */
    private String createBeginTime;

    /**
     * 创建结束时间
     */
    private String createEndTime;
}
