package com.devgyu.banchan.chat;

import lombok.Data;

@Data
public class ChatRegDateDto {
    private Chat chat;
    private String chatRoomRegDate;

    public ChatRegDateDto(Chat chat, String chatRoomRegDate) {
        this.chat = chat;
        this.chatRoomRegDate = chatRoomRegDate;
    }
}
