//
//  QBPushSubscriptionsModule.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBPushSubscriptionsModule.h"
#import <UIKit/UIKit.h>

@implementation QBPushSubscriptionsModule

- (void)create:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject {
    NSString *deviceToken = info[QBMSubscriptionKey.deviceToken];
    
    
    NSData *tokenData = [self dataFromHexString:deviceToken];
    NSString *pushChannel = info[QBMSubscriptionKey.pushChannel];
    
    
    NSString *deviceIdentifier =
    [[[UIDevice currentDevice] identifierForVendor] UUIDString];
    
    QBMSubscription *subscription = [QBMSubscription subscription];
    subscription.notificationChannel = QBMNotificationChannelAPNS;
    subscription.deviceUDID = deviceIdentifier;
    subscription.deviceToken = tokenData;
    if (pushChannel.length) {
        QBMNotificationChannel channel = [QBMSubscription typeWithPushChannel:pushChannel];
        subscription.notificationChannel = channel;
    }
    
    [QBRequest createSubscription:subscription
                     successBlock:^(QBResponse *response,
                                    NSArray *objects) {
                         [objects toQBResultArrayWithResolver:resolve rejecter:reject];
                     } errorBlock:^(QBResponse *response) {
                         [response reject:reject];
                     }];
}

- (void)get:(QBResolveBlock)resolve
   rejecter:(QBRejectBlock)reject {
    [QBRequest subscriptionsWithSuccessBlock:^(QBResponse * _Nonnull response,
                                                  NSArray<QBMSubscription *> * _Nullable objects) {
           [objects toQBResultArrayWithResolver:resolve rejecter:reject];
       } errorBlock:^(QBResponse * _Nonnull response) {
           [response reject:reject];
       }];
}

- (void)remove:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject {
    NSObject *subscriptionIdObject = info[QBMSubscriptionKey.id];
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:subscriptionIdObject
              objectKey:QBMSubscriptionKey.id]) {
        return;
    }
    NSNumber *subscriptionId = (NSNumber *)subscriptionIdObject;
    [QBRequest deleteSubscriptionWithID:subscriptionId.unsignedIntegerValue successBlock:^(QBResponse * _Nonnull response) {
        if (resolve) {
            resolve(nil);
        }
    } errorBlock:^(QBResponse * _Nonnull response) {
        [response reject:reject];
    }];
}

- (NSData *)dataFromHexString:(NSString *)string
{
    string = [string lowercaseString];
    NSMutableData *data= [NSMutableData new];
    unsigned char whole_byte;
    char byte_chars[3] = {'\0','\0','\0'};
    NSUInteger   i = 0;
    NSUInteger length = string.length;
    while (i < length-1) {
        char c = [string characterAtIndex:i++];
        if (c < '0' || (c > '9' && c < 'a') || c > 'f')
            continue;
        byte_chars[0] = c;
        byte_chars[1] = [string characterAtIndex:i++];
        whole_byte = strtol(byte_chars, NULL, 16);
        [data appendBytes:&whole_byte length:1];
    }
    return data;
}

@end
