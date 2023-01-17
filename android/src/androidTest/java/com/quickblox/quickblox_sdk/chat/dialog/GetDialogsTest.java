package com.quickblox.quickblox_sdk.chat.dialog;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.core.exception.QBResponseException;
import com.quickblox.quickblox_sdk.BaseTest;
import com.quickblox.quickblox_sdk.chat.ChatModule;
import com.quickblox.quickblox_sdk.chat.utils.ChatFilterUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4ClassRunner.class)
public class GetDialogsTest extends BaseTest {
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
    public void get() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new ChatModule().getDialogs(null, new ResultImpl() {
            @Override
            public void success(Object o) {
                int dialogsSize = ((List<Object>) ((HashMap) o).get("dialogs")).size();
                assertTrue(dialogsSize > 0);
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void getOneByFilter() throws InterruptedException, QBResponseException {
        CountDownLatch downLatch = new CountDownLatch(1);

        dialogId = buildPrivateDialogId();

        getInstrumentation().runOnMainSync(() -> new ChatModule().getDialogs(ChatFilterUtils.buildFilterDialogId(dialogId), new ResultImpl() {
            @Override
            public void success(Object o) {
                int dialogsSize = ((List<Object>) ((HashMap) o).get("dialogs")).size();
                assertEquals(1, dialogsSize);
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }
}