package com.beechannel.base.handler;

import com.beechannel.base.exception.BeeChannelException;
import com.beechannel.base.exception.RestErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.beechannel.base.exception.CommonError.UNKNOWN_ERROR;

/**
 * @description 全局异常处理器
 * @author eotouch
 * @date 2022/9/6 11:29
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	// Custom system exception
	@ExceptionHandler(BeeChannelException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestErrorResponse doXueChengPlusException(BeeChannelException e) {
		log.error("process error",e.getErrMessage(),e);
		return RestErrorResponse.of(e.getErrMessage());
	}

	// Argument not vail exception
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestErrorResponse doValidException(MethodArgumentNotValidException argumentNotValidException) {

		BindingResult bindingResult = argumentNotValidException.getBindingResult();
		StringBuffer errMsg = new StringBuffer();

		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		fieldErrors.forEach(error -> {
			errMsg.append(error.getDefaultMessage()).append(",");
		});
		log.error(errMsg.toString());
		return RestErrorResponse.of(errMsg.toString());
	}

	@ExceptionHandler(value = BindException.class)
	public RestErrorResponse exceptionHandle(BindException exception) {

		BindingResult result = exception.getBindingResult();
		StringBuilder errorMsg = new StringBuilder();

		List<FieldError> fieldErrors = result.getFieldErrors();
		fieldErrors.forEach(error -> {
			log.error("field: " + error.getField() + ", msg:" + error.getDefaultMessage());
			errorMsg.append(error.getDefaultMessage()).append("!");
		});
		return RestErrorResponse.of(errorMsg.toString());
	}


	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestErrorResponse exception(Exception e) {

		log.error("undefined error",e.getMessage(),e);
		e.printStackTrace();
		if(e.getMessage().equals("不允许访问")){
			return RestErrorResponse.of("no process permission");
		}
		return RestErrorResponse.of(UNKNOWN_ERROR.getErrMessage());


	}
}
