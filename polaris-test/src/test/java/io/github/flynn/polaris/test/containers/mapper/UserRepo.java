package io.github.flynn.polaris.test.containers.mapper;

import io.github.flynn.polaris.test.containers.models.MongoUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<MongoUser, String> {
}
