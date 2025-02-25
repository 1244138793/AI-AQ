package com.aiaq.model.dto.answer;


import com.aiaq.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/27 17:05
 * @Description 用户答题记录查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAnswerQueryRequest extends PageRequest implements Serializable {

    /**
     * 结果名称，如物流师
     */
    private String resultName;

    /**
     * 结果描述
     */
    private String resultDesc;

}