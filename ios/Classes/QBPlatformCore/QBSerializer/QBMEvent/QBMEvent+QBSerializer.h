//
//  QBMEvent+QBSerializer.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Quickblox/Quickblox.h>
#import "QBSerializerProtocol.h"

typedef NS_ENUM(NSInteger, PUSH_TYPE) {
    APNS = 1,
    APNS_VOIP = 2,
    GCM = 3,
    MPNS = 4
};

typedef NS_ENUM(NSInteger, EVENT_PERIOD) {
    DAY_EVENT_PERIOD = 86400,
    WEEK_EVENT_PERIOD = 604800,
    MONTH_EVENT_PERIOD = 2592000,
    YEAR_EVENT_PERIOD = 31557600
};

struct QBMEventKeysStruct {
    __unsafe_unretained NSString * _Nonnull const id;
    __unsafe_unretained NSString * _Nonnull const active;
    __unsafe_unretained NSString * _Nonnull const name;
    __unsafe_unretained NSString * _Nonnull const type;
    __unsafe_unretained NSString * _Nonnull const notificationType;
    __unsafe_unretained NSString * _Nonnull const pushType;
    __unsafe_unretained NSString * _Nonnull const date;
    __unsafe_unretained NSString * _Nonnull const endDate;
    __unsafe_unretained NSString * _Nonnull const period;
    __unsafe_unretained NSString * _Nonnull const occuredCount;
    __unsafe_unretained NSString * _Nonnull const senderId;
    __unsafe_unretained NSString * _Nonnull const recipientsIds;
    __unsafe_unretained NSString * _Nonnull const recipientsTagsAny;
    __unsafe_unretained NSString * _Nonnull const recipientsTagsAll;
    __unsafe_unretained NSString * _Nonnull const recipientsTagsExclude;
    __unsafe_unretained NSString * _Nonnull const payload;
};

extern const struct QBMEventKeysStruct QBMEventKey;

struct QBMEventTypeKeysStruct {
    __unsafe_unretained NSString * _Nonnull const fixedDate;
    __unsafe_unretained NSString * _Nonnull const periodDate;
    __unsafe_unretained NSString * _Nonnull const oneShot;
};

extern const struct QBMEventTypeKeysStruct QBMEventTypeKey;

struct QBMEventNotificationTypeKeysStruct {
    __unsafe_unretained NSString * _Nonnull const push;
    __unsafe_unretained NSString * _Nonnull const email;
};

extern const struct QBMEventNotificationTypeKeysStruct QBMEventNotificationTypeKey;

NS_ASSUME_NONNULL_BEGIN

@interface QBMEvent (QBSerializer) <QBSerializerProtocol>

+ (NSDictionary *)eventTypes;
+ (NSDictionary *)notificationTypes;
+ (NSDictionary *)pushTypes;
+ (NSDictionary *)eventPeriods;

+ (PUSH_TYPE)pushTypeToInteger:(QBMPushType)pushType;
+ (QBMPushType)pushTypeFromInteger:(PUSH_TYPE)pushType;
+ (EVENT_PERIOD)periodFromInteger:(NSUInteger)period;

@end

NS_ASSUME_NONNULL_END
