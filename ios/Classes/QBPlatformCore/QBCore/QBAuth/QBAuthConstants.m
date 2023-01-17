//
//  QBAuthConstants.m
//  quickblox_sdk
//
//  Created by Injoit on 24.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBAuthConstants.h"

const struct AuthKeysStruct AuthKey = {
    .login = @"login",
    .email = @"email",
    .firebaseProjectId = @"firebaseProjectId",
    .password = @"password",
    .token = @"token",
    .user = @"user",
    .session = @"session",
};

const struct QBAuthSessionEventsStruct QBAuthSessionEvent = {
    .expireSession = @"SESSION_EXPIRED",
};

@implementation QBAuthConstants

@end
