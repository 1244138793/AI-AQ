package com.aiaq.model.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

/**
 * App 评分策略枚举
 */
@Getter
public enum AppScoringStrategyEnum {

    CUSTOM("自定义", 0),
    AI("AI", 1);

    private final String text;

    private final int value;

    AppScoringStrategyEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     */
    public static AppScoringStrategyEnum getEnumByValue(Integer value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (AppScoringStrategyEnum anEnum : AppScoringStrategyEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }
}
