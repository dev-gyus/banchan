package com.devgyu.banchan.chat;

import com.devgyu.banchan.chatroom.ChatRoomReadStatus;
import com.devgyu.banchan.chatroom.ChatRoomStatus;
import lombok.Data;

@Data
public class ChatRoomDto {
    private String counselorNickname;
    private String sessionId;
    private String regDate;
    private ChatRoomStatus chatRoomStatus;
    private ChatRoomReadStatus chatRoomReadStatus;

    public ChatRoomDto(String counselorNickname, String sessionId, String regDate, ChatRoomStatus chatRoomStatus, ChatRoomReadStatus chatRoomReadStatus) {
        this.counselorNickname = counselorNickname;
        this.sessionId = sessionId;
        this.regDate = regDate;
        this.chatRoomStatus = chatRoomStatus;
        this.chatRoomReadStatus = chatRoomReadStatus;
    }
}
