package com.aiaq.service.impl;

import cn.hutool.json.JSONUtil;
import com.aiaq.common.ErrorCode;
import com.aiaq.common.PageResult;
import com.aiaq.exception.BusinessException;
import com.aiaq.exception.ThrowUtils;
import com.aiaq.mapper.ScoringResultMapper;
import com.aiaq.model.dto.scoringResult.ScoringResultAddRequest;
import com.aiaq.model.dto.scoringResult.ScoringResultQueryRequest;
import com.aiaq.model.dto.scoringResult.ScoringResultUpdateRequest;
import com.aiaq.model.entity.ScoringResult;
import com.aiaq.model.entity.User;
import com.aiaq.model.vo.ScoringResultDetailVO;
import com.aiaq.model.vo.ScoringResultVO;
import com.aiaq.service.ScoringResultService;
import com.aiaq.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 最紧要开心
* @description 针对表【scoring_result(评分结果)】的数据库操作Service实现
* @createDate 2024-09-28 18:55:28
*/
@Service
public class ScoringResultServiceImpl extends ServiceImpl<ScoringResultMapper, ScoringResult>
    implements ScoringResultService {

    @Resource
    private UserService userService;

    @Override
    public Long addScoringResult(ScoringResultAddRequest scoringResultAddRequest, HttpServletRequest request) {
        // 在此处将实体类和 DTO 进行转换
        ScoringResult scoringResult = new ScoringResult();
        BeanUtils.copyProperties(scoringResultAddRequest, scoringResult);
        List<String> resultProp = scoringResultAddRequest.getResultProp();
        scoringResult.setResultProp(JSONUtil.toJsonStr(resultProp));
        // 填充默认值
        User loginUser = userService.getLoginUser(request);
        scoringResult.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = this.save(scoringResult);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        return scoringResult.getId();
    }

    @Override
    public void deleteScoringResult(Long scoringResultId, HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        // 判断是否存在
        ScoringResult scoringResult = this.getById(scoringResultId);
        ThrowUtils.throwIf(scoringResult == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!scoringResult.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = this.removeById(scoringResultId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }

    @Override
    public void updateScoringResult(ScoringResultUpdateRequest scoringResultUpdateRequest) {
        // 在此处将实体类和 DTO 进行转换
        ScoringResult scoringResult = new ScoringResult();
        BeanUtils.copyProperties(scoringResultUpdateRequest, scoringResult);
        List<String> resultProp = scoringResultUpdateRequest.getResultProp();
        scoringResult.setResultProp(JSONUtil.toJsonStr(resultProp));
        // 判断是否存在
        long id = scoringResultUpdateRequest.getId();
        ScoringResult oldScoringResult = this.getById(id);
        ThrowUtils.throwIf(oldScoringResult == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = this.updateById(scoringResult);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }

    @Override
    public ScoringResultDetailVO detailScoringResult(Long scoringResultId, HttpServletRequest request) {
        // 判断是否存在
        ScoringResult scoringResult = this.getById(scoringResultId);
        ThrowUtils.throwIf(scoringResult == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可查看
        if (!scoringResult.getUserId().equals(userService.getLoginUser(request).getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 获取详情
        ScoringResultDetailVO scoringResultDetailVO = this.baseMapper.detailScoringResult(scoringResultId);
        scoringResultDetailVO.setResultProp(JSONUtil.toList(scoringResultDetailVO.getResultPropStr(), String.class));
        return scoringResultDetailVO;
    }

    @Override
    public PageResult<ScoringResultVO> listScoringResult(ScoringResultQueryRequest scoringResultQueryRequest, HttpServletRequest request) {
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        // 查询应用列表
        Page<ScoringResultVO> page = this.baseMapper.listScoringResult(new Page(scoringResultQueryRequest.getCurrent(), scoringResultQueryRequest.getPageSize()),
                scoringResultQueryRequest, loginUser.getId(), userService.isAdmin(loginUser));
        return new PageResult<>(page);
    }
}




