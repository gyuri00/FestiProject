package com.GDG.Festi.common.response;

import com.GDG.Festi.common.response.resEnum.ErrorCode;
import com.GDG.Festi.common.response.resEnum.ResponseStatus;
import com.GDG.Festi.common.response.resEnum.SuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.GDG.Festi.common.response.resEnum.ResponseStatus.ERROR;
import static com.GDG.Festi.common.response.resEnum.ResponseStatus.SUCCESS;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private ResponseStatus status;
    private T data;

    public ApiResponse(Integer code, ResponseStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public ApiResponse(Integer code, ResponseStatus status, String message, T data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> SUCCESS (SuccessCode code) {
        return new ApiResponse<>(code.getCode(), SUCCESS, code.getMessage());
    }
    public static <T> ApiResponse<T> SUCCESS (SuccessCode code, T data) {
        return new ApiResponse<>(code.getCode(), SUCCESS, code.getMessage(), data);
    }
    public static <T> ApiResponse<T> ERROR (ErrorCode code) {
        return new ApiResponse<>(code.getCode(), ERROR, code.getMessage());
    }
}