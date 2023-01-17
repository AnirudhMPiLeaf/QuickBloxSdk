//
//  QMUsersModule.m
//  quickblox_sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBUsersModule.h"
#import "QBUUser+QBSerializer.h"
#import "QBGeneralResponsePage+QBSerializer.h"
#import "QBUsersConstants.h"

@implementation QBUsersModule

- (void)create:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject {
  if ([NSError reject:reject
                 info:info
         requirements:@[ [Requirement class:NSString.class
                                        key:QBUserKey.login],
                         [Requirement class:NSString.class
                                        key:QBUserKey.password] ]
       ]) {
    return;
  }
  
  QBUUser *user = [QBUUser user];
  user.login = info[QBUserKey.login];
  user.password = info[QBUserKey.password];
  
  NSNumber *blobId = info[QBUserKey.blobId];
  NSString *customData = info[QBUserKey.customData];
  NSString *email = info[QBUserKey.email];
  NSString *externalId = info[QBUserKey.externalId];
  NSString *facebookId = info[QBUserKey.facebookId];
  NSString *fullName = info[QBUserKey.fullName];
  NSString *phone = info[QBUserKey.phone];
  NSArray<NSString *> *tags = info[QBUserKey.tags];
  NSString *twitterId = info[QBUserKey.twitterId];
  NSString *website = info[QBUserKey.website];
  
  if (email.length) {
    user.email = email;
  }
  if (blobId) {
    user.blobID = blobId.unsignedIntegerValue;
  }
  if (externalId.length) {
    user.externalUserID = (NSUInteger)externalId.longLongValue;
  }
  if (facebookId.length) {
    user.facebookID = facebookId;
  }
  if (twitterId.length) {
    user.twitterID = twitterId;
  }
  if (fullName.length) {
    user.fullName = fullName;
  }
  if (phone.length) {
    user.phone = phone;
  }
  if (website.length) {
    user.website = website;
  }
  if (customData.length) {
    user.customData = customData;
  }
  if (tags.count) {
    user.tags = tags;
  }
  
  [QBRequest signUp:user
       successBlock:^(QBResponse *response,
                      QBUUser *user) {
    [user toQBResultDataWithResolver:resolve rejecter:reject];
  } errorBlock:^(QBResponse *response) {
    [response reject:reject];
  }];
}

- (void)update:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject {
  NSString *errorMessage = nil;
  
  NSString *newPassword = info[QBUserKey.newPassword];
  NSString *oldPassword = info[QBUserKey.password];
  if (newPassword.length && !oldPassword.length) {
    errorMessage = NSLocalizedString(@"If field newPassword did set, the password should set as well.", nil);
  }
  
  if (!newPassword.length && oldPassword.length) {
    errorMessage = NSLocalizedString(@"If field password did set, the newPassword should set as well.", nil);
  }
  
  if (errorMessage.length){
    [NSError reject:reject
            message:errorMessage];
    return;
  }
  
  NSString *login = info[QBUserKey.login];
  NSNumber *blobId = info[QBUserKey.blobId];
  NSString *customData = info[QBUserKey.customData];
  NSString *email = info[QBUserKey.email];
  NSString *externalId = info[QBUserKey.externalId];
  NSString *facebookId = info[QBUserKey.facebookId];
  NSString *fullName = info[QBUserKey.fullName];
  NSString *phone = info[QBUserKey.phone];
  NSArray<NSString *> *tags = info[QBUserKey.tags];
  NSString *twitterId = info[QBUserKey.twitterId];
  NSString *website = info[QBUserKey.website];
  
  QBUpdateUserParameters *user = [QBUpdateUserParameters new];
  user.login = login;
  user.password = newPassword;
  user.oldPassword = oldPassword;
  user.email = email;
  user.blobID = blobId.unsignedIntegerValue;
  user.externalUserID = (NSUInteger)externalId.longLongValue;
  user.facebookID = facebookId;
  user.twitterID = twitterId;
  user.fullName = fullName;
  user.phone = phone;
  user.website = website;
  user.customData = customData;
  user.tags = tags;
  
  [QBRequest updateCurrentUser:user
                  successBlock:^(QBResponse *response,
                                 QBUUser *user) {
    [user toQBResultDataWithResolver:resolve rejecter:reject];
  } errorBlock:^(QBResponse *response) {
    [response reject:reject];
  }];
}

- (void)getUsers:(NSDictionary *)info
        resolver:(QBResolveBlock)resolve
        rejecter:(QBRejectBlock)reject {
  NSMutableDictionary *extendedRequest = @{}.mutableCopy;
  
  NSDictionary *sortInfo = info[SortKey.sort];
  if (sortInfo) {
    [extendedRequest addEntriesFromDictionary:[sortInfo toUserSortData]];
  }
  
  NSDictionary *filterInfo = info[FilterKey.filter];
  if (filterInfo) {
    [extendedRequest addEntriesFromDictionary:[filterInfo toUserFilterData]];
  }
  
  NSNumber *pageNumber = info[QBGeneralPageKey.page];
  NSNumber *perPageNumber = info[QBGeneralPageKey.perPage];
  
  NSUInteger currentPage = pageNumber ? pageNumber.unsignedIntegerValue : 1;
  NSUInteger perPage = perPageNumber ? perPageNumber.unsignedIntegerValue : 10;
  QBGeneralResponsePage *page =
  [QBGeneralResponsePage responsePageWithCurrentPage:currentPage
                                             perPage:perPage];
  
  [QBRequest usersWithExtendedRequest:extendedRequest.copy
                                 page:page
                         successBlock:^(QBResponse *response,
                                        QBGeneralResponsePage *page,
                                        NSArray *users) {
    NSError *error = nil;
    NSArray *results = [users toQBResultArray:&error];
    if ([error reject:reject]) {
      return;
    }
    NSDictionary *rnPage = [page toQBResultData:&error];
    if ([error reject:reject]) {
      return;
    }
    NSMutableDictionary *result = @{ QBUsersKey.users: results }.mutableCopy;
    [result addEntriesFromDictionary:rnPage];
    resolve(result.copy);
  } errorBlock:^(QBResponse *response) {
    [response reject:reject];
  }];
}

- (void)getUsersByTag:(NSDictionary *)info
             resolver:(QBResolveBlock)resolve
             rejecter:(QBRejectBlock)reject {
  NSArray<NSString *> *tags = info[QBUserKey.tags];
  
  NSNumber *pageNumber = info[QBGeneralPageKey.page];
  NSNumber *perPageNumber = info[QBGeneralPageKey.perPage];
  
  NSUInteger currentPage = pageNumber ? pageNumber.unsignedIntegerValue : 1;
  NSUInteger perPage = perPageNumber ? perPageNumber.unsignedIntegerValue : 10;
  QBGeneralResponsePage *page =
  [QBGeneralResponsePage responsePageWithCurrentPage:currentPage
                                             perPage:perPage];
  
  [QBRequest usersWithTags:tags
                      page:page
              successBlock:^(QBResponse *response,
                             QBGeneralResponsePage *page,
                             NSArray *users) {
    NSError *error = nil;
    NSArray *results = [users toQBResultArray:&error];
    if ([error reject:reject]) {
      return;
    }
    NSDictionary *rnPage = [page toQBResultData:&error];
    if ([error reject:reject]) {
      return;
    }
    NSMutableDictionary *result = @{ QBUsersKey.users: results }.mutableCopy;
    [result addEntriesFromDictionary:rnPage];
    resolve(result.copy);
  } errorBlock:^(QBResponse *response) {
    [response reject:reject];
  }];
}

@end
