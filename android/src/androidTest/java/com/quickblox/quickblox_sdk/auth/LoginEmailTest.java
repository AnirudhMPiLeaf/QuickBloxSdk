package com.quickblox.quickblox_sdk.auth;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.quickblox_sdk.BaseTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4ClassRunner.class)
public class LoginEmailTest extends BaseTest {

    @Override
    protected void afterEachTest() throws Exception {
        logoutFromRest();
    }

    @Test
    public void login() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new AuthModule().loginWithEmail(BaseTest.USER_EMAIL, BaseTest.USER_PASSWORD, new ResultImpl() {
            @Override
            public void success(Object value) {
                assertNotNull(((HashMap) value).get("session"));
                assertNotNull(((HashMap) value).get("user"));

                int userId = (int) ((HashMap) ((HashMap) value).get("user")).get("id");
                assertEquals(BaseTest.USER_ID, userId);

                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void loginWithWrongEmail() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new AuthModule().loginWithEmail("incorrect_email", BaseTest.USER_PASSWORD, new ResultImpl() {
            @Override
            public void success(Object value) {
                fail("expected: error, actual: success");
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
    public void loginWithWrongPassword() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new AuthModule().loginWithEmail(BaseTest.USER_EMAIL, "incorrect_password", new ResultImpl() {
            @Override
            public void success(Object value) {
                fail("expected: error, actual: success");
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
    public void loginWithEmptyPassword() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new AuthModule().loginWithEmail(BaseTest.USER_EMAIL, "", new ResultImpl() {
            @Override
            public void success(Object value) {
                fail("expected: error, actual: success");
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
    public void loginWithEmptyEmail() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new AuthModule().loginWithEmail("", BaseTest.USER_PASSWORD, new ResultImpl() {
            @Override
            public void success(Object value) {
                fail("expected: error, actual: success");
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
    public void loginWithEmptyEmailAndEmptyPassword() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new AuthModule().loginWithEmail("", "", new ResultImpl() {
            @Override
            public void success(Object value) {
                fail("expected: error, actual: success");
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
    public void loginWithWrongEmailAndWrongPassword() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new AuthModule().loginWithEmail("wrong_email", "wrong_password", new ResultImpl() {
            @Override
            public void success(Object value) {
                fail("expected: error, actual: success");
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