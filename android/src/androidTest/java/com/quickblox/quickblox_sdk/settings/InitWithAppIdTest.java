package com.quickblox.quickblox_sdk.settings;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.quickblox_sdk.BaseTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.flutter.plugin.common.MethodCall;

@RunWith(AndroidJUnit4ClassRunner.class)
public class InitWithAppIdTest extends BaseTest {
    @Test
    public void withCorrect() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> arguments = new HashMap<>();

        arguments.put("appId", APPLICATION_ID);
        arguments.put("accountKey", ACCOUNT_KEY);
        arguments.put("apiEndpoint", API_ENDPOINT);
        arguments.put("chatEndpoint", CHAT_ENDPOINT);

        MethodCall methodCall = new MethodCall(SettingsModule.INIT_WITHOUT_AUTH_KEY_AND_SECRET_METHOD, arguments);

        new SettingsModule(context).handleMethod(methodCall, new BaseTest.ResultImpl() {
            @Override
            public void success(Object o) {
                downLatch.countDown();
            }
        });

        downLatch.await(3, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void withEmptyAppId() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("appId", "");
        arguments.put("accountKey", ACCOUNT_KEY);
        arguments.put("apiEndpoint", API_ENDPOINT);
        arguments.put("chatEndpoint", CHAT_ENDPOINT);

        MethodCall methodCall = new MethodCall(SettingsModule.INIT_WITHOUT_AUTH_KEY_AND_SECRET_METHOD, arguments);

        new SettingsModule(context).handleMethod(methodCall, new BaseTest.ResultImpl() {
            @Override
            public void success(Object o) {
                fail("expected: error, actual: resolve");
            }

            @Override
            public void error(String s, String s1, Object o) {
                downLatch.countDown();
            }
        });

        downLatch.await(3, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test(expected = ClassCastException.class)
    public void withIntegerAppId() throws InterruptedException {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("appId", 475);
        arguments.put("accountKey", ACCOUNT_KEY);
        arguments.put("apiEndpoint", API_ENDPOINT);
        arguments.put("chatEndpoint", CHAT_ENDPOINT);

        MethodCall methodCall = new MethodCall(SettingsModule.INIT_WITHOUT_AUTH_KEY_AND_SECRET_METHOD, arguments);

        new SettingsModule(context).handleMethod(methodCall, null);

        new CountDownLatch(1).await(3, TimeUnit.SECONDS);

        fail("expected: throw ClassCastException, actual: no exception");
    }

    @Test
    public void with_NULL_AppId() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> arguments = new HashMap<>();

        arguments.put("accountKey", ACCOUNT_KEY);
        arguments.put("apiEndpoint", API_ENDPOINT);
        arguments.put("chatEndpoint", CHAT_ENDPOINT);

        MethodCall methodCall = new MethodCall(SettingsModule.INIT_WITHOUT_AUTH_KEY_AND_SECRET_METHOD, arguments);

        new SettingsModule(context).handleMethod(methodCall, new BaseTest.ResultImpl() {
            @Override
            public void success(Object o) {
                fail("expected: error, actual: resolve");
            }

            @Override
            public void error(String s, String s1, Object o) {
                downLatch.countDown();
            }
        });

        downLatch.await(3, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void with_NULL_AccountKey() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("appId", APPLICATION_ID);
        arguments.put("apiEndpoint", API_ENDPOINT);
        arguments.put("chatEndpoint", CHAT_ENDPOINT);

        MethodCall methodCall = new MethodCall(SettingsModule.INIT_WITHOUT_AUTH_KEY_AND_SECRET_METHOD, arguments);

        new SettingsModule(context).handleMethod(methodCall, new BaseTest.ResultImpl() {
            @Override
            public void success(Object o) {
                downLatch.countDown();
            }
        });

        downLatch.await(3, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void with_NULL_ApiEndpoint() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("appId", APPLICATION_ID);
        arguments.put("accountKey", ACCOUNT_KEY);
        arguments.put("chatEndpoint", CHAT_ENDPOINT);

        MethodCall methodCall = new MethodCall(SettingsModule.INIT_WITHOUT_AUTH_KEY_AND_SECRET_METHOD, arguments);

        new SettingsModule(context).handleMethod(methodCall, new BaseTest.ResultImpl() {
            @Override
            public void success(Object o) {
                downLatch.countDown();
            }
        });

        downLatch.await(3, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void with_NULL_ChatEndpoint() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("appId", APPLICATION_ID);
        arguments.put("accountKey", ACCOUNT_KEY);
        arguments.put("apiEndpoint", API_ENDPOINT);

        MethodCall methodCall = new MethodCall(SettingsModule.INIT_WITHOUT_AUTH_KEY_AND_SECRET_METHOD, arguments);

        new SettingsModule(context).handleMethod(methodCall, new BaseTest.ResultImpl() {
            @Override
            public void success(Object o) {
                downLatch.countDown();
            }
        });

        downLatch.await(3, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }
}