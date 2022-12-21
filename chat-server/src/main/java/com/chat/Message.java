package com.chat;

public class Message {
    private String sender;
    private String type;
    /*
    NOTE:
        The types accepted for communications are:
        -[tx0](broadcast)
        -[tx1](private message)
        -[tx2](command)
    */ 
    private String receiver;
    private String body;
    
    public Message() {
    }

    public Message(String sender, String body) {
        this.sender = sender;
        this.type = null;
        this.receiver = null;
        this.body = body;
    }
    
    public Message(String sender, String type, String receiver, String body) {
        this.sender = sender;
        this.type = type;
        this.receiver = receiver;
        this.body = body;
    }
    
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    
}
