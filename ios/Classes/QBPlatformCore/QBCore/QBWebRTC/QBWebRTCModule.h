//
//  QBWebRTCModule.h
//  quickblox_sdk
//
//  Created by Injoit on 03.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import "QBModule.h"
#import "QBWebRTCSessionControllerCache.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBWebRTCModule : QBModule <QBWebRTCSessionControllerCache>

@property (nonatomic, strong) NSMutableDictionary<NSString *, QBWebRTCSessionController*>*cache;

- (void)init:(QBResolveBlock)resolve
    rejecter:(QBRejectBlock)reject;

- (void)release:(QBResolveBlock)resolve
       rejecter:(QBRejectBlock)reject;

- (void)getSession:(NSDictionary *)info
          resolver:(QBResolveBlock)resolve
          rejecter:(QBRejectBlock)reject;

- (void)call:(NSDictionary *)info
    resolver:(QBResolveBlock)resolve
    rejecter:(QBRejectBlock)reject;

- (void)accept:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject;

- (void)reject:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject;

- (void)hangUp:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject;

- (void)enableVideo:(NSDictionary *)info
           resolver:(QBResolveBlock)resolve
           rejecter:(QBRejectBlock)reject;

- (void)enableAudio:(NSDictionary *)info
           resolver:(QBResolveBlock)resolve
           rejecter:(QBRejectBlock)reject;

- (void)switchCamera:(NSDictionary *)info
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject;

- (void)switchAudioOutput:(NSDictionary *)info
                 resolver:(QBResolveBlock)resolve
                 rejecter:(QBRejectBlock)reject;

- (void)subscribeEvents:(NSDictionary *)info
               resolver:(QBResolveBlock)resolve
               rejecter:(QBRejectBlock)reject;

- (void)unsubscribeEvents:(NSDictionary *)info
                 resolver:(QBResolveBlock)resolve
                 rejecter:(QBRejectBlock)reject;

@end

NS_ASSUME_NONNULL_END
