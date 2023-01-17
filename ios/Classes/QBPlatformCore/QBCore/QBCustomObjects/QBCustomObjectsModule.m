//
//  QBCustomObjectsModule.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBCustomObjectsModule.h"
#import "QBCOCustomObject+QBSerializer.h"
#import "QBCustomObjectsConstants.h"
#import "QBResponsePage+QBSerializer.h"

@implementation QBCustomObjectsModule

- (void)create:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject {
    NSString *className = info[QBObjectKey.className];
    NSDictionary<NSString *,NSObject *>*fields = info[QBObjectKey.fields];
    NSArray<NSDictionary *>*objectsValues = info[QBCustomObjectsKey.objects];
    NSMutableArray<NSDictionary *>*objects = objectsValues.count ?
    objectsValues.mutableCopy :
    @[].mutableCopy;
    
    if (fields.count) {
        //        [objects addObject:fields];
        
        QBCOCustomObject *customObject = [QBCOCustomObject customObject];
        [customObject setupFields:fields.copy updateOperators:nil];
        customObject.className = className;
        
        [QBRequest createObject:customObject successBlock:^(QBResponse * _Nonnull response,
                                                            QBCOCustomObject * _Nullable object) {
            [object toQBResultDataWithResolver:resolve rejecter:reject];
        } errorBlock:^(QBResponse * _Nonnull response) {
            [response reject:reject];
        }];
        return;
    }
    
    NSMutableArray<QBCOCustomObject *>*customObjects = @[].mutableCopy;
    
    for (NSDictionary *object in objects) {
        QBCOCustomObject *customObject = [QBCOCustomObject customObject];
        [customObject setupFields:object.copy updateOperators:nil];
        [customObjects addObject:customObject];
    }
    
    [QBRequest createObjects:customObjects.copy
                   className:className
                successBlock:^(QBResponse *response,
                               NSArray *objects) {
        [objects toQBResultArrayWithResolver:resolve rejecter:reject];
    } errorBlock:^(QBResponse *error) {
        [error reject:reject];
    }];
}

- (void)getByIds:(NSDictionary *)info
        resolver:(QBResolveBlock)resolve
        rejecter:(QBRejectBlock)reject {
    NSObject *classNameObject = info[QBObjectKey.className];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:classNameObject
              objectKey:QBObjectKey.className]) {
        return;
    }
    NSString *className = (NSString *)classNameObject;
    
    NSObject *objectsIdsObject = info[QBCustomObjectsKey.objectsIds];
    if ([NSError reject:reject
           checkerClass:NSArray.class
                 object:objectsIdsObject
              objectKey:QBCustomObjectsKey.objectsIds]) {
        return;
    }
    NSArray<NSString *>*objectsIds = (NSArray<NSString *>*)objectsIdsObject;
    
    NSMutableDictionary *extendedRequest = [NSMutableDictionary dictionary];
    [extendedRequest setObject:objectsIds forKey:@"_id[in]"];

    [QBRequest objectsWithClassName:className extendedRequest:extendedRequest successBlock:^(QBResponse *response, NSArray *objects, QBResponsePage *page) {
        if (objects.count) {
            [objects toQBResultArrayWithResolver:resolve rejecter:reject];
            return;
        }
        [NSError reject:reject
                message:@"Entity you are looking for was not found.,The resource wasn’t found, "];
    } errorBlock:^(QBResponse *response) {
        [response reject:reject];
    }];
    // TODO: need fix
//    [QBRequest objectsWithClassName:className
//                                IDs:objectsIds
//                       successBlock:^(QBResponse *response,
//                                      NSArray *objects) {
//        [objects toQBResultArrayWithResolver:resolve rejecter:reject];
//    } errorBlock:^(QBResponse *response) {
//        [response reject:reject];
//    }];
}

- (void)get:(NSDictionary *)info
   resolver:(QBResolveBlock)resolve
   rejecter:(QBRejectBlock)reject {
    NSObject *classNameObject = info[QBObjectKey.className];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:classNameObject
              objectKey:QBObjectKey.className]) {
        return;
    }
    NSString *className = (NSString *)classNameObject;
    
    NSNumber *limit = info[QBPageKey.limit];
    NSNumber *skip = info[QBPageKey.skip];
    
    NSMutableDictionary *extendedRequest = @{ QBPageKey.limit: limit ?: @(100),
                                              QBPageKey.skip: skip ?: @(0) }.mutableCopy;
    NSDictionary *sortInfo = info[SortKey.sort];
    if (sortInfo) {
        [extendedRequest addEntriesFromDictionary:[sortInfo toSortData]];
    }
    
    NSDictionary *filterInfo = info[FilterKey.filter];
    if (filterInfo) {
        [extendedRequest addEntriesFromDictionary:[filterInfo toFilterData]];
    }
    
    NSArray<NSString *>*includes = info[@"include"];
    if (includes.count) {
        extendedRequest[@"output[include]"] = includes;
    }
    
    NSArray<NSString *>*excludes = info[@"exclude"];
    if (excludes.count) {
        extendedRequest[@"output[exclude]"] = excludes;
    }
    
    [QBRequest objectsWithClassName:className
                    extendedRequest:extendedRequest
                       successBlock:^(QBResponse *response,
                                      NSArray *objects,
                                      QBResponsePage *page) {
        [objects toQBResultArrayWithResolver:resolve rejecter:reject];
    } errorBlock:^(QBResponse *response) {
        [response reject:reject];
    }];
}

