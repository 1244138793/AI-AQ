package com.aiaq.service.impl;

import cn.hutool.json.JSONUtil;
import com.aiaq.common.ErrorCode;
import com.aiaq.common.PageResult;
import com.aiaq.exception.BusinessException;
import com.aiaq.exception.ThrowUtils;
import com.aiaq.mapper.UserAnswerMapper;
import com.aiaq.model.dto.answer.UserAnswerAddRequest;
import com.aiaq.model.dto.answer.UserAnswerQueryRequest;
import com.aiaq.model.entity.App;
import com.aiaq.model.entity.User;
import com.aiaq.model.entity.UserAnswer;
import com.aiaq.model.enums.ReviewStatusEnum;
import com.aiaq.model.vo.UserAnswerVO;
import com.aiaq.scoring.ScoringStrategyExecutor;
import com.aiaq.service.AppService;
import com.aiaq.service.UserAnswerService;
import com.aiaq.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 最紧要开心
* @description 针对表【user_answer(用户答题记录)】的数据库操作Service实现
* @createDate 2024-09-16 11:45:23
*/
@Service
@Slf4j
public class UserAnswerServiceImpl extends ServiceImpl<UserAnswerMapper, UserAnswer>
    implements UserAnswerService{

    @Resource
    private AppService appService;

    @Resource
    private UserService userService;

    @Resource
    private ScoringStrategyExecutor scoringStrategyExecutor;

    @Override
    public Long addUserAnswer(UserAnswerAddRequest userAnswerAddRequest, HttpServletRequest request) {
        // 在此处将实体类和 DTO 进行转换
        UserAnswer userAnswer = new UserAnswer();
        BeanUtils.copyProperties(userAnswerAddRequest, userAnswer);
        List<String> choices = userAnswerAddRequest.getChoices();
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        // 判断 app 是否存在
        Long appId = userAnswerAddRequest.getAppId();
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        if (!ReviewStatusEnum.PASS.equals(ReviewStatusEnum.getEnumByValue(app.getReviewStatus()))) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "应用未通过审核，无法答题");
        }
        // 填充默认值
        User loginUser = userService.getLoginUser(request);
        userAnswer.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = this.save(userAnswer);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long userAnswerId = userAnswer.getId();
        // 调用评分模块
        try {
            UserAnswer userAnswerWithResult = scoringStrategyExecutor.doScore(choices, app);
            userAnswerWithResult.setId(userAnswerId);
            this.updateById(userAnswerWithResult);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "评分错误");
        }
        return userAnswerId;
    }

    @Override
    public void deleteUserAnswer(Long userAnswerId, HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        // 判断是否存在
        UserAnswer oldUserAnswer = this.getById(userAnswerId);
        ThrowUtils.throwIf(oldUserAnswer == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldUserAnswer.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = this.removeById(userAnswerId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }

    @Override
    public PageResult<UserAnswerVO> listUserAnswer(UserAnswerQueryRequest userAnswerQueryRequest, HttpServletRequest request) {
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        // 查询应用列表
        Page<UserAnswerVO> page = this.baseMapper.listUserAnswer(new Page(userAnswerQueryRequest.getCurrent(), userAnswerQueryRequest.getPageSize()),
                userAnswerQueryRequest, loginUser.getId());
        return new PageResult<>(page);
    }
}




