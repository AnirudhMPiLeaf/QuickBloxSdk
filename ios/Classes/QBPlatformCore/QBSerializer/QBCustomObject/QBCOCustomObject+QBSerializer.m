//
//  QBCOCustomObject+QBSerializer.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBCOCustomObject+QBSerializer.h"
#import "NSDate+Helper.h"

const struct QBObjectKeysStruct QBObjectKey = {
    .id = @"id",
    .parentId = @"parentId",
    .createdAt = @"createdAt",
    .updatedAt = @"updatedAt",
    .className = @"className",
    .userId = @"userId",
    .fields = @"fields",
    .permission = @"permission"
};

const struct QBUpdateObjectKeysStruct QBUpdateObjectKey = {
    .value = @"value",
    .index = @"index",
    .operator = @"operator",
    .pullFilter = @"pullFilter"
};


const struct QBObjectsPermissionsKeysStruct QBObjectsPermissionsKey = {
    .customObjectId = @"customObjectId",
    .readLevel = @"readLevel",
    .updateLevel = @"updateLevel",
    .deleteLevel = @"deleteLevel"
};

const struct QBObjectsPermissionsLevelKeysStruct QBPermissionsLevelKey = {
    .access = @"access",
    .usersIds = @"usersIds",
    .usersGroups = @"usersGroups"
};

const struct QBObjectsPermissionsLevelNameKeysStruct QBPermissionsLevelNameKey = {
  .open = @"open",
  .owner = @"owner",
  .openForUsersIds = @"open_for_users_ids",
  .openForGroups = @"open_for_groups"
};

@implementation QBCOCustomObject (QBSerializer)

+ (NSDictionary *)permissionsLevel {
    static NSDictionary* _permissionsLevel = nil;
    static dispatch_once_t permissionsLevelToken;
    dispatch_once(&permissionsLevelToken, ^{
        _permissionsLevel = @{ @"OPEN": QBPermissionsLevelNameKey.open,
                               @"OWNER": QBPermissionsLevelNameKey.owner,
                               @"OPEN_FOR_USER_IDS": QBPermissionsLevelNameKey.openForUsersIds,
                               @"OPEN_FOR_GROUPS": QBPermissionsLevelNameKey.openForGroups };
    });
    return _permissionsLevel;
}

+ (NSDictionary *)searchOperators {
    static NSDictionary* _searchOperators = nil;
    static dispatch_once_t searchOperatorsToken;
    dispatch_once(&searchOperatorsToken, ^{
        _searchOperators = @{ @"FOR_TYPE": @{ @"INTEGER": @{ @"LT": @"lt",
                                                             @"LTE": @"lte",
                                                             @"GT": @"gt",
                                                             @"GTE": @"gte",
                                                             @"NE": @"ne",
                                                             @"IN": @"in",
                                                             @"NIN": @"nin",
                                                             @"OR": @"or" },
                                              @"FLOAT": @{ @"LT": @"lt",
                                                           @"LTE": @"lte",
                                                           @"GT": @"gt",
                                                           @"GTE": @"gte",
                                                           @"NE": @"ne",
                                                           @"IN": @"in",
                                                           @"NIN": @"nin",
                                                           @"OR": @"or" },
                                              @"STRING": @{ @"NE": @"ne",
                                                            @"IN": @"in",
                                                            @"NIN": @"nin",
                                                            @"OR": @"or",
                                                            @"CTN": @"ctn" },
                                              @"BOOLEAN": @{ @"NE": @"ne" },
                                              @"ARRAY": @{ @"ALL": @"all" }
                                              }
                              };
    });
    return _searchOperators;
}

+ (NSDictionary *)updateOperators {
    static NSDictionary* _updateOperators = nil;
    static dispatch_once_t updateOperatorsToken;
    dispatch_once(&updateOperatorsToken, ^{
        _updateOperators = @{ @"FOR_TYPE": @{ @"INTEGER": @{ @"INC": @"inc" },
                                              @"FLOAT": @{ @"INC": @"inc" },
                                              @"ARRAY": @{ @"PULL": @"pull",
                                                           @"PULL_ALL": @"pull_all",
                                                           @"POP": @"pop",
                                                           @"PUSH": @"push",
                                                           @"ADD_TO_SET": @"add_to_set", }
                                              }
                              };
    });
    return _updateOperators;
}

