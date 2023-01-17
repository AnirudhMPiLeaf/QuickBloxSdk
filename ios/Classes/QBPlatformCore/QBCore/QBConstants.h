//
//  QBConstants.h
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

#define event(name) [NSString stringWithFormat:@"@QB/%@", name]
#define sdkEvent(name) [NSString stringWithFormat:@"@QB/SDK/EVENT/%@", name]
#define eventName(sdkEvent) [sdkEvent stringByReplacingOccurrencesOfString:@"QB/SDK/EVENT" withString:@"QB"]

struct QBGetActionStruct {
    __unsafe_unretained NSString * _Nonnull const sort;
    __unsafe_unretained NSString * _Nonnull const filter;
};
extern const struct QBGetActionStruct QBGetAction;

struct QBGetActionKeysStruct {
    __unsafe_unretained NSString * _Nonnull const type;
    __unsafe_unretained NSString * _Nonnull const field;
    __unsafe_unretained NSString * _Nonnull const operator;
};
extern const struct QBGetActionKeysStruct QBGetActionKey;

struct QBFieldTypeStruct {
    __unsafe_unretained NSString * _Nonnull const string;
    __unsafe_unretained NSString * _Nonnull const number;
    __unsafe_unretained NSString * _Nonnull const date;
};
extern const struct QBFieldTypeStruct QBFieldType;

struct QBBridgeEventKeysStruct {
    __unsafe_unretained NSString * _Nonnull const type;
    __unsafe_unretained NSString * _Nonnull const payload;
};
extern const struct QBBridgeEventKeysStruct QBBridgeEventKey;

//MARK: SORT/FILTER namespace constants
extern NSString * const kSortKey;
extern NSString * const kFiltersKey;

extern NSString * const TYPE;
extern NSString * const kStringType;
extern NSString * const kNumberType;
extern NSString * const kDateType;

extern NSString * const FIELD;
extern NSString * const kIdField;
extern NSString * const kFullNameField;
extern NSString * const kEmailField;
extern NSString * const kLoginField;
extern NSString * const kPhoneField;
extern NSString * const kWebsiteField;
extern NSString * const kCreatedAtField;
extern NSString * const kUpdatedAtField;
extern NSString * const kLastRequestAtField;
extern NSString * const kExternalUserIdField;
extern NSString * const kTwitterIdField;
extern NSString * const kFacebookIdField;
extern NSString * const klastMessageDateSentField;
extern NSString * const kDateSentField;
extern NSString * const kBodyField;
extern NSString * const kSenderIdField;
extern NSString * const kRecipientIdField;
extern NSString * const kAttachmentTypeField;
extern NSString * const kTypeField;
extern NSString * const kNameField;

extern NSString * const OPERATOR;
extern NSString * const kGT;
extern NSString * const kLT;
extern NSString * const kLTE;
extern NSString * const kGE;
extern NSString * const kGTE;
extern NSString * const kLE;
extern NSString * const kEQ;
extern NSString * const kNE;
extern NSString * const kBETWEEN;
extern NSString * const kIN;
extern NSString * const kNIN;
extern NSString * const kOR;
extern NSString * const kALL;
extern NSString * const kCTN;

NS_ASSUME_NONNULL_END
