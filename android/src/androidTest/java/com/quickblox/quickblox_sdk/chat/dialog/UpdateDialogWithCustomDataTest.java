package com.quickblox.quickblox_sdk.chat.dialog;

import android.text.TextUtils;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.quickblox_sdk.BaseTest;
import com.quickblox.quickblox_sdk.chat.ChatModule;
import com.quickblox.quickblox_sdk.chat.utils.ChatDialogUtils;
import com.quickblox.quickblox_sdk.chat.utils.CustomDataUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
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
public class UpdateDialogWithCustomDataTest extends BaseTest {
    private String dialogId;

    @Override
    protected void beforeEachTest() throws Exception {
        dialogId = null;
        loginToRest();

        QBChatDialog dialog = DialogUtils.buildDialog("Flutter Updated dialog", QBDialogType.PRIVATE,
                new ArrayList<Integer>() {{
                    add(ChatDialogUtils.QWE_11);
                    add(ChatDialogUtils.QWE_44);
                }});

        QBChatDialog chatDialog = QBRestChatService.createChatDialog(dialog).perform();

        dialogId = chatDialog.getDialogId();
    }

    @Override
    protected void afterEachTest() throws Exception {
        deleteDialogFromRest(dialogId);
        logoutFromRest();
    }

    @Test
    public void updateDialogWithCorrectCustomData() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> uploadedCustomData = CustomDataUtils.buildUpdatedCorrectCustomData();
        int uploadedCustomDataSize = uploadedCustomData.size();
        String uploadedValue = (String) uploadedCustomData.get("customString");

        Map<String, Object> updatedDialogData = ChatDialogUtils.buildPrivateDialog(dialogId);
        updatedDialogData.put("customData", uploadedCustomData);

        getInstrumentation().runOnMainSync(() -> new ChatModule().updateDialog(updatedDialogData, new ResultImpl() {
            @Override
            public void success(Object value) {
                Map<?, ?> customDataMap = (Map<?, ?>) ((Map<?, ?>) value).get("customData");

                int downloadedCustomDataSize = customDataMap.size();

                assertEquals(uploadedCustomDataSize, downloadedCustomDataSize);

                String downloadedValue = (String) customDataMap.get("customString");

                assertEquals(uploadedValue, downloadedValue);

                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void updateDialogWithEmptyCustomData() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> dialogData = ChatDialogUtils.buildPrivateDialog(dialogId);
        dialogData.put("customData", new HashMap<String, Object>());

        getInstrumentation().runOnMainSync(() -> new ChatModule().updateDialog(dialogData, new ResultImpl() {
            @Override
            public void success(Object o) {
                fail("expected: error, actual: resolve");
            }

            @Override
            public void error(String errorMessage, String s1, Object o) {
                assertFalse(TextUtils.isEmpty(errorMessage));
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void updateDialogWithEmptyCustomDataButHasClassName() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> dialogData = ChatDialogUtils.buildPrivateDialog(dialogId);
        dialogData.put("customData", CustomDataUtils.buildEmptyCustomDataButHasClassName());

        getInstrumentation().runOnMainSync(() -> new ChatModule().updateDialog(dialogData, new ResultImpl() {
            @Override
            public void success(Object o) {
                assertNotNull(o);
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void updateDialogWithWrongCustomData() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> dialogData = ChatDialogUtils.buildPrivateDialog(dialogId);
        dialogData.put("customData", CustomDataUtils.buildWrongCustomData());

        getInstrumentation().runOnMainSync(() -> new ChatModule().updateDialog(dialogData, new ResultImpl() {
            @Override
            public void success(Object o) {
                fail("expected: error, actual: resolve");
            }

            @Override
            public void error(String errorMessage, String s1, Object o) {
                assertFalse(TextUtils.isEmpty(errorMessage));
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void updateDialogWith_3correct_1Wrong_CustomData() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> dialogData = ChatDialogUtils.buildPrivateDialog(dialogId);
        dialogData.put("customData", CustomDataUtils.build_3correct_1WrongCustomData());

        getInstrumentation().runOnMainSync(() -> new ChatModule().updateDialog(dialogData, new ResultImpl() {
            @Override
            public void success(Object o) {
                fail("expected: error, actual: resolve");
            }

            @Override
            public void error(String errorMessage, String s1, Object o) {
                assertFalse(TextUtils.isEmpty(errorMessage));
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }
}