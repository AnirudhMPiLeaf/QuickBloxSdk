package com.quickblox.quickblox_sdk.chat.ping;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.quickblox_sdk.BaseTest;
import com.quickblox.quickblox_sdk.chat.ChatModule;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4ClassRunner.class)
public class PingUserTest extends BaseTest {

    @Override
    protected void beforeEachTest() throws Exception {
        loginToChat();
    }

    @Override
    protected void afterEachTest() {
        logoutFromChat();
    }

    @Test
    public void pingOnlineUser() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> data = new HashMap<>();
        data.put("userId", BaseTest.USER_ID);

        getInstrumentation().runOnMainSync(() -> new ChatModule().pingUser(data, new ResultImpl() {
            @Override
            public void success(Object result) {
                assertNotNull(result);
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void pingOfflineUser() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> data = new HashMap<>();
        data.put("userId", 1000000001);

        getInstrumentation().runOnMainSync(() -> new ChatModule().pingUser(data, new ResultImpl() {
            @Override
            public void success(Object result) {
                assertFalse((boolean) result);
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void pingWithoutConnectToChat() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        logoutFromChat();

        getInstrumentation().runOnMainSync(() -> new ChatModule().pingServer(new ResultImpl() {
            @Override
            public void success(Object result) {
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
    public void pingWith_NULL_UserId() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> data = new HashMap<>();
        data.put("userId", null);

        getInstrumentation().runOnMainSync(() -> new ChatModule().pingUser(data, new ResultImpl() {
            @Override
            public void success(Object result) {
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
