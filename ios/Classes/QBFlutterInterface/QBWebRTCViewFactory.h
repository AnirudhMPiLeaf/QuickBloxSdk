//
//  QBWebRTCViewFactory.h
//  quickblox_sdk
//
//  Created by Injoit on 09.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Flutter/Flutter.h>
#import "QBWebRTCSessionControllerCache.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBWebRTCViewFactory : NSObject <FlutterPlatformViewFactory>

@property (nonatomic, weak) id<QBWebRTCSessionControllerCache> delegate;

+ (instancetype)factoryWithMessenger:(NSObject<FlutterBinaryMessenger>*)messenger;

@end

NS_ASSUME_NONNULL_END
