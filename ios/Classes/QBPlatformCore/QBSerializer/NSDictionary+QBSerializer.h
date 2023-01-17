//
//  NSDictionary+QBSerializer.h
//  quickblox_sdk
//
//  Created by Injoit on 24.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, QBResultType) {
    QBResultTypeDefault = 0,
    QBResultTypeObject,
    QBResultTypeJSON
};

struct QBFilterKeysStruct {
    __unsafe_unretained NSString * _Nonnull const filter;
    __unsafe_unretained NSString * _Nonnull const field;
    __unsafe_unretained NSString * _Nonnull const instruction;
    __unsafe_unretained NSString * _Nonnull const value;
    __unsafe_unretained NSString * _Nonnull const type;
};

extern const struct QBFilterKeysStruct FilterKey;

struct QBSortKeysStruct {
    __unsafe_unretained NSString * _Nonnull const sort;
    __unsafe_unretained NSString * _Nonnull const field;
    __unsafe_unretained NSString * _Nonnull const ascending;
    __unsafe_unretained NSString * _Nonnull const type;
};

extern const struct QBSortKeysStruct SortKey;

@interface NSDictionary (QBSerializer)

- (id)toQBResultWithType:(QBResultType)type
                   error:(NSError **)error;

- (NSDictionary *)toSortData;
- (NSDictionary *)toFilterData;

- (NSDictionary *)toUserSortData;
- (NSDictionary *)toUserFilterData;

@end

NS_ASSUME_NONNULL_END
