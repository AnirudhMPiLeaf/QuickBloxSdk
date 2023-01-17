//
//  QBResponsePage+QBSerializer.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Quickblox/Quickblox.h>
#import "QBSerializerProtocol.h"

struct QBResponsePageKeysStruct {
    __unsafe_unretained NSString * _Nonnull const skip;
    __unsafe_unretained NSString * _Nonnull const limit;
    __unsafe_unretained NSString * _Nonnull const total;
};
extern const struct QBResponsePageKeysStruct QBPageKey;

NS_ASSUME_NONNULL_BEGIN

@interface QBResponsePage (QBSerializer) <QBSerializerProtocol>

@end

NS_ASSUME_NONNULL_END
