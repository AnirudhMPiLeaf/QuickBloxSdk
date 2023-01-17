//
//  QBCustomObjectsConstants.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

struct QBCustomObjectsStruct {
    __unsafe_unretained NSString * _Nonnull const objects;
    __unsafe_unretained NSString * _Nonnull const objectsIds;
};
extern const struct QBCustomObjectsStruct QBCustomObjectsKey;

@interface QBCustomObjectsConstants : NSObject

@end

NS_ASSUME_NONNULL_END
