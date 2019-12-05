package com.yellowdoge.app.xiyoumusic.mapper;

import com.yellowdoge.app.xiyoumusic.model.ShouCang;
import com.yellowdoge.app.xiyoumusic.model.ShouCangKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ShouCangMapper {
    int deleteByPrimaryKey(ShouCangKey key);

    int insert(ShouCang record);

    int insertSelective(ShouCang record);

    ShouCang selectByPrimaryKey(ShouCangKey key);

    int updateByPrimaryKeySelective(ShouCang record);

    int updateByPrimaryKey(ShouCang record);

    @Select("select * from table_sc where xh = '${xh}'")
    List<ShouCang> getShoucang(@Param(value = "xh") String xh);
}