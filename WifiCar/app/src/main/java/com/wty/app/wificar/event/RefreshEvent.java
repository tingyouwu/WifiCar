package com.wty.app.wificar.event;

public class RefreshEvent {

    private String msg;

    public RefreshEvent(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
