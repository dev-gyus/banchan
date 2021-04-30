package com.devgyu.banchan.chat;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatApiDto {
    private List<ChatDto> chatDtoList = new ArrayList<>();
    private boolean last;

    public ChatApiDto(List<ChatDto> chatDtoList, boolean last) {
        this.chatDtoList = chatDtoList;
        this.last = last;
    }
}
