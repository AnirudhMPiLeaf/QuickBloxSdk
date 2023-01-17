package com.quickblox.quickblox_sdk.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;

import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.Query;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogCustomData;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.request.QBDialogRequestBuilder;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.exception.QBRuntimeException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.core.server.Performer;
import com.quickblox.quickblox_sdk.base.BaseModule;
import com.quickblox.quickblox_sdk.chat.listeners.ConnectionListener;
import com.quickblox.quickblox_sdk.chat.listeners.IncomingMessageListener;
import com.quickblox.quickblox_sdk.chat.listeners.StatusMessageListener;
import com.quickblox.quickblox_sdk.chat.listeners.SystemMessageListener;
import com.quickblox.quickblox_sdk.chat.listeners.TypingListener;
import com.quickblox.quickblox_sdk.concurrent.Executor;
import com.quickblox.quickblox_sdk.concurrent.ExecutorImpl;
import com.quickblox.quickblox_sdk.concurrent.Task;
import com.quickblox.quickblox_sdk.event.EventHandler;
import com.quickblox.quickblox_sdk.utils.EventsUtil;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
public class ChatModule implements BaseModule {
    static final String CHANNEL_NAME = "FlutterQBChatChannel";

    public static final String CONNECT_METHOD = "connect";
    public static final String DISCONNECT_METHOD = "disconnect";
    public static final String IS_CONNECTED_METHOD = "isConnected";
    public static final String PING_SERVER_METHOD = "pingServer";
    public static final String PING_USER_METHOD = "pingUser";
    public static final String GET_DIALOGS_METHOD = "getDialogs";
    public static final String GET_DIALOGS_COUNT_METHOD = "getDialogsCount";
    public static final String UPDATE_DIALOG_METHOD = "updateDialog";
    public static final String CREATE_DIALOG_METHOD = "createDialog";
    public static final String DELETE_DIALOG_METHOD = "deleteDialog";
    public static final String LEAVE_DIALOG_METHOD = "leaveDialog";
    public static final String JOIN_DIALOG_METHOD = "joinDialog";
    public static final String IS_JOINED_DIALOG_METHOD = "isJoinedDialog";
    public static final String GET_ONLINE_USERS_METHOD = "getOnlineUsers";
    public static final String SEND_MESSAGE_METHOD = "sendMessage";
    public static final String SEND_SYSTEM_MESSAGE_METHOD = "sendSystemMessage";
    public static final String MARK_MESSAGE_READ_METHOD = "markMessageRead";
    public static final String MARK_MESSAGE_DELIVERED_METHOD = "markMessageDelivered";
    public static final String SEND_IS_TYPING_METHOD = "sendIsTyping";
    public static final String SEND_STOPPED_TYPING_METHOD = "sendStoppedTyping";
    public static final String GET_DIALOG_MESSAGES_METHOD = "getDialogMessages";
    public static final String GET_TOTAL_UNREAD_MESSAGES_COUNT = "getTotalUnreadMessagesCount";

    private final Executor executor = new ExecutorImpl();

    private final Set<QBChatDialog> dialogsCache = new QBDialogsSet(chatDialog -> chatDialog.addIsTypingListener(
            new TypingListener(chatDialog.getDialogId())));

    private final ConnectionListener connectionListener = new ConnectionListener();

    private BinaryMessenger binaryMessenger;

    public ChatModule(BinaryMessenger binaryMessenger) {
        this.binaryMessenger = binaryMessenger;
        initEventHandler();
    }

    public ChatModule() {
        //for tests
    }

    @Override
    public void initEventHandler() {
        EventHandler.init(ChatConstants.getAllEvents(), binaryMessenger);
    }

    @Override
    public String getChannelName() {
        return CHANNEL_NAME;
    }

    @Override
    public MethodChannel.MethodCallHandler getMethodHandler() {
        return this::handleMethod;
    }

