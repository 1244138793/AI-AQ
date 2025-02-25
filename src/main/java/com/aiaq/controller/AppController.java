package com.aiaq.controller;

import cn.hutool.json.JSONUtil;
import com.aiaq.annotation.AuthCheck;
import com.aiaq.common.BaseResponse;
import com.aiaq.common.PageResult;
import com.aiaq.common.ResultUtils;
import com.aiaq.constant.UserConstant;
import com.aiaq.model.dto.app.AppCreateRequest;
import com.aiaq.model.dto.app.AppQueryRequest;
import com.aiaq.model.dto.app.AppReviewRequest;
import com.aiaq.model.dto.app.AppUpdateRequest;
import com.aiaq.model.vo.AppDetailVO;
import com.aiaq.model.vo.AppVO;
import com.aiaq.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/11 11:22
 * @Description 应用控制器
 */
@RestController
@RequestMapping("/app")
@Slf4j
public class AppController {

    @Resource
    private AppService appService;

    /**
     * 创建应用
     * @param appCreateRequest 应用添加请求
     * @return 应用ID
     */
    @PostMapping("/create")
    public BaseResponse<Long> createApp(@Validated @RequestBody AppCreateRequest appCreateRequest, HttpServletRequest request) {
        log.info("用户创建App入参={}", JSONUtil.toJsonStr(appCreateRequest));
        return ResultUtils.success(appService.createApp(appCreateRequest, request));
    }

    /**
     * 更新应用
     * @param appUpdateRequest 应用更新请求
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> updateApp(@Validated @RequestBody AppUpdateRequest appUpdateRequest, HttpServletRequest request) {
        log.info("用户更新App入参={}", JSONUtil.toJsonStr(appUpdateRequest));
        appService.editApp(appUpdateRequest, request);
        return ResultUtils.success();
    }

    /**
     * 删除应用
     * @param appId 应用ID
     */
    @GetMapping("/delete/{appId}")
    public BaseResponse<Boolean> deleteApp(@PathVariable("appId") Long appId, HttpServletRequest request) {
        log.info("用户删除App入参={}", appId);
        appService.deleteApp(appId, request);
        return ResultUtils.success();
    }

    /**
     * 查询应用管理列表
     * @param appQueryRequest 查询条件
     * @return App列表
     */
    @PostMapping("/list")
    public BaseResponse<PageResult<AppVO>> listApp(@RequestBody AppQueryRequest appQueryRequest, HttpServletRequest request) {
        log.info("查询App列表入参={}", JSONUtil.toJsonStr(appQueryRequest));
        return ResultUtils.success(appService.listApp(appQueryRequest, request));
    }

    /**
     * 查询应用详情
     * @param appId 应用ID
     * @return App详情
     */
    @GetMapping("/detail/{appId}")
    public BaseResponse<AppDetailVO> detailApp(@PathVariable("appId") Long appId, HttpServletRequest request) {
        log.info("查询App详情入参={}", appId);
        return ResultUtils.success(appService.detailApp(appId, request));
    }

    /**
     * 查询主页应用列表
     * @param appQueryRequest 查询条件
     * @return App列表
     */
    @PostMapping("/index/list")
    public BaseResponse<PageResult<AppVO>> listIndexApp(@RequestBody AppQueryRequest appQueryRequest) {
        log.info("查询主页App列表入参={}", JSONUtil.toJsonStr(appQueryRequest));
        return ResultUtils.success(appService.listIndexApp(appQueryRequest));
    }

    /**
     * 管理员审核应用
     * @param appReviewRequest 审核请求
     */
    @PostMapping("/review")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> reviewApp(@Validated @RequestBody AppReviewRequest appReviewRequest, HttpServletRequest request) {
        log.info("审核App入参={}", JSONUtil.toJsonStr(appReviewRequest));
        appService.reviewApp(appReviewRequest, request);
        return ResultUtils.success();
    }

    /**
     * 查询应用管理列表
     * @param appQueryRequest 查询条件
     * @return App列表
     */
    @PostMapping("/my/list")
    public BaseResponse<PageResult<AppVO>> listMyApp(@RequestBody AppQueryRequest appQueryRequest, HttpServletRequest request) {
        log.info("查询我的App列表入参={}", JSONUtil.toJsonStr(appQueryRequest));
        return ResultUtils.success(appService.listMyApp(appQueryRequest, request));
    }
}
