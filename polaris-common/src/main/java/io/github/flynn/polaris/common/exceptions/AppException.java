package io.github.flynn.polaris.common.exceptions;

import io.github.flynn.polaris.common.models.ErrorCode;

public class AppException extends RuntimeException{

  private final ErrorCode errorCode;
  private final String errorMsg;

  public AppException(ErrorCode errorCode, String errorMsg) {
    super();
    this.errorCode = errorCode;
    this.errorMsg = errorMsg;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  @Override
  public String getMessage() {
    return errorMsg;
  }
}
