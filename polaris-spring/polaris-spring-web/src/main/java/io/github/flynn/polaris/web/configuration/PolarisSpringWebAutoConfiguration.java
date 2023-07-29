package io.github.flynn.polaris.web.configuration;

import io.github.flynn.polaris.web.exceptions.handler.PolarisGlobalExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(PolarisGlobalExceptionHandler.class)
public class PolarisSpringWebAutoConfiguration {
}
