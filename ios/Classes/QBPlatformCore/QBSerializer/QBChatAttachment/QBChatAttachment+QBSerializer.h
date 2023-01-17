//
//  QBChatAttachment+QBSerializer.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Quickblox/Quickblox.h>
#import "QBSerializerProtocol.h"

struct QBChatAttachmentKeysStruct {
    __unsafe_unretained NSString * _Nonnull const id;
    __unsafe_unretained NSString * _Nonnull const name;
    __unsafe_unretained NSString * _Nonnull const contentType;
    __unsafe_unretained NSString * _Nonnull const type;
    __unsafe_unretained NSString * _Nonnull const url;
    __unsafe_unretained NSString * _Nonnull const data;
    __unsafe_unretained NSString * _Nonnull const size;
    __unsafe_unretained NSString * _Nonnull const height;
    __unsafe_unretained NSString * _Nonnull const width;
    __unsafe_unretained NSString * _Nonnull const duration;
};
extern const struct QBChatAttachmentKeysStruct QBAttachmentKey;

NS_ASSUME_NONNULL_BEGIN

@interface QBChatAttachment (QBSerializer) <QBSerializerProtocol>

+ (NSArray<QBChatAttachment *> *)attachmentsWithInfo:(NSArray<NSDictionary<NSString *,id> *> *)info;

@end

NS_ASSUME_NONNULL_END
