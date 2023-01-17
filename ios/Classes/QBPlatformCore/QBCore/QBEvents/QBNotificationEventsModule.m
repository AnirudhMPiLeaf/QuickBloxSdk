//
//  QBNotificationEventsModule.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBNotificationEventsModule.h"
#import "QBMEvent+QBSerializer.h"
#import "NSDate+Helper.h"
#import "QBGeneralResponsePage+QBSerializer.h"

@implementation QBNotificationEventsModule

- (void)create:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject {
    QBMEvent *event = [QBMEvent event];
       NSString *name = info[QBMEventKey.name];
       if (name.length) {
           event.name = name;
       }
       NSString *type = info[QBMEventKey.type];
       if (type.length) {
           event.type = [QBMEvent eventTypeFromString:type];
       }
       NSString *notificationType = info[QBMEventKey.notificationType];
       if (notificationType.length) {
           event.notificationType = [QBMEvent notificationTypeFromString:notificationType];
       }
       NSNumber *pushType = info[QBMEventKey.pushType];
       if (pushType) {
           event.pushType =
           [QBMEvent pushTypeFromInteger:(PUSH_TYPE)pushType.integerValue];
       }
       NSNumber *date = info[QBMEventKey.date];
       if (date) {
           event.date = [NSDate numberConvert:date];
       }
       NSNumber *endDate = info[QBMEventKey.endDate];
       if (endDate) {
           event.endDate = [NSDate numberConvert:endDate];
       }
       NSNumber *period = info[QBMEventKey.period];
       if (period) {
           event.period = period.unsignedIntegerValue;
       }
       NSNumber *occuredCount = info[QBMEventKey.occuredCount];
       if (occuredCount) {
           event.occuredCount = occuredCount.unsignedIntegerValue;
       }
       NSNumber *senderId = info[QBMEventKey.senderId];
       if (senderId) {
           event.senderID = senderId.unsignedIntegerValue;
       }
       NSArray<NSNumber *>*recipientsIds = info[QBMEventKey.recipientsIds];
       if (recipientsIds.count) {
           NSSet *recipientsIdsSet = [[NSSet alloc] initWithArray:recipientsIds];
           event.usersIDs = [recipientsIdsSet.allObjects componentsJoinedByString:@","];
       }
       NSArray<NSString *>*recipientsTagsAny = info[QBMEventKey.recipientsTagsAny];
       if (recipientsTagsAny.count) {
           NSSet *recipientsTagsAnySet = [[NSSet alloc] initWithArray:recipientsTagsAny];
           event.usersTagsAny = [recipientsTagsAnySet.allObjects componentsJoinedByString:@","];
       }
       NSArray<NSString *>*recipientsTagsAll = info[QBMEventKey.recipientsTagsAll];
       if (recipientsTagsAll.count) {
           NSSet *recipientsTagsAllSet = [[NSSet alloc] initWithArray:recipientsTagsAll];
           event.usersTagsAll = [recipientsTagsAllSet.allObjects componentsJoinedByString:@","];
       }
       NSArray<NSString *>*recipientsTagsExclude = info[QBMEventKey.recipientsTagsExclude];
       if (recipientsTagsExclude.count) {
           NSSet *recipientsTagsExcludeSet = [[NSSet alloc] initWithArray:recipientsTagsExclude];
           event.usersTagsExclude = [recipientsTagsExcludeSet.allObjects componentsJoinedByString:@","];
       }
       
       NSString *message = @"";
       id messageValue = info[QBMEventKey.payload];
       if ([messageValue isKindOfClass:[NSDictionary class]]) {
           NSDictionary *payload = (NSDictionary *)messageValue;
           NSError *error = nil;
           NSData *data = [NSJSONSerialization
                           dataWithJSONObject:payload
                           options:NSJSONWritingPrettyPrinted
                           error:&error];
           if ([error reject:reject]) {
               return;
           }
           message =
           [[NSString alloc] initWithData:data
                                 encoding:NSUTF8StringEncoding];
       } else if ([messageValue isKindOfClass:[NSString class]]) {
           message = (NSString *)messageValue;
       }
       
       if (message.length) {
           event.message = message;
       }
       
       [QBRequest createEvent:event
                 successBlock:^(QBResponse *response,
                                NSArray *objects) {
                     [objects toQBResultArrayWithResolver:resolve rejecter:reject];
                 } errorBlock:^(QBResponse *response) {
                     [response reject:reject];
                 }];
}

