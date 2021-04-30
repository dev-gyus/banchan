package com.devgyu.banchan.chat;

import com.devgyu.banchan.chatroom.ChatRoomStatus;
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
    private ChatRoomStatus chatRoomStatus;
    private String regDate;

    public ChatDto(String nickname, String message, String sendDate, ChatRole chatRole, String sessionId) {
        this.nickname = nickname;
        this.message = message;
        this.sendDate = sendDate;
        this.chatRole = chatRole;
        this.sessionId = sessionId;
    }

    public ChatDto(String nickname, String message, String sendDate, ChatRole chatRole, String sessionId, String regDate) {
        this.nickname = nickname;
        this.message = message;
        this.sendDate = sendDate;
        this.chatRole = chatRole;
        this.sessionId = sessionId;
        this.regDate = regDate;
    }
}
