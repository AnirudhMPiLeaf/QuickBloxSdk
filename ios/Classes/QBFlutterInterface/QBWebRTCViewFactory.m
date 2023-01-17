//
//  QBWebRTCViewFactory.m
//  quickblox_sdk
//
//  Created by Injoit on 09.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import "QBWebRTCViewFactory.h"
#import "QBWebRTCFlutterVideoView.h"

@interface QBWebRTCViewFactory ()

@property (nonatomic, strong) NSObject<FlutterBinaryMessenger> *messenger;

@end

@implementation QBWebRTCViewFactory

+ (instancetype)factoryWithMessenger:(NSObject<FlutterBinaryMessenger> *)messenger {
    return [[QBWebRTCViewFactory alloc] initWithBinaryMessenger:messenger];
}

- (instancetype)initWithBinaryMessenger:(NSObject<FlutterBinaryMessenger>*)messenger {
    self = [super init];
    if (self) {
        _messenger = messenger;
    }
    return self;
}

- (nonnull NSObject<FlutterPlatformView> *)createWithFrame:(CGRect)frame
                                            viewIdentifier:(int64_t)viewId
                                                 arguments:(id _Nullable)args {
    QBWebRTCFlutterVideoView *view = [QBWebRTCFlutterVideoView viewWithMessenger:self.messenger
                                                                           frame:frame
                                                                  viewIdentifier:viewId];
    view.delegate = self.delegate;
    return view;
}

@end
