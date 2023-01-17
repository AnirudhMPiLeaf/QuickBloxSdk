package com.quickblox.quickblox_sdk.chat.dialog;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.core.exception.QBResponseException;
import com.quickblox.quickblox_sdk.BaseTest;
import com.quickblox.quickblox_sdk.chat.ChatModule;
import com.quickblox.quickblox_sdk.chat.utils.ChatDialogUtils;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4ClassRunner.class)
public class LeaveDialogTest extends BaseTest {
    private String dialogId;

    @Override
    protected void beforeEachTest() throws Exception {
        dialogId = null;
        loginToRest();
    }

    @Override
    protected void afterEachTest() throws Exception {
        deleteDialogFromRest(dialogId);
        logoutFromChat();
        logoutFromRest();
    }

    @Test
    public void leaveWith_NULL_Id() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> data = new HashMap<>();
        data.put("dialogId", null);

        getInstrumentation().runOnMainSync(() -> new ChatModule().leaveDialog(data, new ResultImpl() {
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
    public void leaveWithEmptyId() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> data = new HashMap<>();
        data.put("dialogId", "");

        getInstrumentation().runOnMainSync(() -> new ChatModule().leaveDialog(data, new ResultImpl() {
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
    public void leaveWithWrongId() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> data = new HashMap<>();
        data.put("dialogId", "wrong_dialog_id");

        getInstrumentation().runOnMainSync(() -> new ChatModule().leaveDialog(data, new ResultImpl() {
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
    public void leavePrivateWithoutLoggedToChat() throws InterruptedException, QBResponseException {
        CountDownLatch downLatch = new CountDownLatch(1);

        dialogId = buildPrivateDialogId();

        Map<String, Object> leaveDialogData = new HashMap<>();
        leaveDialogData.put("dialogId", dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().leaveDialog(leaveDialogData, new ResultImpl() {
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
    public void leavePrivateWithLoggedToChat() throws InterruptedException, XMPPException, IOException, SmackException {
        CountDownLatch downLatch = new CountDownLatch(1);

        loginToChat();

        dialogId = buildPrivateDialogId();

        Map<String, Object> leaveDialogData = new HashMap<>();
        leaveDialogData.put("dialogId", dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().leaveDialog(leaveDialogData, new ResultImpl() {
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
    public void leaveGroupWithoutLoggedToChat() throws InterruptedException, QBResponseException {
        CountDownLatch downLatch = new CountDownLatch(1);

        dialogId = buildGroupDialogId();

        Map<String, Object> leaveDialogData = new HashMap<>();
        leaveDialogData.put("dialogId", dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().leaveDialog(leaveDialogData, new ResultImpl() {
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
    public void leaveGroupWithLoggedToChat() throws InterruptedException, XMPPException, IOException, SmackException {
        CountDownLatch downLatch = new CountDownLatch(1);

        loginToChat();

        dialogId = buildGroupDialogId();

        Map<String, Object> leaveDialogData = new HashMap<>();
        leaveDialogData.put("dialogId", dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().leaveDialog(leaveDialogData, new ResultImpl() {
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
    public void leaveGroupWithLoggedToChatAndJoined() throws InterruptedException, XMPPException, IOException, SmackException {
        CountDownLatch downLatch = new CountDownLatch(2);

        loginToChat();

        dialogId = buildGroupDialogId();

        Map<String, Object> joinDialogData = ChatDialogUtils.buildGroupDialog();
        joinDialogData.put("dialogId", dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().joinDialog(joinDialogData, new ResultImpl() {
            @Override
            public void success(Object value) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        Map<String, Object> leaveDialogData = new HashMap<>();
        leaveDialogData.put("dialogId", dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().leaveDialog(leaveDialogData, new ResultImpl() {
            @Override
            public void success(Object o) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }
}