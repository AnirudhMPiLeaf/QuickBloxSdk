package com.quickblox.quickblox_sdk.chat.connect;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.quickblox_sdk.BaseTest;
import com.quickblox.quickblox_sdk.chat.ChatModule;
import com.quickblox.quickblox_sdk.chat.utils.ConnectUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4ClassRunner.class)
public class ConnectTest extends BaseTest {

    @Override
    protected void beforeEachTest() throws Exception {
        loginToRest();
    }

    @Override
    protected void afterEachTest() throws Exception {
        logoutFromChat();
        logoutFromRest();
    }

    @Test
    public void connect() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new ChatModule().connect(ConnectUtils.buildCorrectCredentials(), new ResultImpl() {
            @Override
            public void success(Object o) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void connectWithWrongUserId() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> credentialsData = new HashMap<>();
        credentialsData.put("userId", 0);
        credentialsData.put("password", BaseTest.USER_PASSWORD);

        getInstrumentation().runOnMainSync(() -> new ChatModule().connect(credentialsData, new ResultImpl() {
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
    public void connectWith_NULL_UserId() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> credentialsData = new HashMap<>();
        credentialsData.put("userId", null);
        credentialsData.put("password", BaseTest.USER_PASSWORD);

        getInstrumentation().runOnMainSync(() -> new ChatModule().connect(credentialsData, new ResultImpl() {
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
    public void connectWithWrongPassword() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> credentialsData = new HashMap<>();
        credentialsData.put("userId", BaseTest.USER_ID);
        credentialsData.put("password", "no_password");

        getInstrumentation().runOnMainSync(() -> new ChatModule().connect(credentialsData, new ResultImpl() {
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
    public void connectWith_NULL_password() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> credentialsData = new HashMap<>();
        credentialsData.put("userId", BaseTest.USER_ID);
        credentialsData.put("password", null);

        getInstrumentation().runOnMainSync(() -> new ChatModule().connect(credentialsData, new ResultImpl() {
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
    public void connectWithWrongUserIdAndPassword() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> credentialsData = new HashMap<>();
        credentialsData.put("userId", 0);
        credentialsData.put("password", "no_password");

        getInstrumentation().runOnMainSync(() -> new ChatModule().connect(credentialsData, new ResultImpl() {
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
    public void connectWith_NULL_UserIdAndPassword() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> credentialsData = new HashMap<>();
        credentialsData.put("userId", null);
        credentialsData.put("password", null);

        getInstrumentation().runOnMainSync(() -> new ChatModule().connect(credentialsData, new ResultImpl() {
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
    public void doubleConnect() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(2);

        Map<String, Object> credentialData = new HashMap<>();
        credentialData.put("userId", BaseTest.USER_ID);
        credentialData.put("password", BaseTest.USER_PASSWORD);

        getInstrumentation().runOnMainSync(() -> new ChatModule().connect(credentialData, new ResultImpl() {
            @Override
            public void success(Object o) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        getInstrumentation().runOnMainSync(() -> new ChatModule().connect(credentialData, new ResultImpl() {
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
    public void isConnected_YES() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(2);

        Map<String, Object> credentialData = new HashMap<>();
        credentialData.put("userId", BaseTest.USER_ID);
        credentialData.put("password", BaseTest.USER_PASSWORD);

        getInstrumentation().runOnMainSync(() -> new ChatModule().connect(credentialData, new ResultImpl() {
            @Override
            public void success(Object o) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        getInstrumentation().runOnMainSync(() -> new ChatModule().isConnected(new ResultImpl() {
            @Override
            public void success(Object o) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void isConnected_NO() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new ChatModule().isConnected(new ResultImpl() {
            @Override
            public void success(Object o) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }
}
