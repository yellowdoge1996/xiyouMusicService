package com.yellowdoge.app.xiyoumusic.listener;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SocketUserSet {
    private final static Set<String> userSet = new HashSet<>();

    /**
     * 注册User
     * @param xh
     */
    public synchronized void registerUser(String xh) {
        userSet.add(xh);
    }

    /**
     * 移除User
     * @param xh
     */
    public synchronized void removeUser(String xh) {
        userSet.remove(xh);
    }

    /**
     * 判断用户是否在线
     * @param xh
     * @return
     */
    public synchronized boolean isOnline(String xh){
        return userSet.contains(xh);
    }
    /**
     * 获取集合的大小
     */
    public int onlineCount(){
        return userSet.size();
    }
}