    @Override
    public void handleMethod(MethodCall methodCall, MethodChannel.Result result) {
        switch (methodCall.method) {
            case CONNECT_METHOD:
                connect(methodCall.arguments(), result);
                break;
            case DISCONNECT_METHOD:
                disconnect(result);
                break;
            case IS_CONNECTED_METHOD:
                isConnected(result);
                break;
            case PING_SERVER_METHOD:
                pingServer(result);
                break;
            case PING_USER_METHOD:
                pingUser(methodCall.arguments(), result);
                break;
            case GET_DIALOGS_METHOD:
                getDialogs(methodCall.arguments(), result);
                break;
            case GET_DIALOGS_COUNT_METHOD:
                getDialogsCount(methodCall.arguments(), result);
                break;
            case UPDATE_DIALOG_METHOD:
                updateDialog(methodCall.arguments(), result);
                break;
            case CREATE_DIALOG_METHOD:
                createDialog(methodCall.arguments(), result);
                break;
            case DELETE_DIALOG_METHOD:
                deleteDialog(methodCall.arguments(), result);
                break;
            case LEAVE_DIALOG_METHOD:
                leaveDialog(methodCall.arguments(), result);
                break;
            case JOIN_DIALOG_METHOD:
                joinDialog(methodCall.arguments(), result);
                break;
            case IS_JOINED_DIALOG_METHOD:
                isJoinedDialog(methodCall.arguments(), result);
                break;
            case GET_ONLINE_USERS_METHOD:
                getOnlineUsers(methodCall.arguments(), result);
                break;
            case SEND_MESSAGE_METHOD:
                sendMessage(methodCall.arguments(), result);
                break;
            case SEND_SYSTEM_MESSAGE_METHOD:
                sendSystemMessage(methodCall.arguments(), result);
                break;
            case MARK_MESSAGE_READ_METHOD:
                markMessageRead(methodCall.arguments(), result);
                break;
            case MARK_MESSAGE_DELIVERED_METHOD:
                markMessageDelivered(methodCall.arguments(), result);
                break;
            case SEND_IS_TYPING_METHOD:
                sendIsTyping(methodCall.arguments(), result);
                break;
            case SEND_STOPPED_TYPING_METHOD:
                sendStoppedTyping(methodCall.arguments(), result);
                break;
            case GET_DIALOG_MESSAGES_METHOD:
                getDialogMessages(methodCall.arguments(), result);
                break;
            case GET_TOTAL_UNREAD_MESSAGES_COUNT:
                getTotalUnreadMessagesCount(methodCall.arguments(), result);
                break;
        }
    }

    private QBChatDialog getDialogFromCacheOrLoadFromRest(String dialogId) throws QBResponseException {
        if (TextUtils.isEmpty(dialogId)) {
            throw new QBResponseException("required parameter dialogId has a wrong value");
        }

        QBChatDialog dialog = getDialogFromCache(dialogId);
        if (dialog == null) {
            dialog = QBRestChatService.getChatDialogById(dialogId).perform();
            dialogsCache.add(dialog);
        }
        return dialog;
    }

    private QBChatDialog getDialogFromCache(String dialogId) throws QBResponseException {
        if (TextUtils.isEmpty(dialogId)) {
            throw new QBResponseException("required parameter dialogId has a wrong value");
        }

        QBChatDialog dialog = null;
        for (QBChatDialog chatDialog : dialogsCache) {
            if (chatDialog.getDialogId().equals(dialogId)) {
                dialog = chatDialog;
                break;
            }
        }
        return dialog;
    }

