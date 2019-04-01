package com.feiyu.common.base;

import java.util.Objects;

/**
 * @author feiyu127@gmail.com
 * @date 2018-08-28 11:39
 */
public class ApiResult<T> {
    public Integer code;
    public T data;
    public String errorMsg;

    public ApiResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ApiResult(Integer code, T data, String errorMsg) {
        this.code = code;
        this.data = data;
        this.errorMsg = errorMsg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public static <E> ApiResult<E> buildSuccess(E data) {
        return new ApiResult(200, data, "success");
    }

    public static ApiResult buildFail(String errorMsg) {
        return new ApiResult(500, null, errorMsg);
    }

    public static <E> ApiResult<E> buildCondition(boolean isSuccess, E data, String errorMsg) {
        if (isSuccess) {
            return buildSuccess(data);
        } else {
            return buildFail(errorMsg);
        }
    }

    private boolean success;

    public boolean isSuccess() {
        return Objects.equals(200, code);
    }

}
