package com.aiaq.model.vo;

import com.aiaq.model.dto.question.QuestionContent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/15 10:51
 * @Description 问题视图
 */
@Data
public class QuestionVO {
    /**
     * id
     */
    private Long id;

    /**
     * 题目内容（json格式）
     */
    @JsonIgnore
    private String questionContentStr;

    /**
     * 题目内容（json格式）
     */
    private List<QuestionContent> questionContent;

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
