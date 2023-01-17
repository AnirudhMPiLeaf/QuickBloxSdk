//
//  QBConferenceFlutterVideoView.m
//  quickblox_sdk
//
//  Created by Injoit on 12.02.2021.
//  Copyright © 2021 Injoit LTD. All rights reserved.
//

#import "QBConferenceFlutterVideoView.h"
#import "QBWebRTCVideoView.h"
#import "QBConferenceWrapper.h"

struct QBConferenceViewKeysStruct {
    __unsafe_unretained NSString * const mirror;
    __unsafe_unretained NSString * const scalingType;
    __unsafe_unretained NSString * const userId;
    __unsafe_unretained NSString * const sessionId;
};
extern const struct QBConferenceViewKeysStruct QBConferenceViewKey;

const struct QBConferenceViewKeysStruct QBConferenceViewKey = {
    .mirror = @"mirror",
    .scalingType = @"scalingType",
    .userId = @"userId",
    .sessionId = @"sessionId",
};

typedef NS_ENUM(NSInteger, QBConferenceViewScaleType) {
    QBConferenceViewScaleTypeFill,
    QBConferenceViewScaleTypeFit,
    QBConferenceViewScaleTypeAuto
};

@interface QBConferenceFlutterVideoView ()

@property (nonatomic, strong) QBWebRTCVideoView *view;
@property (nonatomic, assign) int64_t viewId;
@property (nonatomic, strong) QBRTCCameraCapture *videoCapture;

@end

@implementation QBConferenceFlutterVideoView

+ (instancetype)viewWithMessenger:(NSObject<FlutterBinaryMessenger> *)messenger
                            frame:(CGRect)frame
                   viewIdentifier:(int64_t)viewId {
    return [[QBConferenceFlutterVideoView alloc] initWithBinaryMessenger:messenger
                                                               frame:frame
                                                      viewIdentifier:viewId];
}

- (instancetype)initWithBinaryMessenger:(NSObject<FlutterBinaryMessenger>*)messenger
                                  frame:(CGRect)frame
                         viewIdentifier:(int64_t)viewId  {
    self = [super init];
    if (self) {
        _view = [[QBWebRTCVideoView alloc] initWithFrame:frame];
        _view.videoGravity = AVLayerVideoGravityResizeAspectFill.qbRemoteLayerFix;
        _viewId = viewId;
        
        NSString *className = NSStringFromClass(self.class);
        NSString *channelName = [className stringByAppendingString:[NSString stringWithFormat:@"Channel/%@", @(viewId)]];
        FlutterMethodChannel* channel = [FlutterMethodChannel
                                         methodChannelWithName:channelName
                                         binaryMessenger:messenger];
        [channel setMethodCallHandler:^(FlutterMethodCall * _Nonnull call,
                                        FlutterResult  _Nonnull result) {
            NSString *getSelectorString = [call.method stringByAppendingFormat:@":rejecter:"];
            NSString *setSelectorString = [call.method stringByAppendingFormat:@":resolver:rejecter:"];
            SEL getSelector = NSSelectorFromString(getSelectorString);
            SEL setSelector = NSSelectorFromString(setSelectorString);
            QBResolveBlock resolve = ^(id _Nullable qbResult) {
                result(qbResult);
            };
            
            QBRejectBlock reject = ^(NSString * _Nonnull code,
                                     NSString * _Nullable message,
                                     NSError * _Nullable error) {
                result([FlutterError errorWithCode:[[code stringByAppendingString:@"\n"] stringByAppendingString:message] message:nil details:error.localizedDescription]);
            };
            
            NSMutableDictionary *args = @{}.mutableCopy;
            id arguments = call.arguments;
            if ([arguments isKindOfClass:NSDictionary.class]) {
                NSDictionary *objectData = (NSDictionary *)arguments;
                for (NSString *key in objectData.allKeys) {
                    id value = objectData[key];
                    if (![value isKindOfClass:NSNull.class]) {
                        args[key] = value;
                    }
                }
            }
            
            NSDictionary *objectValue = args.copy;
            
            if ([self respondsToSelector:setSelector]) {
                NSMethodSignature *signature  = [self methodSignatureForSelector:setSelector];
                NSInvocation      *invocation = [NSInvocation invocationWithMethodSignature:signature];
                [invocation setTarget:self];
                [invocation setSelector:setSelector];
                [invocation setArgument:&objectValue atIndex:2];
                [invocation setArgument:&resolve atIndex:3];
                [invocation setArgument:&reject atIndex:4];
                
                [invocation invoke];
            } else if ([self respondsToSelector:getSelector]) {
                NSMethodSignature *signature  = [self methodSignatureForSelector:getSelector];
                NSInvocation      *invocation = [NSInvocation invocationWithMethodSignature:signature];
                [invocation setTarget:self];
                [invocation setSelector:getSelector];
                [invocation setArgument:&resolve atIndex:2];
                [invocation setArgument:&reject atIndex:3];
                
                [invocation invoke];
            } else {
                result(FlutterMethodNotImplemented);
            }
        }];
    }
    return self;
}

