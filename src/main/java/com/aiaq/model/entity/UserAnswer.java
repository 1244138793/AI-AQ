package com.aiaq.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户答题记录
 * @TableName user_answer
 */
@TableName(value ="user_answer")
@Data
public class UserAnswer implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应用 id
     */
    @TableField(value = "appId")
    private Long appId;

    /**
     * 应用类型（0-得分类，1-角色测评类）
     */
    @TableField(value = "appType")
    private Integer appType;

    /**
     * 评分策略（0-自定义，1-AI）
     */
    @TableField(value = "scoringStrategy")
    private Integer scoringStrategy;

    /**
     * 用户答案（JSON 数组）
     */
    @TableField(value = "choices")
    private String choices;

    /**
     * 评分结果 id
     */
    @TableField(value = "resultId")
    private Long resultId;

    /**
     * 结果名称，如物流师
     */
    @TableField(value = "resultName")
    private String resultName;

    /**
     * 结果描述
     */
    @TableField(value = "resultDesc")
    private String resultDesc;

    /**
     * 结果图标
     */
    @TableField(value = "resultPicture")
    private String resultPicture;

    /**
     * 得分
     */
    @TableField(value = "resultScore")
    private Integer resultScore;

    /**
     * 用户 id
     */
    @TableField(value = "userId")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "isDelete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String userName;
}