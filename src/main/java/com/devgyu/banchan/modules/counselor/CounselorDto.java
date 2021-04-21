package com.devgyu.banchan.modules.counselor;

import lombok.Data;

@Data
public class CounselorDto {
    private String sessionId;
    private Long chatRoomId;
    private String nickname;

    public CounselorDto(String sessionId, Long chatRoomId, String nickname) {
        this.sessionId = sessionId;
        this.chatRoomId = chatRoomId;
        this.nickname = nickname;
    }
}
