package com.aiaq.model.dto.scoringResult;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/28 19:15
 * @Description 评分结果新增请求
 */
@Data
public class ScoringResultAddRequest {

    /**
     * 结果名称，如物流师
     */
    @NotBlank(message = "结果名称不能为空")
    private String resultName;

    /**
     * 结果描述
     */
    @NotBlank(message = "结果描述不能为空")
    private String resultDesc;

    /**
     * 结果属性集合 JSON，如 [I,S,T,J]
     */
    @NotEmpty(message = "结果属性集合不能为空")
    private List<String> resultProp;

    /**
     * 结果得分范围
     */
    @PositiveOrZero(message = "结果得分范围必须大于等于0")
    private Integer resultScoreRange;

    /**
     * 应用 id
     */
    @NotNull(message = "应用id不能为空")
    private Long appId;
}
