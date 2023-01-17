package com.quickblox.quickblox_sdk.auth;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.QBSDK;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.SessionExpirationTimer;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.quickblox_sdk.BaseTest;
import com.quickblox.quickblox_sdk.auth.listeners.SessionListenerImpl;
import com.quickblox.quickblox_sdk.auth.utils.SessionUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4ClassRunner.class)
public class StartSessionWithTokenTest extends BaseTest {
    @Override
    protected void initCredentials() {
        QBSDK.initWithAppId(context, BaseTest.APPLICATION_ID, BaseTest.ACCOUNT_KEY);
    }

    @Override
    protected void beforeEachTest() {
        QBSessionManager.getInstance().deleteActiveSession();
    }

    @Override
    protected void afterEachTest() {
        try {
            QBAuth.deleteSession().perform();
        } catch (QBResponseException exception) {
            //need to ignore, because some tests expects error
        } finally {
            QBSessionManager.getInstance().removeListener(new SessionListenerImpl());
        }
    }

    @Test
    public void withWrongToken() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new AuthModule().startSessionWithToken("wrong_token", new ResultImpl() {
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
    public void withEmptyToken() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new AuthModule().startSessionWithToken("", new ResultImpl() {
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
    public void with_NULL_Token() throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);

        getInstrumentation().runOnMainSync(() -> new AuthModule().startSessionWithToken(null, new ResultImpl() {
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
    public void withCorrectToken() throws InterruptedException, MalformedURLException, SignatureException {
        CountDownLatch downLatch = new CountDownLatch(1);

        String createdToken = SessionUtils.createApplicationSessionToken();

        getInstrumentation().runOnMainSync(() -> new AuthModule().startSessionWithToken(createdToken, new ResultImpl() {
            @Override
            public void success(Object value) {
                checkSessionResult(((HashMap<?, ?>) value), createdToken);

                downLatch.countDown();
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void sessionExpiredListenerImmediately() throws InterruptedException, MalformedURLException, SignatureException {
        CountDownLatch downLatch = new CountDownLatch(1);

        String createdToken = SessionUtils.createApplicationSessionToken();

        QBSessionManager.getInstance().addListener(new SessionListenerImpl() {
            @Override
            public void onSessionExpired() {
                downLatch.countDown();
            }
        });

        getInstrumentation().runOnMainSync(() -> new AuthModule().startSessionWithToken(createdToken, new ResultImpl() {
            @Override
            public void success(Object value) {
                checkSessionResult(((HashMap<?, ?>) value), createdToken);

                Date tokenExpirationDate = QBSessionManager.getInstance().getTokenExpirationDate();
                Date expiredDate = new Date(tokenExpirationDate.getTime() - Duration.ofDays(1).toMillis());

                QBSessionManager.getInstance().createActiveSession(createdToken, expiredDate);

                boolean validSession = QBSessionManager.getInstance().isValidActiveSession();
                assertFalse(validSession);
            }
        }));

        downLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, downLatch.getCount());
    }

    @Test
    public void sessionExpiredListener_in_10_seconds() throws InterruptedException, MalformedURLException, SignatureException {
        CountDownLatch sessionDownLatch = new CountDownLatch(1);
        CountDownLatch expirationDownLatch = new CountDownLatch(1);

        String createdToken = SessionUtils.createApplicationSessionToken();

        QBSessionManager.getInstance().addListener(new SessionListenerImpl() {
            @Override
            public void onSessionExpired() {
                expirationDownLatch.countDown();
            }
        });

        getInstrumentation().runOnMainSync(() -> new AuthModule().startSessionWithToken(createdToken, new ResultImpl() {
            @Override
            public void success(Object value) {
                checkSessionResult(((HashMap<?, ?>) value), createdToken);

                createSessionWithExpiredTokenIn10Seconds(createdToken);

                boolean validSession = QBSessionManager.getInstance().isValidActiveSession();
                assertTrue(validSession);

                sessionDownLatch.countDown();
            }
        }));

        sessionDownLatch.await(10, TimeUnit.SECONDS);

        startTokenExpirationTimer();

        expirationDownLatch.await(10, TimeUnit.SECONDS);

        assertEquals(0, sessionDownLatch.getCount());
        assertEquals(0, expirationDownLatch.getCount());
    }

    private void checkSessionResult(HashMap<?, ?> result, String createdToken) {
        Integer applicationId = (Integer) result.get("applicationId");
        assertNotNull(applicationId);

        String expirationDate = (String) result.get("expirationDate");
        assertNotNull(expirationDate);

        String token = (String) result.get("token");
        assertNotNull(token);

        assertEquals(createdToken, token);
    }

    private void createSessionWithExpiredTokenIn10Seconds(String token) {
        Date tokenExpirationDate = QBSessionManager.getInstance().getTokenExpirationDate();
        Date expiredDate = buildExpirationDateIn10Seconds(tokenExpirationDate);
        QBSessionManager.getInstance().createActiveSession(token, expiredDate);
    }

    private Date buildExpirationDateIn10Seconds(Date tokenExpirationDate) {
        long hour_1 = Duration.ofHours(1).toMillis();
        long minutes_59 = Duration.ofMinutes(59).toMillis();
        long seconds_50 = Duration.ofSeconds(50).toMillis();

        long duration = hour_1 + minutes_59 + seconds_50;

        return new Date(tokenExpirationDate.getTime() - duration);
    }

    private void startTokenExpirationTimer() {
        long tokenFinishMilliseconds = QBSessionManager.getInstance().getTokenExpirationDate().getTime();
        long currentTimeMilliseconds = new Date().getTime();
        long timeLeftMilliseconds = tokenFinishMilliseconds - currentTimeMilliseconds;

        SessionExpirationTimer.start(timeLeftMilliseconds);
    }
}