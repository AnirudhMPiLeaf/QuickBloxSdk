//
//  QBChatMessage+QBSerializer.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Quickblox/Quickblox.h>
#import "QBSerializerProtocol.h"

struct QBChatMessageKeysStruct {
    __unsafe_unretained NSString * _Nonnull const id;
    __unsafe_unretained NSString * _Nonnull const dialogId;
    __unsafe_unretained NSString * _Nonnull const body;
    __unsafe_unretained NSString * _Nonnull const dateSent;
    __unsafe_unretained NSString * _Nonnull const senderId;
    __unsafe_unretained NSString * _Nonnull const recipientId;
    __unsafe_unretained NSString * _Nonnull const readIds;
    __unsafe_unretained NSString * _Nonnull const deliveredIds;
    __unsafe_unretained NSString * _Nonnull const attachments;
    __unsafe_unretained NSString * _Nonnull const markable;
    __unsafe_unretained NSString * _Nonnull const delayed;
    __unsafe_unretained NSString * _Nonnull const properties;
    __unsafe_unretained NSString * _Nonnull const saveToHistory;
};
extern const struct QBChatMessageKeysStruct QBMessageKey;

NS_ASSUME_NONNULL_BEGIN

@interface QBChatMessage (QBSerializer) <QBSerializerProtocol>

@end

NS_ASSUME_NONNULL_END
