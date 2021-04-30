package com.devgyu.banchan.modules.counselor;

import com.devgyu.banchan.chatroom.ChatRoomStatus;
import lombok.Data;

@Data
public class StatusNicknameDto {
    private ChatRoomStatus previousStatus;
    private String accountNickname;

    public StatusNicknameDto(ChatRoomStatus previousStatus, String accountNickname) {
        this.previousStatus = previousStatus;
        this.accountNickname = accountNickname;
    }
}
