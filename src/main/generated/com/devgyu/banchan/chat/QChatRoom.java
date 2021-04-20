package com.devgyu.banchan.chat;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QChatRoom is a Querydsl query type for ChatRoom
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QChatRoom extends EntityPathBase<ChatRoom> {

    private static final long serialVersionUID = 1000766441L;

    public static final QChatRoom chatRoom = new QChatRoom("chatRoom");

    public final EnumPath<ChatRoomStatus> chatRoomStatus = createEnum("chatRoomStatus", ChatRoomStatus.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath sessionId = createString("sessionId");

    public QChatRoom(String variable) {
        super(ChatRoom.class, forVariable(variable));
    }

    public QChatRoom(Path<? extends ChatRoom> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatRoom(PathMetadata metadata) {
        super(ChatRoom.class, metadata);
    }

}

