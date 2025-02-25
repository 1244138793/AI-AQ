package com.aiaq.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aiaq.common.ErrorCode;
import com.aiaq.common.PageResult;
import com.aiaq.constant.CommonConstant;
import com.aiaq.exception.BusinessException;
import com.aiaq.exception.ThrowUtils;
import com.aiaq.mapper.AppMapper;
import com.aiaq.model.dto.app.AppCreateRequest;
import com.aiaq.model.dto.app.AppQueryRequest;
import com.aiaq.model.dto.app.AppReviewRequest;
import com.aiaq.model.dto.app.AppUpdateRequest;
import com.aiaq.model.entity.App;
import com.aiaq.model.entity.User;
import com.aiaq.model.enums.ReviewStatusEnum;
import com.aiaq.model.vo.AppDetailVO;
import com.aiaq.model.vo.AppVO;
import com.aiaq.service.AppService;
import com.aiaq.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
* @author 最紧要开心
* @description 针对表【app(应用)】的数据库操作Service实现
* @createDate 2024-09-11 11:51:29
*/
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>
    implements AppService {

    @Resource
    private UserService userService;

    @Override
    public Long createApp(AppCreateRequest appCreateRequest, HttpServletRequest request) {
        // 转换实体类
        App app = BeanUtil.copyProperties(appCreateRequest, App.class);
        // 校验名称是否重复
        this.checkAppNameUnique(null, app.getAppName());
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        app.setUserId(loginUser.getId());
        app.setReviewStatus(ReviewStatusEnum.REVIEWING.getValue());
        // 保存应用信息
        boolean result = this.save(app);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return app.getId();
    }

    @Override
    public void editApp(AppUpdateRequest appUpdateRequest, HttpServletRequest request) {
        // 转换实体类
        App app = BeanUtil.copyProperties(appUpdateRequest, App.class);
        // 应用是否存在
        ThrowUtils.throwIf(this.getById(app.getId()) == null, ErrorCode.NOT_FOUND_ERROR);
        // 校验名称是否重复
        this.checkAppNameUnique(app.getId(), app.getAppName());
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        // 仅本人或管理员可编辑
        if (!loginUser.getId().equals(app.getUserId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 重置应用审核状态
        app.setReviewStatus(ReviewStatusEnum.REVIEWING.getValue());
        // 更新应用信息
        boolean result = this.updateById(app);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }

    @Override
    public void deleteApp(Long appId, HttpServletRequest request) {
        // 应用是否存在
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        // 仅本人或管理员可删除
        if (!loginUser.getId().equals(app.getUserId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 删除应用信息
        boolean result = this.removeById(appId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }

    @Override
    public PageResult<AppVO> listApp(AppQueryRequest appQueryRequest, HttpServletRequest request) {
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        // 查询应用列表
        Page<AppVO> page = this.baseMapper.listApp(new Page(appQueryRequest.getCurrent(), appQueryRequest.getPageSize()),
                appQueryRequest, loginUser.getId(), userService.isAdmin(loginUser));
        return new PageResult<>(page);
    }

    @Override
    public AppDetailVO detailApp(Long appId, HttpServletRequest request) {
        // 应用是否存在
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        // 审核未通过的仅本人或管理员可查看
        if (ReviewStatusEnum.PASS.getValue() != app.getReviewStatus() && !loginUser.getId().equals(app.getUserId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return this.baseMapper.detailApp(appId);
    }

    @Override
    public PageResult<AppVO> listIndexApp(AppQueryRequest appQueryRequest) {
        // 查询应用列表
        Page<AppVO> page = this.baseMapper.listIndexApp(new Page(appQueryRequest.getCurrent(), appQueryRequest.getPageSize()),
                appQueryRequest);
        return new PageResult<>(page);
    }

    @Override
    public void reviewApp(AppReviewRequest appReviewRequest, HttpServletRequest request) {
        // 应用是否存在
        App app = this.getById(appReviewRequest.getId());
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        // 应用是否已被审核
        ThrowUtils.throwIf(ReviewStatusEnum.REVIEWING.getValue() != app.getReviewStatus(), ErrorCode.OPERATION_ERROR, "应用已被审核！");
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        // 完善实体信息
        app.setReviewStatus(appReviewRequest.getReviewStatus());
        app.setReviewMessage(appReviewRequest.getReviewMessage());
        app.setReviewerId(loginUser.getId());
        app.setReviewTime(LocalDateTime.now());
        // 更新应用审核状态
        boolean result = this.updateById(app);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }

    @Override
    public PageResult<AppVO> listMyApp(AppQueryRequest appQueryRequest, HttpServletRequest request) {
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        // 查询应用列表
        Page<AppVO> page = this.baseMapper.listApp(new Page(appQueryRequest.getCurrent(), appQueryRequest.getPageSize()),
                appQueryRequest, loginUser.getId(), false);
        return new PageResult<>(page);
    }

    /**
     * 校验应用名称是否唯一
     * @param appId 应用ID
     * @param appName 应用名称
     */
    private void checkAppNameUnique(Long appId, String appName) {
        Long count = this.lambdaQuery()
                // 更新时排除当前应用
                .ne(appId != null, App::getId, appId)
                .eq(App::getAppName, appName)
                .eq(App::getIsDelete, CommonConstant.NOT_DELETED)
                .count();
        ThrowUtils.throwIf(count > 0, ErrorCode.OPERATION_ERROR, "应用名称已存在");
    }
}




