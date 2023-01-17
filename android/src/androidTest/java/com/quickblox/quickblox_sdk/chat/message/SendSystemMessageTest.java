package com.quickblox.quickblox_sdk.chat.message;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.quickblox_sdk.BaseTest;
import com.quickblox.quickblox_sdk.chat.ChatModule;
import com.quickblox.quickblox_sdk.chat.utils.ChatDialogUtils;
import com.quickblox.quickblox_sdk.chat.utils.MessageUtils;

import org.jivesoftware.smack.SmackException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4ClassRunner.class)
public class SendSystemMessageTest extends BaseTest {

    @Override
    protected void beforeEachTest() throws Exception {
        loginToRest();
        loginToChat();
    }

    @Override
    protected void afterEachTest() throws Exception {
        logoutFromChat();
        logoutFromRest();
    }

    @Test
    public void sendWithWrongId() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        HashMap<String, Object> data = new HashMap<>();
        data.put("recipientId", -100);

        getInstrumentation().runOnMainSync(() -> new ChatModule().sendSystemMessage(data, new ResultImpl() {
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
        data.put("recipientId", null);

        getInstrumentation().runOnMainSync(() -> new ChatModule().sendSystemMessage(data, new ResultImpl() {
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
    public void sendWithNotConnectedChat() throws InterruptedException, SmackException.NotConnectedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        logoutFromChat();

        HashMap<String, Object> data = new HashMap<>();
        data.put("recipientId", null);

        getInstrumentation().runOnMainSync(() -> new ChatModule().sendSystemMessage(data, new ResultImpl() {
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
    public void send() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        HashMap<String, Object> data = new HashMap<>();
        data.put("recipientId", ChatDialogUtils.QWE_22);
        data.put("properties", MessageUtils.buildProperties());

        getInstrumentation().runOnMainSync(() -> new ChatModule().sendSystemMessage(data, new ResultImpl() {
            @Override
            public void success(Object o) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }
}