package com.aiaq.mapper;

import com.aiaq.model.dto.app.AppQueryRequest;
import com.aiaq.model.entity.App;
import com.aiaq.model.vo.AppDetailVO;
import com.aiaq.model.vo.AppVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
* @author 最紧要开心
* @description 针对表【app(应用)】的数据库操作Mapper
* @createDate 2024-09-11 11:51:29
* @Entity com.aiaq.model.entity.App
*/
public interface AppMapper extends BaseMapper<App> {

    /**
     * 查询应用列表
     * @param appQueryRequest 查询条件
     * @param userId 用户ID
     * @param isAdmin 是否管理员
     * @return 应用列表
     */
    Page<AppVO> listApp(@Param("page") Page page, @Param("appQueryRequest") AppQueryRequest appQueryRequest,
                        @Param("userId") Long userId, @Param("isAdmin") boolean isAdmin);

    /**
     * 查询应用详情
     * @param appId 应用ID
     * @return 应用详情
     */
    AppDetailVO detailApp(@Param("appId") Long appId);

    /**
     * 查询首页应用列表
     * @param appQueryRequest 查询条件
     * @return 应用列表
     */
    Page<AppVO> listIndexApp(@Param("page") Page page, @Param("appQueryRequest") AppQueryRequest appQueryRequest);
}




