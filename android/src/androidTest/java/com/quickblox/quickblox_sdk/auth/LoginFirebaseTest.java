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
public class LoginFirebaseTest extends BaseTest {

    @Override
    protected void afterEachTest() throws Exception {
        logoutFromRest();
    }

    @Test
    public void login() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new AuthModule().loginWithFirebase(BaseTest.FIREBASE_PROJECT_ID, BaseTest.FIREBASE_TOKEN, new ResultImpl() {
            @Override
            public void success(Object value) {
                assertNotNull(((HashMap<?, ?>) value).get("session"));
                assertNotNull(((HashMap<?, ?>) value).get("user"));

                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void loginWithWrongToken() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new AuthModule().loginWithFirebase(BaseTest.FIREBASE_PROJECT_ID, "wrong_token", new ResultImpl() {
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
    public void loginWithEmptyToken() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new AuthModule().loginWithFirebase(BaseTest.FIREBASE_PROJECT_ID, "", new ResultImpl() {
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
    public void loginWithWrongProjectId() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new AuthModule().loginWithFirebase("wrong_project_id", BaseTest.FIREBASE_TOKEN, new ResultImpl() {
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
    public void loginWithEmptyProjectId() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new AuthModule().loginWithFirebase("", BaseTest.FIREBASE_TOKEN, new ResultImpl() {
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
    public void loginWithEmptyProjectIdAndEmptyToken() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new AuthModule().loginWithFirebase("", "", new ResultImpl() {
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
    public void loginWithWrongProjectIdAndWrongToken() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new AuthModule().loginWithFirebase("wrong_project_id", "wrong_token", new ResultImpl() {
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