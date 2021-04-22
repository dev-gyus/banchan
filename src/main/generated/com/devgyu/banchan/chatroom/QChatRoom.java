package com.devgyu.banchan.chatroom;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoom is a Querydsl query type for ChatRoom
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QChatRoom extends EntityPathBase<ChatRoom> {

    private static final long serialVersionUID = -750854802L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatRoom chatRoom = new QChatRoom("chatRoom");

    public final com.devgyu.banchan.account.QAccount account;

    public final ListPath<com.devgyu.banchan.chat.Chat, com.devgyu.banchan.chat.QChat> chatList = this.<com.devgyu.banchan.chat.Chat, com.devgyu.banchan.chat.QChat>createList("chatList", com.devgyu.banchan.chat.Chat.class, com.devgyu.banchan.chat.QChat.class, PathInits.DIRECT2);

    public final EnumPath<ChatRoomReadStatus> chatRoomReadStatus = createEnum("chatRoomReadStatus", ChatRoomReadStatus.class);

    public final EnumPath<ChatRoomStatus> chatRoomStatus = createEnum("chatRoomStatus", ChatRoomStatus.class);

    public final com.devgyu.banchan.modules.counselor.QCounselor counselor;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final StringPath sessionId = createString("sessionId");

    public QChatRoom(String variable) {
        this(ChatRoom.class, forVariable(variable), INITS);
    }

    public QChatRoom(Path<? extends ChatRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatRoom(PathMetadata metadata, PathInits inits) {
        this(ChatRoom.class, metadata, inits);
    }

    public QChatRoom(Class<? extends ChatRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new com.devgyu.banchan.account.QAccount(forProperty("account"), inits.get("account")) : null;
        this.counselor = inits.isInitialized("counselor") ? new com.devgyu.banchan.modules.counselor.QCounselor(forProperty("counselor")) : null;
    }

}

