//
//  QBWebRTCFlutterVideoView.h
//  quickblox_sdk
//
//  Created by Injoit on 10.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Flutter/Flutter.h>

#import "NSError+Helper.h"
#import "QBBridgeMethod.h"
#import "QBResponse+Helper.h"
#import "QBWebRTCSessionControllerCache.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBWebRTCFlutterVideoView : NSObject <FlutterPlatformView>

@property (nonatomic, weak) id<QBWebRTCSessionControllerCache> delegate;

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
