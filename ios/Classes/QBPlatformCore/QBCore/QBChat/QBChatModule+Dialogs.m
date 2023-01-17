//
//  QBChatModule+Dialogs.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatModule+Dialogs.h"
#import "QBResponsePage+QBSerializer.h"
#import "QBChatDialog+QBSerializer.h"

@implementation QBChatModule (Dialogs)

- (void)getDialogs:(NSDictionary *)info
          resolver:(QBResolveBlock)resolve
          rejecter:(QBRejectBlock)reject {
    NSNumber *limit = info[QBPageKey.limit];
    NSNumber *skip = info[QBPageKey.skip];
    QBResponsePage *page =
    [QBResponsePage responsePageWithLimit:limit ? limit.integerValue : 100
                                     skip:skip ? skip.integerValue : 0];
    
    NSMutableDictionary *extendedRequest = @{}.mutableCopy;
    NSDictionary *sortInfo = info[SortKey.sort];
    if (sortInfo) {
        [extendedRequest addEntriesFromDictionary:[sortInfo toSortData]];
    }
    
    NSDictionary *filterInfo = info[FilterKey.filter];
    if (filterInfo) {
        [extendedRequest addEntriesFromDictionary:[filterInfo toFilterData]];
    }
    
    __weak __typeof(self)weakSelf = self;
    [QBRequest dialogsForPage:page
              extendedRequest:extendedRequest.copy
                 successBlock:^(QBResponse *response,
                                NSArray *dialogObjects,
                                NSSet *dialogsUsersIDs,
                                QBResponsePage *page) {
        [weakSelf addDialogsToCache:dialogObjects];
        NSError *error = nil;
        NSArray *resultArray = [dialogObjects toQBResultArray:&error];
        if ([error reject:reject]) {
            return;
        }
        NSDictionary *resultPage = [page toQBResultData:&error];
        if ([error reject:reject]) {
            return;
        }
        
        NSMutableDictionary *result = @{ QBChatKey.dialogs: resultArray }.mutableCopy;
        [result addEntriesFromDictionary:resultPage];
        resolve(result.copy);
    } errorBlock:^(QBResponse *response) {
        [response reject:reject];
    }];
}


- (void)getDialogsCount:(NSDictionary *)info
               resolver:(QBResolveBlock)resolve
               rejecter:(QBRejectBlock)reject {
    NSMutableDictionary *extendedRequest = @{}.mutableCopy;
    NSDictionary *filterInfo = info[FilterKey.filter];
    if (filterInfo) {
        [extendedRequest addEntriesFromDictionary:[filterInfo toFilterData]];
    }
    
    [QBRequest countOfDialogsWithExtendedRequest:extendedRequest.copy
                                    successBlock:^(QBResponse *response,
                                                   NSUInteger count) {
        if (resolve) {
            resolve(@(count));
        }
    } errorBlock:^(QBResponse *response) {
        [response reject:reject];
    }];
}

- (void)createDialog:(NSDictionary *)info
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject {
    // parameter type (requered)
    NSNumber *typeNumber = info[QBDialogKey.type];
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:typeNumber
              objectKey:QBDialogKey.type]) {
        return;
    }
    
    NSInteger typeIntegerValue = typeNumber.integerValue;
    if (typeIntegerValue < 1 || typeIntegerValue > 3) {
        NSString *description = @"Required parameter type has a wrong value: ";
        NSString *errorMessage = [NSString stringWithFormat:@"%@%@", description, typeNumber];
        [NSError reject:reject message:errorMessage];
        return;
    }
    
    QBChatDialogType type = typeIntegerValue;
    
    QBChatDialog *chatDialog =
    [[QBChatDialog alloc] initWithDialogID:nil
                                      type:type];
    
    // parameter name
    NSString *name = info[QBDialogKey.name];
    if (name.length) {
        chatDialog.name = name;
    }
    
    // parameter photo
    NSString *photo = info[QBDialogKey.photo];
    if (photo.length) {
        chatDialog.photo = photo;
    }
    
    // parameter occupantsIds
    if ([info[QBDialogKey.occupantsIds] isKindOfClass:[NSArray class]]) {
        NSArray<NSNumber *>*Ids = info[QBDialogKey.occupantsIds];
        chatDialog.occupantIDs = Ids.copy;
    }
    
    // parameter customData
    NSDictionary *customData = info[QBDialogKey.customData];
    if ([self validateDialogCustomData:customData rejecter:reject] == NO) {
        return;
    }
    chatDialog.data = customData;
    
    __weak __typeof(self)weakSelf = self;
    [QBRequest createDialog:chatDialog
               successBlock:^(QBResponse *response,
                              QBChatDialog *createdDialog) {
        [weakSelf addDialogsToCache:@[createdDialog]];
        [createdDialog toQBResultDataWithResolver:resolve rejecter:reject];
    } errorBlock:^(QBResponse *response) {
        [response reject:reject];
    }];
}

