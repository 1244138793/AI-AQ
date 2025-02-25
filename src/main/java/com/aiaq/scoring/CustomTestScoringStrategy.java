package com.aiaq.scoring;

import cn.hutool.json.JSONUtil;
import com.aiaq.common.ErrorCode;
import com.aiaq.exception.ThrowUtils;
import com.aiaq.model.dto.question.QuestionContent;
import com.aiaq.model.entity.App;
import com.aiaq.model.entity.Question;
import com.aiaq.model.entity.ScoringResult;
import com.aiaq.model.entity.UserAnswer;
import com.aiaq.model.vo.QuestionVO;
import com.aiaq.service.QuestionService;
import com.aiaq.service.ScoringResultService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/30 12:27
 * @Description 自定义测评类评分策略
 */
@ScoringStrategyConfig(appType = 1, scoringStrategy = 0)
public class CustomTestScoringStrategy implements ScoringStrategy {

    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        // 获取题目
        Question question = questionService.getOne(Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, app.getId()));
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        ThrowUtils.throwIf(sra == null, ErrorCode.SYSTEM_ERROR, "无法获取请求");
        HttpServletRequest request = sra.getRequest();
        QuestionVO questionVO = questionService.detailQuestion(question.getId(), request);
        List<QuestionContent> questionContent = questionVO.getQuestionContent();

        // 2. 统计用户每个选择对应的属性个数，如 I = 10 个，E = 5 个
        Map<String, Integer> optionCount = new HashMap<>();
        for (QuestionContent content : questionContent) {
            for (String answer : choices) {
                for (QuestionContent.Option option : content.getOptions()) {
                    // 如果答案和选项的key匹配
                    if (option.getKey().equals(answer)) {
                        String result = option.getResult();
                        // 在optionCount中增加计数
                        if (!optionCount.containsKey(result)) {
                            optionCount.put(result, 0);
                        }
                        optionCount.put(result, optionCount.get(result) + 1);
                    }
                }
            }
        }
        // 3. 遍历每种评分结果，获取最高评分对应的结果
        int maxScore = 0;
        List<ScoringResult> scoringResultList = scoringResultService.list(Wrappers.lambdaQuery(ScoringResult.class)
                .eq(ScoringResult::getAppId, app.getId())
                .orderByDesc(ScoringResult::getResultScoreRange));
        ScoringResult maxScoringResult = null;

        // 遍历评分结果列表
        for (ScoringResult scoringResult : scoringResultList) {
            List<String> resultProp = JSONUtil.toList(scoringResult.getResultProp(), String.class);
            // 计算当前评分结果的分数
            int score = resultProp.stream()
                    .mapToInt(prop -> optionCount.getOrDefault(prop, 0))
                    .sum();
            // 如果分数高于当前最高分数，更新最高分数和最高分数对应的评分结果
            if (score > maxScore) {
                maxScore = score;
                maxScoringResult = scoringResult;
            }
        }
        ThrowUtils.throwIf(maxScoringResult == null, ErrorCode.NOT_FOUND_ERROR);
        // 更新用户答题记录信息
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(app.getId());
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(0);
        userAnswer.setResultId(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultScore(maxScoringResult.getResultScoreRange());
        return userAnswer;
    }
}
