//
//  QBChatConstants.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatConstants.h"

const struct QBChatKeysStruct QBChatKey = {
    .dialogId = @"dialogId",
    .dialogs = @"dialogs",
    .addUsers = @"addUsers",
    .removeUsers = @"removeUsers",
    .messages = @"messages",
    .message = @"message",
    .messageId = @"messageId",
    .userId = @"userId",
};

const struct QBMessageEventsStruct QBMessageEvent = {
    .receivedSystemMessage = @"RECEIVED_SYSTEM_MESSAGE",
    .receivedNewMessage = @"RECEIVED_NEW_MESSAGE",
};

const struct QBMessageStatusEventsStruct QBMessageStatusEvent = {
    .delivered = @"MESSAGE_DELIVERED",
    .read = @"MESSAGE_READ",
};

const struct QBChatConnectEventsStruct QBChatConnectEvent = {
    .connected = @"CONNECTED",
    .connectionClosed = @"CONNECTION_CLOSED",
    .connectionClosedOnError = @"CONNECTION_CLOSED_ON_ERROR",
    .reconnectionSuccessful = @"RECONNECTION_SUCCESSFUL",
    .reconnectionFailed = @"RECONNECTION_FAILED",
};

const struct QBUserTypingEventsStruct QBUserTypingEvent = {
    .isTyping = @"USER_IS_TYPING",
    .stopTyping = @"USER_STOPPED_TYPING",
};

@implementation QBChatConstants

@end
