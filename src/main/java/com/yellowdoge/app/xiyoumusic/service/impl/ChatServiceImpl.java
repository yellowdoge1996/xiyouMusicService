package com.yellowdoge.app.xiyoumusic.service.impl;

import com.yellowdoge.app.xiyoumusic.listener.SocketUserSet;
import com.yellowdoge.app.xiyoumusic.mapper.LiuYanMapper;
import com.yellowdoge.app.xiyoumusic.mapper.UserMapper;
import com.yellowdoge.app.xiyoumusic.model.LiuYan;
import com.yellowdoge.app.xiyoumusic.model.User;
import com.yellowdoge.app.xiyoumusic.service.ChatService;
import com.yellowdoge.app.xiyoumusic.util.MyException;
import com.yellowdoge.app.xiyoumusic.util.MyResponse;
import com.yellowdoge.app.xiyoumusic.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "chatService")
public class ChatServiceImpl implements ChatService {
    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    SocketUserSet socketUserSet;
    @Autowired
    UserMapper userMapper;
    @Autowired
    LiuYanMapper liuYanMapper;

    @Override
    public void chat(LiuYan liuYan) {
        User user = userMapper.selectByPrimaryKey(liuYan.getLyzxh());
        User user1 = userMapper.selectByPrimaryKey(liuYan.getJszxh());
        if (user == null || user1 == null){
            return;
        }
        liuYan.setLyid(SnowFlake.nextId());
        if (socketUserSet.isOnline(liuYan.getJszxh())){
            System.out.println(liuYan.getJszxh()+"在线");
            String destination = "/topic/"+liuYan.getJszxh();
            System.out.println(destination);
            messagingTemplate.convertAndSend(destination, liuYan);
        }else{
            System.out.println(liuYan.getJszxh()+"不在线");
            liuYanMapper.insert(liuYan);
        }
        String destination = "/topic/"+liuYan.getLyzxh();
        System.out.println(destination);
        messagingTemplate.convertAndSend(destination, liuYan);
    }

    @Override
    public void getMessage(String xh) {
        try {
            User user = userMapper.selectByPrimaryKey(xh);
            if (user == null){
                throw new MyException("请先登录");
            }
            if (!socketUserSet.isOnline(xh)){
                throw new MyException("危险操作");
            }
            List<LiuYan> liuYanList;
            liuYanList = liuYanMapper.getMessages(xh);
            MyResponse<List> myResponse = new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg("success");
            myResponse.setLyData(liuYanList);
            if(liuYanList.size()>0){
                for (LiuYan liuYan : liuYanList) {
                    liuYanMapper.deleteByPrimaryKey(liuYan.getLyid());
                }
            }
            messagingTemplate.convertAndSend("/queue/"+xh, myResponse);
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            messagingTemplate.convertAndSend("/queue/"+xh, myResponse);
        }
    }
}