- (void)updateDialog:(NSDictionary *)info
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject {
    __weak __typeof(self)weakSelf = self;
    [self dialogWithInfo:info
                 success:^(QBChatDialog *dialog) {
        NSArray<NSNumber *>*addUsers = info[QBChatKey.addUsers];
        NSArray<NSNumber *>*removeUsers = info[QBChatKey.removeUsers];
        NSString *name = info[QBDialogKey.name];
        NSString *photo = info[QBDialogKey.photo];
        if ((removeUsers.count &&
             weakSelf.currentId != dialog.userID) &&
            (removeUsers.count > 1 ||
             ![removeUsers containsObject:@(weakSelf.currentId)])) {
            [NSError reject:reject
                    message:@"Only dialog's creator(owner) can remove any users from occupants_ids."];
            return;
        }
        QBChatDialog *updateDialog =
        [[QBChatDialog alloc] initWithDialogID:dialog.ID
                                          type:QBChatDialogTypeGroup];
        NSMutableArray<NSString *>*pushOccupantsIDs = @[].mutableCopy;
        for (NSNumber *numberId in addUsers) {
            [pushOccupantsIDs addObject:numberId.stringValue];
        }
        if (pushOccupantsIDs.count) {
            updateDialog.pushOccupantsIDs = pushOccupantsIDs.copy;
        }
        
        __block NSMutableArray<NSString *>*pullOccupantsIDs = @[].mutableCopy;
        __block BOOL needRemoveSeparate = NO;
        for (NSNumber *numberId in removeUsers) {
            [pullOccupantsIDs addObject:numberId.stringValue];
        }
        if (pullOccupantsIDs.count) {
            if (pushOccupantsIDs.count) {
                needRemoveSeparate = YES;
            } else {
                updateDialog.pullOccupantsIDs = pullOccupantsIDs.copy;
            }
        }
        
        if (name != nil) {
            updateDialog.name = name;
        }
        
        if (photo != nil) {
            updateDialog.photo = photo;
        }
        
        NSDictionary *customData = info[QBDialogKey.customData];
        if ([self validateDialogCustomData:customData rejecter:reject] == NO) {
            return;
        }
        updateDialog.data = customData;
        
        [QBRequest updateDialog:updateDialog
                   successBlock:^(QBResponse *responce,
                                  QBChatDialog *dialog) {
            if (needRemoveSeparate) {
                QBChatDialog *removeUsersDialog =
                [[QBChatDialog alloc] initWithDialogID:dialog.ID
                                                  type:QBChatDialogTypeGroup];
                removeUsersDialog.pullOccupantsIDs = pullOccupantsIDs.copy;
                [QBRequest updateDialog:removeUsersDialog
                           successBlock:^(QBResponse *responce,
                                          QBChatDialog *dialog) {
                    [weakSelf addDialogsToCache:@[dialog]];
                    [dialog toQBResultDataWithResolver:resolve
                                              rejecter:reject];
                } errorBlock:^(QBResponse *response) {
                    [response reject:reject];
                }];
                return;
            }
            [weakSelf addDialogsToCache:@[dialog]];
            [dialog toQBResultDataWithResolver:resolve rejecter:reject];
        } errorBlock:^(QBResponse *response) {
            [response reject:reject];
        }];
    } rejecter:reject];
}

- (BOOL)validateDialogCustomData:(NSDictionary *)customData
                        rejecter:(QBRejectBlock)reject {
    if (customData == nil) {
        return YES;
    }
    
    NSString *stringType = NSStringFromClass(NSDictionary.class);
    stringType = [stringType stringByReplacingOccurrencesOfString:@"NS"
                                                       withString:@""];
    if (![customData isKindOfClass:NSDictionary.class]) {
        NSString *message = [NSString stringWithFormat:@"\"customData\" should be of type %@", stringType];
        [NSError reject:reject message:message];
        return NO;
    }
    
    if (!customData.count) {
        NSString *message = @"Error: the custom data shouldn't be empty";
        [NSError reject:reject message:message];
        return NO;
    }
    
    for (NSString *key in customData.allKeys) {
        NSObject *value = customData[key];
        if ((key.length == 0) || ([value isKindOfClass:NSNull.class])) {
            NSString *message = [NSString stringWithFormat:@"Error parse custom data: \nkey -> %@ \nvalue -> %@", key, value];
            [NSError reject:reject message:message];
            return NO;
        }
    }
    return YES;
}

