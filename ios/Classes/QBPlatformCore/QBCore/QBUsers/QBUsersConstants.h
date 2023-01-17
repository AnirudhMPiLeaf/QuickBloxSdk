//
//  QBUsersConstants.h
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>

struct QBUsersKeysStruct {
    __unsafe_unretained NSString * _Nonnull const users;
};
extern const struct QBUsersKeysStruct QBUsersKey;

NS_ASSUME_NONNULL_BEGIN

@interface QBUsersConstants : NSObject

@end

NS_ASSUME_NONNULL_END
