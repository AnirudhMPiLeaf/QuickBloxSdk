//
//  QBChatModule+Connection.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatModule+Connection.h"
#import "QBUUser+QBSerializer.h"

@implementation QBChatModule (Connection)

- (void)connect:(NSDictionary *)info
       resolver:(QBResolveBlock)resolve
       rejecter:(QBRejectBlock)reject {
    NSObject *userIdObject = info[QBChatKey.userId];
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:userIdObject
              objectKey:QBChatKey.userId]) {
        return;
    }
    NSNumber *userId = (NSNumber *)userIdObject;
    
    NSObject *passwordObject = info[QBUserKey.password];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:passwordObject
              objectKey:QBUserKey.password]) {
        return;
    }
    NSString *password = (NSString *)passwordObject;
    
    if (QBChat.instance.isConnecting) {
        if (resolve) {
            resolve(nil);
        }
        return;
    }
    
    [[QBChat instance] connectWithUserID:userId.unsignedIntegerValue
                                password:password
                              completion:^(NSError *error) {
        if (error && reject) {
            reject([NSString stringWithFormat:@"%@",
                    @(error.code)],
                   error.localizedRecoverySuggestion
                   ?: error.localizedDescription,
                   error);
        } else if (resolve) {
            resolve(nil);
        }
    }];
}

- (void)isConnected:(QBResolveBlock)resolve
           rejecter:(QBRejectBlock)reject {
    if (resolve) {
        resolve(@(QBChat.instance.isConnected));
    }
}

- (void)disconnect:(QBResolveBlock)resolve
          rejecter:(QBRejectBlock)reject {
    if ((QBChat.instance.isConnected == NO) && (QBChat.instance.isConnecting == NO)) {
        if (resolve) { resolve(nil); }
        return;
    }
    [[QBChat instance] disconnectWithCompletionBlock:^(NSError *error) {
        if (error && reject) {
            reject([NSString stringWithFormat:@"%@",
                    @(error.code)],
                   error.localizedRecoverySuggestion
                   ?: error.localizedDescription,
                   error);
        } else if (resolve) {
            resolve(nil);
        }
    }];
}

@end

//MARK: - QBChatConnectionProtocol
@implementation QBChatModule(ConnectionProtocol)

- (void)chatDidConnect {
    [self postQBEventWithName:QBChatConnectEvent.connected body:nil];
}

- (void)chatDidNotConnectWithError:(NSError *)error {
    
}

- (void)chatDidFailWithStreamError:(NSError *)error {
    
}

- (void)chatDidDisconnectWithError:(NSError *)error {
    [self postQBEventWithName:QBChatConnectEvent.connectionClosed body:nil];
}

- (void)chatDidAccidentallyDisconnect {
    [self postQBEventWithName:QBChatConnectEvent.reconnectionFailed body:nil];
}

- (void)chatDidReconnect {
    [self postQBEventWithName:QBChatConnectEvent.reconnectionSuccessful body:nil];
}

@end
