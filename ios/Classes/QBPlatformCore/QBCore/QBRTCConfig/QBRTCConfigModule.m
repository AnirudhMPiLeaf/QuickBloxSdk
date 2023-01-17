//
//  QBRTCConfigModule.m
//  quickblox_sdk
//
//  Created by Injoit on 14.01.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import "QBRTCConfigModule.h"
#import <QuickbloxWebRTC/QuickbloxWebRTC.h>

@implementation QBRTCConfigModule

- (instancetype)init {
    self = [super init];
    if (self) {
        QBRTCConfig.answerTimeInterval = 60.0;
        QBRTCConfig.dialingTimeInterval = 5.0;
    }
    return self;
}

- (NSArray<NSString *> *)methodsWithArray {
  return @[
      NSStringFromSelector(@selector(setICEServers:resolver:rejecter:))
  ];
}

- (void)setAnswerTimeInterval:(NSNumber *)interval
                     resolver:(QBResolveBlock)resolve
                     rejecter:(QBRejectBlock)reject {
    NSTimeInterval minimumInterval =  10.0;
    
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:interval
              objectKey:@"interval"]) {
        return;
    }
    
    if (interval.doubleValue < minimumInterval) {
        [NSError reject:reject message:@"Value should be equal to or greater than 10"];
        return;
    }
    
    QBRTCConfig.answerTimeInterval = interval.doubleValue;
    resolve(nil);
}

- (void)getAnswerTimeInterval:(QBResolveBlock)resolve
                     rejecter:(QBRejectBlock)reject {
    NSNumber *result = @((NSInteger)QBRTCConfig.answerTimeInterval);
    resolve(result);
}

- (void)setDialingTimeInterval:(NSNumber *)interval
                      resolver:(QBResolveBlock)resolve
                      rejecter:(QBRejectBlock)reject {
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:interval
              objectKey:@"interval"]) {
        return;
    }
    
    if (interval.doubleValue < 3.0) {
        [NSError reject:reject message:@"Value should be equal to or greater than 3"];
        return;
    }
    
    QBRTCConfig.dialingTimeInterval = interval.doubleValue;
    resolve(nil);
}

- (void)getDialingTimeInterval:(QBResolveBlock)resolve
                      rejecter:(QBRejectBlock)reject {
    NSNumber *result = @((NSInteger)QBRTCConfig.dialingTimeInterval);
    resolve(result);
}

- (void)setReconnectionTimeInterval:(NSNumber *)interval
                           resolver:(QBResolveBlock)resolve
                           rejecter:(QBRejectBlock)reject {
    NSTimeInterval minimumInterval =  10.0;
    
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:interval
              objectKey:@"interval"]) {
        return;
    }
    
    if (interval.doubleValue < minimumInterval) {
        [NSError reject:reject message:@"Value should be equal to or greater than 10"];
        return;
    }
    
    QBRTCConfig.disconnectTimeInterval = interval.doubleValue;
    resolve(nil);
}

- (void)getReconnectionTimeInterval:(QBResolveBlock)resolve
                           rejecter:(QBRejectBlock)reject {
    NSNumber *result = @((NSInteger)QBRTCConfig.disconnectTimeInterval);
    resolve(result);
}

- (void)setICEServers:(NSArray *)servers
             resolver:(QBResolveBlock)resolve
             rejecter:(QBRejectBlock)reject {
    if ([NSError reject:reject
           checkerClass:NSArray.class
                 object:servers
              objectKey:@"servers"]) {
        return;
    }
    
    NSMutableArray<QBRTCICEServer *>*qbservers = @[].mutableCopy;
    BOOL rejected = NO;

    for (NSDictionary *server in servers) {
        NSString *url = server[@"url"];
        rejected = [NSError reject:reject
                      checkerClass:NSString .class
                            object:url
                         objectKey:@"url"];
        if (rejected) {
            return;
        }
        NSString *userName = server[@"userName"] ?: @"";
        NSString *password = server[@"password"] ?: @"";
        
        QBRTCICEServer *iceServer = [QBRTCICEServer serverWithURLs:@[url]
                                                          username:userName
                                                          password:password];
        [qbservers addObject:iceServer];
    }
    
    if (rejected) {
        return;
    }
    
    if (qbservers.count == 0) {
        [NSError reject:reject
                message:@"There are no valid ice servers for set"];
        return;
    }
    
    [QBRTCConfig setICEServers:qbservers.copy];
    resolve(nil);
}

- (void)getICEServers:(QBResolveBlock)resolve
             rejecter:(QBRejectBlock)reject {
    NSArray<QBRTCICEServer *>*qbservers = QBRTCConfig.iceServers;
    NSMutableArray<NSDictionary<NSString *,NSString *> *>*servers = @[].mutableCopy;
    for (QBRTCICEServer *qbserver in qbservers) {
        for (NSString *subURL in qbserver.urls) {
            NSMutableDictionary<NSString *,NSString *>*server = @{}.mutableCopy;
            if (qbserver.userName.length) {
                server[@"userName"] = qbserver.userName;
            }
            if (qbserver.password.length) {
                server[@"password"] = qbserver.password;
            }
            server[@"url"] = subURL;
            
            [servers addObject:server];
        }
    }

    resolve(servers.copy);
}

@end
