package io.github.flynn.polaris.web.controller;

import io.github.flynn.polaris.common.exceptions.AppException;
import io.github.flynn.polaris.common.models.Response;
import io.github.flynn.polaris.web.models.TestErrorCode;
import io.github.flynn.polaris.web.models.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestController {

  @GetMapping("/appException")
  Response<Void> appException() {
    throwException();
    return Response.ok();
  }

  private void throwException() {
    throw new AppException(TestErrorCode.TEST_ERROR_CODE, "test error msg");
  }

  @GetMapping("/exception")
  Response<Void> exception() {
    throw new RuntimeException("unknown error");
  }

  @GetMapping("/methodNotSupportedException")
  Response<Void> methodNotSupportedException() {
    return Response.ok();
  }

  @PostMapping("/bindException")
  Response<Void> bindException(@RequestBody User user) {
    return Response.ok();
  }

  @PostMapping("/validateException")
  Response<Void> validateException(@Validated @RequestBody User user) {
    return Response.ok();
  }

  @GetMapping("/requestParamException")
  Response<Void> validateException(@RequestParam("id") Long id) {
    return Response.ok();
  }
}
