//
//  NSDictionary+QBSerializer.m
//  quickblox_sdk
//
//  Created by Injoit on 24.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "NSDictionary+QBSerializer.h"
#import "NSError+Helper.h"

const struct QBFilterKeysStruct FilterKey = {
    .filter = @"filter",
    .field = @"field",
    .instruction = @"operator",
    .value = @"value",
    .type = @"type"
};

const struct QBSortKeysStruct SortKey = {
    .sort = @"sort",
    .field = @"field",
    .ascending = @"ascending",
    .type = @"type"
};

@implementation NSDictionary (QBSerializer)

- (id)toQBResultWithType:(QBResultType)type
                   error:(NSError *__autoreleasing *)error {
    if (self.count == 0) {
        *error = [NSError errrorWithRNQBMessage:@"[NSDictionary+QBSerializer] - no data to serialize"];
        return @{};
    }
    
    switch (type) {
        case QBResultTypeDefault:
        case QBResultTypeObject: {
            return self;
        }
        case QBResultTypeJSON: {
            NSData *jsonData = [NSJSONSerialization dataWithJSONObject:self
                                                               options:NSJSONWritingPrettyPrinted
                                                                 error:error];
            id value = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
            return value;
        }
    }
}

- (NSDictionary *)toSortData {
    NSString *field = self[SortKey.field];
    NSString *rule = [self[SortKey.ascending] boolValue] ? @"sort_asc" : @"sort_desc";
    if (field.length) {
        return @{rule: field};
    }
    return @{};
}

- (NSDictionary *)toFilterData {
    NSString *field = self[FilterKey.field];
    NSString *instruction = self[FilterKey.instruction];
    NSObject *objectValue = self[FilterKey.value];
    NSString *value = nil;
    if ([objectValue isKindOfClass:[NSArray class]]) {
        value = [(NSArray *)objectValue componentsJoinedByString:@","];
    } else if ([objectValue isKindOfClass:[NSString class]]) {
        value = (NSString *)objectValue;
    }
    if (field.length && value.length) {
        NSString *key = instruction.length ?
        [NSString stringWithFormat:@"%@[%@]", field, instruction] :
        field;
        
        return @{key: value};
    }
    return @{};
}

- (NSDictionary *)toUserSortData {
    NSString *type = self[SortKey.type];
    NSString *field = self[SortKey.field];
    NSString *rule = [self[SortKey.ascending] boolValue] ? @"asc" : @"desc";
    if (type.length && field.length) {
        return @{ @"order": [NSString stringWithFormat:@"%@ %@ %@",
                             rule,
                             type,
                             field] };
    }
    return @{};
}

- (NSDictionary *)toUserFilterData {
    NSString *type = self[FilterKey.type];
    NSString *field = self[FilterKey.field];
    NSString *instruction = self[FilterKey.instruction];
    NSObject *objectValue = self[FilterKey.value];
    NSString *value = nil;
    if ([objectValue isKindOfClass:[NSArray class]]) {
        value = [(NSArray *)objectValue componentsJoinedByString:@","];
    } else if ([objectValue isKindOfClass:[NSString class]]) {
        value = (NSString *)objectValue;
    }
    if (type.length && field.length && instruction.length && value.length) {
        return @{ @"filter[]": [NSString stringWithFormat:@"%@ %@ %@ %@",
                                type,
                                field,
                                instruction,
                                value] };
    }
    return @{};
}

@end