    public void connect(Map<String, Object> data, final MethodChannel.Result result) {
        Integer userId = data != null && data.containsKey("userId") ? (Integer) data.get("userId") : null;
        String password = data != null && data.containsKey("password") ? (String) data.get("password") : null;

        if (userId == null || TextUtils.isEmpty(password)) {
            result.error("The required parameters userId, password have a wrong value: \nuserId: " + userId
                    + "\n password: " + password, null, null);
            return;
        }

        executor.add(new Task<Void>() {
            @Override
            public Void performBackground() throws Exception {
                QBUser user = new QBUser();
                user.setPassword(password);
                user.setId(userId);

                QBChatService.getInstance().setUseStreamManagement(true);

                QBChatService.getInstance().removeConnectionListener(connectionListener);
                QBChatService.getInstance().addConnectionListener(connectionListener);

                QBChatService.getInstance().login(user);

                QBChatService.getInstance().getSystemMessagesManager().addSystemMessageListener(new SystemMessageListener());
                QBChatService.getInstance().getMessageStatusesManager().addMessageStatusListener(new StatusMessageListener());
                QBChatService.getInstance().getIncomingMessagesManager().addDialogMessageListener(new IncomingMessageListener());

                return null;
            }

            @Override
            public void performForeground(Void aVoid) {
                result.success(aVoid);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    public void disconnect(final MethodChannel.Result result) {
        executor.add(new Task<Void>() {
            @Override
            public Void performBackground() throws Exception {
                QBChatService.getInstance().removeConnectionListener(connectionListener);

                if (QBChatService.getInstance().getSystemMessagesManager() != null) {
                    QBChatService.getInstance().getSystemMessagesManager().removeSystemMessageListener(new SystemMessageListener());
                }

                if (QBChatService.getInstance().getMessageStatusesManager() != null) {
                    QBChatService.getInstance().getMessageStatusesManager().removeMessageStatusListener(new StatusMessageListener());
                }

                if (QBChatService.getInstance().getIncomingMessagesManager() != null) {
                    QBChatService.getInstance().getIncomingMessagesManager().removeDialogMessageListrener(new IncomingMessageListener());
                }

                QBChatService.getInstance().logout();

                dialogsCache.clear();

                return null;
            }

            @Override
            public void performForeground(Void aVoid) {
                result.success(aVoid);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    public void isConnected(final MethodChannel.Result result) {
        result.success(QBChatService.getInstance().isLoggedIn());
    }

    public void pingServer(final MethodChannel.Result result) {
        executor.add(new Task<Boolean>() {
            @Override
            public Boolean performBackground() throws Exception {
                return QBChatService.getInstance().getPingManager().pingServer();
            }

            @Override
            public void performForeground(Boolean isPinged) {
                result.success(isPinged);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    public void pingUser(Map<String, Object> data, final MethodChannel.Result result) {
        Integer userId = data != null && data.containsKey("userId") ? (Integer) data.get("userId") : null;

        if (userId == null || userId <= 0) {
            result.error("The userId parameter has a wrong value: " + userId, null, null);
            return;
        }

        executor.add(new Task<Boolean>() {
            @Override
            public Boolean performBackground() throws Exception {
                return QBChatService.getInstance().getPingManager().pingUser(userId);
            }

            @Override
            public void performForeground(Boolean isPinged) {
                result.success(isPinged);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    public void getDialogs(Map<String, Object> data, final MethodChannel.Result result) {
        Map<String, Object> sortMap = data != null && data.containsKey("sort") ? (Map) data.get("sort") : null;
        Map<String, Object> filterMap = data != null && data.containsKey("filter") ? (Map) data.get("filter") : null;
        int limit = data != null && data.containsKey("limit") ? (int) data.get("limit") : 100;
        int skip = data != null && data.containsKey("skip") ? (int) data.get("skip") : 0;

        executor.add(new Task<Pair<List<QBChatDialog>, Bundle>>() {
            @Override
            public Pair<List<QBChatDialog>, Bundle> performBackground() throws Exception {
                QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
                requestBuilder.setLimit(limit);
                requestBuilder.setSkip(skip);

                ChatMapper.addDialogFilterToRequestBuilder(requestBuilder, filterMap);
                ChatMapper.addDialogSortToRequestBuilder(requestBuilder, sortMap);

                Performer<?> performer = QBRestChatService.getChatDialogs(null, requestBuilder);
                List<QBChatDialog> dialogs = (List) performer.perform();
                Bundle bundle = ((Query<?>) performer).getBundle();

                dialogsCache.removeAll(dialogs);
                dialogsCache.addAll(dialogs);

                return new Pair<>(dialogs, bundle);
            }

            @Override
            public void performForeground(Pair<List<QBChatDialog>, Bundle> pair) {
                List<Map<String, Object>> dialogsList = new ArrayList<>();
                for (QBChatDialog qbDialog : pair.first) {
                    Map<String, Object> dialog = ChatMapper.qbChatDialogToMap(qbDialog);
                    dialogsList.add(dialog);
                }

                Map<String, Object> payload = new HashMap<>();
                payload.put("dialogs", dialogsList);
                payload.put("skip", pair.second.containsKey("skip") ? pair.second.getInt("skip") : skip);
                payload.put("limit", pair.second.containsKey("limit") ? pair.second.getInt("limit") : limit);
                payload.put("total", pair.second.containsKey("total_entries") ? pair.second.getInt("total_entries") : -1);

                result.success(payload);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    public void getDialogsCount(Map<String, Object> data, final MethodChannel.Result result) {
        Map<String, Object> filterMap = data != null && data.containsKey("filter") ? (Map) data.get("filter") : null;
        int limit = data != null && data.containsKey("limit") ? (int) data.get("limit") : 100;
        int skip = data != null && data.containsKey("skip") ? (int) data.get("skip") : 0;

        executor.add(new Task<Integer>() {
            @Override
            public Integer performBackground() throws Exception {
                QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
                requestBuilder.setLimit(limit);
                requestBuilder.setSkip(skip);

                ChatMapper.addDialogFilterToRequestBuilder(requestBuilder, filterMap);

                return QBRestChatService.getChatDialogsCount(requestBuilder, null).perform();
            }

            @Override
            public void performForeground(Integer countOfDialogs) {
                result.success(countOfDialogs);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    public void updateDialog(Map<String, Object> data, final MethodChannel.Result result) {
        final String dialogId = data != null && data.containsKey("dialogId") ? (String) data.get("dialogId") : null;
        List<Integer> addUsers = data != null && data.containsKey("addUsers") ? (List) data.get("addUsers") : null;
        List<Integer> removeUsers = data != null && data.containsKey("removeUsers") ? (List) data.get("removeUsers") : null;
        String name = data != null && data.containsKey("name") ? (String) data.get("name") : null;
        String photo = data != null && data.containsKey("photo") ? (String) data.get("photo") : null;
        Map<String, Object> customData = data != null && data.containsKey("customData") ? (Map) data.get("customData") : null;

        if (TextUtils.isEmpty(dialogId)) {
            result.error("Required parameter dialogId has a wrong value: " + dialogId, null, null);
            return;
        }

        executor.add(new Task<QBChatDialog>() {
            @Override
            public QBChatDialog performBackground() throws Exception {
                QBChatDialog dialog = getDialogFromCacheOrLoadFromRest(dialogId);

                if (customData != null) {
                    dialog.setCustomData(parseCustomData(customData));
                }

                dialog.setName(name);
                dialog.setPhoto(photo);

                QBDialogRequestBuilder requestBuilder = new QBDialogRequestBuilder();
                if (removeUsers != null) {
                    for (int userId : removeUsers) {
                        requestBuilder.removeUsers(userId);
                    }
                }
                if (addUsers != null) {
                    for (int userId : addUsers) {
                        requestBuilder.addUsers(userId);
                    }
                }

                QBChatDialog loadedDialog = QBRestChatService.updateChatDialog(dialog, requestBuilder).perform();

                dialogsCache.remove(dialog);
                dialogsCache.add(loadedDialog);

                return loadedDialog;
            }

            @Override
            public void performForeground(QBChatDialog dialog) {
                result.success(ChatMapper.qbChatDialogToMap(dialog));
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    public void createDialog(Map<String, Object> data, final MethodChannel.Result result) {
        List<Integer> occupantsIds = data != null && data.containsKey("occupantsIds") ? (List) data.get("occupantsIds") : null;
        String name = data != null && data.containsKey("name") ? (String) data.get("name") : null;
        Integer type = data != null && data.containsKey("type") ? (Integer) data.get("type") : null;
        String photo = data != null && data.containsKey("photo") ? (String) data.get("photo") : null;
        Map<String, Object> customData = data != null && data.containsKey("customData") ? (Map) data.get("customData") : null;

        if (type == null || type <= 0 || type > 3) {
            result.error("Required parameter type has a wrong value: " + type, null, null);
            return;
        }

        executor.add(new Task<QBChatDialog>() {
            @Override
            public QBChatDialog performBackground() throws Exception {
                QBChatDialog dialog = DialogUtils.buildDialog(name, QBDialogType.parseByCode(type), occupantsIds);
                dialog.setPhoto(photo);

                if (customData != null) {
                    dialog.setCustomData(parseCustomData(customData));
                }

                QBChatDialog loadedDialog = QBRestChatService.createChatDialog(dialog).perform();

                dialogsCache.remove(loadedDialog);
                dialogsCache.add(loadedDialog);

                return loadedDialog;
            }

            @Override
            public void performForeground(QBChatDialog dialog) {
                result.success(ChatMapper.qbChatDialogToMap(dialog));
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    private QBDialogCustomData parseCustomData(Map<String, Object> customData) throws NoSuchFieldException {
        QBDialogCustomData dialogCustomData = new QBDialogCustomData();

        if (customData.isEmpty()) {
            throw new NoSuchFieldException("Error: the custom data shouldn't be empty");
        }

        for (Map.Entry<String, Object> entry : customData.entrySet()) {
            boolean isKeyClassNameExist = entry.getKey() != null && !TextUtils.isEmpty(entry.getKey())
                    && Objects.equals(entry.getKey(), "class_name");

            boolean isValueClassNameExist = entry.getValue() != null && entry.getValue() instanceof String
                    && !TextUtils.isEmpty((String) entry.getValue());

            if (isKeyClassNameExist && isValueClassNameExist) {
                dialogCustomData.setClassName((String) entry.getValue());
                continue;
            }

            String key = entry.getKey();
            Object value = entry.getValue();

            if (!TextUtils.isEmpty(key) && value != null) {
                dialogCustomData.put(key, value);
            } else {
                throw new NoSuchFieldException("Error parse custom data: " + "\nkey -> " + key + "\nvalue -> " + value);
            }
        }

        return dialogCustomData;
    }

    public void deleteDialog(Map<String, Object> data, final MethodChannel.Result result) {
        final String dialogId = data != null && data.containsKey("dialogId") ? (String) data.get("dialogId") : null;
        final boolean force = data != null && data.containsKey("force") && (boolean) data.get("force");

        if (TextUtils.isEmpty(dialogId)) {
            result.error("Required parameter dialogId has a wrong value: " + dialogId, null, null);
            return;
        }

        executor.add(new Task<Void>() {
            @Override
            public Void performBackground() throws Exception {
                QBRestChatService.deleteDialog(dialogId, force).perform();
                dialogsCache.remove(getDialogFromCache(dialogId));
                return null;
            }

            @Override
            public void performForeground(Void aVoid) {
                result.success(aVoid);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    public void leaveDialog(Map<String, Object> data, final MethodChannel.Result result) {
        String dialogId = data != null && data.containsKey("dialogId") ? (String) data.get("dialogId") : null;

        if (TextUtils.isEmpty(dialogId)) {
            result.error("Required parameter dialogId has a wrong value: " + dialogId, null, null);
            return;
        }

        executor.add(new Task<Void>() {
            @Override
            public Void performBackground() throws Exception {
                QBChatDialog dialog = getDialogFromCacheOrLoadFromRest(dialogId);

                if (dialog.isPrivate()) {
                    throw new QBRuntimeException("The private dialog shouldn't be leave");
                }

                if (!dialog.isJoined()) {
                    throw new QBRuntimeException("The dialog is not joined");
                }

                dialog.leave();
                return null;
            }

            @Override
            public void performForeground(Void aVoid) {
                result.success(aVoid);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    public void joinDialog(Map<String, Object> data, final MethodChannel.Result result) {
        String dialogId = data != null && data.containsKey("dialogId") ? (String) data.get("dialogId") : null;

        if (TextUtils.isEmpty(dialogId)) {
            result.error("Required parameter dialogId has a wrong value: " + dialogId, null, null);
            return;
        }

        executor.add(new Task<QBChatDialog>() {
            @Override
            public QBChatDialog performBackground() throws Exception {
                QBChatDialog dialog = getDialogFromCacheOrLoadFromRest(dialogId);

                if (dialog.isPrivate()) {
                    throw new QBRuntimeException("The private dialog shouldn't be joined");
                }

                dialog.join(null);
                return dialog;
            }

            @Override
            public void performForeground(QBChatDialog dialog) {
                result.success(ChatMapper.qbChatDialogToMap(dialog));
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    public void isJoinedDialog(Map<String, Object> data, final MethodChannel.Result result) {
        String dialogId = data != null && data.containsKey("dialogId") ? (String) data.get("dialogId") : null;

        if (TextUtils.isEmpty(dialogId)) {
            result.error("Required parameter dialogId has a wrong value: " + dialogId, null, null);
            return;
        }

        executor.add(new Task<Boolean>() {
            @Override
            public Boolean performBackground() throws Exception {
                QBChatDialog dialog = getDialogFromCacheOrLoadFromRest(dialogId);

                if (dialog.isPrivate()) {
                    throw new QBRuntimeException("The private dialog shouldn't be joined");
                }

                return dialog.isJoined();
            }

            @Override
            public void performForeground(Boolean isJoined) {
                result.success(isJoined);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    public void getOnlineUsers(Map<String, Object> data, final MethodChannel.Result result) {
        String dialogId = data != null && data.containsKey("dialogId") ? (String) data.get("dialogId") : null;

        if (TextUtils.isEmpty(dialogId)) {
            result.error("Required parameter dialogId has a wrong value: " + dialogId, null, null);
            return;
        }

        executor.add(new Task<Collection<Integer>>() {
            @Override
            public Collection<Integer> performBackground() throws Exception {
                QBChatDialog dialog = getDialogFromCacheOrLoadFromRest(dialogId);

                if (dialog.isPrivate()) {
                    throw new QBRuntimeException("Chat dialog type cannot be private.");
                }

                return dialog.requestOnlineUsers();
            }

            @Override
            public void performForeground(Collection<Integer> users) {
                result.success(users);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    public void sendMessage(Map<String, Object> data, final MethodChannel.Result result) {
        final String dialogId = data != null && data.containsKey("dialogId") ? (String) data.get("dialogId") : null;
        final String body = data != null && data.containsKey("body") ? (String) data.get("body") : null;
        final List<Map<String, Object>> attachments = data != null && data.containsKey("attachments") ? (List) data.get("attachments") : null;
        final Map<String, Object> properties = data != null && data.containsKey("properties") ? (Map) data.get("properties") : null;
        final boolean markable = data != null && data.containsKey("markable") && (boolean) data.get("markable");
        final long dateSent = data != null && (data.containsKey("dateSent") && data.get("dateSent") != null) ? (long) data.get("dateSent") : System.currentTimeMillis() / 1000;
        final boolean saveToHistory = data != null && data.containsKey("saveToHistory") && (boolean) data.get("saveToHistory");

        if (TextUtils.isEmpty(dialogId)) {
            result.error("Required parameter dialogId has a wrong value: " + dialogId, null, null);
            return;
        }

        executor.add(new Task<QBChatMessage>() {
            @Override
            public QBChatMessage performBackground() throws Exception {
                QBChatMessage message = new QBChatMessage();
                message.setBody(body);
                message.setMarkable(markable);
                message.setSaveToHistory(saveToHistory);
                message.setDateSent(dateSent);
                message.setAttachments(buildAttachments(attachments));

                if (properties != null) {
                    addPropertiesToMessage(message, properties);
                }

                QBChatDialog dialog = getDialogFromCacheOrLoadFromRest(dialogId);

                if (dialog.isPrivate()) {
                    message.setRecipientId(dialog.getRecipientId());
                }

                if (!dialog.getType().equals(QBDialogType.PUBLIC_GROUP)) {
                    Integer loggedUserId = QBSessionManager.getInstance().getSessionParameters().getUserId();
                    message.setDeliveredIds(Collections.singletonList(loggedUserId));
                    message.setReadIds(Collections.singletonList(loggedUserId));
                }

                dialog.sendMessage(message);

                return message;
            }

            @Override
            public void performForeground(QBChatMessage message) {
                result.success(null);

                message.setDialogId(dialogId);
                message.setDateSent(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                message.setSenderId(QBSessionManager.getInstance().getSessionParameters().getUserId());

                Map<String, Object> parsedMessage = ChatMapper.qbChatMessageToMap(message);
                String eventName = ChatConstants.Events.RECEIVED_NEW_MESSAGE;
                Map<String, Object> payload = EventsUtil.buildPayload(eventName, parsedMessage);
                EventHandler.sendEvent(eventName, payload);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    private List<QBAttachment> buildAttachments(List<Map<String, Object>> attachments) {
        List<QBAttachment> qbAttachments = new ArrayList<>();

        if (attachments == null) {
            return qbAttachments;
        }

        for (Map<String, Object> attachmentMap : attachments) {
            QBAttachment attachment = ChatMapper.mapToQbAttachment(attachmentMap);
            if (attachment != null) {
                qbAttachments.add(attachment);
            }
        }

        return qbAttachments;
    }

    public void sendSystemMessage(Map<String, Object> data, final MethodChannel.Result result) {
        Integer recipientId = data != null && data.containsKey("recipientId") ? (Integer) data.get("recipientId") : null;
        Map<String, Object> properties = data != null && data.containsKey("properties") ? (Map) data.get("properties") : null;

        if (recipientId == null || recipientId <= 0) {
            result.error("Required parameter recipientId has a wrong value: " + recipientId, null, null);
            return;
        }

        executor.add(new Task<Void>() {
            @Override
            public Void performBackground() throws Exception {
                QBChatMessage message = new QBChatMessage();
                message.setRecipientId(recipientId);

                if (properties != null) {
                    addPropertiesToMessage(message, properties);
                }

                QBChatService.getInstance().getSystemMessagesManager().sendSystemMessage(message);
                return null;
            }

            @Override
            public void performForeground(Void aVoid) {
                result.success(aVoid);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    QBChatMessage addPropertiesToMessage(QBChatMessage message, Map<String, Object> properties) {
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String propertyName = entry.getKey();
            Object propertyValue = entry.getValue();
            message.setProperty(propertyName, String.valueOf(propertyValue));
        }
        return message;
    }

    public void markMessageRead(Map<String, Object> data, final MethodChannel.Result result) {
        Map<String, Object> messageMap = data != null && data.containsKey("message") ? (Map) data.get("message") : null;

        if (messageMap == null) {
            result.error("Required parameter message has a wrong value: " + messageMap, null, null);
            return;
        }

        executor.add(new Task<Void>() {
            @Override
            public Void performBackground() throws Exception {
                QBChatMessage message = ChatMapper.mapToQBChatMessage(messageMap);
                getDialogFromCacheOrLoadFromRest(message.getDialogId()).readMessage(message);
                return null;
            }

            @Override
            public void performForeground(Void aVoid) {
                result.success(aVoid);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    public void markMessageDelivered(Map<String, Object> data, final MethodChannel.Result result) {
        Map<String, Object> messageMap = data != null && data.containsKey("message") ? (Map) data.get("message") : null;

        if (messageMap == null) {
            result.error("Required parameter message has a wrong value: " + messageMap, null, null);
            return;
        }

        executor.add(new Task<Void>() {
            @Override
            public Void performBackground() throws Exception {
                QBChatMessage message = ChatMapper.mapToQBChatMessage(messageMap);
                getDialogFromCacheOrLoadFromRest(message.getDialogId()).deliverMessage(message);
                return null;
            }

            @Override
            public void performForeground(Void aVoid) {
                result.success(aVoid);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    public void sendIsTyping(Map<String, Object> data, final MethodChannel.Result result) {
        String dialogId = data != null && data.containsKey("dialogId") ? (String) data.get("dialogId") : null;

        if (TextUtils.isEmpty(dialogId)) {
            result.error("Required parameter dialogId has a wrong value: " + dialogId, null, null);
            return;
        }

        executor.add(new Task<Void>() {
            @Override
            public Void performBackground() throws Exception {
                getDialogFromCacheOrLoadFromRest(dialogId).sendIsTypingNotification();
                return null;
            }

            @Override
            public void performForeground(Void avoid) {
                result.success(null);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    public void sendStoppedTyping(Map<String, Object> data, final MethodChannel.Result result) {
        String dialogId = data != null && data.containsKey("dialogId") ? (String) data.get("dialogId") : null;

        if (TextUtils.isEmpty(dialogId)) {
            result.error("Required parameter dialogId has a wrong value: " + dialogId, null, null);
            return;
        }

        executor.add(new Task<Void>() {
            @Override
            public Void performBackground() throws Exception {
                getDialogFromCacheOrLoadFromRest(dialogId).sendStopTypingNotification();
                return null;
            }

            @Override
            public void performForeground(Void avoid) {
                result.success(null);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    public void getDialogMessages(Map<String, Object> data, final MethodChannel.Result result) {
        String dialogId = data != null && data.containsKey("dialogId") ? (String) data.get("dialogId") : null;
        Map<String, Object> sortMap = data != null && data.containsKey("sort") ? (Map) data.get("sort") : null;
        Map<String, Object> filterMap = data != null && data.containsKey("filter") ? (Map) data.get("filter") : null;
        int limit = data != null && data.containsKey("limit") ? (int) data.get("limit") : 100;
        int skip = data != null && data.containsKey("skip") ? (int) data.get("skip") : 0;
        boolean markAsRead = data != null && data.containsKey("markAsRead") && (boolean) data.get("markAsRead");

        if (TextUtils.isEmpty(dialogId)) {
            result.error("Required parameter dialogId has a wrong value: " + dialogId, null, null);
            return;
        }

        executor.add(new Task<Pair<List<QBChatMessage>, Bundle>>() {
            @Override
            public Pair<List<QBChatMessage>, Bundle> performBackground() throws Exception {
                QBMessageGetBuilder qbRequestGetBuilder = new QBMessageGetBuilder();
                qbRequestGetBuilder.setSkip(skip);
                qbRequestGetBuilder.setLimit(limit);
                qbRequestGetBuilder.markAsRead(markAsRead);

                ChatMapper.addMessageFilterToRequestBuilder(qbRequestGetBuilder, filterMap);
                ChatMapper.addMessageSortToRequestBuilder(qbRequestGetBuilder, sortMap);

                Performer<?> performer = QBRestChatService.getDialogMessages(new QBChatDialog(dialogId), qbRequestGetBuilder);
                List<QBChatMessage> messages = (List) performer.perform();
                Bundle bundle = ((Query<?>) performer).getBundle();
                return new Pair<>(messages, bundle);
            }

            @Override
            public void performForeground(Pair<List<QBChatMessage>, Bundle> pair) {
                List<Map<String, Object>> messages = new ArrayList<>();

                for (QBChatMessage qbChatMessage : pair.first) {
                    Map<String, Object> message = ChatMapper.qbChatMessageToMap(qbChatMessage);
                    messages.add(message);
                }

                Map<String, Object> payload = new HashMap<>();

                payload.put("messages", messages);
                payload.put("skip", pair.second.containsKey("skip") ? pair.second.getInt("skip") : skip);
                payload.put("limit", pair.second.containsKey("limit") ? pair.second.getInt("limit") : limit);
                payload.put("total", pair.second.containsKey("total_entries") ? pair.second.getInt("total_entries") : -1);

                result.success(payload);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    public void getTotalUnreadMessagesCount(final List<Object> data, final MethodChannel.Result result) {
        List<Object> dialogIdsList = data != null ? data : new ArrayList<>();

        Set<String> dialogIds = new HashSet<>();

        for (Object item : dialogIdsList) {
            if (item instanceof String) {
                String dialogId = (String) item;
                dialogIds.add(dialogId);
            } else {
                result.error("The dialog id should be a String. \nActual value: " + item.getClass().getName(), null, null);
                return;
            }
        }

        executor.add(new Task<Pair<Integer, Bundle>>() {
            @Override
            public Pair<Integer, Bundle> performBackground() throws Exception {
                Performer performer = QBRestChatService.getTotalUnreadMessagesCount(dialogIds, new Bundle());
                Integer messagesCount = (Integer) performer.perform();
                Bundle bundle = ((Query) performer).getBundle();
                return new Pair<>(messagesCount, bundle);
            }

            @Override
            public void performForeground(Pair<Integer, Bundle> pair) {
                Map<String, Object> resultMap = new HashMap<>();

                try {
                    resultMap.put("dialogsCount", parseUnreadMessageCountFromBundle(pair.second));
                } catch (NoSuchFieldException e) {
                    result.error(e.getMessage(), null, null);
                }

                resultMap.put("totalCount", pair.first);

                result.success(resultMap);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    private List<Map<String, Integer>> parseUnreadMessageCountFromBundle(Bundle bundle) throws NoSuchFieldException {
        if (bundle == null || bundle.keySet() == null) {
            throw new NoSuchFieldException("Error parse response, bundle has a wrong value");
        }

        List<Map<String, Integer>> dialogMaps = new ArrayList<>();

        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            if (value instanceof Integer) {
                Map<String, Integer> dialogMap = new HashMap<>();
                dialogMap.put(key, (Integer) value);
                dialogMaps.add(dialogMap);
            }
        }

        return dialogMaps;
    }
}