package com.quickblox.quickblox_sdk.chat.dialog;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.core.exception.QBResponseException;
import com.quickblox.quickblox_sdk.BaseTest;
import com.quickblox.quickblox_sdk.chat.ChatModule;
import com.quickblox.quickblox_sdk.chat.utils.ChatDialogUtils;
import com.quickblox.quickblox_sdk.chat.utils.CustomDataUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

/**
 * Created by Injoit on 2022-04-07.
 * Copyright © 2022 Quickblox. All rights reserved.
 */

@RunWith(AndroidJUnit4ClassRunner.class)
public class CreateDialogWithCustomDataTest extends BaseTest {
    private String dialogId;

    @Override
    protected void beforeEachTest() throws QBResponseException {
        dialogId = null;
        loginToRest();
    }

    @Override
    protected void afterEachTest() throws Exception {
        deleteDialogFromRest(dialogId);
        logoutFromRest();
    }

    @Test
    public void createDialogWithCorrectCustomData() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> uploadedCustomData = CustomDataUtils.buildCorrectCustomData();
        int uploadedCustomDataSize = uploadedCustomData.size();

        Map<String, Object> dialogData = ChatDialogUtils.buildPrivateDialog();
        dialogData.put("customData", uploadedCustomData);

        getInstrumentation().runOnMainSync(() -> new ChatModule().createDialog(dialogData, new ResultImpl() {
            @Override
            public void success(Object value) {
                dialogId = (String) ((Map<?, ?>) value).get("id");

                Map<?, ?> customDataMap = (Map<?, ?>) ((Map<?, ?>) value).get("customData");

                int downloadedCustomDataSize = customDataMap.size();

                assertEquals(uploadedCustomDataSize, downloadedCustomDataSize);

                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void createDialogWithEmptyCustomData() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> dialogData = ChatDialogUtils.buildPrivateDialog();
        dialogData.put("customData", new HashMap<String, Object>());

        getInstrumentation().runOnMainSync(() -> new ChatModule().createDialog(dialogData, new ResultImpl() {
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
    public void createDialogWithEmptyCustomDataButHasClassName() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> dialogData = ChatDialogUtils.buildPrivateDialog();
        dialogData.put("customData", CustomDataUtils.buildEmptyCustomDataButHasClassName());

        getInstrumentation().runOnMainSync(() -> new ChatModule().createDialog(dialogData, new ResultImpl() {
            @Override
            public void success(Object value) {
                dialogId = (String) ((Map<?, ?>) value).get("id");

                assertNotNull(value);

                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void createDialogWithWrongCustomData() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> dialogData = ChatDialogUtils.buildPrivateDialog();
        dialogData.put("customData", CustomDataUtils.buildWrongCustomData());

        getInstrumentation().runOnMainSync(() -> new ChatModule().createDialog(dialogData, new ResultImpl() {
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
    public void createDialogWith_3correct_1Wrong_CustomData() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> dialogData = ChatDialogUtils.buildPrivateDialog();
        dialogData.put("customData", CustomDataUtils.build_3correct_1WrongCustomData());

        getInstrumentation().runOnMainSync(() -> new ChatModule().createDialog(dialogData, new ResultImpl() {
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
}