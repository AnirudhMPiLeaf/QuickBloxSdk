package com.quickblox.quickblox_sdk.chat.message;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.quickblox_sdk.BaseTest;
import com.quickblox.quickblox_sdk.chat.ChatModule;
import com.quickblox.quickblox_sdk.chat.utils.MessageUtils;

import org.jivesoftware.smack.SmackException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MarkMessageDeliveredTest extends BaseTest {
    private String dialogId;
    private String messageId;

    @Override
    protected void beforeEachTest() throws Exception {
        messageId = null;
        loginToRest();
        loginToChat();
    }

    @Override
    protected void afterEachTest() throws Exception {
        deleteDialogFromRest(dialogId);
        logoutFromChat();
        logoutFromRest();
    }

    @Test
    public void markWithEmptyValues() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new ChatModule().markMessageDelivered(MessageUtils.buildMarkMessageEmpty(), new ResultImpl() {
            @Override
            public void success(Object o) {
                fail("expected: error, actual: resolve");
            }

            @Override
            public void error(String s, String s1, Object o) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void markWith_NULL_Values() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new ChatModule().markMessageDelivered(null, new ResultImpl() {
            @Override
            public void success(Object o) {
                fail("expected: error, actual: resolve");
            }

            @Override
            public void error(String s, String s1, Object o) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void markWithWrongValues() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new ChatModule().markMessageDelivered(MessageUtils.buildMarkMessageWrong(), new ResultImpl() {
            @Override
            public void success(Object o) {
                fail("expected: error, actual: resolve");
            }

            @Override
            public void error(String s, String s1, Object o) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void mark() throws InterruptedException, QBResponseException, SmackException.NotConnectedException {
        CountDownLatch downLatch = new CountDownLatch(2);

        QBChatDialog dialog = buildPrivateDialog();
        dialogId = dialog.getDialogId();

        sendMessage(dialog);

        HashMap<String, Object> getDialogsData = new HashMap<>();
        getDialogsData.put("dialogId", dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().getDialogMessages(getDialogsData, new ResultImpl() {
            @Override
            public void success(Object o) {
                List<?> messages = (List<?>) ((HashMap<?, ?>) o).get("messages");
                messageId = (String) ((HashMap<?, ?>) messages.get(0)).get("id");
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        Map<String, Object> markMessageData = MessageUtils.buildMarkMessage(messageId, BaseTest.USER_ID, dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().markMessageDelivered(markMessageData, new ResultImpl() {
            @Override
            public void success(Object o) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void markWithNotConnectedChat() throws InterruptedException, SmackException.NotConnectedException, QBResponseException {
        CountDownLatch downLatch = new CountDownLatch(1);

        dialogId = buildPrivateDialogId();

        logoutFromChat();

        Map<String, Object> markMessageData = MessageUtils.buildMarkMessage(messageId, BaseTest.USER_ID, dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().markMessageDelivered(markMessageData, new ResultImpl() {
            @Override
            public void success(Object o) {
                fail("expected: error, actual: resolve");
            }

            @Override
            public void error(String s, String s1, Object o) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }
}