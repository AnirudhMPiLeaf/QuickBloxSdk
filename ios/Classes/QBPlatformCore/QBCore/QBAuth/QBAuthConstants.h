//
//  QBAuthConstants.h
//  quickblox_sdk
//
//  Created by Injoit on 24.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>

struct AuthKeysStruct {
    __unsafe_unretained NSString * _Nonnull const login;
    __unsafe_unretained NSString * _Nonnull const email;
    __unsafe_unretained NSString * _Nonnull const firebaseProjectId;
    __unsafe_unretained NSString * _Nonnull const password;
    __unsafe_unretained NSString * _Nonnull const token;
    __unsafe_unretained NSString * _Nonnull const user;
    __unsafe_unretained NSString * _Nonnull const session;
};
extern const struct AuthKeysStruct AuthKey;

struct QBAuthSessionEventsStruct {
    __unsafe_unretained NSString * _Nonnull const expireSession;
};
extern const struct QBAuthSessionEventsStruct QBAuthSessionEvent;

NS_ASSUME_NONNULL_BEGIN

@interface QBAuthConstants : NSObject

@end

NS_ASSUME_NONNULL_END
