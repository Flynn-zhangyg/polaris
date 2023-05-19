package io.github.flynn.polaris.test.containers.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
@Builder
@Data
public class MongoUser {
  @Id
  private String id;
  private String name;
}
