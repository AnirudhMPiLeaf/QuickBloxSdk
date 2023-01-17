package com.quickblox.quickblox_sdk.chat.message;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.quickblox_sdk.BaseTest;
import com.quickblox.quickblox_sdk.chat.ChatModule;
import com.quickblox.quickblox_sdk.chat.utils.UnreadMessagesUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

/**
 * Created by Injoit on 2022-04-21.
 * Copyright © 2022 Quickblox. All rights reserved.
 */

@RunWith(AndroidJUnit4ClassRunner.class)
public class GetTotalUnreadMessageCountTest extends BaseTest {
    private final List<QBChatDialog> dialogs = new ArrayList<>();

    @Override
    protected void beforeEachTest() throws Exception {
        loginToRest();

        List<QBChatDialog> dialogs = QBRestChatService.getChatDialogs(QBDialogType.PRIVATE, null).perform();

        if (dialogs.isEmpty() || dialogs.size() < 2) {
            clearDialogsAndLogout();

            throw new QBResponseException("The dialogs size doesn't enough for test: " +
                    "\nshould be more then 1" +
                    "\nactual size: " + dialogs.size());
        }

        if (UnreadMessagesUtils.calculateCountFromDialogs(dialogs) == 0) {
            throw new QBResponseException("The unread messages size doesn't enough for test:" +
                    "\nshould be more then 1" +
                    "\nactual size: " + UnreadMessagesUtils.calculateCountFromDialogs(dialogs));
        }

        this.dialogs.addAll(dialogs);
    }

    @Override
    protected void afterEachTest() throws Exception {
        clearDialogsAndLogout();
    }

    private void clearDialogsAndLogout() throws QBResponseException {
        dialogs.clear();
        logoutFromRest();
    }

    @Test
    public void getWithCorrectArrayIds() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        final int expectedUnreadMessagesCount = UnreadMessagesUtils.calculateCountFromDialogs(dialogs);

        getInstrumentation().runOnMainSync(() -> new ChatModule().getTotalUnreadMessagesCount(UnreadMessagesUtils.getIdsFromDialogs(dialogs), new ResultImpl() {
            @Override
            public void success(Object result) {
                List arrayDialogCounts = (List) ((Map) result).get("dialogsCount");

                int actualUnreadMessagesCount = UnreadMessagesUtils.getCountFromArrayOfMap(arrayDialogCounts);
                int totalCount = (int) ((Map) result).get("totalCount");

                assertEquals(expectedUnreadMessagesCount, actualUnreadMessagesCount);
                assertTrue(totalCount > 0);

                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void getWithCorrectOneId() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        final QBChatDialog dialog = dialogs.get(1);

        final int expectedUnreadMessagesCount = dialog.getUnreadMessageCount();

        final List<Object> dialogIds = new ArrayList<Object>() {{
            add(dialog.getDialogId());
        }};

        getInstrumentation().runOnMainSync(() -> new ChatModule().getTotalUnreadMessagesCount(dialogIds, new ResultImpl() {
            @Override
            public void success(Object result) {
                List arrayDialogCounts = (List) ((Map) result).get("dialogsCount");

                int actualUnreadMessagesCount = UnreadMessagesUtils.getCountFromArrayOfMap(arrayDialogCounts);
                int totalCount = (int) ((Map) result).get("totalCount");

                assertEquals(expectedUnreadMessagesCount, actualUnreadMessagesCount);
                assertTrue(totalCount > 0);

                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void getWithEmptyIds() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new ChatModule().getTotalUnreadMessagesCount(new ArrayList(), new ResultImpl() {
            @Override
            public void success(Object result) {
                List arrayDialogCounts = (List) ((Map) result).get("dialogsCount");

                int actualUnreadMessagesCount = UnreadMessagesUtils.getCountFromArrayOfMap(arrayDialogCounts);
                int totalCount = (int) ((Map) result).get("totalCount");

                assertEquals(0, actualUnreadMessagesCount);
                assertTrue(totalCount > 0);

                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void getWith_NULL_Ids() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new ChatModule().getTotalUnreadMessagesCount(null, new ResultImpl() {
            @Override
            public void success(Object result) {
                List arrayDialogCounts = (List) ((Map) result).get("dialogsCount");

                int actualUnreadMessagesCount = UnreadMessagesUtils.getCountFromArrayOfMap(arrayDialogCounts);
                int totalCount = (int) ((Map) result).get("totalCount");

                assertEquals(0, actualUnreadMessagesCount);
                assertTrue(totalCount > 0);

                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void getWithWrongIds() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new ChatModule().getTotalUnreadMessagesCount(UnreadMessagesUtils.buildWrongDialogIds(), new ResultImpl() {
            @Override
            public void success(Object o) {
                fail("expected: error, actual: resolve");
            }

            @Override
            public void error(String s, String s1, Object o) {
                assertNotNull(s);
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test(expected = RuntimeException.class)
    public void getWith_3Integer_Ids() {
        getInstrumentation().runOnMainSync(() -> new ChatModule().getTotalUnreadMessagesCount(UnreadMessagesUtils.build_3Integer_DialogIds(), new ResultImpl() {
            @Override
            public void success(Object o) {
                fail("expected: error, actual: resolve");
            }
        }));

        fail("expected: error, actual: resolve");
    }

    @Test(expected = RuntimeException.class)
    public void getWith_2String_1Integer_Ids() {
        getInstrumentation().runOnMainSync(() -> new ChatModule().getTotalUnreadMessagesCount(UnreadMessagesUtils.build_2String_1Integer_DialogIds(), new ResultImpl() {
            @Override
            public void success(Object o) {
                fail("expected: error, actual: resolve");
            }
        }));

        fail("expected: error, actual: resolve");
    }
}