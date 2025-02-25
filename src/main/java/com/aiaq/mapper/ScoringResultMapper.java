package com.aiaq.mapper;

import com.aiaq.model.dto.scoringResult.ScoringResultQueryRequest;
import com.aiaq.model.entity.ScoringResult;
import com.aiaq.model.vo.ScoringResultDetailVO;
import com.aiaq.model.vo.ScoringResultVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
* @author 最紧要开心
* @description 针对表【scoring_result(评分结果)】的数据库操作Mapper
* @createDate 2024-09-28 18:55:28
* @Entity com.aiaq.model.entity.ScoringResult
*/
public interface ScoringResultMapper extends BaseMapper<ScoringResult> {

    /**
     * 查询评分结果详情
     * @param scoringResultId 评分结果 id
     * @return 评分结果详情
     */
    ScoringResultDetailVO detailScoringResult(@Param("scoringResultId") Long scoringResultId);

    /**
     * 分页查询评分结果
     * @param page 分页对象
     * @param scoringResultQueryRequest 评分结果查询请求
     * @param userId 用户 id
     * @param admin 是否为管理员
     * @return 评分结果列表
     */
    Page<ScoringResultVO> listScoringResult(@Param("page") Page page, @Param("scoringResultQueryRequest") ScoringResultQueryRequest scoringResultQueryRequest,
                                            @Param("userId") Long userId, @Param("isAdmin") boolean admin);
}




