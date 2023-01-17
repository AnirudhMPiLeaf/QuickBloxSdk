package com.quickblox.quickblox_sdk.webrtc;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.quickblox.quickblox_sdk.BaseTest;
import com.quickblox.quickblox_sdk.webrtc.utils.IceServerUtils;
import com.quickblox.videochat.webrtc.QBRTCConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import io.flutter.plugin.common.MethodCall;

@RunWith(AndroidJUnit4ClassRunner.class)
public class SetGetIceServersTest extends BaseTest {

    @Before
    public void before() {
        super.initContext();
        super.initCredentials();

        QBRTCConfig.setIceServerList(null);
    }

    @After
    public void after() {
        QBRTCConfig.setIceServerList(null);
    }

    @Test
    public void setCorrectIceServersTest() {
        MethodCall methodCall = new MethodCall(QBRTCConfigModule.SET_ICE_SERVERS_METHOD, IceServerUtils.buildCorrectIceServers());

        new QBRTCConfigModule().handleMethod(methodCall, new ResultImpl() {
            @Override
            public void success(Object o) {
                assertNull(o);
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void setWrongIceServersTest() {
        MethodCall methodCall = new MethodCall(QBRTCConfigModule.SET_ICE_SERVERS_METHOD, IceServerUtils.buildWrongIceServers());

        new QBRTCConfigModule().handleMethod(methodCall, new ResultImpl() {
            @Override
            public void success(Object o) {
                fail("expected: error, actual: success");
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void set_3_correct_1_wrong_IceServersTest() {
        MethodCall methodCall = new MethodCall(QBRTCConfigModule.SET_ICE_SERVERS_METHOD, IceServerUtils.build_3_correct_1_Wrong_IceServers());

        new QBRTCConfigModule().handleMethod(methodCall, new ResultImpl() {
            @Override
            public void success(Object o) {
                fail("expected: error, actual: success");
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void setEmptyIceServersTest() {
        MethodCall methodCall = new MethodCall(QBRTCConfigModule.SET_ICE_SERVERS_METHOD, new ArrayList<Map<String, String>>());

        new QBRTCConfigModule().handleMethod(methodCall, new ResultImpl() {
            @Override
            public void success(Object o) {
                fail("expected: error, actual: success");
            }
        });
    }

    @Test
    public void get_4_Default_IceServersTest() {
        MethodCall methodCall = new MethodCall(QBRTCConfigModule.GET_ICE_SERVERS_METHOD, null);

        new QBRTCConfigModule().handleMethod(methodCall, new ResultImpl() {
            @Override
            public void success(Object o) {
                List<Map<String, String>> servers = (List<Map<String, String>>) o;
                assertEquals(4, servers.size());
            }
        });
    }

    @Test
    public void setCorrectAndGetCorrectIceServersTest() {
        List<Map<String, String>> uploadedIceServers = IceServerUtils.buildCorrectIceServers();

        MethodCall setIceServersMethodCall = new MethodCall(QBRTCConfigModule.SET_ICE_SERVERS_METHOD, uploadedIceServers);

        new QBRTCConfigModule().handleMethod(setIceServersMethodCall, new ResultImpl() {
            @Override
            public void success(Object o) {
                assertNull(o);
            }
        });

        MethodCall getIceServersMethodCall = new MethodCall(QBRTCConfigModule.GET_ICE_SERVERS_METHOD, null);

        new QBRTCConfigModule().handleMethod(getIceServersMethodCall, new ResultImpl() {
            @Override
            public void success(Object o) {
                List<Map<String, String>> downloadedIceServers = (List<Map<String, String>>) o;
                assertFalse(downloadedIceServers.isEmpty());
                assertEquals(downloadedIceServers.size(), uploadedIceServers.size());
            }
        });
    }

    @Test
    public void setWrongAndGet_4_Default_IceServersTest() {
        CountDownLatch wrongCount = new CountDownLatch(1);

        MethodCall setIceServersMethodCall = new MethodCall(QBRTCConfigModule.SET_ICE_SERVERS_METHOD, IceServerUtils.buildWrongIceServers());

        new QBRTCConfigModule().handleMethod(setIceServersMethodCall, new ResultImpl() {
            @Override
            public void success(Object o) {
                fail("expected: error, actual: success");
            }

            @Override
            public void error(String s, String s1, Object o) {
                assertNotNull(s);
                wrongCount.countDown();
            }
        });

        assertEquals(0, wrongCount.getCount());

        MethodCall getIceServersMethodCall = new MethodCall(QBRTCConfigModule.GET_ICE_SERVERS_METHOD, null);

        new QBRTCConfigModule().handleMethod(getIceServersMethodCall, new ResultImpl() {
            @Override
            public void success(Object o) {
                List<Map<String, String>> servers = (List<Map<String, String>>) o;
                assertEquals(4, servers.size());
            }
        });
    }
}