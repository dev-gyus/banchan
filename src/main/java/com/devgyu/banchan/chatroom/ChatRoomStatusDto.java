package com.devgyu.banchan.chatroom;

import lombok.Data;

@Data
public class ChatRoomStatusDto {
    private ChatRoomStatus chatRoomStatus;
    private ChatRoomReadStatus chatRoomReadStatus;

    public ChatRoomStatusDto(ChatRoomStatus chatRoomStatus, ChatRoomReadStatus chatRoomReadStatus) {
        this.chatRoomStatus = chatRoomStatus;
        this.chatRoomReadStatus = chatRoomReadStatus;
    }
}
