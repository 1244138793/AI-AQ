package com.aiaq.model.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

/**
 * App 应用类型枚举
 */
@Getter
public enum AppTypeEnum {

    SCORE("得分类", 0),
    TEST("测评类", 1);

    private final String text;

    private final int value;

    AppTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     */
    public static AppTypeEnum getEnumByValue(Integer value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (AppTypeEnum anEnum : AppTypeEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }
}
