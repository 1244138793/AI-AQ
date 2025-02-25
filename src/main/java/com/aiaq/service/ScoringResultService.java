package com.aiaq.service;

import com.aiaq.common.PageResult;
import com.aiaq.model.dto.scoringResult.ScoringResultAddRequest;
import com.aiaq.model.dto.scoringResult.ScoringResultQueryRequest;
import com.aiaq.model.dto.scoringResult.ScoringResultUpdateRequest;
import com.aiaq.model.entity.ScoringResult;
import com.aiaq.model.vo.ScoringResultDetailVO;
import com.aiaq.model.vo.ScoringResultVO;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 最紧要开心
* @description 针对表【scoring_result(评分结果)】的数据库操作Service
* @createDate 2024-09-28 18:55:28
*/
public interface ScoringResultService extends IService<ScoringResult> {

    /**
     * 添加评分结果
     * @param scoringResultAddRequest 评分结果添加请求
     * @return 评分结果ID
     */
    Long addScoringResult(ScoringResultAddRequest scoringResultAddRequest, HttpServletRequest request);

    /**
     * 删除评分结果
     * @param scoringResultId 评分结果ID
     */
    void deleteScoringResult(Long scoringResultId, HttpServletRequest request);

    /**
     * 更新评分结果
     * @param scoringResultUpdateRequest 评分结果更新请求
     */
    void updateScoringResult(ScoringResultUpdateRequest scoringResultUpdateRequest);

    /**
     * 评分结果详情
     * @param scoringResultId 评分结果ID
     * @return 评分结果详情
     */
    ScoringResultDetailVO detailScoringResult(Long scoringResultId, HttpServletRequest request);

    /**
     * 分页查询评分结果
     * @param scoringResultQueryRequest 评分结果查询请求
     * @return 评分结果列表
     */
    PageResult<ScoringResultVO> listScoringResult(ScoringResultQueryRequest scoringResultQueryRequest, HttpServletRequest request);
}
