package com.yellowdoge.app.xiyoumusic.mapper;

import com.yellowdoge.app.xiyoumusic.model.LiuYan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LiuYanMapper {
    int deleteByPrimaryKey(Long lyid);

    int insert(LiuYan record);

    int insertSelective(LiuYan record);

    LiuYan selectByPrimaryKey(Long lyid);

    int updateByPrimaryKeySelective(LiuYan record);

    int updateByPrimaryKey(LiuYan record);

    @Select("select * from table_ly where jszxh = '${xh}'")
    List<LiuYan> getMessages(@Param(value = "xh")String xh);
}