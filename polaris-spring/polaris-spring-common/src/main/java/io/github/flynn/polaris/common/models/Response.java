package io.github.flynn.polaris.common.models;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(NON_NULL)
public record Response<T>(Integer code, T data, String msg) {

  public Response(Integer code) {
    this(code, null, null);
  }

  public Response(Integer code, T data) {
    this(code, data, null);
  }

  public Response(Integer code, String msg) {
    this(code, null, msg);
  }

  public static <T> Response<T> ok(Integer code) {
    return new Response<>(code);
  }

  public static <T> Response<T> ok(Integer code, String msg) {
    return new Response<>(code, msg);
  }

  public static <T> Response<T> ok(Integer code, T data) {
    return new Response<>(code, data);
  }
  public static <T> Response<T> ok(T data) {
    return new Response<>(HttpStatus.OK.value(), data);
  }

  public static <T> Response<T> ok() {
    return new Response<>(HttpStatus.OK.value());
  }

  public static <T> Response<T> failure(Integer code) {
    return new Response<>(code);
  }

  public static <T> Response<T> failure(Integer code, String msg) {
    return new Response<>(code, msg);
  }

  public static <T> Response<T> failure(Integer code, T data) {
    return new Response<>(code, data);
  }
}
