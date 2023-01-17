package com.quickblox.quickblox_sdk;

import android.content.Context;
import android.text.TextUtils;

import androidx.test.platform.app.InstrumentationRegistry;

import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.quickblox_sdk.chat.utils.ChatDialogUtils;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import junit.framework.TestCase;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.flutter.plugin.common.MethodChannel;

public abstract class BaseTest extends TestCase {
    public static final int USER_ID = 0;
    public static final String USER_PASSWORD = "";
    public static final String USER_LOGIN = "";
    public static final String USER_EMAIL = "";

    public static final String APPLICATION_ID = "";
    public static final String AUTHORIZATION_KEY = "";
    public static final String AUTHORIZATION_SECRET = "";
    public static final String ACCOUNT_KEY = "";

    public static final String FACEBOOK_TOKEN = "";

    public static final String FIREBASE_PROJECT_ID = "";
    public static final String FIREBASE_TOKEN = "";

    public static final String API_ENDPOINT = "https://api.quickblox.com";
    public static final String CHAT_ENDPOINT = "chat.quickblox.com";

    protected Context context;

    @Before
    public void init() throws Exception {
        initContext();
        initCredentials();

        new CountDownLatch(1).await(3, TimeUnit.SECONDS);

        beforeEachTest();
    }

    protected void beforeEachTest() throws Exception {
        //template method
    }

    @After
    public void release() throws Exception {
        afterEachTest();
    }

    protected void afterEachTest() throws Exception {
        //template method
    }

    protected void initContext() {
        this.context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    protected void initCredentials() {
        QBSettings.getInstance().init(context, APPLICATION_ID, AUTHORIZATION_KEY, AUTHORIZATION_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

        //QBSettings.getInstance().setEndpoints("https://apirc.quickblox.com", "chatrc.quickblox.com", ServiceZone.DEVELOPMENT);
    }

    protected QBUser buildUser() {
        QBUser user = new QBUser();
        user.setPassword(USER_PASSWORD);
        user.setId(BaseTest.USER_ID);
        user.setLogin(USER_LOGIN);

        return user;
    }

    protected void loginToRest() throws QBResponseException {
        QBUser user = buildUser();
        QBUsers.signIn(user).perform();
    }

    protected void loginToChat() throws IOException, XMPPException, SmackException {
        QBUser user = buildUser();
        QBChatService.getInstance().login(user);
    }

    protected void logoutFromRest() throws QBResponseException {
        QBUsers.signOut().perform();
    }

    protected void logoutFromChat() {
        QBChatService.getInstance().destroy();
    }

    protected String buildPrivateDialogId() throws QBResponseException {
        return buildPrivateDialog().getDialogId();
    }

    protected QBChatDialog buildPrivateDialog() throws QBResponseException {
        String name = "Test private Flutter/Android dialog " + System.currentTimeMillis();
        List<Integer> occupants = Arrays.asList(ChatDialogUtils.QWE_11, ChatDialogUtils.QWE_22);
        QBChatDialog dialog = DialogUtils.buildDialog(name, QBDialogType.PRIVATE, occupants);
        return QBRestChatService.createChatDialog(dialog).perform();
    }

    protected String buildGroupDialogId() throws QBResponseException {
        return buildGroupDialog().getDialogId();
    }

    protected QBChatDialog buildGroupDialog() throws QBResponseException {
        String name = "Test group Flutter/Android dialog " + System.currentTimeMillis();
        List<Integer> occupants = Arrays.asList(ChatDialogUtils.QWE_11, ChatDialogUtils.QWE_22, ChatDialogUtils.QWE_33,
                ChatDialogUtils.QWE_44);
        QBChatDialog dialog = DialogUtils.buildDialog(name, QBDialogType.GROUP, occupants);
        return QBRestChatService.createChatDialog(dialog).perform();
    }

    protected void sendMessage(QBChatDialog dialog) throws SmackException.NotConnectedException {
        QBChatMessage message = new QBChatMessage();
        message.setBody("Test message from Flutter/Android" + System.currentTimeMillis());
        message.setMarkable(true);
        message.setSaveToHistory(true);
        message.setDateSent(System.currentTimeMillis());

        dialog.sendMessage(message);
    }

    protected void deleteDialogFromRest(String dialogId) throws QBResponseException {
        if (!TextUtils.isEmpty(dialogId)) {
            QBRestChatService.deleteDialog(dialogId, false).perform();
        }
    }

    public static class ResultImpl implements MethodChannel.Result {
        @Override
        public void success(Object o) {

        }

        @Override
        public void error(String s, String s1, Object o) {
            throw new RuntimeException(s);
        }

        @Override
        public void notImplemented() {
            throw new RuntimeException("This method hasn't implemented yet");
        }
    }
}