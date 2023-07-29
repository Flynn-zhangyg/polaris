package io.github.flynn.polaris.data.properties;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;

@AllArgsConstructor
@Data
public class PolarisDataSourceProperties {

  private MongoDataSources mongo;
  private RedisDataSources redis;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class MongoDataSources {
    private Map<String, MongoProperties> datasource;
    private String primary;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class RedisDataSources {
    private Map<String, RedisProperties> datasource;
    private String primary;
  }
}
