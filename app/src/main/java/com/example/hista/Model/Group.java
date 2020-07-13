package com.example.hista.Model;

import com.google.firebase.database.DataSnapshot;

public class Group {
    private String msgDate;
    private String msg;
    private String msgOwner;
    private String msgTime;

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgOwner() {
        return msgOwner;
    }

    public void setMsgOwner(String msgOwner) {
        this.msgOwner = msgOwner;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }
}
