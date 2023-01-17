//
//  QBCOCustomObject+QBSerializer.h
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Quickblox/Quickblox.h>
#import "QBSerializerProtocol.h"

NS_ASSUME_NONNULL_BEGIN

struct QBObjectKeysStruct {
    __unsafe_unretained NSString * const id;
    __unsafe_unretained NSString * const parentId;
    __unsafe_unretained NSString * const createdAt;
    __unsafe_unretained NSString * const updatedAt;
    __unsafe_unretained NSString * const className;
    __unsafe_unretained NSString * const userId;
    __unsafe_unretained NSString * const fields;
    __unsafe_unretained NSString * const permission;
};
extern const struct QBObjectKeysStruct QBObjectKey;

struct QBUpdateObjectKeysStruct {
    __unsafe_unretained NSString * const value;
    __unsafe_unretained NSString * const index;
    __unsafe_unretained NSString * const operator;
    __unsafe_unretained NSString * const pullFilter;
};
extern const struct QBUpdateObjectKeysStruct QBUpdateObjectKey;

struct QBObjectsPermissionsKeysStruct {
    __unsafe_unretained NSString * const customObjectId;
    __unsafe_unretained NSString * const readLevel;
    __unsafe_unretained NSString * const updateLevel;
    __unsafe_unretained NSString * const deleteLevel;
};
extern const struct QBObjectsPermissionsKeysStruct QBObjectsPermissionsKey;

struct QBObjectsPermissionsLevelKeysStruct {
    __unsafe_unretained NSString * const access;
    __unsafe_unretained NSString * const usersIds;
    __unsafe_unretained NSString * const usersGroups;
};

extern const struct QBObjectsPermissionsLevelKeysStruct QBPermissionsLevelKey;

struct QBObjectsPermissionsLevelNameKeysStruct {
    __unsafe_unretained NSString * const open;
    __unsafe_unretained NSString * const owner;
    __unsafe_unretained NSString * const openForUsersIds;
    __unsafe_unretained NSString * const openForGroups;
};

extern const struct QBObjectsPermissionsLevelNameKeysStruct QBPermissionsLevelNameKey;

@interface QBCOCustomObject (QBSerializer) <QBSerializerProtocol>

+ (NSDictionary *)permissionsLevel;
+ (NSDictionary *)searchOperators;
+ (NSDictionary *)updateOperators;
+ (NSDictionary *)pullFilters;

- (void)setupFields:(NSDictionary<NSString *,NSObject *>*)fields
    updateOperators:(NSMutableDictionary<NSString *, NSString *>*_Nullable*_Nullable)updateOperators;

@end

NS_ASSUME_NONNULL_END
