//
//  QBModule.h
//  quickblox_sdk
//
//  Created by Injoit on 24.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "NSError+Helper.h"
#import "QBBridgeMethod.h"
#import "QBResponse+Helper.h"
#import "QBConstants.h"

#import <Quickblox/Quickblox.h>

NS_ASSUME_NONNULL_BEGIN

@interface QBModule : NSObject

@property (readonly, strong, nonatomic) NSArray<NSString *>* events;
@property (readonly, strong, nonatomic) NSArray<NSString *>* methodsWithArray;

- (void)postQBEventWithName:(NSString * _Nonnull)eventName body:(id _Nullable)body;

@end

NS_ASSUME_NONNULL_END
