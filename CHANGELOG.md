## 0.1.0-alpha
* Created first version of QuickBlox Flutter SDK

## 0.2.0-alpha
* Added WebRTC video calls
* Bug fixing
* Improved implementation for Android

## 0.2.1-alpha
* Improved the methods for subscribe, unsubscribe methods in chat
* Fixed mapping for dialogs in chat

## 0.2.2-alpha
* Bug fixing

## 0.2.3-alpha
* Fixed "sendMessage" method
* Fixed QBMessage model

## 0.2.4-alpha
* Fixed "getUsers" method
* Fixed logic for WebRTC

## 0.2.5-alpha
* added "getUsersByTag" method
* Fixed logic for filtering and sorting
* refactored WebRTC logic

## 0.2.6-alpha
* Added "release video views" method
* Fixed logic for init and release WebRTC service
* Fixed logic for create subscription
* Refactored WebRTC logic
* Fixed logic for subscribe\unsubscribe messages in Chat

## 0.2.7-alpha
* Fixed logic for "SWITCH_AUDIO_OUTPUT" method
* Fixed event names
* Refactored "switch audio output" code

## 0.2.8-alpha
* Fixed logic for "SWITCH_AUDIO_OUTPUT" method
* Refactored iOS side
* Fixed logic for Audio Sessions in iOS side

## 0.2.9-alpha
* Updated version of Android SDK to 3.9.5
* Fixed logic for "Peer Connections Events"

## 0.2.10-alpha

* Updated version of Android SDK to 3.9.6
* Fixed logic for subscriptions
* refactored code

## 0.2.11-alpha
* Fixed logic for events

## 0.3.0-alpha
* Fixed logic for delete dialog
* Fixed logic for leave dialog
* Fixed logic for event subscription
* Refactored code
* fixed filter mapper
* added logic for Development\Production endpoints

## 0.3.1-alpha
* Fixed logic for "sort" and "filter" in custom objects module

## 0.3.2-alpha
* added "noUserAction" logic for Android
* fixed "hangUp" logic for Android
* fixed "AudioManager" logic for Android
* added string resource and image mipmap for Android "WebRTCCallService"

## 0.3.3-alpha
* added QBAudioManager for correct work with audio mode in video call.

## 0.3.4-alpha
* Fixed logic for Sorting in Users module

## 0.3.5-alpha
* Fixed logic for listeners in Android side

## 0.3.6-alpha
* IOS: QBFileModule upload file logic fixed
* IOS: QBFileModule progress upload file logic  fixed
* IOS: QuickBloxSDK was updated to 2.17.8
* IOS: support iOS version was upgraded to 12 or higher* Android: fixed upload file logic
* Android: fixed progress upload file logic
* Android: update SDK to 3.9.8
* Dart: refactored logic in content module

## 0.3.7-alpha
* IOS: fixed sending messages to group/public dialogs

## 0.3.8-alpha
* Android: fixed upload file logic

## 0.3.9-alpha
* Android: fixed logic for join chats after reconnection

## 0.3.10-alpha
* added QBRTCConfig module
* refactored code
* Android: updated SDK version to 3.9.9
* Android: fixed upload files logic
* IOS updated SDK version to 2.17.9
* IOS increased minimum deployment target to 12.0 

## 0.3.11-beta
* Android: added logic for check active session
* Android: fixed logic for get cacheDir in File Module

## 0.4.0-beta
* Added conference functionality
* Added setSession\getSession in Auth module

## 0.4.1-beta
* Hotfix for WebRTC audioManager logic in Android side
* refactored Conference module, File module

## 0.4.2-beta
* Hotfix for Chat module logic in Android side

## 0.4.3-beta
* Hotfix for IOS side
* Added photo parameter in update/create dialog logic

## 0.5.0-beta
* Migrated to null safety
* refactored code
* Android: added logic for disable/enable video/audio track for specific user
* Android: fixed conference module
* IOS: fixes
* IOS: fixed error messages

