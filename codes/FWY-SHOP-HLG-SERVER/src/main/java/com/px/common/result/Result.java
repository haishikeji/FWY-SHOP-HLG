package com.px.common.result;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 返回工具类
 *
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {


    private static final long serialVersionUID = -4823388468910670152L;
    @ApiModelProperty(name = "响应结果码")
    private Integer code;
    @ApiModelProperty(name = "响应结果信息")
    private String msg;
    @ApiModelProperty(name = "响应结果数据")
    private T data;
    @ApiModelProperty(name = "响应结果数据")
    private String error;

    public Result() {
    }

    /**
     * 返回数据（普通方式）
     */
    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 返回数据（枚举方式）
     */
    public Result(ResultType resultType, T data) {
        this.code = resultType.getCode();
        this.msg = resultType.getMsg();
        this.data = data;
    }


    /**
     * code，msg方式
     */
    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    /**
     * code，msg方式
     */
    public Result(Integer code, String msg, T data,String error) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.error = error;
    }


    /**
     * 短信
     *
     * @param t
     * @return
     */
    public Result<T> bindData(T t) {
        if (t instanceof List && ((List) t).size() == 0) {
            this.data = null;
        } else if (t instanceof Map && ((Map) t).size() == 0) {
            this.data = null;
        } else if (t instanceof String && StringUtils.isEmpty(t)) {
            this.data = null;
        } else if (t instanceof Page && ((Page) t).getRecords().size() == 0) {
            this.data = t;
        } else if (t == null || t == "") {
            this.data = null;
        } else {
            this.data = t;
        }
        return this;
    }

    //  返回带参数成功
    public static <T> Result<T> success(T data) {
        return new Result(ResultType.SUCCESS, data);
    }

    //返回成功
    public static Result<Void> success() {
        return new Result(ResultType.SUCCESS, null);
    }

    //  通用返回失败（传入自定义错误code+msg）
    public static <T> Result<T> failure(Integer code, String msg) {
        return new Result(code, msg, null);
    }

    //  通用返回失败（传入自定义枚举）
    public static <T> Result<T> failure(ResultType resultType) {
        return new Result(resultType, null);
    }

    // 通用返回失败（传入自定义枚举）
    public static <T> Result<T> failure(ResultType resultType, T t) {
        return new Result(resultType, t);
    }

    public static <T> Result failure(Integer code, String msg, T data, String errorMsg) {return new Result(code, msg,data,errorMsg);}
    public static <T> Result failure(ResultType resultType, T data, String errorMsg) {return new Result(resultType.getCode(), resultType.getMsg(),data,errorMsg);}
}

