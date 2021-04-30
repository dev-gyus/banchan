package com.devgyu.banchan.modules.counselor;

import com.devgyu.banchan.chatroom.ChatRoomReadStatus;
import com.devgyu.banchan.chatroom.ChatRoomStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CounselorDto {
    private String sessionId;
    private Long chatRoomId;
    private String nickname;
    private ChatRoomStatus chatRoomStatus;
    private ChatRoomReadStatus chatRoomReadStatus;
    private String regDate;

    public CounselorDto(String sessionId, Long chatRoomId, String nickname, ChatRoomStatus chatRoomStatus, ChatRoomReadStatus chatRoomReadStatus, String regDate) {
        this.sessionId = sessionId;
        this.chatRoomId = chatRoomId;
        this.nickname = nickname;
        this.chatRoomStatus = chatRoomStatus;
        this.chatRoomReadStatus = chatRoomReadStatus;
        this.regDate = regDate;
    }
}
