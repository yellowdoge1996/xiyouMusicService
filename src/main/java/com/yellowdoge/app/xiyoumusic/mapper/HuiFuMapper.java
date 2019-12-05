package com.yellowdoge.app.xiyoumusic.mapper;

import com.yellowdoge.app.xiyoumusic.model.HuiFu;
import com.yellowdoge.app.xiyoumusic.model.HuifuDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface HuiFuMapper {
    int deleteByPrimaryKey(Long hfid);

    int insert(HuiFu record);

    int insertSelective(HuiFu record);

    HuiFu selectByPrimaryKey(Long hfid);

    int updateByPrimaryKeySelective(HuiFu record);

    int updateByPrimaryKey(HuiFu record);

    @Select("SELECT hfid, hfnr, plid, hfzxh, hfmbid, bhfzxh, " +
            "u.txlj as hfztxlj, u.nc as hfznc, u.sr as hfzsr, " +
            " u.gxqm as hfzgxqm, bu.txlj as bhfztxlj, bu.nc as bhfznc," +
            " bu.sr as bhfzsr, bu.gxqm as bhfzgxqm" +
            " FROM table_hf" +
            " join table_user bu on table_hf.bhfzxh = bu.xh" +
            " join table_user u on table_hf.hfzxh = u.xh" +
            "  where bhfzxh='${xh}'")
    List<HuifuDetails> getHuifu(@Param(value = "xh")String xh);
}