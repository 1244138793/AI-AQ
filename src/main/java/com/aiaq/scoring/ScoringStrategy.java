package com.aiaq.scoring;

import com.aiaq.model.entity.App;
import com.aiaq.model.entity.UserAnswer;

import java.util.List;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/28 18:12
 * @Description 评分策略接口
 */
public interface ScoringStrategy {

    /**
     * 执行评分
     * @param choices 用户答案
     * @param app 答题应用
     * @return 用户答题记录
     */
    UserAnswer doScore(List<String> choices, App app) throws Exception;
}
