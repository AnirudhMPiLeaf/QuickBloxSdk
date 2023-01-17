//
//  QBUUser+QBSerializer.h
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Quickblox/Quickblox.h>
#import "QBSerializerProtocol.h"

struct QBUserKeysStruct {
    __unsafe_unretained NSString * _Nonnull const id;
    __unsafe_unretained NSString * _Nonnull const externalId;
    __unsafe_unretained NSString * _Nonnull const twitterId;
    __unsafe_unretained NSString * _Nonnull const blobId;
    __unsafe_unretained NSString * _Nonnull const customData;
    __unsafe_unretained NSString * _Nonnull const email;
    __unsafe_unretained NSString * _Nonnull const facebookId;
    __unsafe_unretained NSString * _Nonnull const fullName;
    __unsafe_unretained NSString * _Nonnull const login;
    __unsafe_unretained NSString * _Nonnull const password;
    __unsafe_unretained NSString * _Nonnull const newPassword;
    __unsafe_unretained NSString * _Nonnull const phone;
    __unsafe_unretained NSString * _Nonnull const tags;
    __unsafe_unretained NSString * _Nonnull const website;
    __unsafe_unretained NSString * _Nonnull const lastRequestAt;
};
extern const struct QBUserKeysStruct QBUserKey;

NS_ASSUME_NONNULL_BEGIN

@interface QBUUser (QBSerializer) <QBSerializerProtocol>

@end

NS_ASSUME_NONNULL_END
