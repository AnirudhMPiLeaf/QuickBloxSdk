//
//  QBWebRTCSessionControllerCache.h
//  quickblox_sdk
//
//  Created by Injoit on 10.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "QBWebRTCSessionController.h"

NS_ASSUME_NONNULL_BEGIN

@protocol QBWebRTCSessionControllerCache <NSObject>

- (NSMutableDictionary<NSString *, QBWebRTCSessionController*>*)cache;

@end

NS_ASSUME_NONNULL_END
