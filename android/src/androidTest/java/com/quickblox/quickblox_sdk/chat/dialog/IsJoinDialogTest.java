package com.quickblox.quickblox_sdk.chat.dialog;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.core.exception.QBResponseException;
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
public class IsJoinDialogTest extends BaseTest {
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
    public void isJoinWithWrongId() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        HashMap<String, Object> data = new HashMap<>();
        data.put("dialogId", "wrong_dialog_id");

        getInstrumentation().runOnMainSync(() -> new ChatModule().isJoinedDialog(data, new ResultImpl() {
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
    public void isJoinWithEmptyId() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        HashMap<String, Object> data = new HashMap<>();
        data.put("dialogId", "");

        getInstrumentation().runOnMainSync(() -> new ChatModule().isJoinedDialog(data, new ResultImpl() {
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
    public void isJoinWith_NULL_Id() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        HashMap<String, Object> data = new HashMap<>();
        data.put("dialogId", null);

        getInstrumentation().runOnMainSync(() -> new ChatModule().isJoinedDialog(data, new ResultImpl() {
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
    public void isJoinGroupYes() throws InterruptedException, QBResponseException {
        CountDownLatch downLatch = new CountDownLatch(2);

        dialogId = buildGroupDialogId();

        Map<String, Object> joinDialogData = new HashMap<>();
        joinDialogData.put("dialogId", dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().joinDialog(joinDialogData, new ResultImpl() {
            @Override
            public void success(Object value) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        getInstrumentation().runOnMainSync(() -> new ChatModule().isJoinedDialog(joinDialogData, new ResultImpl() {
            @Override
            public void success(Object value) {
                assertTrue((Boolean) value);
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void isJoinGroupNo() throws InterruptedException, QBResponseException {
        CountDownLatch downLatch = new CountDownLatch(1);

        dialogId = buildGroupDialogId();

        Map<String, Object> joinDialogData = new HashMap<>();
        joinDialogData.put("dialogId", dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().isJoinedDialog(joinDialogData, new ResultImpl() {
            @Override
            public void success(Object value) {
                assertFalse((Boolean) value);
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void isJoinPrivate() throws InterruptedException, QBResponseException {
        CountDownLatch downLatch = new CountDownLatch(1);

        dialogId = buildPrivateDialogId();

        Map<String, Object> joinDialogData = new HashMap<>();
        joinDialogData.put("dialogId", dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().isJoinedDialog(joinDialogData, new ResultImpl() {
            @Override
            public void success(Object value) {
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
