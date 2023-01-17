//
//  QBConferenceFlutterVideoView.h
//  quickblox_sdk
//
//  Created by Injoit on 12.02.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Flutter/Flutter.h>

#import "NSError+Helper.h"
#import "QBBridgeMethod.h"
#import "QBResponse+Helper.h"
#import "QBConferenceCacheProtocol.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBConferenceFlutterVideoView : NSObject <FlutterPlatformView>

@property (nonatomic, weak) id<QBConferenceCacheProtocol> delegate;

+ (instancetype)viewWithMessenger:(NSObject<FlutterBinaryMessenger> *)messenger
                            frame:(CGRect)frame
                   viewIdentifier:(int64_t)viewId;

- (void)mirror:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject;

- (void)scaleType:(NSDictionary *)info
         resolver:(QBResolveBlock)resolve
         rejecter:(QBRejectBlock)reject;

- (void)play:(NSDictionary *)info
    resolver:(QBResolveBlock)resolve
    rejecter:(QBRejectBlock)reject;

- (void)release:(NSDictionary *)info
       resolver:(QBResolveBlock)resolve
       rejecter:(QBRejectBlock)reject;

@end

NS_ASSUME_NONNULL_END
