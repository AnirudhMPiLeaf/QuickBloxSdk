//
//  QBSettingsModule.m
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBSettingsModule.h"
#import "QBSettingsConstants.h"

@implementation QBSettingsModule

- (void)init:(NSDictionary *)info
    resolver:(QBResolveBlock)resolve
    rejecter:(QBRejectBlock)reject {
    
    if ([NSError reject:reject
                   info:info
           requirements:@[ [Requirement class:NSString.class
                                          key:SettingsKey.appId],
                           [Requirement class:NSString.class
                                          key:SettingsKey.authKey],
                           [Requirement class:NSString.class
                                          key:SettingsKey.authSecret],
                           [Requirement class:NSString.class
                                          key:SettingsKey.accountKey] ]
        ]) {
        return;
    } else {
        NSString *appId = info[SettingsKey.appId];
        [QBSettings setApplicationID:(NSUInteger)appId.longLongValue];
        
        NSString *authKey = info[SettingsKey.authKey];
        [QBSettings setAuthKey:authKey];
        
        NSString *authSecret = info[SettingsKey.authSecret];
        [QBSettings setAuthSecret:authSecret];
        
        NSString *accountKey = info[SettingsKey.accountKey];
        [QBSettings setAccountKey:accountKey];
    }
    
    [self setEndpoints:info];
    
    if (resolve) { resolve(nil); }
}

- (void)initWithoutAuthKeyAndSecret:(NSDictionary *)info
                           resolver:(QBResolveBlock)resolve
                           rejecter:(QBRejectBlock)reject {
    if ([NSError reject:reject
                   info:info
           requirements:@[ [Requirement class:NSString.class
                                          key:SettingsKey.appId] ] ]) {
               return;
           }
    
    NSString *appId = info[SettingsKey.appId];
    NSString *accountKey = info[SettingsKey.accountKey];
    
    [Quickblox initWithApplicationId:appId.longLongValue
                          accountKey:accountKey];
    
    [self setEndpoints:info];
    
    if (resolve) { resolve(nil); }
}

- (void)setEndpoints:(NSDictionary *)info {
    NSString *apiEndpoint = info[SettingsKey.apiEndpoint];
    if (apiEndpoint.length) {
        [QBSettings setApiEndpoint:apiEndpoint];
    }
    
    NSString *chatEndpoint = info[SettingsKey.chatEndpoint];
    if (chatEndpoint.length) {
        [QBSettings setChatEndpoint:chatEndpoint];
    }
    
    NSNumber *chatEndpointPort = info[SettingsKey.chatEndpointPort];
    if (chatEndpointPort) {
        [QBSettings setChatEndpointPort:chatEndpointPort.unsignedIntegerValue];
    }
}

- (void)get:(QBResolveBlock)resolve
   rejecter:(QBRejectBlock)reject {
    NSMutableDictionary *info = @{}.mutableCopy;
    info[SettingsKey.appId] = @(QBSettings.applicationID).stringValue;
    info[SettingsKey.authKey] = QBSettings.authKey;
    info[SettingsKey.authSecret] = QBSettings.authSecret;
    info[SettingsKey.accountKey] = QBSettings.accountKey;
    info[SettingsKey.apiEndpoint] = QBSettings.apiEndpoint;
    info[SettingsKey.chatEndpoint] = QBSettings.chatEndpoint;
    info[SettingsKey.sdkVersion] = QuickbloxFrameworkVersion;
    //    info[@"chatEndpointPort"] = @(QBSettings.chatEndpointPort);
    
    resolve(info.copy);
}

- (void)initStreamManagement:(NSDictionary *)info
                    resolver:(QBResolveBlock)resolve
                    rejecter:(QBRejectBlock)reject {
    NSNumber *enableNumber = info[SettingsKey.autoReconnect];
    NSNumber *messageTimeout = info[SettingsKey.messageTimeout];
    if (enableNumber) {
        [QBSettings setAutoReconnectEnabled:enableNumber.boolValue];
    }
    if (messageTimeout) {
        QBSettings.streamManagementSendMessageTimeout = messageTimeout.unsignedIntegerValue;
    }
    if (resolve) { resolve(nil); }
}

- (void)enableAutoReconnect:(NSDictionary *)info
                   resolver:(QBResolveBlock)resolve
                   rejecter:(QBRejectBlock)reject {
    NSNumber *enableNumber = info[SettingsKey.enable];
    BOOL enable = enableNumber ? enableNumber.boolValue : NO;
    [QBSettings setAutoReconnectEnabled:enable];
    if (resolve) { resolve(nil); }
}

- (void)enableCarbons:(QBResolveBlock)resolve
             rejecter:(QBRejectBlock)reject {
    QBSettings.carbonsEnabled = YES;
    if (resolve) { resolve(nil); }
}

- (void)disableCarbons:(QBResolveBlock)resolve
              rejecter:(QBRejectBlock)reject {
    QBSettings.carbonsEnabled = NO;
    if (resolve) { resolve(nil); }
}

- (void)enableLogging:(QBResolveBlock)resolve
             rejecter:(QBRejectBlock)reject {
    QBSettings.logLevel = QBLogLevelDebug;
    resolve(nil);
}

- (void)disableLogging:(QBResolveBlock)resolve
              rejecter:(QBRejectBlock)reject {
    QBSettings.logLevel = QBLogLevelNothing;
    resolve(nil);
}

- (void)enableXMPPLogging:(QBResolveBlock)resolve
                 rejecter:(QBRejectBlock)reject {
    [QBSettings enableXMPPLogging];
    resolve(nil);
}

- (void)disableXMPPLogging:(QBResolveBlock)resolve
                  rejecter:(QBRejectBlock)reject {
    [QBSettings disableFileLogging];
    resolve(nil);
}

@end
