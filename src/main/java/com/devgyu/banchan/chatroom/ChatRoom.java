package com.devgyu.banchan.chatroom;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.chat.Chat;
import com.devgyu.banchan.modules.counselor.Counselor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class ChatRoom {
    @Id @GeneratedValue
    @Column(name = "chat_room_id")
    private Long id;
    private String sessionId;
    @Enumerated(EnumType.STRING)
    private ChatRoomStatus chatRoomStatus = ChatRoomStatus.WAITING;
    @Enumerated(EnumType.STRING)
    private ChatRoomReadStatus chatRoomReadStatus = ChatRoomReadStatus.DEFAULT;
    private LocalDateTime regDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_id")
    private Counselor counselor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatRoom",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Chat> chatList = new ArrayList<>();

    public ChatRoom(Account account, String sessionId) {
        this.account = account;
        this.account.getChatRoomList().add(this);
        this.sessionId = sessionId;
    }

    public void changeCounselling(Counselor counselor){
        this.chatRoomStatus = ChatRoomStatus.COUNSELLING;
        this.counselor = counselor;
        counselor.getCounselList().add(this);
    }
}
