#import "QuickbloxSdkPlugin.h"

#import "QBSettingsModule+Flutter.h"
#import "QBFileModule+Flutter.h"
#import "QBPushSubscriptionsModule+Flutter.h"
#import "QBNotificationEventsModule+Flutter.h"
#import "QBChatModule+Flutter.h"
#import "QBUsersModule+Flutter.h"
#import "QBAuthModule+Flutter.h"
#import "QBCustomObjectsModule+Flutter.h"
#import "QBWebRTCModule+Flutter.h"
#import "QBRTCConfigModule+Flutter.h"
#import "QBConferenceModule+Flutter.h"

@interface QuickbloxSdkPlugin()

@end

@implementation QuickbloxSdkPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    [QBSettingsModule registerWithRegistrar:registrar];
    [QBFileModule registerWithRegistrar:registrar];
    [QBPushSubscriptionsModule registerWithRegistrar:registrar];
    [QBNotificationEventsModule registerWithRegistrar:registrar];
    [QBChatModule registerWithRegistrar:registrar];
    [QBUsersModule registerWithRegistrar:registrar];
    [QBAuthModule registerWithRegistrar:registrar];
    [QBCustomObjectsModule registerWithRegistrar:registrar];
    [QBWebRTCModule registerWithRegistrar:registrar];
    [QBRTCConfigModule registerWithRegistrar:registrar];
    [QBConferenceModule registerWithRegistrar:registrar];
}

@end
