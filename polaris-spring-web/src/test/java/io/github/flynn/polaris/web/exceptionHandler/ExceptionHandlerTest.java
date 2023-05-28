package io.github.flynn.polaris.web.exceptionHandler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.flynn.polaris.web.controller.TestController;
import io.github.flynn.polaris.web.exceptions.handler.PolarisGlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@Import({
    PolarisGlobalExceptionHandler.class,
    TestController.class,
    PolarisGlobalExceptionHandler.class
})
public class ExceptionHandlerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldCatchAppException() throws Exception {

    mockMvc.perform(get("/test/appException"))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.code").value(100000))
        .andExpect(jsonPath("$.msg").value("test error|test error msg"))
    ;
  }

  @Test
  void shouldCatchException() throws Exception {

    mockMvc.perform(get("/test/exception"))
        .andExpect(status().is(500))
        .andExpect(jsonPath("$.code").value(500))
        .andExpect(jsonPath("$.msg").value("unknown error"))
    ;
  }

  @Test
  void shouldCatchMethodNotSupportedException() throws Exception {

    mockMvc.perform(post("/test/methodNotSupportedException"))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.code").value(400));
  }

  @Test
  void shouldCatchBindException() throws Exception {

    mockMvc.perform(post("/test/bindException")
            .accept("application/json")
            .contentType("application/json")
            .content("{\"id\": \"qwe\"}"))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.code").value(400));
  }

  @Test
  void shouldCatchValidateException() throws Exception {

    mockMvc.perform(post("/test/validateException")
            .accept("application/json")
            .contentType("application/json")
            .content("{\"name\": \"qwe\"}"))
        .andExpect(status().is(400))
        .andDo(print())
        .andExpect(jsonPath("$.code").value(400));
  }

  @Test
  void shouldCatchMissingRequestParamException() throws Exception {

    mockMvc.perform(get("/test/requestParamException")
            .accept("application/json")
            .param("name", "qwe"))
        .andExpect(status().is(400))
        .andDo(print())
        .andExpect(jsonPath("$.code").value(400));
  }

  @Test
  void shouldCatchRequestParamBindException() throws Exception {

    mockMvc.perform(get("/test/requestParamException")
            .accept("application/json")
            .param("id", "qwe"))
        .andExpect(status().is(400))
        .andDo(print())
        .andExpect(jsonPath("$.code").value(400));
  }
}
