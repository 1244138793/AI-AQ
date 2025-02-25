package com.aiaq.scoring;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/28 18:18
 * @Description 评分策略配置
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ScoringStrategyConfig {

    /**
     * 应用类型
     */
    int appType();

    /**
     * 评分策略
     */
    int scoringStrategy();
}
