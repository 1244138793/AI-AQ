package com.aiaq.service;

import com.aiaq.common.PageResult;
import com.aiaq.model.dto.answer.UserAnswerAddRequest;
import com.aiaq.model.dto.answer.UserAnswerQueryRequest;
import com.aiaq.model.entity.UserAnswer;
import com.aiaq.model.vo.UserAnswerVO;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 最紧要开心
* @description 针对表【user_answer(用户答题记录)】的数据库操作Service
* @createDate 2024-09-16 11:45:23
*/
public interface UserAnswerService extends IService<UserAnswer> {

    /**
     * 添加用户答题记录
     * @param userAnswerAddRequest 用户答题记录添加请求
     * @return 用户答题记录ID
     */
    Long addUserAnswer(UserAnswerAddRequest userAnswerAddRequest, HttpServletRequest request);

    /**
     * 删除用户答题记录
     * @param userAnswerId 用户答题记录ID
     */
    void deleteUserAnswer(Long userAnswerId, HttpServletRequest request);

    /**
     * 分页获取当前登录用户创建的用户答题记录列表
     * @param userAnswerQueryRequest 用户答题记录查询请求
     * @return 用户答题记录列表
     */
    PageResult<UserAnswerVO> listUserAnswer(UserAnswerQueryRequest userAnswerQueryRequest, HttpServletRequest request);
}
