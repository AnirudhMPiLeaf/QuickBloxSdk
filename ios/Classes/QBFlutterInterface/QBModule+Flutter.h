//
//  QBModule+Flutter.h
//  quickblox_sdk
//
//  Created by Injoit on 27.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBModule.h"
#import <Flutter/Flutter.h>

NS_ASSUME_NONNULL_BEGIN

@interface QBModule (Flutter) <FlutterStreamHandler>

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result;
- (void)setupRegistrar:(nonnull NSObject<FlutterPluginRegistrar> *)registrar;

@end

NS_ASSUME_NONNULL_END
