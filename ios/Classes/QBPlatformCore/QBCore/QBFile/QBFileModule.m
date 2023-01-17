//
//  QBFileModule.m
//  quickblox_sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBFileModule.h"
#import <MobileCoreServices/MobileCoreServices.h>
#import "QBCBlob+QBSerializer.h"
#import "QBFileConstants.h"

NSString * const kFileUploadProgress = @"FILE_UPLOAD_PROGRESS";

@interface QBFileModule()

@property (nonatomic, strong) NSMutableSet *progressListenersURLs;

@end


@implementation QBFileModule

- (NSArray<NSString *> *)events {
    return @[kFileUploadProgress];
}

- (NSMutableSet *)progressListenersURLs {
    if (!_progressListenersURLs) {
        _progressListenersURLs = [NSMutableSet set];
    }
    return _progressListenersURLs;
}

- (void)upload:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject {
    NSObject *urlObject = info[QBFileKey.url];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:urlObject
              objectKey:QBFileKey.url]) {
        return;
    }
    NSString *url = (NSString *)urlObject;
    
    NSNumber *public = info[QBFileKey.public];
    if (public == nil) {
        public = @(NO);
    }
    
    NSString *fileName = [url lastPathComponent];
    NSURL *URL = nil;
    if ([NSFileManager.defaultManager fileExistsAtPath:url]) {
        URL = [NSURL fileURLWithPath:url];
    } else {
        URL = [NSURL URLWithString:url];
    }
    
    NSString *fileExtension = [URL pathExtension];
    NSString *UTI =
    (__bridge_transfer NSString *)UTTypeCreatePreferredIdentifierForTag(kUTTagClassFilenameExtension,
                                                                        (__bridge CFStringRef)fileExtension,
                                                                        NULL);
    NSString *contentType =
    (__bridge_transfer NSString *)UTTypeCopyPreferredTagWithClass((__bridge CFStringRef)UTI,
                                                                  kUTTagClassMIMEType);
    
    __weak __typeof(self)weakSelf = self;
    [QBRequest uploadFileWithUrl:URL
                        fileName:fileName
                     contentType:contentType
                        isPublic:public.boolValue
                    successBlock:^(QBResponse * _Nonnull response,
                                   QBCBlob * _Nonnull tBlob) {
        [tBlob toQBResultDataWithResolver:resolve rejecter:reject];
    } statusBlock:^(QBRequest * _Nonnull request,
                    QBRequestStatus * _Nonnull status) {
        if ([weakSelf.progressListenersURLs containsObject:url]) {
            NSUInteger progress = @(status.percentOfCompletion * 100).integerValue;
            [weakSelf postQBEventWithName:kFileUploadProgress
                                     body:  @{ QBFileKey.url: url,
                                               QBFileKey.progress: @(progress) }];
        }
    } errorBlock:^(QBResponse * _Nonnull response) {
        [response reject:reject];
    }];
}

- (void)subscribeUploadProgress:(NSDictionary *)info
                       resolver:(QBResolveBlock)resolve
                       rejecter:(QBRejectBlock)reject {
    NSObject *urlObject = info[QBFileKey.url];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:urlObject
              objectKey:QBFileKey.url]) {
        return;
    }
    NSString *url = (NSString *)urlObject;
    [self.progressListenersURLs addObject:url];
    resolve(nil);
}

- (void)unsubscribeUploadProgress:(NSDictionary *)info
                         resolver:(QBResolveBlock)resolve
                         rejecter:(QBRejectBlock)reject {
    NSObject *urlObject = info[QBFileKey.url];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:urlObject
              objectKey:QBFileKey.url]) {
        return;
    }
    NSString *url = (NSString *)urlObject;
    [self.progressListenersURLs removeObject:url];
    resolve(nil);
}

- (void)getInfo:(NSDictionary *)info
       resolver:(QBResolveBlock)resolve
       rejecter:(QBRejectBlock)reject {
    NSObject *idObject = info[QBCBlobKey.id];
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:idObject
              objectKey:QBCBlobKey.id]) {
        return;
    }
    NSNumber *ID = (NSNumber *)idObject;
    
    [QBRequest blobWithID:ID.unsignedIntegerValue
             successBlock:^(QBResponse * _Nonnull response,
                            QBCBlob * _Nonnull tBlob) {
                 [tBlob toQBResultDataWithResolver:resolve rejecter:reject];
             } errorBlock:^(QBResponse * _Nonnull response) {
                 [response reject:reject];
             }];
}

- (void)getPublicURL:(NSDictionary *)info
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject {
    NSObject *idObject = info[QBCBlobKey.uid];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:idObject
              objectKey:QBCBlobKey.uid]) {
        return;
    }
    NSString *uid = (NSString *)idObject;
    NSString *url = [QBCBlob publicUrlForFileUID:uid];
    if (resolve) {
        resolve(url);
    }
}

- (void)getPrivateURL:(NSDictionary *)info
             resolver:(QBResolveBlock)resolve
             rejecter:(QBRejectBlock)reject {
    NSObject *idObject = info[QBCBlobKey.uid];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:idObject
              objectKey:QBCBlobKey.uid]) {
        return;
    }
    NSString *uid = (NSString *)idObject;
    NSString *url = [QBCBlob privateUrlForFileUID:uid];
    if (resolve) {
        resolve(url);
    }
}

@end
