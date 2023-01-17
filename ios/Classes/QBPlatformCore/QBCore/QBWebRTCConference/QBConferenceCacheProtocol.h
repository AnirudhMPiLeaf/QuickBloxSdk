//
//  QBConferenceCacheProtocol.h
//  quickblox_sdk
//
//  Created by Injoit on 03.02.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "QBConferenceCache.h"

NS_ASSUME_NONNULL_BEGIN

@protocol QBConferenceCacheProtocol <NSObject>

- (QBConferenceCache *)cache;

@end

NS_ASSUME_NONNULL_END
