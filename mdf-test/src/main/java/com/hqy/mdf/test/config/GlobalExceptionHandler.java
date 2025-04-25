package com.hqy.mdf.test.config;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.hqy.mdf.common.bean.Result;
import com.hqy.mdf.common.enums.ErrorEnum;
import com.hqy.mdf.common.util.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * @author hqy
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {



    /**
     * 处理Validated校验异常
     */
    @ExceptionHandler(BindException.class)
    public <T> Result<T> handleException(BindException e) {
        StringBuilder errorMsg = new StringBuilder();
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            errorMsg.append(error.getDefaultMessage()).append(" ");
        }
        return ResultUtils.error(ErrorEnum.PARAM_ERROR.getCode(), errorMsg.toString());
    }

    /**
     * 处理Validated校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public <T> Result<T> handleException(MethodArgumentNotValidException e) {
        StringBuilder errorMsg = new StringBuilder();
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            errorMsg.append(error.getDefaultMessage()).append(" ");
        }
        return ResultUtils.error(ErrorEnum.PARAM_ERROR.getCode(), errorMsg.toString());
    }

    /**
     * 处理Validated校验异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public <T> Result<T> handleException(ConstraintViolationException e) {
        StringBuilder errorMsg = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            errorMsg.append(constraintViolation.getMessage()).append(" ");
        }
        return ResultUtils.error(ErrorEnum.PARAM_ERROR.getCode(), errorMsg.toString());
    }

    /**
     * 处理缺失请求404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public <T> Result<T> handleException(NoHandlerFoundException e) {
        return ResultUtils.error(ErrorEnum.REQUEST_PATH_ERROR);
    }
    /**
     * 处理缺失请求参数异常
     * RequestParam 注解的参数缺失
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public <T> Result<T> handleException(MissingServletRequestParameterException e) {
        return ResultUtils.error(ErrorEnum.PARAM_ERROR.getCode(), e.getMessage());
    }

    /**
     * 处理请求参数无法读取异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public <T> Result<T> handleException(HttpMessageNotReadableException e) {
        StringBuilder errorMsg = new StringBuilder();
        if(e.getCause() instanceof InvalidFormatException){
            InvalidFormatException cause = (InvalidFormatException)e.getCause();
            for (JsonMappingException.Reference reference : cause.getPath()) {
                errorMsg.append(reference.getFieldName()).append(" ");
            }
            return ResultUtils.error(ErrorEnum.PARAM_FORMAT_ERROR.getCode(), errorMsg.toString());
        }
        log.error(e.getMessage(), e);
        return ResultUtils.error(ErrorEnum.PARAM_FORMAT_ERROR.getCode(),e.getMessage());
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public <T> Result<T> handleException(HttpRequestMethodNotSupportedException e) {
        return ResultUtils.error(ErrorEnum.REQUEST_METHODS_NOT_SUPPORTED.getCode(),e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public <T> Result<T> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResultUtils.error(ErrorEnum.UNKNOWN_ERROR);
    }
}
