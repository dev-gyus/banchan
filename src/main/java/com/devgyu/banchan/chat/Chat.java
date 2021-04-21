package com.devgyu.banchan.chat;

import com.devgyu.banchan.account.chatroom.ChatRoom;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    @Id @GeneratedValue
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    private String message;
    private LocalDateTime sendDate = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private ChatRole chatRole;

    public Chat(ChatRoom chatRoom, String message, ChatRole chatRole) {
        this.chatRoom = chatRoom;
        this.message = message;
        this.chatRole = chatRole;
        chatRoom.getChatList().add(this);
    }
}
