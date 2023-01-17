package com.quickblox.quickblox_sdk.auth.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quickblox.auth.Consts;
import com.quickblox.auth.model.QBSessionWrap;
import com.quickblox.auth.session.QBSession;
import com.quickblox.core.RestMethod;
import com.quickblox.core.helper.Lo;
import com.quickblox.core.helper.SignHelper;
import com.quickblox.core.rest.RestRequest;
import com.quickblox.core.rest.RestResponse;
import com.quickblox.quickblox_sdk.BaseTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.SignatureException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class SessionUtils {
    private static final String API_ENDPOINT = "https://api.quickblox.com";

    private SessionUtils() {
        // private
    }

    public static String createApplicationSessionToken() throws MalformedURLException, SignatureException {
        RestRequest request = buildRequest();
        Lo.g(request.toString());

        RestResponse response = request.syncRequest();
        Lo.g(response.toString());

        String token = parseTokenFromResponse(response);
        return token;
    }

    private static RestRequest buildRequest() throws MalformedURLException, SignatureException {
        RestRequest request = new RestRequest();
        request.setMethod(RestMethod.POST);
        request.setUrl(buildURL());
        request.setParameters(buildParameters());

        String parametersInString = request.getParamsOnlyStringNotEncoded();
        String signature = buildSignature(parametersInString);
        request.getParameters().put(Consts.SIGNATURE, signature);

        return request;
    }

    private static URL buildURL() throws MalformedURLException {
        URL url = new URL(API_ENDPOINT + "/session.json");
        return url;
    }

    private static Map<String, Object> buildParameters() {
        Map<String, Object> parameters = new LinkedHashMap<>();

        parameters.put(Consts.APP_ID, BaseTest.APPLICATION_ID);
        parameters.put(Consts.AUTH_KEY, BaseTest.AUTHORIZATION_KEY);
        parameters.put(Consts.NONCE, new Random().nextInt());
        parameters.put(Consts.TIMESTAMP, System.currentTimeMillis() / 1000);

        return parameters;
    }

    private static String buildSignature(String parametersInString) throws SignatureException {
        String signature = SignHelper.calculateHMAC_SHA(parametersInString, BaseTest.AUTHORIZATION_SECRET);
        return signature;
    }

    private static String parseTokenFromResponse(RestResponse response) {
        String rawJson = response.getRawBody();
        Gson gson = new GsonBuilder().create();
        QBSessionWrap wrappedSession = gson.fromJson(rawJson, QBSessionWrap.class);
        QBSession session = wrappedSession.getSession();
        return session.getToken();
    }
}