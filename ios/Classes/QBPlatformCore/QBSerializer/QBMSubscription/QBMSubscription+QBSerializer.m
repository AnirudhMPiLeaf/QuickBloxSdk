//
//  QBMSubscription+QBSerializer.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBMSubscription+QBSerializer.h"
#import "NSDictionary+QBSerializer.h"

const struct QBPushChannelKeysStruct QBPushChannelKey = {
    .gcm = @"gcm",
    .apns = @"apns",
    .apnsVoip = @"apns_voip",
    .email = @"email"
};

const struct QBMSubscriptionKeysStruct QBMSubscriptionKey = {
    .id = @"id",
    .deviceToken = @"deviceToken",
    .deviceUdid = @"deviceUdid",
    .devicePlatform = @"devicePlatform",
    .pushChannel = @"pushChannel"
};

@implementation QBMSubscription (QBSerializer)

+ (NSString *)pushChannelWithType:(QBMNotificationChannel)type {
    switch (type) {
        case QBMNotificationChannelGCM: return QBPushChannelKey.gcm;
        case QBMNotificationChannelAPNS: return QBPushChannelKey.apns;
        case QBMNotificationChannelAPNSVOIP: return QBPushChannelKey.apnsVoip;
        case QBMNotificationChannelEmail: return QBPushChannelKey.email;
        default: return @"";
    }
}

+ (QBMNotificationChannel)typeWithPushChannel:(NSString *)pushChannel {
    if ([pushChannel isEqualToString:QBPushChannelKey.gcm]) {
        return QBMNotificationChannelGCM;
    } else if ([pushChannel isEqualToString:QBPushChannelKey.apns]) {
        return QBMNotificationChannelAPNS;
    } else if ([pushChannel isEqualToString:QBPushChannelKey.apnsVoip]) {
        return QBMNotificationChannelAPNSVOIP;
    } else if ([pushChannel isEqualToString:QBPushChannelKey.email]) {
        return QBMNotificationChannelEmail;
    } else {
        return QBMNotificationChannelAPNS;
    }
}

- (id)toQBResultData:(NSError *__autoreleasing *)error {
    
    NSMutableDictionary* info = [NSMutableDictionary dictionary];
    
    if (self.ID != 0) {
        info[QBMSubscriptionKey.id] = @(self.ID);
    }
    
    if (self.deviceToken.length) {
        info[QBMSubscriptionKey.deviceToken] = self.deviceToken;
    }
    
    if (self.deviceUDID.length) {
        info[QBMSubscriptionKey.deviceUdid] = self.deviceUDID;
    }
    
    if (self.devicePlatform.length) {
        info[QBMSubscriptionKey.devicePlatform] = self.devicePlatform;
    }
    
    if (self.notificationChannel != QBMNotificationChannelMPNS) {
        NSString *pushChannel =
        [QBMSubscription pushChannelWithType:self.notificationChannel];
        if (pushChannel.length) {
            info[QBMSubscriptionKey.pushChannel] = pushChannel;
        }
    }
    
    return [info.copy toQBResultWithType:QBResultTypeDefault error:error];
    
}

@end
