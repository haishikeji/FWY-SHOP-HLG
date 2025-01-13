package com.px.common.enums;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

@Getter
@ApiModel(value = "基础枚举", description = "状态 \n" +
        "    NORMAL(0, \"启用\"),\n" +
        "    DELETE(1, \"禁用\")"
)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)  //返回对象
public enum BaseStateEnum implements IEnum<Integer> {
    ENABLE(0, "已启用"),
    DISABLE(1, "已禁用"),
    ;
    /**
     * 枚举的第一个参数，数据库字段
     */
    @EnumValue
    int value;
    /**
     * 枚举的第二个参数
     */
    String desc;


    BaseStateEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 获取数据值-数据库值
     */
    @Override
    public Integer getValue() {
        return value;
    }


    /**
     * 主要用于展示-->
     * 1、枚举数据在java代码中查看对象值时直接展示的内容，
     * 2、 swagger 注释直接展示的内容
     */
    @Override
    public String toString() {
        return this.name() + ":{\"value\":" + this.value + ",\"desc\":\"" + this.desc + "\"}";
    }

    private static BaseStateEnum query(String key) {
        if (NumberUtil.isNumber(key)) {
            BaseStateEnum[] values = BaseStateEnum.values();
            int intKey = Integer.parseInt(key);
            for (BaseStateEnum result : values) {
                if (result.getValue() == intKey) {
                    return result;
                }
            }
        }
        return null;
    }

    public static BaseStateEnum queryByKey(String key) {
        BaseStateEnum result = query(key);
        return result;
    }
}