- (void)deleteDialog:(NSDictionary *)info
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject {
    NSObject *dialogIdObject = info[QBChatKey.dialogId];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:dialogIdObject
              objectKey:QBChatKey.dialogId]) {
        return;
    }
    
    BOOL force = NO;
    NSObject *forceValue = info[QBDialogKey.force];
    if ([forceValue isKindOfClass:NSString.class]) {
        NSString *forceString = (NSString *)forceValue;
        force = forceString.boolValue;
    }
    if ([forceValue isKindOfClass:NSNumber.class]) {
        NSNumber *forceNumber = (NSNumber *)forceValue;
        force = forceNumber.boolValue;
    }
 
    NSString *dialogId = (NSString *)dialogIdObject;
    [self deleteDialog:dialogId force:force resolver:resolve rejecter:reject];
}

- (void)getOnlineUsers:(NSDictionary *)info
              resolver:(QBResolveBlock)resolve
              rejecter:(QBRejectBlock)reject {
    [self dialogWithInfo:info
                 success:^(QBChatDialog *dialog) {
        [dialog requestOnlineUsersWithCompletionBlock:
         ^(NSMutableArray<NSNumber *> * _Nullable onlineUsers,
           NSError * _Nullable error) {
            if (![error reject:reject] && resolve) {
                resolve(onlineUsers.copy);
            }
        }];
    } rejecter:reject];
}

- (void)joinDialog:(NSDictionary *)info
          resolver:(QBResolveBlock)resolve
          rejecter:(QBRejectBlock)reject {
    [self dialogWithInfo:info
                 success:^(QBChatDialog *dialog) {
        if (dialog.type == QBChatDialogTypePrivate) {
            NSString *errorMessage = @"The private dialog shouldn't be join";
            [NSError reject:reject message:errorMessage];
            return;
        }
        [dialog joinWithCompletionBlock:^(NSError * _Nullable error) {
            if (![error reject:reject] && resolve) {
                [dialog toQBResultDataWithResolver:resolve
                                          rejecter:reject];
            }
        }];
    } rejecter:reject];
}

- (void)leaveDialog:(NSDictionary *)info
           resolver:(QBResolveBlock)resolve
           rejecter:(QBRejectBlock)reject {
    [self dialogWithInfo:info
                 success:^(QBChatDialog *dialog) {
        if (dialog.type == QBChatDialogTypePrivate) {
            NSString *errorMessage = @"The private dialog shouldn't be leave";
            [NSError reject:reject message:errorMessage];
            return;
        }
        [dialog leaveWithCompletionBlock:^(NSError * _Nullable error) {
            if (![error reject:reject] && resolve) {
                [dialog toQBResultDataWithResolver:resolve
                                          rejecter:reject];
            }
        }];
    } rejecter:reject];
}

- (void)isJoinedDialog:(NSDictionary *)info
              resolver:(QBResolveBlock)resolve
              rejecter:(QBRejectBlock)reject {
    [self dialogWithInfo:info
                 success:^(QBChatDialog *dialog) {
        if (dialog.type == QBChatDialogTypePrivate) {
            NSString *errorMessage = @"The private dialog shouldn't be joined";
            [NSError reject:reject message:errorMessage];
            return;
        }
        BOOL isJoined = dialog.isJoined;
        resolve(@(isJoined));
    } rejecter:reject];
}

//MARK - Internal methods

- (void)deleteDialog:(NSString *)dialogId
               force:(BOOL)force
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject {
    NSSet *dialogsIDs = [NSSet setWithObject:dialogId];
    __weak __typeof(self)weakSelf = self;
    [QBRequest deleteDialogsWithIDs:dialogsIDs
                        forAllUsers:force
                       successBlock:^(QBResponse *response,
                                      NSArray *deletedObjectsIDs,
                                      NSArray *notFoundObjectsIDs,
                                      NSArray *wrongPermissionsObjectsIDs) {
        if (resolve) {
            NSMutableArray *dialogsToRemove = @[].mutableCopy;
            for (NSString *dialogId in deletedObjectsIDs) {
                for (QBChatDialog *dialog in weakSelf.dialogsCache.allValues) {
                    if ([dialog.ID isEqualToString:dialogId]) {
                        [dialogsToRemove addObject:dialog];
                        continue;
                    }
                }
            }
            [weakSelf removeDialogsFromCache:dialogsToRemove];
            resolve(nil);
        }
    } errorBlock:^(QBResponse *response) {
        [response reject:reject];
    }];
}

@end
