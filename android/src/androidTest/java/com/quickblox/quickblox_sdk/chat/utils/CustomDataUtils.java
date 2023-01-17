package com.quickblox.quickblox_sdk.chat.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Injoit on 2022-04-08.
 * Copyright © 2022 Quickblox. All rights reserved.
 */

public class CustomDataUtils {
    public static final String CUSTOM_DATA_CLASS_NAME = "FlutterCustomData";

    private CustomDataUtils() {
        //private
    }

    public static Map<String, Object> buildCorrectCustomData() {
        Map<String, Object> customData = new HashMap<>();

        customData.put("class_name", CUSTOM_DATA_CLASS_NAME);

        customData.put("customBoolean", true);
        customData.put("customInteger", 800);
        customData.put("customString", "test string from Android");
        customData.put("customFloat", 7.7f);
        customData.put("customArray", Arrays.asList("test 1", "test 2", "test 3"));

        return customData;
    }

    public static Map<String, Object> buildUpdatedCorrectCustomData() {
        Map<String, Object> customData = new HashMap<>();

        customData.put("class_name", CUSTOM_DATA_CLASS_NAME);

        customData.put("customBoolean", false);
        customData.put("customInteger", 700);
        customData.put("customString", "updated test string from Android " + System.currentTimeMillis());
        customData.put("customFloat", 8.8f);
        customData.put("customArray", Arrays.asList("updated test 1", "updated test 2", "updated test 3"));

        return customData;
    }

    public static Map<String, Object> buildWrongCustomData() {
        Map<String, Object> customData = new HashMap<>();

        customData.put("customString", null);

        return customData;
    }

    public static Map<String, Object> buildEmptyCustomDataButHasClassName() {
        Map<String, Object> customData = new HashMap<>();

        customData.put("class_name", CUSTOM_DATA_CLASS_NAME);

        return customData;
    }

    public static Map<String, Object> build_3correct_1WrongCustomData() {
        Map<String, Object> customData = new HashMap<>();

        customData.put("class_name", CUSTOM_DATA_CLASS_NAME);

        customData.put("customBoolean", false);
        customData.put("customInteger", 700);
        customData.put("customFloat", null);
        customData.put("customArray", Arrays.asList("updated test 1", "updated test 2", "updated test 3"));

        return customData;
    }
}
