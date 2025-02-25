package com.aiaq.mapper;

import com.aiaq.model.dto.answer.UserAnswerQueryRequest;
import com.aiaq.model.entity.UserAnswer;
import com.aiaq.model.vo.UserAnswerVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
* @author 最紧要开心
* @description 针对表【user_answer(用户答题记录)】的数据库操作Mapper
* @createDate 2024-09-16 11:45:23
* @Entity com.aiaq.model.entity.UserAnswer
*/
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {

    /**
     * 分页获取当前登录用户创建的用户答案列表
     * @param page 分页信息
     * @param userAnswerQueryRequest 查询条件
     * @param userId 用户ID
     * @return 用户答案列表
     */
    Page<UserAnswerVO> listUserAnswer(@Param("page") Page page, @Param("userAnswerQueryRequest") UserAnswerQueryRequest userAnswerQueryRequest,
                               @Param("userId") Long userId);
}




