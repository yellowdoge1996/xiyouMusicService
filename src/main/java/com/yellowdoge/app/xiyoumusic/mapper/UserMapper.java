package com.yellowdoge.app.xiyoumusic.mapper;

import com.yellowdoge.app.xiyoumusic.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(String xh);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String xh);

    User selectByUUID(String uuid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    @Select("select distinct nc from table_user where nc like '${keyword}%'")
    List<String> getSuggestions(@Param(value = "keyword") String keyword);

    @Select("select * from table_user where nc like '%${keyword}%'")
    List<User> searchUser(@Param(value = "keyword") String keyword);
}