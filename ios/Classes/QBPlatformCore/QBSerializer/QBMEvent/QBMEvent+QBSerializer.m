//
//  QBMEvent+QBSerializer.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBMEvent+QBSerializer.h"

const struct QBMEventKeysStruct QBMEventKey = {
    .id = @"id",
    .active = @"active",
    .name = @"name",
    .type = @"type",
    .notificationType = @"notificationType",
    .pushType = @"pushType",
    .date = @"date",
    .endDate = @"endDate",
    .period = @"period",
    .occuredCount = @"occuredCount",
    .senderId = @"senderId",
    .recipientsIds = @"recipientsIds",
    .recipientsTagsAny = @"recipientsTagsAny",
    .recipientsTagsAll = @"recipientsTagsAll",
    .recipientsTagsExclude = @"recipientsTagsExclude",
    .payload = @"payload"
};

const struct QBMEventTypeKeysStruct QBMEventTypeKey = {
    .fixedDate = @"fixed_date",
    .periodDate = @"period_date",
    .oneShot = @"one_shot"
};

const struct QBMEventNotificationTypeKeysStruct QBMEventNotificationTypeKey = {
    .push = @"push",
    .email = @"email"
};

@implementation QBMEvent (QBSerializer)

+ (NSDictionary *)eventTypes {
    static NSDictionary* _eventTypes = nil;
    static dispatch_once_t eventTypesToken;
    dispatch_once(&eventTypesToken, ^{
        _eventTypes = @{ @"FIXED_DATE": QBMEventTypeKey.fixedDate,
                         @"PERIOD_DATE": QBMEventTypeKey.periodDate,
                         @"ONE_SHOT": QBMEventTypeKey.oneShot };
    });
    return _eventTypes;
}

+ (NSDictionary *)notificationTypes {
    static NSDictionary* _notificationTypes = nil;
    static dispatch_once_t notificationTypesToken;
    dispatch_once(&notificationTypesToken, ^{
        _notificationTypes = @{ @"PUSH": QBMEventNotificationTypeKey.push,
                                @"EMAIL": QBMEventNotificationTypeKey.email };
    });
    return _notificationTypes;
}

+ (NSDictionary *)eventPeriods {
  static NSDictionary* _eventPeriods = nil;
  static dispatch_once_t eventPeriodsToken;
  dispatch_once(&eventPeriodsToken, ^{
      _eventPeriods = @{ @"DAY": @(DAY_EVENT_PERIOD),
                         @"WEEK": @(WEEK_EVENT_PERIOD),
                         @"MONTH": @(MONTH_EVENT_PERIOD),
                         @"YEAR": @(YEAR_EVENT_PERIOD) };
  });
  return _eventPeriods;
}

+ (NSDictionary *)pushTypes {
    static NSDictionary* _pushTypes = nil;
    static dispatch_once_t pushTypesToken;
    dispatch_once(&pushTypesToken, ^{
        _pushTypes = @{ @"APNS": @(APNS),
                        @"APNS_VOIP": @(APNS_VOIP),
                        @"GCM": @(GCM),
                        @"MPNS": @(MPNS) };
    });
    return _pushTypes;
}

+ (PUSH_TYPE)pushTypeToInteger:(QBMPushType)pushType {
    switch (pushType) {
        case QBMPushTypeAPNS: return APNS;
        case QBMPushTypeAPNSVOIP: return APNS_VOIP;
        case QBMPushTypeGCM: return GCM;
        case QBMPushTypeMPNS: return MPNS;
        default: return APNS;
    }
}

+ (QBMPushType)pushTypeFromInteger:(PUSH_TYPE)pushType {
    if (pushType == APNS) {
        return QBMPushTypeAPNS;
    } else if (pushType == APNS_VOIP) {
        return QBMPushTypeAPNSVOIP;
    } else if (pushType == GCM) {
        return QBMPushTypeGCM;
    } else if (pushType == MPNS) {
        return QBMPushTypeMPNS;
    } else {
        return QBMPushTypeAPNS;
    }
}

+ (EVENT_PERIOD)periodFromInteger:(NSUInteger)period {
    if (period == DAY_EVENT_PERIOD) {
        return DAY_EVENT_PERIOD;
    } else if (period == WEEK_EVENT_PERIOD) {
        return WEEK_EVENT_PERIOD;
    } else if (period == MONTH_EVENT_PERIOD) {
        return MONTH_EVENT_PERIOD;
    } else if (period == YEAR_EVENT_PERIOD) {
        return YEAR_EVENT_PERIOD;
    } else {
        return DAY_EVENT_PERIOD;
    }
}

- (id)toQBResultData:(NSError *__autoreleasing *)error {
    NSMutableDictionary *info = @{}.mutableCopy;
    
    if (self.ID != 0) {
        info[QBMEventKey.id] = @(self.ID);
    }
    
    if (self.name.length) {
        info[QBMEventKey.name] = self.name;
    }
    
    info[QBMEventKey.active] = @(self.active);
    
    info[QBMEventKey.type] = [QBMEvent eventTypeToString:self.type];
    
    info[QBMEventKey.notificationType] = [QBMEvent notificationTypeToString:self.notificationType];
    
    if (self.pushType != QBMPushTypeUndefined) {
        info[QBMEventKey.pushType] = @([QBMEvent pushTypeToInteger:self.pushType]);
    }
    
    if (self.date) {
        NSUInteger dateInterval = [self.date timeIntervalSince1970] * 1000;
        info[QBMEventKey.date] = @(dateInterval);
    }
    
    if (self.endDate) {
        NSUInteger dateInterval = [self.endDate timeIntervalSince1970] * 1000;
        info[QBMEventKey.endDate] = @(dateInterval);
    }
    
    if (self.period > 0) {
        info[QBMEventKey.period] = @([QBMEvent periodFromInteger:self.period]);
    }
    
    if (self.occuredCount > 0) {
        info[QBMEventKey.occuredCount] = @(self.occuredCount);
    }
    
    if (self.senderID > 0) {
        info[QBMEventKey.senderId] = @(self.senderID);
    }
    
    if (self.usersIDs.length) {
        NSMutableArray<NSNumber *>*recipientsIds = @[].mutableCopy;
        NSArray<NSString *>*ids = [self.usersIDs componentsSeparatedByString:@","];
        for (NSString *ID in ids) {
            [recipientsIds addObject:@(ID.integerValue)];
        }
        
        if (recipientsIds.count) {
            info[QBMEventKey.recipientsIds] = recipientsIds;
        }
    }
    
    if (self.usersTagsAny.length) {
        NSArray<NSString *>*tags = [self.usersTagsAny componentsSeparatedByString:@","];
        if (tags.count) {
            info[QBMEventKey.recipientsTagsAny] = tags;
        }
    }
    
    if (self.usersTagsAll.length) {
        NSArray<NSString *>*tags = [self.usersTagsAll componentsSeparatedByString:@","];
        if (tags.count) {
            info[QBMEventKey.recipientsTagsAll] = tags;
        }
    }
    
    if (self.usersTagsExclude.length) {
        NSArray<NSString *>*tags = [self.usersTagsExclude componentsSeparatedByString:@","];
        if (tags.count) {
            info[QBMEventKey.recipientsTagsExclude] = tags;
        }
    }
    
    if (self.message) {
        info[QBMEventKey.payload] = self.message;
    }
    
    return [info.copy toQBResultWithType:QBResultTypeDefault error:error];
}

@end
