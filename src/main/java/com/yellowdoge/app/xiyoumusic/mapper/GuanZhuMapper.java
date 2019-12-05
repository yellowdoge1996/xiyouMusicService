package com.yellowdoge.app.xiyoumusic.mapper;

import com.yellowdoge.app.xiyoumusic.model.GuanZhu;
import com.yellowdoge.app.xiyoumusic.model.GuanZhuKey;
import com.yellowdoge.app.xiyoumusic.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GuanZhuMapper {
    int deleteByPrimaryKey(GuanZhuKey key);

    int insert(GuanZhu record);

    int insertSelective(GuanZhu record);

    GuanZhu selectByPrimaryKey(GuanZhuKey key);

    int updateByPrimaryKeySelective(GuanZhu record);

    int updateByPrimaryKey(GuanZhu record);

    @Select("select u.* from table_gz g join table_user u on g.bgzzxh=u.xh where gzzxh = '${xh}'")
    List<User> myGuanzhu(@Param(value = "xh") String xh);

    @Select("select u.* from table_gz g join table_user u on g.gzzxh=u.xh where bgzzxh = '${xh}'")
    List<User> myFans(@Param(value = "xh") String xh);
}