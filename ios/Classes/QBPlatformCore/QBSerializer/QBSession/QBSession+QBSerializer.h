//
//  QBSession+QBSerializer.h
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Quickblox/Quickblox.h>
#import "QBSerializerProtocol.h"

struct QBSessionKeysStruct {
    __unsafe_unretained NSString * _Nonnull const token;
    __unsafe_unretained NSString * _Nonnull const expirationDate;
    __unsafe_unretained NSString * _Nonnull const userId;
    __unsafe_unretained NSString * _Nonnull const applicationId;
};
extern const struct QBSessionKeysStruct QBSessionKey;

NS_ASSUME_NONNULL_BEGIN

@interface QBSession (QBSerializer) <QBSerializerProtocol>

@end

NS_ASSUME_NONNULL_END
