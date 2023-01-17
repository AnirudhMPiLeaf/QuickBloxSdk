//
//  QBCBlob+QBSerializer.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Quickblox/Quickblox.h>
#import "QBSerializerProtocol.h"

NS_ASSUME_NONNULL_BEGIN

struct QBCBlobKeysStruct {
    __unsafe_unretained NSString * const id;
    __unsafe_unretained NSString * const uid;
    __unsafe_unretained NSString * const contentType;
    __unsafe_unretained NSString * const name;
    __unsafe_unretained NSString * const size;
    __unsafe_unretained NSString * const completedAt;
    __unsafe_unretained NSString * const isPublic;
    __unsafe_unretained NSString * const lastReadAccessTime;
    __unsafe_unretained NSString * const tags;
};

extern const struct QBCBlobKeysStruct QBCBlobKey;

@interface QBCBlob (QBSerializer) <QBSerializerProtocol>

@end

NS_ASSUME_NONNULL_END
