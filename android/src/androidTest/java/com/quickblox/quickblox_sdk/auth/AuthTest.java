package com.quickblox.quickblox_sdk.auth;

import android.text.TextUtils;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.quickblox_sdk.BaseTest;
import com.quickblox.quickblox_sdk.utils.DateUtil;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4ClassRunner.class)
public class AuthTest extends BaseTest {

    @Override
    protected void afterEachTest() throws QBResponseException {
        // TODO: 11.05.2022 need to find more better solution and understand why token is null in next tests after it test 
        logoutFromRest();
        QBAuth.createSession().perform();
        QBAuth.deleteSession().perform();
    }

    @Test
    public void setSessionTest() throws QBResponseException {
        QBUser user = buildUser();

        QBSession session = QBAuth.createSession(user).perform();

        String token = session.getToken();
        assertFalse(TextUtils.isEmpty(token));

        Date generatedTokenExpirationDate = DateUtil.generateFutureDate();
        QBSessionManager.getInstance().createActiveSession(token, generatedTokenExpirationDate);

        QBSession qbSession = QBSessionManager.getInstance().getActiveSession();
        qbSession.setUserId(BaseTest.USER_ID);
        qbSession.setAppId(Integer.valueOf(BaseTest.APPLICATION_ID));

        QBUsers.signIn(user).perform();

        Date loadedTokenExpirationDate = QBSessionManager.getInstance().getActiveSession().getTokenExpirationDate();

        assertNotNull(loadedTokenExpirationDate);

        assertNotEquals(generatedTokenExpirationDate, loadedTokenExpirationDate);
    }
}