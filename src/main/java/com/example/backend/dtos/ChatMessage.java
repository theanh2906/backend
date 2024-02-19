package com.example.backend.dtos;

public class ChatMessage {
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
    private MessageType type;
    private String content;
    private String sender;
    private Long time;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
