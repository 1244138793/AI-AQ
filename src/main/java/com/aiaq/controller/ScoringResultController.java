package com.aiaq.controller;

import cn.hutool.json.JSONUtil;
import com.aiaq.common.BaseResponse;
import com.aiaq.common.PageResult;
import com.aiaq.common.ResultUtils;
import com.aiaq.model.dto.scoringResult.ScoringResultAddRequest;
import com.aiaq.model.dto.scoringResult.ScoringResultQueryRequest;
import com.aiaq.model.dto.scoringResult.ScoringResultUpdateRequest;
import com.aiaq.model.vo.ScoringResultDetailVO;
import com.aiaq.model.vo.ScoringResultVO;
import com.aiaq.service.ScoringResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/11 11:22
 * @Description 评分结果控制器
 */
@RestController
@RequestMapping("/scoringResult")
@Slf4j
public class ScoringResultController {

    @Resource
    private ScoringResultService scoringResultService;

    /**
     * 创建评分结果
     * @param scoringResultAddRequest 评分结果添加请求
     * @return 评分结果ID
     */
    @PostMapping("/add")
    public BaseResponse<Long> addScoringResult(@RequestBody ScoringResultAddRequest scoringResultAddRequest, HttpServletRequest request) {
        log.info("添加评分结果入参={}", JSONUtil.toJsonStr(scoringResultAddRequest));
        return ResultUtils.success(scoringResultService.addScoringResult(scoringResultAddRequest, request));
    }

    /**
     * 删除评分结果
     * @param scoringResultId 评分结果ID
     */
    @GetMapping("/delete/{scoringResultId}")
    public BaseResponse<Boolean> deleteScoringResult(@PathVariable Long scoringResultId, HttpServletRequest request) {
        log.info("删除评分结果入参={}", scoringResultId);
        scoringResultService.deleteScoringResult(scoringResultId, request);
        return ResultUtils.success();
    }

    /**
     * 更新评分结果
     * @param scoringResultUpdateRequest 评分结果更新请求
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateScoringResult(@RequestBody ScoringResultUpdateRequest scoringResultUpdateRequest) {
        log.info("更新评分结果入参={}", JSONUtil.toJsonStr(scoringResultUpdateRequest));
        scoringResultService.updateScoringResult(scoringResultUpdateRequest);
        return ResultUtils.success();
    }

    /**
     * 评分结果详情
     * @param scoringResultId 评分结果ID
     * @return 评分结果详情
     */
    @GetMapping("/detail/{scoringResultId}")
    public BaseResponse<ScoringResultDetailVO> detailScoringResult(@PathVariable("scoringResultId") Long scoringResultId, HttpServletRequest request) {
        log.info("查询评分结果详情入参={}", scoringResultId);
        return ResultUtils.success(scoringResultService.detailScoringResult(scoringResultId, request));
    }

    /**
     * 分页获取评分结果列表
     *
     * @param scoringResultQueryRequest 评分结果查询请求
     * @return 评分结果列表
     */
    @PostMapping("/list/page")
    public BaseResponse<PageResult<ScoringResultVO>> listScoringResult(@RequestBody ScoringResultQueryRequest scoringResultQueryRequest, HttpServletRequest request) {
        log.info("查询评分结果列表入参={}", JSONUtil.toJsonStr(scoringResultQueryRequest));
        return ResultUtils.success(scoringResultService.listScoringResult(scoringResultQueryRequest, request));
    }
}
