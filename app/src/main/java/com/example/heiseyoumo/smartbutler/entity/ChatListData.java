package com.example.heiseyoumo.smartbutler.entity;

/**
 * 对话列表实体
 */
public class ChatListData {

    //文本
    private String text;
    //type  区分左边还是右边
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
