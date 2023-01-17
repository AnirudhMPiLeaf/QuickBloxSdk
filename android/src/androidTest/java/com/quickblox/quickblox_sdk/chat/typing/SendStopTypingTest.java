package com.quickblox.quickblox_sdk.chat.typing;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.core.exception.QBResponseException;
import com.quickblox.quickblox_sdk.BaseTest;
import com.quickblox.quickblox_sdk.chat.ChatModule;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4ClassRunner.class)
public class SendStopTypingTest extends BaseTest {
    private String dialogId;

    @Override
    protected void beforeEachTest() throws Exception {
        dialogId = null;
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
    public void sendWithWrongId() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        HashMap<String, Object> data = new HashMap<>();
        data.put("dialogId", "wrongDialogId");

        getInstrumentation().runOnMainSync(() -> new ChatModule().sendStoppedTyping(data, new ResultImpl() {
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
    public void sendWithEmptyId() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        HashMap<String, Object> data = new HashMap<>();
        data.put("dialogId", "");

        getInstrumentation().runOnMainSync(() -> new ChatModule().sendStoppedTyping(data, new ResultImpl() {
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
    public void sendWith_NULL_Id() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        HashMap<String, Object> data = new HashMap<>();
        data.put("dialogId", null);

        getInstrumentation().runOnMainSync(() -> new ChatModule().sendStoppedTyping(data, new ResultImpl() {
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
    public void sendToPrivate() throws InterruptedException, QBResponseException {
        CountDownLatch downLatch = new CountDownLatch(1);

        dialogId = buildPrivateDialogId();

        HashMap<String, Object> sendTypingData = new HashMap<>();
        sendTypingData.put("dialogId", dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().sendStoppedTyping(sendTypingData, new ResultImpl() {
            @Override
            public void success(Object o) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void sendToGroup() throws InterruptedException, QBResponseException {
        CountDownLatch downLatch = new CountDownLatch(1);

        dialogId = buildGroupDialogId();

        HashMap<String, Object> dialogData = new HashMap<>();
        dialogData.put("dialogId", dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().sendStoppedTyping(dialogData, new ResultImpl() {
            @Override
            public void success(Object o) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void sendToGroupWithNotConnectedChat() throws InterruptedException, QBResponseException {
        CountDownLatch downLatch = new CountDownLatch(1);

        logoutFromChat();

        dialogId = buildGroupDialogId();

        HashMap<String, Object> dialogData = new HashMap<>();
        dialogData.put("dialogId", dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().sendStoppedTyping(dialogData, new ResultImpl() {
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
    public void sendToPrivateWithNotConnectedChat() throws InterruptedException, QBResponseException {
        CountDownLatch downLatch = new CountDownLatch(1);

        logoutFromChat();

        dialogId = buildPrivateDialogId();

        HashMap<String, Object> dialogData = new HashMap<>();
        dialogData.put("dialogId", dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().sendStoppedTyping(dialogData, new ResultImpl() {
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