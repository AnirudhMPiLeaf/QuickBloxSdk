//
//  QBConferenceModule.h
//  quickblox_sdk
//
//  Created by Injoit on 03.02.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import "QBModule.h"
#import "QBConferenceCacheProtocol.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBConferenceModule : QBModule <QBConferenceCacheProtocol>

@property (nonatomic, strong) QBConferenceCache *cache;

- (void)init:(NSString *)endpoint
    resolver:(QBResolveBlock)resolve
    rejecter:(QBRejectBlock)reject;

- (void)release:(QBResolveBlock)resolve
       rejecter:(QBRejectBlock)reject;

- (void)create:(NSString *)roomId
              :(NSNumber *)type
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject;

- (void)joinAsPublisher:(NSString *)id
        resolver:(QBResolveBlock)resolve
        rejecter:(QBRejectBlock)reject;

- (void)getOnlineParticipants:(NSString *)id
        resolver:(QBResolveBlock)resolve
        rejecter:(QBRejectBlock)reject;

- (void)subscribeToParticipant:(NSNumber *)id
              :(NSString *)sessionId
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject;

- (void)unsubscribeFromParticipant:(NSNumber *)id
              :(NSString *)sessionId
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject;

- (void)leave:(NSString *)id
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

@end

NS_ASSUME_NONNULL_END
