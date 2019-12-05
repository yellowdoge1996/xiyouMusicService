package com.yellowdoge.app.xiyoumusic.mapper;

import com.yellowdoge.app.xiyoumusic.model.DianZan;
import com.yellowdoge.app.xiyoumusic.model.DianZanKey;

public interface DianZanMapper {
    int deleteByPrimaryKey(DianZanKey key);

    int insert(DianZan record);

    int insertSelective(DianZan record);

    DianZan selectByPrimaryKey(DianZanKey key);

    int updateByPrimaryKeySelective(DianZan record);

    int updateByPrimaryKey(DianZan record);
}