package com.yellowdoge.app.xiyoumusic.controller;

import com.yellowdoge.app.xiyoumusic.listener.SocketUserSet;
import com.yellowdoge.app.xiyoumusic.model.LiuYan;
import com.yellowdoge.app.xiyoumusic.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    @Autowired
    SocketUserSet socketUserSet;
    @Autowired
    ChatService chatService;

    @MessageMapping("/chatOut/{xh}")
    public void chatOut(@DestinationVariable(value = "xh")String xh) {
        System.out.println("下线：" + xh);
        if (socketUserSet.isOnline(xh))
        socketUserSet.removeUser(xh);
    }

    @MessageMapping("/getMessage/{xh}")
    public void getMessage(@DestinationVariable(value = "xh")String xh){
        chatService.getMessage(xh);
    }
    @MessageMapping("/chat")
    public void chat(LiuYan liuYan) {
        chatService.chat(liuYan);
    }
}
