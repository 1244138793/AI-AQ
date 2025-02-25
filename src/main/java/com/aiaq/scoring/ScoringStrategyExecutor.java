package com.aiaq.scoring;

import com.aiaq.common.ErrorCode;
import com.aiaq.exception.BusinessException;
import com.aiaq.model.entity.App;
import com.aiaq.model.entity.UserAnswer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/28 19:06
 * @Description 评分策略执行器
 */
@Component
public class ScoringStrategyExecutor {

    @Resource
    private List<ScoringStrategy> scoringStrategies;

    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        Integer appType = app.getAppType();
        Integer appScoringStrategy = app.getScoringStrategy();
        if (appType == null || appScoringStrategy == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用配置有误，未找到匹配的策略");
        }
        for (ScoringStrategy scoringStrategy : scoringStrategies) {
            if (scoringStrategy.getClass().isAnnotationPresent(ScoringStrategyConfig.class)) {
                ScoringStrategyConfig scoringStrategyConfig = scoringStrategy.getClass().getAnnotation(ScoringStrategyConfig.class);
                if (scoringStrategyConfig.appType() == appType && scoringStrategyConfig.scoringStrategy() == appScoringStrategy) {
                    return scoringStrategy.doScore(choices, app);
                }
            }
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用配置有误，未找到匹配的策略");
    }
}
