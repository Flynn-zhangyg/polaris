package io.github.flynn.polaris.test.containers.mapper;

import io.github.flynn.polaris.test.containers.models.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

  String TABLE_NAME = "testdb.user";

  @Select({
      "select * from",
      TABLE_NAME,
      "where id = #{id}"
  })
  User findById(@Param("id") long id);
}
