
package com.quickblox.quickblox_sdk.auth;

import android.text.TextUtils;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBProvider;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.core.exception.QBRuntimeException;
import com.quickblox.quickblox_sdk.auth.listeners.SessionListenerImpl;
import com.quickblox.quickblox_sdk.base.BaseModule;
import com.quickblox.quickblox_sdk.concurrent.Executor;
import com.quickblox.quickblox_sdk.concurrent.ExecutorImpl;
import com.quickblox.quickblox_sdk.concurrent.Task;
import com.quickblox.quickblox_sdk.event.EventHandler;
import com.quickblox.quickblox_sdk.utils.DateUtil;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
public class AuthModule implements BaseModule {

    static final String CHANNEL_NAME = "FlutterQBAuthChannel";

    private static final String LOGIN_METHOD = "login";
    private static final String LOGOUT_METHOD = "logout";
    private static final String LOGIN_WITH_EMAIL_METHOD = "loginWithEmail";
    private static final String LOGIN_WITH_FACEBOOK_METHOD = "loginWithFacebook";
    private static final String LOGIN_WITH_FIREBASE_METHOD = "loginWithFirebase";
    private static final String SET_SESSION_METHOD = "setSession";
    private static final String GET_SESSION_METHOD = "getSession";
    private static final String START_SESSION_WITH_TOKEN_METHOD = "startSessionWithToken";
    private static final String CLEAR_SESSION = "clearSession";

    private final Executor executor = new ExecutorImpl();

    private BinaryMessenger binaryMessenger;

    public AuthModule(BinaryMessenger binaryMessenger) {
        this.binaryMessenger = binaryMessenger;
        initEventHandler();
    }

    public AuthModule() {
        //for tests
    }

