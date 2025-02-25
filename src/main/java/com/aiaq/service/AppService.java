package com.aiaq.service;

import com.aiaq.common.PageResult;
import com.aiaq.model.dto.app.AppCreateRequest;
import com.aiaq.model.dto.app.AppQueryRequest;
import com.aiaq.model.dto.app.AppReviewRequest;
import com.aiaq.model.dto.app.AppUpdateRequest;
import com.aiaq.model.entity.App;
import com.aiaq.model.vo.AppDetailVO;
import com.aiaq.model.vo.AppVO;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 最紧要开心
* @description 针对表【app(应用)】的数据库操作Service
* @createDate 2024-09-11 11:51:29
*/
public interface AppService extends IService<App> {

    /**
     * 创建应用
     * @param appCreateRequest 应用添加请求
     * @return 应用ID
     */
    Long createApp(AppCreateRequest appCreateRequest, HttpServletRequest request);

    /**
     * 更新应用
     * @param appUpdateRequest 应用更新请求
     */
    void editApp(AppUpdateRequest appUpdateRequest, HttpServletRequest request);

    /**
     * 删除应用
     * @param appId 应用ID
     */
    void deleteApp(Long appId, HttpServletRequest request);

    /**
     * 查询App列表
     * @param appQueryRequest 查询条件
     * @return App列表
     */
    PageResult<AppVO> listApp(AppQueryRequest appQueryRequest, HttpServletRequest request);

    /**
     * 查询App详情
     * @param appId 应用ID
     * @return App详情
     */
    AppDetailVO detailApp(Long appId, HttpServletRequest request);

    /**
     * 查询首页App列表
     * @param appQueryRequest 查询条件
     * @return App列表
     */
    PageResult<AppVO> listIndexApp(AppQueryRequest appQueryRequest);

    /**
     * 审核应用
     * @param appReviewRequest 审核请求
     */
    void reviewApp(AppReviewRequest appReviewRequest, HttpServletRequest request);

    PageResult<AppVO> listMyApp(AppQueryRequest appQueryRequest, HttpServletRequest request);
}
