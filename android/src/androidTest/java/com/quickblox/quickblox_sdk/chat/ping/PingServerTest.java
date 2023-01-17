package com.quickblox.quickblox_sdk.chat.ping;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.quickblox_sdk.BaseTest;
import com.quickblox.quickblox_sdk.chat.ChatModule;

import org.jivesoftware.smack.SmackException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4ClassRunner.class)
public class PingServerTest extends BaseTest {

    @Override
    protected void beforeEachTest() throws Exception {
        loginToChat();
    }

    @Override
    protected void afterEachTest() throws Exception {
        logoutFromChat();
    }

    @Test
    public void ping() throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new ChatModule().pingServer(new ResultImpl() {
            @Override
            public void success(Object result) {
                assertNotNull(result);
                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void pingWithNotConnectedChat() throws InterruptedException, SmackException.NotConnectedException {
        final CountDownLatch downLatch = new CountDownLatch(1);

        logoutFromChat();

        getInstrumentation().runOnMainSync(() -> new ChatModule().pingServer(new ResultImpl() {
            @Override
            public void success(Object result) {
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
