//
//  QBSession+QBSerializer.m
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBSession+QBSerializer.h"
#import "NSDate+Helper.h"

const struct QBSessionKeysStruct QBSessionKey = {
    .token = @"token",
    .expirationDate = @"expirationDate",
    .userId = @"userId",
    .applicationId = @"applicationId",
};

@implementation QBSession (QBSerializer)

- (id)toQBResultData:(NSError *__autoreleasing *)error {
    NSMutableDictionary *info = @{}.mutableCopy;
    
    if (self.sessionDetails.token.length) {
        info[QBSessionKey.token] = self.sessionDetails.token;
    }
    
    NSString *expirationDate = [NSDate stringFromISO8601Date:self.sessionExpirationDate];
    if (expirationDate.length) {
        info[QBSessionKey.expirationDate] = expirationDate;
    }
  
  if (self.sessionDetails.userID) {
    info[QBSessionKey.userId] = @(self.sessionDetails.userID);
  }
  
  if (self.sessionDetails.applicationID) {
    info[QBSessionKey.applicationId] = @(self.sessionDetails.applicationID);
  }
    
    return [info.copy toQBResultWithType:QBResultTypeDefault error:error];
}

@end
