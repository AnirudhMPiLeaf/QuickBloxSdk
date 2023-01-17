//
//  QBConstants.m
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBConstants.h"

const struct QBBridgeEventKeysStruct QBBridgeEventKey = {
    .type = @"type",
    .payload = @"payload",
};

//MARK: SORT/FILTER namespace constants
NSString * const kSortKey     = @"sort";
NSString * const kFiltersKey  = @"filters";

NSString * const TYPE        = @"TYPE";
NSString * const kStringType = @"STRING";
NSString * const kNumberType = @"NUMBER";
NSString * const kDateType   = @"DATE";

NSString * const FIELD                      = @"FIELD";
NSString * const kIdField                   = @"ID";
NSString * const kFullNameField             = @"FULL_NAME";
NSString * const kEmailField                = @"EMAIL";
NSString * const kLoginField                = @"LOGIN";
NSString * const kPhoneField                = @"PHONE";
NSString * const kWebsiteField              = @"WEBSITE";
NSString * const kCreatedAtField            = @"CREATED_AT";
NSString * const kUpdatedAtField            = @"UPDATED_AT";
NSString * const kLastRequestAtField        = @"LAST_REQUEST_AT";
NSString * const kExternalUserIdField       = @"EXTERNAL_USER_ID";
NSString * const kTwitterIdField            = @"TWITTER_ID";
NSString * const kFacebookIdField           = @"FACEBOOK_ID";
NSString * const klastMessageDateSentField  = @"LAST_MESSAGE_DATE_SENT";
NSString * const kDateSentField             = @"DATE_SENT";
NSString * const kBodyField                 = @"BODY";
NSString * const kSenderIdField             = @"SENDER_ID";
NSString * const kRecipientIdField          = @"RECIPIENT_ID";
NSString * const kAttachmentTypeField       = @"ATTACHMENTS_TYPE";
NSString * const kTypeField                 = @"TYPE";
NSString * const kNameField                 = @"NAME";

NSString * const OPERATOR   = @"OPERATOR";
NSString * const kGT        = @"GT";
NSString * const kLT        = @"LT";
NSString * const kLTE       = @"LTE";
NSString * const kGE        = @"GE";
NSString * const kGTE       = @"GTE";
NSString * const kLE        = @"LE";
NSString * const kEQ        = @"EQ";
NSString * const kNE        = @"NE";
NSString * const kBETWEEN   = @"BETWEEN";
NSString * const kIN        = @"IN";
NSString * const kNIN       = @"NIN";
NSString * const kOR        = @"OR";
NSString * const kALL        = @"ALL";
NSString * const kCTN        = @"CTN";
