package io.github.flynn.polaris.data.anno;

import io.github.flynn.polaris.data.configuration.RedisMultiDataSourceConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@ImportAutoConfiguration(
    value = RedisMultiDataSourceConfiguration.class,
    exclude = {
        RedisAutoConfiguration.class
    }
)
public @interface EnableRedisMultiDataSource {
}
