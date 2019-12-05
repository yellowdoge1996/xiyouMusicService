package com.yellowdoge.app.xiyoumusic.util;

public class MyException extends RuntimeException {
    private String errorCode;
    private String msg;

    public MyException(String message) {
        super(message);
    }

    public MyException(String errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
