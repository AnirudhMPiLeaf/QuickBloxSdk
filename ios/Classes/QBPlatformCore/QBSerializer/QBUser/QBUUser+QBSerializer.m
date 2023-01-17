//
//  QBUUser+QBSerializer.m
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBUUser+QBSerializer.h"
#import "NSDate+Helper.h"

const struct QBUserKeysStruct QBUserKey = {
    .id = @"id",
    .externalId = @"externalId",
    .twitterId = @"twitterId",
    .blobId = @"blobId",
    .customData = @"customData",
    .email = @"email",
    .facebookId = @"facebookId",
    .fullName = @"fullName",
    .login = @"login",
    .password = @"password",
    .newPassword = @"newPassword",
    .phone = @"phone",
    .tags = @"tags",
    .website = @"website",
    .lastRequestAt = @"lastRequestAt"
};

@implementation QBUUser (QBSerializer)

- (id)toQBResultData:(NSError *__autoreleasing *)error {
    NSMutableDictionary* info = [NSMutableDictionary dictionary];
    
    if (self.blobID != 0) {
        info[QBUserKey.blobId] = @(self.blobID);
    }
    
    if (self.customData.length) {
        info[QBUserKey.customData] = self.customData;
    }
    
    if (self.email.length) {
        info[QBUserKey.email] = self.email;
    }
    
    if (self.externalUserID != 0) {
        info[QBUserKey.externalId] = @(self.externalUserID).stringValue;
    }
    
    if (self.facebookID.length) {
        info[QBUserKey.facebookId] = self.facebookID;
    }
    
    if (self.fullName.length) {
        info[QBUserKey.fullName] = self.fullName;
    }
    
    if (self.ID != 0) {
        info[QBUserKey.id] = @(self.ID);
    }
    
    if (self.login.length) {
        info[QBUserKey.login] = self.login;
    }
    
    if (self.phone.length) {
        info[QBUserKey.phone] = self.phone;
    }
    
    if (self.tags.count > 0) {
        info[QBUserKey.tags] = self.tags;
    }
    
    if (self.twitterID.length) {
        info[QBUserKey.twitterId] = self.twitterID;
    }
    
    if (self.website.length) {
        info[QBUserKey.website] = self.website;
    }
    
    NSString *last_request_at = [NSDate stringFromISO8601Date:self.lastRequestAt];
    if (last_request_at != nil) {
        info[QBUserKey.lastRequestAt] = last_request_at;
    }
    
    return [info.copy toQBResultWithType:QBResultTypeDefault error:error];
}

@end
