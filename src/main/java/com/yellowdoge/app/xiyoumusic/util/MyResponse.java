package com.yellowdoge.app.xiyoumusic.util;

import java.util.List;

public class MyResponse<T extends List> {
    private String state = null;
    private String msg = null;
    private String error = null;
    private T userData = null;
    private T gzData = null;

    private T musicData = null;
    private T scDate = null;
    private T dzData = null;
    private T plData = null;
    private T hfData = null;

    private T lyData = null;
    private T commentData = null;
    private T dongtaiData = null;
    private T huifuData = null;
    public MyResponse() {
    }

    public T getHuifuData() {
        return huifuData;
    }

    public void setHuifuData(T huifuData) {
        this.huifuData = huifuData;
    }

    public T getDongtaiData() {
        return dongtaiData;
    }

    public void setDongtaiData(T dongtaiData) {
        this.dongtaiData = dongtaiData;
    }

    public T getCommentData() {
        return commentData;
    }

    public void setCommentData(T commentData) {
        this.commentData = commentData;
    }

    public T getScDate() {
        return scDate;
    }

    public void setScDate(T scDate) {
        this.scDate = scDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getUserData() {
        return userData;
    }

    public void setUserData(T userData) {
        this.userData = userData;
    }

    public T getGzData() {
        return gzData;
    }

    public void setGzData(T gzData) {
        this.gzData = gzData;
    }

    public T getMusicData() {
        return musicData;
    }

    public void setMusicData(T musicData) {
        this.musicData = musicData;
    }

    public T getDzData() {
        return dzData;
    }

    public void setDzData(T dzData) {
        this.dzData = dzData;
    }

    public T getPlData() {
        return plData;
    }

    public void setPlData(T plData) {
        this.plData = plData;
    }

    public T getHfData() {
        return hfData;
    }

    public void setHfData(T hfData) {
        this.hfData = hfData;
    }

    public T getLyData() {
        return lyData;
    }

    public void setLyData(T lyData) {
        this.lyData = lyData;
    }
}
