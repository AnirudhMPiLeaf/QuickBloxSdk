//
//  QBWebRTCVideoView.h
//  quickblox_sdk
//
//  Created by Injoit on 09.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import <QuickbloxWebRTC/QuickbloxWebRTC.h>

// Temp fix video scaling
// TODO: remove extention after scaling bug fix for the Remote Video View
// (Metal settings, native iOS SDK)
@interface NSString (CameraVideoGravity)
- (NSString *)qbRemoteLayerFix;
- (NSString *)qbCameraLayerFix;
@end

NS_ASSUME_NONNULL_BEGIN

@interface QBWebRTCVideoView : QBRTCRemoteVideoView

@property (nonatomic, assign) BOOL mirror;

@property (weak, nonatomic) AVCaptureVideoPreviewLayer *videoLayer;

@end

NS_ASSUME_NONNULL_END
