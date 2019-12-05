package com.yellowdoge.app.xiyoumusic.mapper;

import com.yellowdoge.app.xiyoumusic.model.CommentBean;
import com.yellowdoge.app.xiyoumusic.model.PingLun;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PingLunMapper {
    int deleteByPrimaryKey(Long plid);

    int insert(PingLun record);

    int insertSelective(PingLun record);

    PingLun selectByPrimaryKey(Long plid);

    int updateByPrimaryKeySelective(PingLun record);

    int updateByPrimaryKey(PingLun record);

    List<CommentBean> getComment(long gqid);

    List<CommentBean> getCommentBean(long plid);
}