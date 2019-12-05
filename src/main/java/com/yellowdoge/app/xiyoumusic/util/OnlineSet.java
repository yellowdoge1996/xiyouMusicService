package com.yellowdoge.app.xiyoumusic.util;

import java.util.concurrent.ConcurrentSkipListSet;

public class OnlineSet {
    private final static ConcurrentSkipListSet<String> onlineSet = new ConcurrentSkipListSet<>();

    public synchronized void registerUser(String xh) {
        onlineSet.add(xh);
    }

    public synchronized void removeUser(String xh) {
        onlineSet.remove(xh);
    }

    public int onlineCount(){
        return onlineSet.size();
    }

    public boolean isOnline(String xh){
        if (onlineSet.contains(xh)){
            return true;
        }else{
            return false;
        }
    }
}
