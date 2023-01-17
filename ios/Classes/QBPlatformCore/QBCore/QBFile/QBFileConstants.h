//
//  QBFileConstants.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

struct QBFileKeysStruct {
    __unsafe_unretained NSString * const url;
    __unsafe_unretained NSString * const public;
    __unsafe_unretained NSString * const progress;
};
extern const struct QBFileKeysStruct QBFileKey;

@interface QBFileConstants : NSObject

@end

NS_ASSUME_NONNULL_END