## 0.5.1-beta
* fixed message mapper
* refactored code
* Android: updated gradle version

## 0.5.2-beta
* fixed message mapper for Android side

## 0.6.0-beta
* Flutter: fixed QBMessageMapper
* Flutter: fixed QBMessage model
* Flutter: fixed sendMessage, sendSystemMessage methods
* Android: fixed logic for ChatMapper
* Android: fixed sendMessage method

## 0.6.1-beta
* Android: updated version SDK to 3.9.11
* Android: fixed logic for chat listeners in Chat module
* Android: fixed logic for send message in Chat module
* Flutter: refactored code

## 0.6.2-beta
* Android: updated gradle version to 5.4.1
* Android: fixed call module
* Android: fixed file module
* Flutter: fixed message mapper
* Flutter: migrated to embedding v2

## 0.6.3-beta
* Android: fixed logic for audio manager
* Android: fixed logic for remove user
* IOS: fixed logic for parse attachment
* IOS: fixed dialog creation flow

## 0.6.4-beta
* Android: refactored code
* IOS: fixed camera logic

## 0.6.5-beta
* Android: refactored code
* Android: removed context from PushModule
* Android: updated SDK version to 3.9.14
* Android: removed expiration date from setSession parameters
* IOS: removed expiration date from setSession parameters

## 0.6.6-beta
* Flutter: added set/get ice-servers
* Android: updated dependencies
* Android: updated Quickblox SDK to version 3.9.15
* Android: fixed chat module
* IOS: refactored code
* IOS: updated SDK version

## 0.6.7-beta
* Flutter: added logging in settings module
* Flutter: added custom data for create/update dialog in chat module
* Flutter: added get unread messages count in chat module
* Android: updated android API to 31
* Android: added support Android 12
* Android: fixes
* IOS: updated QuickBlox version to 2.17.11
* IOS: updated Quickblox-WebRTC version to 2.7.6
* IOS: fixes

## 0.7.0-beta
* Flutter: added auth by email, firebase, facebook
* Flutter: added isJoinDialog method
* Android: fixed join dialog flow
* Android: fixed chat module
* Android: fixed logging
* Android: fixed auth module
* IOS: fixed logic for send event
* IOS: fixed join dialog flow
* IOS: fixed chat module

## 0.7.1
* Android: updated version SDK to 3.10.1

## 0.7.2
* Flutter: fixed logic for custom objects functionality
* Android: fixed logic for nullable fields in custom objects
* IOS: fixed logic for get custom objects

## 0.8.0
* Flutter: changed logic for join dialog
* Android: changed logic for join dialog
### Migration guide from 0.7.2 to 0.8.0
#### Chat module:
##### method `QB.chat.joinDialog(String dialogId)`
| version 0.7.2 | version 0.8.0 |
| - | - |
| `Future<void> joinDialog(String dialogId)`- **returns type is void** | `Future<QBDialog?> joinDialog(String dialogId)` - **returns type is QBDialog**  |

## 0.9.0
* Flutter: added functional for init SDK with Application Id
* Flutter: added functional for start session with token

## 0.10.0
* Flutter: added reconnection
* Android: added reconnection
* Android: updated Android QuickBlox version to 4.0.0
* IOS: added reconnection
* IOS: updated library
* IOS: fixed logic for local video view

## 0.10.1
* IOS: hotfix for auth module

## 0.11.0
* Flutter: changed logic for update custom objects
* Android: hotfix for custom objects
* IOS: hotfix for custom objects
### Migration guide from 0.10.1 to 0.11.0
#### CustomObjects module:
##### method `QB.data.update(String className)`
| version 0.10.1 | version 0.11.0 |
| - | - |
| `Future<QBCustomObject?> QB.data.update(String className)`- **returns type is QBCustomObject?** | `Future<List<QBCustomObject?>> update(String className)` - **returns type is List<QBCustomObject?>**  |