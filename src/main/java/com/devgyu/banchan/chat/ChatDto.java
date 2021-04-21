package com.devgyu.banchan.chat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatDto {
    private String nickname;
    private String message;
    private String sendDate;
    private ChatRole chatRole;
    private String sessionId;

    public ChatDto(String nickname, String message, String sendDate, ChatRole chatRole) {
        this.nickname = nickname;
        this.message = message;
        this.sendDate = sendDate;
        this.chatRole = chatRole;
    }
}