    @Override
    public void initEventHandler() {
        EventHandler.init(AuthConstants.getAllEvents(), binaryMessenger);
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
            case LOGIN_METHOD:
                login(methodCall.arguments(), result);
                break;
            case LOGOUT_METHOD:
                logout(result);
                break;
            case LOGIN_WITH_EMAIL_METHOD:
                String email = (String) (((List) methodCall.arguments()).get(0));
                String password = (String) (((List) methodCall.arguments()).get(1));
                loginWithEmail(email, password, result);
                break;
            case LOGIN_WITH_FACEBOOK_METHOD:
                String facebookToken = (String) (((List) methodCall.arguments()).get(0));
                loginWithFacebook(facebookToken, result);
                break;
            case LOGIN_WITH_FIREBASE_METHOD:
                String firebaseProjectId = (String) (((List) methodCall.arguments()).get(0));
                String firebaseToken = (String) (((List) methodCall.arguments()).get(1));
                loginWithFirebase(firebaseProjectId, firebaseToken, result);
                break;
            case SET_SESSION_METHOD:
                setSession(methodCall.arguments(), result);
                break;
            case START_SESSION_WITH_TOKEN_METHOD:
                String token = (String) (((List) methodCall.arguments()).get(0));
                startSessionWithToken(token, result);
                break;
            case CLEAR_SESSION:
                clearSession(result);
                break;
            case GET_SESSION_METHOD:
                getSession(result);
                break;
        }
    }

    private void login(Map<String, Object> data, final MethodChannel.Result result) {
        String login = data != null && data.containsKey("login") ? (String) data.get("login") : null;
        String password = data != null && data.containsKey("password") ? (String) data.get("password") : null;

        executor.add(new Task<QBUser>() {
            @Override
            public QBUser performBackground() throws Exception {
                return QBUsers.signIn(login, password).perform();
            }

            @Override
            public void performForeground(QBUser user) {
                if (QBSessionManager.getInstance().getActiveSession() == null) {
                    result.error("The session doesn't exist", null, null);
                    return;
                }

                QBSessionManager.getInstance().getActiveSession().setUserId(user.getId());

                Map<String, Object> map = new HashMap<>();

                Map<String, Object> session = AuthMapper.qbSessionToMap(QBSessionManager.getInstance().getActiveSession());
                map.put("session", session);

                Map<String, Object> mappedUser = AuthMapper.qbUserToMap(user);
                map.put("user", mappedUser);

                result.success(map);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    private void logout(final MethodChannel.Result result) {
        executor.add(new Task<Void>() {
            @Override
            public Void performBackground() throws Exception {
                QBUsers.signOut().perform();
                return null;
            }

            @Override
            public void performForeground(Void aVoid) {
                QBSessionManager.getInstance().removeListeners();
                result.success(aVoid);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    void loginWithEmail(String email, String password, final MethodChannel.Result result) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            result.error("The required parameters email, password have a wrong value: \nemail: " + email
                    + "\n password: " + password, null, null);
            return;
        }

        executor.add(new Task<QBUser>() {
            @Override
            public QBUser performBackground() throws Exception {
                return QBUsers.signInByEmail(email, password).perform();
            }

            @Override
            public void performForeground(QBUser user) {
                if (QBSessionManager.getInstance().getActiveSession() == null) {
                    result.error("The session doesn't exist", null, null);
                    return;
                }

                QBSessionManager.getInstance().getActiveSession().setUserId(user.getId());

                Map<String, Object> sessionMap = AuthMapper.qbSessionToMap(QBSessionManager.getInstance().getActiveSession());
                Map<String, Object> userMap = AuthMapper.qbUserToMap(user);

                Map<String, Object> map = new HashMap<>();
                map.put("session", sessionMap);
                map.put("user", userMap);

                result.success(map);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });

    }

    void loginWithFacebook(String token, final MethodChannel.Result result) {
        if (TextUtils.isEmpty(token)) {
            result.error("The required parameter token has a wrong value:\ntoken: " + token, null, null);
            return;
        }

        executor.add(new Task<QBUser>() {
            @Override
            public QBUser performBackground() throws Exception {
                return QBUsers.signInUsingSocialProvider(QBProvider.FACEBOOK, token, null).perform();
            }

            @Override
            public void performForeground(QBUser user) {
                if (QBSessionManager.getInstance().getActiveSession() == null) {
                    result.error("The session doesn't exist", null, null);
                    return;
                }

                QBSessionManager.getInstance().getActiveSession().setUserId(user.getId());

                Map<String, Object> map = new HashMap<>();

                Map<String, Object> sessionMap = AuthMapper.qbSessionToMap(QBSessionManager.getInstance().getActiveSession());
                map.put("session", sessionMap);

                Map<String, Object> userMap = AuthMapper.qbUserToMap(user);
                map.put("user", userMap);

                result.success(map);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    void loginWithFirebase(String projectId, String token, final MethodChannel.Result result) {
        if (TextUtils.isEmpty(projectId) || TextUtils.isEmpty(token)) {
            result.error("The required parameters projectId, token have a wrong values: \nprojectId: " + projectId
                    + "\n token: " + token, null, null);
            return;
        }

        executor.add(new Task<QBUser>() {
            @Override
            public QBUser performBackground() throws Exception {
                return QBUsers.signInUsingFirebase(projectId, token).perform();
            }

            @Override
            public void performForeground(QBUser user) {
                if (QBSessionManager.getInstance().getActiveSession() == null) {
                    result.error("The session doesn't exist", null, null);
                    return;
                }

                QBSessionManager.getInstance().getActiveSession().setUserId(user.getId());

                Map<String, Object> sessionMap = AuthMapper.qbSessionToMap(QBSessionManager.getInstance().getActiveSession());
                Map<String, Object> userMap = AuthMapper.qbUserToMap(user);

                Map<String, Object> map = new HashMap<>();
                map.put("session", sessionMap);
                map.put("user", userMap);

                result.success(map);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    }

    private void setSession(Map<String, Object> data, final MethodChannel.Result result) {
        String token = data != null && data.containsKey("token") ? (String) data.get("token") : null;
        Integer userId = data != null && data.containsKey("userId") ? (Integer) data.get("userId") : null;
        Integer applicationId = data != null && data.containsKey("applicationId") ? (Integer) data.get("applicationId") : null;
        String tokenExpirationDate = data != null && data.containsKey("expirationDate") ? (String) data.get("expirationDate") : null;

        if (TextUtils.isEmpty(token)) {
            result.error("token is required", null, null);
            return;
        }
        if (userId == null || userId <= 0) {
            result.error("userId is required", null, null);
            return;
        }
        if (applicationId == null || applicationId <= 0) {
            result.error("applicationId is required", null, null);
            return;
        }

        Date parsedTokenExpirationDate;
        try {
            parsedTokenExpirationDate = parseTokenExpirationDate(tokenExpirationDate);
        } catch (ParseException e) {
            parsedTokenExpirationDate = DateUtil.generateFutureDate();
        }

        QBSessionManager.getInstance().createActiveSession(token, parsedTokenExpirationDate);

        QBSession qbSession = QBSessionManager.getInstance().getActiveSession();

        qbSession.setUserId(userId);
        qbSession.setAppId(applicationId);

        Map<String, Object> session = AuthMapper.qbSessionToMap(qbSession);
        result.success(session);
    }

    private Date parseTokenExpirationDate(String expirationDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        Date parsedDate = dateFormat.parse(expirationDate);
        if (parsedDate == null || parsedDate.before(new Date())) {
            throw new ParseException("", 0);
        }
        return parsedDate;
    }

    private void getSession(final MethodChannel.Result result) {
        if (QBSessionManager.getInstance().getActiveSession() == null) {
            result.error("The session doesn't exist", null, null);
            return;
        }

        QBSession qbSession = QBSessionManager.getInstance().getActiveSession();
        Map<String, Object> session = AuthMapper.qbSessionToMap(qbSession);
        result.success(session);
    }

    public void startSessionWithToken(String token, MethodChannel.Result result) {
        if (TextUtils.isEmpty(token)) {
            result.error("The required parameter token has a wrong value: \ntoken: " + token, null, null);
            return;
        }

        QBSessionManager.getInstance().addListener(new SessionListenerImpl());

        executor.add(new Task<QBSession>() {
            @Override
            public QBSession performBackground() throws Exception {
                QBSession session = QBAuth.startSessionWithToken(token).perform();

                String token = QBSessionManager.getInstance().getToken();
                boolean isIncorrectToken = TextUtils.isEmpty(token);
                if (isIncorrectToken) {
                    throw new QBRuntimeException("The session token has incorrect value");
                }

                return session;
            }

            @Override
            public void performForeground(QBSession qbSession) {
                QBSessionManager.getInstance().addListener(new SessionListenerImpl());

                Map<String, Object> session = AuthMapper.qbSessionToMap(qbSession);
                result.success(session);
            }

            @Override
            public void performError(Exception exception) {
                result.error(exception.getMessage(), null, null);
            }
        });
    } 

    public void clearSession(MethodChannel.Result result) {  
        if (QBSessionManager.getInstance().getActiveSession() == null) {
            // result.success();
            return;
        }
        QBSessionManager.getInstance().deleteActiveSession();
        // result.success();            
    }
}