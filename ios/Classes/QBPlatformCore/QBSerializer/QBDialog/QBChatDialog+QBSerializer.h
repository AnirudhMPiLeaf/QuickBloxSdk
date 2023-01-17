//
//  QBChatDialog+QBSerializer.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Quickblox/Quickblox.h>
#import "QBSerializerProtocol.h"

struct QBChatDialogKeysStruct {
    __unsafe_unretained NSString * _Nonnull const id;
    __unsafe_unretained NSString * _Nonnull const createdAt;
    __unsafe_unretained NSString * _Nonnull const updatedAt;
    __unsafe_unretained NSString * _Nonnull const roomJid;
    __unsafe_unretained NSString * _Nonnull const type;
    __unsafe_unretained NSString * _Nonnull const name;
    __unsafe_unretained NSString * _Nonnull const photo;
    __unsafe_unretained NSString * _Nonnull const lastMessage;
    __unsafe_unretained NSString * _Nonnull const lastMessageDateSent;
    __unsafe_unretained NSString * _Nonnull const lastMessageUserId;
    __unsafe_unretained NSString * _Nonnull const unreadMessagesCount;
    __unsafe_unretained NSString * _Nonnull const occupantsIds;
    __unsafe_unretained NSString * _Nonnull const userId;
    __unsafe_unretained NSString * _Nonnull const customData;
    __unsafe_unretained NSString * _Nonnull const isJoined;
    __unsafe_unretained NSString * _Nonnull const force;
};
extern const struct QBChatDialogKeysStruct QBDialogKey;

NS_ASSUME_NONNULL_BEGIN

@interface QBChatDialog (QBSerializer) <QBSerializerProtocol>

@end

NS_ASSUME_NONNULL_END