- (void)update:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject {
    NSObject *classNameObject = info[QBObjectKey.className];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:classNameObject
              objectKey:QBObjectKey.className]) {
        return;
    }
    NSString *className = (NSString *)classNameObject;
    
    NSArray<NSDictionary<NSString *, NSObject *>*>*objects = info[QBCustomObjectsKey.objects];
    if (objects.count) {
        NSMutableArray<QBCOCustomObject *>*customObjects = @[].mutableCopy;
        for (NSDictionary<NSString *, NSObject *>*objectValue in objects) {
            NSObject *idObject = objectValue[QBObjectKey.id];
            NSObject *fieldsObject = objectValue[QBObjectKey.fields];
            if (![idObject isKindOfClass:NSString.class] ||
                ![fieldsObject isKindOfClass:NSDictionary.class]) {
                [NSError reject:reject
                        message:NSLocalizedString(@"id and fields are required.", nil)];
                return;
            }
            NSString *ID = (NSString *)idObject;
            NSDictionary<NSString *,NSObject *>*fields = (NSDictionary<NSString *,NSObject *>*)fieldsObject;
            if (!ID.length || !fields.count) {
                [NSError reject:reject
                        message:NSLocalizedString(@"id and fields are required.", nil)];
                return;
            }
            QBCOCustomObject *customObject = [QBCOCustomObject customObject];
            customObject.ID = ID;
            customObject.fields = fields.mutableCopy;
            [customObjects  addObject:customObject];
        }
        
        [QBRequest updateObjects:customObjects.copy
                       className:className
                    successBlock:^(QBResponse *response,
                                   NSArray *objects,
                                   NSArray *notFoundObjectsIds) {
            [objects toQBResultArrayWithResolver:resolve rejecter:reject];
        } errorBlock:^(QBResponse *error) {
            [error reject:reject];
        }];
        return;
    }
    
    NSDictionary<NSString *,NSObject *>*fields = info[QBObjectKey.fields];
    if (fields.count) {
        NSObject *idObject = info[QBObjectKey.id];
        NSObject *fieldsObject = info[QBObjectKey.fields];
        if (![idObject isKindOfClass:NSString.class] ||
            ![fieldsObject isKindOfClass:NSDictionary.class]) {
            [NSError reject:reject
                    message:NSLocalizedString(@"id and fields are required.", nil)];
            return;
        }
        NSString *ID = (NSString *)idObject;
        
        QBCOCustomObject *customObject = [QBCOCustomObject customObject];
        customObject.ID = ID;
        customObject.fields = fields.mutableCopy;
        customObject.className = className;
        
        [QBRequest updateObject:customObject
                   successBlock:^(QBResponse *response, QBCOCustomObject *object) {
            [object toQBResultDataWithResolver:resolve rejecter:reject];
        } errorBlock:^(QBResponse *response) {
            [response reject:reject];
        }];
    }
}

- (void)remove:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject {
    NSObject *classNameObject = info[QBObjectKey.className];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:classNameObject
              objectKey:QBObjectKey.className]) {
        return;
    }
    NSString *className = (NSString *)classNameObject;
    
    NSObject *objectsIdsObject = info[@"ids"];
    if ([NSError reject:reject
           checkerClass:NSArray.class
                 object:objectsIdsObject
              objectKey:@"ids"]) {
        return;
    }
    NSArray<NSString *>*objectsIds = (NSArray<NSString *>*)objectsIdsObject;
    
    [QBRequest deleteObjectsWithIDs:objectsIds
                          className:className
                       successBlock:^(QBResponse * _Nonnull response,
                                      NSArray<NSString *> * _Nullable deletedObjectsIDs,
                                      NSArray<NSString *> * _Nullable notFoundObjectsIDs,
                                      NSArray<NSString *> * _Nullable wrongPermissionsObjectsIDs) {
        if (resolve) {
            resolve(nil);
        }
    } errorBlock:^(QBResponse * _Nonnull response) {
        [response reject:reject];
    }];
}

@end
