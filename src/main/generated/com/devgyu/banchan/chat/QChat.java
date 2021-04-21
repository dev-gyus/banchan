package com.devgyu.banchan.chat;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChat is a Querydsl query type for Chat
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QChat extends EntityPathBase<Chat> {

    private static final long serialVersionUID = 1984179694L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChat chat = new QChat("chat");

    public final EnumPath<ChatRole> chatRole = createEnum("chatRole", ChatRole.class);

    public final com.devgyu.banchan.account.chatroom.QChatRoom chatRoom;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath message = createString("message");

    public final DateTimePath<java.time.LocalDateTime> sendDate = createDateTime("sendDate", java.time.LocalDateTime.class);

    public QChat(String variable) {
        this(Chat.class, forVariable(variable), INITS);
    }

    public QChat(Path<? extends Chat> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChat(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChat(PathMetadata metadata, PathInits inits) {
        this(Chat.class, metadata, inits);
    }

    public QChat(Class<? extends Chat> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new com.devgyu.banchan.account.chatroom.QChatRoom(forProperty("chatRoom"), inits.get("chatRoom")) : null;
    }

}

