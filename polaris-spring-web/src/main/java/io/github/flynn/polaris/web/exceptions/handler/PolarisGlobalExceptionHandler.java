package io.github.flynn.polaris.web.exceptions.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.github.flynn.polaris.common.exceptions.AppException;
import io.github.flynn.polaris.common.models.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class PolarisGlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  Response<?> handleException(Exception e) {
    errorLog(e);
    return Response.failure(INTERNAL_SERVER_ERROR.value(), e.getMessage());
  }

  @ExceptionHandler(AppException.class)
  @ResponseStatus(BAD_REQUEST)
  Response<?> handleAppException(AppException e) {
    warnLog(e);
    return Response.failure(e.getErrorCode().getCode(), e.getErrorCode().getDesc() + "|" + e.getMessage());
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(BAD_REQUEST)
  Response<?> handleAppException(HttpRequestMethodNotSupportedException e) {
    warnLog(e);
    return Response.failure(BAD_REQUEST.value(), e.getMessage());
  }

  @ExceptionHandler({
      HttpMessageConversionException.class,
      MethodArgumentNotValidException.class,
      IllegalArgumentException.class,
      HttpMediaTypeNotAcceptableException.class,
      MissingServletRequestParameterException.class,
      MethodArgumentTypeMismatchException.class,
      HttpMessageNotReadableException.class,
      InvalidFormatException.class
  })
  @ResponseStatus(BAD_REQUEST)
  Response<?> handleMessageConvertException(Exception e) {
    warnLog(e);
    return Response.failure(BAD_REQUEST.value(), e.getMessage());
  }

  private void warnLog(Exception e) {
    log.warn("Unexpected exception caught when processing request, exception: ", e);
  }

  private void errorLog(Exception e) {
    log.error("Unexpected exception caught when processing request, exception: ", e);
  }
}
