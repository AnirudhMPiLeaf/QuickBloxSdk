//
//  QBMSubscription+QBSerializer.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Quickblox/Quickblox.h>
#import "QBSerializerProtocol.h"

NS_ASSUME_NONNULL_BEGIN

struct QBPushChannelKeysStruct {
    __unsafe_unretained NSString * const gcm;
    __unsafe_unretained NSString * const apns;
    __unsafe_unretained NSString * const apnsVoip;
    __unsafe_unretained NSString * const email;
};

extern const struct QBPushChannelKeysStruct QBPushChannelKey;

struct QBMSubscriptionKeysStruct {
    __unsafe_unretained NSString * const id;
    __unsafe_unretained NSString * const deviceToken;
    __unsafe_unretained NSString * const deviceUdid;
    __unsafe_unretained NSString * const devicePlatform;
    __unsafe_unretained NSString * const pushChannel;
};

extern const struct QBMSubscriptionKeysStruct QBMSubscriptionKey;

@interface QBMSubscription (QBSerializer) <QBSerializerProtocol>

+ (NSString *)pushChannelWithType:(QBMNotificationChannel)type;
+ (QBMNotificationChannel)typeWithPushChannel:(NSString *)pushChannel;

@end

NS_ASSUME_NONNULL_END
