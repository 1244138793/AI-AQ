package com.aiaq.mapper;

import com.aiaq.model.entity.Question;
import com.aiaq.model.vo.QuestionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 最紧要开心
* @description 针对表【question(题目)】的数据库操作Mapper
* @createDate 2024-09-12 17:07:22
* @Entity com.aiaq.model.entity.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 获取题目详情
     * @param questionId 题目ID
     * @return 题目详情
     */
    QuestionVO detailQuestion(@Param("questionId") Long questionId);
}