- (void)mirror:(NSDictionary *)info
      resolver:(QBResolveBlock)resolve
      rejecter:(QBRejectBlock)reject {
    NSObject *mirrorObject = info[QBConferenceViewKey.mirror];
    //    if ([NSError reject:reject
    //           checkerClass:NSNumber.class
    //                 object:mirrorObject
    //              objectKey:QBWebRTCViewKey.mirror]) {
    //        return;
    //    }
    NSNumber *mirrorNumber = (NSNumber *)mirrorObject;
    if (!mirrorNumber) {
        self.view.mirror = NO;
    } else {
        self.view.mirror = mirrorNumber.boolValue;
    }
    if (resolve) {
        resolve(nil);
    }
}

- (void)scaleType:(NSDictionary *)info
         resolver:(QBResolveBlock)resolve
         rejecter:(QBRejectBlock)reject {
    NSNumber *scaleTypeNumber = info[QBConferenceViewKey.scalingType];
    QBConferenceViewScaleType scaleType = scaleTypeNumber.integerValue;
    switch (scaleType) {
        case QBConferenceViewScaleTypeFill: {
            self.view.videoGravity = AVLayerVideoGravityResizeAspectFill.qbRemoteLayerFix;
            break;
        }
        case QBConferenceViewScaleTypeFit: {
            self.view.videoGravity = AVLayerVideoGravityResizeAspect.qbRemoteLayerFix;
            break;
        }
        case QBConferenceViewScaleTypeAuto: {
            self.view.videoGravity = AVLayerVideoGravityResize.qbRemoteLayerFix;
            break;
        }
    }
    
    if (resolve) {
        resolve(nil);
    }
}

- (void)play:(NSDictionary *)info
    resolver:(QBResolveBlock)resolve
    rejecter:(QBRejectBlock)reject {
    NSObject *userIdObject = info[QBConferenceViewKey.userId];
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:userIdObject
              objectKey:QBConferenceViewKey.mirror]) {
        return;
    }
    NSNumber *userId = (NSNumber *)userIdObject;
    
    NSObject *sessionIdObject = info[QBConferenceViewKey.sessionId];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:sessionIdObject
              objectKey:QBConferenceViewKey.sessionId]) {
        return;
    }
    NSString *sessionId = (NSString *)sessionIdObject;
    
    if (![self.delegate respondsToSelector:@selector(cache)]) {
        NSLog(@"Cannot find QBWebRTCwrapperCache");
        NSString *message =
        [NSString stringWithFormat:@"The session with id: %@ has not found",
         sessionId];
        [NSError reject:reject message:message];
        
        return;
    }
    
    QBConferenceWrapper *wrapper = [self.delegate.cache wrapperWithUUID:sessionId];
    
    if (!wrapper.session) {
        NSString *message =
        [NSString stringWithFormat:@"The session with id: %@ has not found",
         sessionId];
        [NSError reject:reject message:message];
        
        return;
    }
    
    if ([userId isEqual:wrapper.currentUserId]) {
        // Setup local stream
        dispatch_async(dispatch_get_main_queue(), ^{
            QBRTCCameraCapture *videoCapture = wrapper.videoCapture;
            videoCapture.previewLayer.videoGravity = self.view.videoGravity.qbCameraLayerFix;
            self.view.videoLayer = videoCapture.previewLayer;
            self.videoCapture = videoCapture;
            if (!videoCapture.hasStarted && !videoCapture.isRunning) {
                [videoCapture startSession:^{
                    resolve(nil);
                }];
                return;
            }
        });
    } else {
        // Setup remote stream
        QBRTCVideoTrack *videoTrack = [wrapper.session remoteVideoTrackWithUserID:userId];
        if (!videoTrack) {
            NSString *message =
            [NSString stringWithFormat:@"The video track for user: %@ has not found",
             userId];
            [NSError reject:reject message:message];
            
            return;
        }
        self.view.videoTrack = videoTrack;
    }
    self.view.transform = CGAffineTransformMakeScale(self.view.mirror ? -1.0 : 1.0, 1.0);
}

- (void)release:(NSDictionary *)info
       resolver:(QBResolveBlock)resolve
       rejecter:(QBRejectBlock)reject {
    if (self.videoCapture.hasStarted || self.videoCapture.isRunning) {
        [self.videoCapture stopSession:nil];
    }
    self.videoCapture = nil;
    self.view = nil;
    
    if (resolve) {
        return;
    }
    resolve(nil);
}

@end