+ (NSDictionary *)pullFilters {
    static NSDictionary* _pullFilters = nil;
    static dispatch_once_t pullFiltersToken;
    dispatch_once(&pullFiltersToken, ^{
        _pullFilters = @{ @"LT": @"lt",
                         @"LTE": @"lte",
                         @"GT": @"gt",
                         @"GTE": @"gte",
                         @"NE": @"ne",
                         @"IN": @"in",
                         @"NIN": @"nin",
                         @"OR": @"or" };
    });
    return _pullFilters;
}

- (id)toQBResultData:(NSError *__autoreleasing *)error {
    NSMutableDictionary *info = @{}.mutableCopy;
    
    if (self.ID.length) {
        info[QBObjectKey.id] = self.ID;
    }
    
    if (self.parentID.length) {
        info[QBObjectKey.parentId] = self.parentID;
    }
    
    NSString *created_at = [NSDate stringFromISO8601Date:self.createdAt];
    if (created_at != nil) {
        info[QBObjectKey.createdAt] = created_at;
    }
    
    NSString *updated_at = [NSDate stringFromISO8601Date:self.updatedAt];
    if (updated_at != nil) {
        info[QBObjectKey.updatedAt] = updated_at;
    }
    
    if (self.className.length) {
        info[QBObjectKey.className] = self.className;
    }
    
    if (self.userID != 0) {
        info[QBObjectKey.userId] = @(self.userID);
    }
    
    if (self.fields.count) {
        info[QBObjectKey.fields] = self.fields.copy;
    }
    
    if (self.permissions) {
        NSMutableDictionary *permission = @{}.mutableCopy;
        
        if (self.permissions.recordID.length) {
            permission[QBObjectsPermissionsKey.customObjectId] = self.permissions.recordID;
        } else if (self.ID.length) {
            permission[QBObjectsPermissionsKey.customObjectId] = self.ID;
        }
        
        NSMutableDictionary *readLevel = @{}.mutableCopy;
        if (self.permissions.readAccess != QBCOPermissionsAccessNotAllowed) {
            readLevel[QBPermissionsLevelKey.access] =
            [QBCOPermissions permissionsAccessToString: self.permissions.readAccess];
            readLevel[QBPermissionsLevelKey.usersIds] = self.permissions.usersIDsForReadAccess;
            readLevel[QBPermissionsLevelKey.usersGroups] = self.permissions.usersGroupsForReadAccess;
        }
        if (readLevel.count) {
            permission[QBObjectsPermissionsKey.readLevel] = readLevel.copy;
        }
        
        NSMutableDictionary *updateLevel = @{}.mutableCopy;
        if (self.permissions.updateAccess != QBCOPermissionsAccessNotAllowed) {
            updateLevel[QBPermissionsLevelKey.access] =
            [QBCOPermissions permissionsAccessToString: self.permissions.updateAccess];
            updateLevel[QBPermissionsLevelKey.usersIds] = self.permissions.usersIDsForUpdateAccess;
            updateLevel[QBPermissionsLevelKey.usersGroups] = self.permissions.usersGroupsForUpdateAccess;
        }
        if (updateLevel.count) {
            permission[QBObjectsPermissionsKey.updateLevel] = updateLevel.copy;
        }
        
        NSMutableDictionary *deleteLevel = @{}.mutableCopy;
        if (self.permissions.deleteAccess != QBCOPermissionsAccessNotAllowed) {
            deleteLevel[QBPermissionsLevelKey.access] =
            [QBCOPermissions permissionsAccessToString: self.permissions.deleteAccess];
            deleteLevel[QBPermissionsLevelKey.usersIds] = self.permissions.usersIDsForDeleteAccess;
            deleteLevel[QBPermissionsLevelKey.usersGroups] = self.permissions.usersGroupsForDeleteAccess;
        }
        if (deleteLevel.count) {
            permission[QBObjectsPermissionsKey.deleteLevel] = deleteLevel.copy;
        }
        
        if (permission.count) {
            info[QBObjectKey.permission] = permission.copy;
        }
    }
    return [info.copy toQBResultWithType:QBResultTypeDefault error:error];
}

