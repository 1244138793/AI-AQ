package com.aiaq.controller;

import cn.hutool.json.JSONUtil;
import com.aiaq.common.BaseResponse;
import com.aiaq.common.PageResult;
import com.aiaq.common.ResultUtils;
import com.aiaq.model.dto.answer.UserAnswerAddRequest;
import com.aiaq.model.dto.answer.UserAnswerQueryRequest;
import com.aiaq.model.entity.User;
import com.aiaq.model.entity.UserAnswer;
import com.aiaq.model.vo.UserAnswerVO;
import com.aiaq.service.UserAnswerService;
import com.aiaq.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/16 12:16
 * @Description 用户答题控制器
 */
@RestController
@RequestMapping("/userAnswer")
@Slf4j
public class UserAnswerController {

    @Resource
    private UserAnswerService userAnswerService;

    @Resource
    private UserService userService;

    /**
     * 创建用户答案
     *
     * @param userAnswerAddRequest 用户答案添加请求
     * @return 用户答案ID
     */
    @PostMapping("/add")
    public BaseResponse<Long> addUserAnswer(@RequestBody UserAnswerAddRequest userAnswerAddRequest, HttpServletRequest request) {
        log.info("添加用户答案入参={}", JSONUtil.toJsonStr(userAnswerAddRequest));
        return ResultUtils.success(userAnswerService.addUserAnswer(userAnswerAddRequest, request));
    }

    /**
     * 删除用户答题记录
     * @param userAnswerId 用户答题记录ID
     */
    @GetMapping("/delete/{userAnswerId}")
    public BaseResponse<Boolean> deleteUserAnswer(@PathVariable("userAnswerId") Long userAnswerId, HttpServletRequest request) {
        log.info("删除用户答案入参={}", userAnswerId);
        userAnswerService.deleteUserAnswer(userAnswerId, request);
        return ResultUtils.success();
    }

    /**
     * 分页获取当前登录用户创建的用户答案列表
     * @param userAnswerQueryRequest 用户答案查询请求
     * @return 用户答案列表
     */
    @PostMapping("/list")
    public BaseResponse<PageResult<UserAnswerVO>> listUserAnswer(@RequestBody UserAnswerQueryRequest userAnswerQueryRequest, HttpServletRequest request) {
        log.info("查询用户答案列表入参={}", JSONUtil.toJsonStr(userAnswerQueryRequest));
        return ResultUtils.success(userAnswerService.listUserAnswer(userAnswerQueryRequest, request));
    }

    /**
     * 根据 id 获取用户答案（封装类）
     */
    @GetMapping("/get/{userAnswerId}")
    public BaseResponse<UserAnswer> getUserAnswerVOById(@PathVariable("userAnswerId") Long userAnswerId, HttpServletRequest request) {
        // 查询数据库
        UserAnswer userAnswer = userAnswerService.getById(userAnswerId);
        User loginUser = userService.getLoginUser(request);
        userAnswer.setUserName(loginUser.getUserName());
        return ResultUtils.success(userAnswer);
    }
}
