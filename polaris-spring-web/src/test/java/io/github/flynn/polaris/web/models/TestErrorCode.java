package io.github.flynn.polaris.web.models;

import io.github.flynn.polaris.common.models.ErrorCode;

public enum TestErrorCode implements ErrorCode {

  TEST_ERROR_CODE(100000, "test error");

  private final int code;
  private final String desc;

  TestErrorCode(int code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getDesc() {
    return desc;
  }
}
