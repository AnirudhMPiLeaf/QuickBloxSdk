//
//  QBGeneralResponsePage+QBSerializer.h
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Quickblox/Quickblox.h>
#import "QBSerializerProtocol.h"

struct QBGeneralResponsePageKeysStruct {
    __unsafe_unretained NSString * _Nonnull const page;
    __unsafe_unretained NSString * _Nonnull const perPage;
    __unsafe_unretained NSString * _Nonnull const total;
};
extern const struct QBGeneralResponsePageKeysStruct QBGeneralPageKey;

NS_ASSUME_NONNULL_BEGIN

@interface QBGeneralResponsePage (QBSerializer) <QBSerializerProtocol>

@end

NS_ASSUME_NONNULL_END
