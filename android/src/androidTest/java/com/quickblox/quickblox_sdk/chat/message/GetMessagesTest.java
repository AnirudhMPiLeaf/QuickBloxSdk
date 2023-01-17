package com.quickblox.quickblox_sdk.chat.message;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.core.exception.QBResponseException;
import com.quickblox.quickblox_sdk.BaseTest;
import com.quickblox.quickblox_sdk.chat.ChatModule;
import com.quickblox.quickblox_sdk.chat.utils.ChatFilterUtils;
import com.quickblox.quickblox_sdk.chat.utils.ChatSortUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4ClassRunner.class)
public class GetMessagesTest extends BaseTest {
    String dialogId;

    @Override
    protected void beforeEachTest() throws Exception {
        loginToRest();
    }

    @Override
    protected void afterEachTest() throws Exception {
        deleteDialogFromRest(dialogId);
        logoutFromRest();
    }

    @Test
    public void getWithWrongId() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        HashMap<String, Object> getDialogsData = new HashMap<>();
        getDialogsData.put("dialogId", "wrong_dialog_id");

        getInstrumentation().runOnMainSync(() -> new ChatModule().getDialogMessages(getDialogsData, new ResultImpl() {
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
    public void getWithEmptyId() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        HashMap<String, Object> getDialogsData = new HashMap<>();
        getDialogsData.put("dialogId", "");

        getInstrumentation().runOnMainSync(() -> new ChatModule().getDialogMessages(getDialogsData, new ResultImpl() {
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
    public void getWith_NULL_Id() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        HashMap<String, Object> getDialogsData = new HashMap<>();
        getDialogsData.put("dialogId", null);

        getInstrumentation().runOnMainSync(() -> new ChatModule().getDialogMessages(getDialogsData, new ResultImpl() {
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
    public void get() throws InterruptedException, QBResponseException {
        CountDownLatch downLatch = new CountDownLatch(1);

        dialogId = buildPrivateDialogId();

        HashMap<String, Object> data = new HashMap<>();
        data.put("dialogId", dialogId);
        data.put("sort", ChatSortUtils.buildSortMessageByDateSend());
        data.put("filter", ChatFilterUtils.buildFilterMessageSenderId());
        data.put("limit", 10);
        data.put("skip", 5);
        data.put("markAsRead", false);

        getInstrumentation().runOnMainSync(() -> new ChatModule().getDialogMessages(data, new ResultImpl() {
            @Override
            public void success(Object value) {
                assertNotNull(value);
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }
}
