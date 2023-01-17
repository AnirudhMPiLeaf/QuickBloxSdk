//
//  QBConferenceViewFactory.h
//  quickblox_sdk
//
//  Created by Injoit on 12.02.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Flutter/Flutter.h>
#import "QBConferenceCacheProtocol.h"

NS_ASSUME_NONNULL_BEGIN

@interface QBConferenceViewFactory : NSObject <FlutterPlatformViewFactory>
@property (nonatomic, weak) id<QBConferenceCacheProtocol> delegate;
+ (instancetype)factoryWithMessenger:(NSObject<FlutterBinaryMessenger>*)messenger;

@end

NS_ASSUME_NONNULL_END