- (void)setupFields:(NSDictionary<NSString *,NSObject *>*)fields
    updateOperators:(NSMutableDictionary<NSString *, NSString *>**)updateOperators {
    if (!self.fields) {
        self.fields = @{}.mutableCopy;
    }
    for (NSString *fieldName in fields.allKeys) {
        NSObject *value = fields[fieldName];
        if ([value isKindOfClass:NSDictionary.class] && updateOperators != nil) {
            NSDictionary<NSString *, NSObject *>*updater =
            (NSDictionary<NSString *, NSObject *>*)value;
            NSObject *updateValue = updater[QBUpdateObjectKey.value];
            NSArray *values = nil;
            NSString *stringValue = @"";
            if ([updateValue isKindOfClass:NSArray.class]) {
                values = (NSArray *)updateValue;
                stringValue = [values componentsJoinedByString:@","];
            } else if ([updateValue isKindOfClass:NSString.class]) {
                stringValue = (NSString *)updateValue;
            } else if ([updateValue isKindOfClass:NSNumber.class]) {
                NSNumber *numberValue = (NSNumber *)updateValue;
                stringValue = [NSString stringWithFormat:@"%@", numberValue];
            } else {
                continue;
            }
            NSString *operator = (NSString *)updater[QBUpdateObjectKey.operator];
            
            NSObject *index = updater[QBUpdateObjectKey.index];
            NSNumber *indexNumber = nil;
            NSString *indexString = nil;
            if ([index isKindOfClass:NSNumber.class]) {
                indexNumber = (NSNumber *)index;
                indexString = [NSString stringWithFormat:@"%@", indexNumber];
            } else if ([index isKindOfClass:NSString.class]) {
                indexString = (NSString *)index;
                indexNumber = @(indexString.integerValue);
            }
            if (indexString.length) {
                NSString *key = [NSString stringWithFormat:@"%@[%@]", fieldName, indexString];
                [*updateOperators addEntriesFromDictionary:@{key: stringValue}];
            } else if (operator.length) {
                NSString *key = [NSString stringWithFormat:@"%@[%@]", operator, fieldName];
                if ([operator isEqualToString:@"pull_all"] ||
                    [operator isEqualToString:@"push"]) {
                    for (NSObject *value in values) {
                        NSString *stringValue = @"";
                        if ([value isKindOfClass:NSString.class]) {
                            stringValue = (NSString *)value;
                        } else if ([value isKindOfClass:NSNumber.class]) {
                            stringValue = [NSString stringWithFormat:@"%@", value];
                        } else {
                            continue;
                        }
                        if (!stringValue.length) {
                            continue;
                        }
                        if ([operator isEqualToString:@"pull_all"]) {
                            key = [key stringByAppendingString:@"[]"];
                        }
                        [*updateOperators setObject:stringValue forKey:key];
                    }
                } else {
                    if ([updater[QBUpdateObjectKey.pullFilter] isKindOfClass:NSString.class]) {
                        NSString *pullFilter = (NSString *)updater[QBUpdateObjectKey.pullFilter];
                        if (pullFilter.length) {
                            key = [key stringByAppendingString:[NSString stringWithFormat:@"[%@]",pullFilter]];
                        }
                    }
                    if (!key.length || !stringValue.length) {
                        continue;
                    }
                    [*updateOperators setObject:stringValue forKey:key];
                }
            } else {
                continue;
            }
        } else {
            self.fields[fieldName] = value;
        }
    }
}

@end
