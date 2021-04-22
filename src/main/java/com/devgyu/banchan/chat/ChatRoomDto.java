package com.devgyu.banchan.chat;

import lombok.Data;

@Data
public class ChatRoomDto {
    private String counselorNickname;
    private String sessionId;
    private String regDate;

    public ChatRoomDto(String counselorNickname, String sessionId, String regDate) {
        this.counselorNickname = counselorNickname;
        this.sessionId = sessionId;
        this.regDate = regDate;
    }
}