- (void)get:(NSDictionary *)info
   resolver:(QBResolveBlock)resolve
   rejecter:(QBRejectBlock)reject {
    NSNumber *page = info[QBGeneralPageKey.page];
    NSNumber *perPage = info[QBGeneralPageKey.perPage];
    
    QBGeneralResponsePage *requestPage =
    [QBGeneralResponsePage responsePageWithCurrentPage:page.unsignedIntegerValue ?: 1
                                               perPage:perPage.unsignedIntegerValue ?:10];
    [QBRequest eventsForPage:requestPage
                successBlock:^(QBResponse *response,
                               QBGeneralResponsePage *page,
                               NSArray *events) {
                    [events toQBResultArrayWithResolver:resolve rejecter:reject];
                } errorBlock:^(QBResponse *response) {
                    [response reject:reject];
                }];
}

- (void)getById:(NSDictionary *)info
       resolver:(QBResolveBlock)resolve
       rejecter:(QBRejectBlock)reject {
    NSObject *evenIdObject = info[QBMEventKey.id];
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:evenIdObject
              objectKey:QBMEventKey.id]) {
        return;
    }
    NSNumber *evenId = (NSNumber *)evenIdObject;
    [QBRequest eventWithID:evenId.unsignedIntegerValue
              successBlock:^(QBResponse *response,
                             QBMEvent *event) {
                  [event toQBResultDataWithResolver:resolve rejecter:reject];
              } errorBlock:^(QBResponse *response) {
                  [response reject:reject];
              }];
}

- (void)update:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject {
    NSObject *evenIdObject = info[QBMEventKey.id];
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:evenIdObject
              objectKey:QBMEventKey.id]) {
        return;
    }
    NSNumber *evenId = (NSNumber *)evenIdObject;
    
    QBMEvent *event = [QBMEvent event];
    event.ID = evenId.unsignedIntegerValue;
    
    NSNumber *active = info[QBMEventKey.active];
    if (active) {
        event.active = active.boolValue;
    }
    
    NSString *message = @"";
    id messageValue = info[QBMEventKey.payload];
    if ([messageValue isKindOfClass:[NSDictionary class]]) {
        NSDictionary *payload = (NSDictionary *)messageValue;
        NSError *error = nil;
        NSData *data = [NSJSONSerialization
                        dataWithJSONObject:payload
                        options:NSJSONWritingPrettyPrinted
                        error:&error];
        if ([error reject:reject]) {
            return;
        }
        message =
        [[NSString alloc] initWithData:data
                              encoding:NSUTF8StringEncoding];
    } else if ([messageValue isKindOfClass:[NSString class]]) {
        message = (NSString *)messageValue;
    }
    
    if (message.length) {
        event.message = message;
    }
    
    NSNumber *date = info[QBMEventKey.date];
    if (date) {
        event.date = [NSDate numberConvert:date];
    }
    
    NSNumber *period = info[QBMEventKey.period];
    if (period) {
        event.period = period.unsignedIntegerValue;
    }
    
    NSString *name = info[QBMEventKey.name];
    if (name.length) {
        event.name = name;
    }
    
    [QBRequest updateEvent:event
              successBlock:^(QBResponse *response,
                             QBMEvent *event) {
                  [event toQBResultDataWithResolver:resolve rejecter:reject];
              } errorBlock:^(QBResponse *response) {
                  [response reject:reject];
              }];

}

- (void)remove:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject {
    NSObject *evenIdObject = info[QBMEventKey.id];
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:evenIdObject
              objectKey:QBMEventKey.id]) {
        return;
    }
    NSNumber *evenId = (NSNumber *)evenIdObject;
    
    [QBRequest deleteEventWithID:evenId.unsignedIntegerValue
                    successBlock:^(QBResponse *response) {
                        if (resolve) {
                            resolve(nil);
                        }
                    } errorBlock:^(QBResponse *response) {
                        [response reject:reject];
                    }];
}

@end
