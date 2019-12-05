package com.yellowdoge.app.xiyoumusic.mapper;

import com.yellowdoge.app.xiyoumusic.model.Dongtai;
import com.yellowdoge.app.xiyoumusic.model.Music;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MusicMapper {
    int deleteByPrimaryKey(Long gqid);

    int insert(Music record);

    int insertSelective(Music record);

    Music selectByPrimaryKey(Long gqid);

    int updateByPrimaryKeySelective(Music record);

    int updateByPrimaryKey(Music record);

    @Select("select distinct gqmc from table_music where gqmc like '${keyword}%'")
    List<String> getSuggestions(@Param(value = "keyword") String keyword);

    @Select("select * from table_music where gqmc like '%${keyword}%'")
    List<Music> searchMusic(@Param(value = "keyword") String keyword);

    @Select("select * from table_music where zzxh = '${xh}'")
    List<Music> myMusic(@Param(value = "xh") String xh);

    @Select("select * from table_gz g " +
            "join table_user u " +
            "on g.bgzzxh=u.xh " +
            "join table_music m " +
            "on u.xh = m.zzxh " +
            "where g.gzzxh = '${xh}' " +
            "ORDER BY m.gqid desc ")
    List<Dongtai> getDongtai(@Param(value = "xh")String xh);
}