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
public class DeleteDialogTest extends BaseTest {

    @Override
    protected void beforeEachTest() throws Exception {
        loginToRest();
    }

    @Override
    protected void afterEachTest() throws Exception {
        logoutFromRest();
    }

    @Test
    public void deletePrivate() throws InterruptedException, QBResponseException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        String dialogId = buildPrivateDialogId();

        Map<String, Object> deleteDialogData = new HashMap<>();
        deleteDialogData.put("dialogId", dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().deleteDialog(deleteDialogData, new ResultImpl() {
            @Override
            public void success(Object value) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void deletePrivateWithForceFalse() throws InterruptedException, QBResponseException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        String dialogId = buildPrivateDialogId();

        Map<String, Object> deleteDialogData = new HashMap<>();
        deleteDialogData.put("dialogId", dialogId);
        deleteDialogData.put("force", false);

        getInstrumentation().runOnMainSync(() -> new ChatModule().deleteDialog(deleteDialogData, new ResultImpl() {
            @Override
            public void success(Object value) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    // TODO: 01.06.2022 need to fix because server always returns dialog which we can delete.
    /*@Test
    public void deletePrivateWithForceTrue() throws InterruptedException, QBResponseException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        String dialogId = buildPrivateDialogId();

        Map<String, Object> deleteDialogData = new HashMap<>();
        deleteDialogData.put("dialogId", dialogId);
        deleteDialogData.put("force", true);

        getInstrumentation().runOnMainSync(() -> new ChatModule().deleteDialog(deleteDialogData, new ResultImpl() {
            @Override
            public void success(Object value) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }*/

    @Test
    public void deleteGroup() throws InterruptedException, QBResponseException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        String dialogId = buildGroupDialogId();

        Map<String, Object> deleteDialogData = new HashMap<>();
        deleteDialogData.put("dialogId", dialogId);

        getInstrumentation().runOnMainSync(() -> new ChatModule().deleteDialog(deleteDialogData, new ResultImpl() {
            @Override
            public void success(Object value) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void deleteGroupWithForceTrue() throws InterruptedException, QBResponseException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        String dialogId = buildGroupDialogId();

        Map<String, Object> deleteDialogData = new HashMap<>();
        deleteDialogData.put("dialogId", dialogId);
        deleteDialogData.put("force", true);

        getInstrumentation().runOnMainSync(() -> new ChatModule().deleteDialog(deleteDialogData, new ResultImpl() {
            @Override
            public void success(Object value) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void deleteGroupWithForceFalse() throws InterruptedException, QBResponseException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        String dialogId = buildGroupDialogId();

        Map<String, Object> deleteDialogData = new HashMap<>();
        deleteDialogData.put("dialogId", dialogId);
        deleteDialogData.put("force", false);

        getInstrumentation().runOnMainSync(() -> new ChatModule().deleteDialog(deleteDialogData, new ResultImpl() {
            @Override
            public void success(Object value) {
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void deleteWithoutId() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> deleteDialogData = new HashMap<>();

        getInstrumentation().runOnMainSync(() -> new ChatModule().deleteDialog(deleteDialogData, new ResultImpl() {
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

    @Test
    public void deleteWith_NULL_Id() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> deleteDialogData = new HashMap<>();
        deleteDialogData.put("dialogId", null);

        getInstrumentation().runOnMainSync(() -> new ChatModule().deleteDialog(deleteDialogData, new ResultImpl() {
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

    @Test
    public void deleteWithEmptyId() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> deleteDialogData = new HashMap<>();
        deleteDialogData.put("dialogId", "");

        getInstrumentation().runOnMainSync(() -> new ChatModule().deleteDialog(deleteDialogData, new ResultImpl() {
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

    @Test
    public void deleteWithWrongId() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        Map<String, Object> deleteDialogData = new HashMap<>();
        deleteDialogData.put("dialogId", "wrong_dialog_id");

        getInstrumentation().runOnMainSync(() -> new ChatModule().deleteDialog(deleteDialogData, new ResultImpl() {
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
