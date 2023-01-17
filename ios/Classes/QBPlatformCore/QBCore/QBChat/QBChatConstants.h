//
//  QBChatConstants.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>

struct QBChatKeysStruct {
    __unsafe_unretained NSString * _Nonnull const dialogId;
    __unsafe_unretained NSString * _Nonnull const dialogs;
    __unsafe_unretained NSString * _Nonnull const addUsers;
    __unsafe_unretained NSString * _Nonnull const removeUsers;
    __unsafe_unretained NSString * _Nonnull const messages;
    __unsafe_unretained NSString * _Nonnull const message;
    __unsafe_unretained NSString * _Nonnull const messageId;
    __unsafe_unretained NSString * _Nonnull const userId;
};
extern const struct QBChatKeysStruct QBChatKey;

struct QBMessageEventsStruct {
    __unsafe_unretained NSString * _Nonnull const receivedSystemMessage;
    __unsafe_unretained NSString * _Nonnull const receivedNewMessage;
};
extern const struct QBMessageEventsStruct QBMessageEvent;

struct QBMessageStatusEventsStruct {
    __unsafe_unretained NSString * _Nonnull const delivered;
    __unsafe_unretained NSString * _Nonnull const read;
};
extern const struct QBMessageStatusEventsStruct QBMessageStatusEvent;

struct QBChatConnectEventsStruct {
    __unsafe_unretained NSString * _Nonnull const connected;
    __unsafe_unretained NSString * _Nonnull const connectionClosed;
    __unsafe_unretained NSString * _Nonnull const connectionClosedOnError;
    __unsafe_unretained NSString * _Nonnull const reconnectionSuccessful;
    __unsafe_unretained NSString * _Nonnull const reconnectionFailed;
};
extern const struct QBChatConnectEventsStruct QBChatConnectEvent;

struct QBUserTypingEventsStruct {
    __unsafe_unretained NSString * _Nonnull const isTyping;
    __unsafe_unretained NSString * _Nonnull const stopTyping;
};
extern const struct QBUserTypingEventsStruct QBUserTypingEvent;

NS_ASSUME_NONNULL_BEGIN

@interface QBChatConstants : NSObject

@end

NS_ASSUME_NONNULL_END
