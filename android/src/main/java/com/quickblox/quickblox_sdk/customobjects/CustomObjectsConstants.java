package com.quickblox.quickblox_sdk.customobjects;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class CustomObjectsConstants {

    ///////////////////////////////////////////////////////////////////////////
    // PERMISSIONS LEVELS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            PermissionLevels.OPEN,
            PermissionLevels.OWNER,
            PermissionLevels.OPEN_FOR_USER_IDS,
            PermissionLevels.OPEN_FOR_GROUPS
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface PermissionLevels {
        String OPEN = "open";
        String OWNER = "owner";
        String OPEN_FOR_USER_IDS = "open_for_users_ids";
        String OPEN_FOR_GROUPS = "open_for_groups";
    }

    ///////////////////////////////////////////////////////////////////////////
    // INTEGER SEARCH TYPES
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            IntegerSearchTypes.LT,
            IntegerSearchTypes.LTE,
            IntegerSearchTypes.GT,
            IntegerSearchTypes.GTE,
            IntegerSearchTypes.NE,
            IntegerSearchTypes.IN,
            IntegerSearchTypes.NIN,
            IntegerSearchTypes.OR

    })
    @Retention(RetentionPolicy.SOURCE)
    @interface IntegerSearchTypes {
        String LT = "lt";
        String LTE = "lte";
        String GT = "gt";
        String GTE = "gte";
        String NE = "ne";
        String IN = "in";
        String NIN = "nin";
        String OR = "or";
    }

    ///////////////////////////////////////////////////////////////////////////
    // FLOAT SEARCH TYPES
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            FloatSearchTypes.LT,
            FloatSearchTypes.LTE,
            FloatSearchTypes.GT,
            FloatSearchTypes.GTE,
            FloatSearchTypes.NE,
            FloatSearchTypes.IN,
            FloatSearchTypes.NIN,
            FloatSearchTypes.OR
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface FloatSearchTypes {
        String LT = "lt";
        String LTE = "lte";
        String GT = "gt";
        String GTE = "gte";
        String NE = "ne";
        String IN = "in";
        String NIN = "nin";
        String OR = "or";
    }

    ///////////////////////////////////////////////////////////////////////////
    // STRING SEARCH TYPES
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            StringSearchTypes.NE,
            StringSearchTypes.IN,
            StringSearchTypes.NIN,
            StringSearchTypes.OR,
            StringSearchTypes.CTN
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface StringSearchTypes {
        String NE = "ne";
        String IN = "in";
        String NIN = "nin";
        String OR = "or";
        String CTN = "ctn";
    }

    ///////////////////////////////////////////////////////////////////////////
    // BOOLEAN SEARCH TYPES
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            BooleanSearchTypes.NE
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface BooleanSearchTypes {
        String NE = "ne";
    }

    ///////////////////////////////////////////////////////////////////////////
    // ARRAY SEARCH TYPES
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            ArraySearchTypes.ALL
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface ArraySearchTypes {
        String ALL = "all";
    }

    ///////////////////////////////////////////////////////////////////////////
    // INTEGER UPDATE TYPES
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            IntegerUpdateTypes.INC
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface IntegerUpdateTypes {
        String INC = "inc";
    }

    ///////////////////////////////////////////////////////////////////////////
    // FLOAT UPDATE TYPES
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            FloatUpdateTypes.INC
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface FloatUpdateTypes {
        String INC = "inc";
    }

    ///////////////////////////////////////////////////////////////////////////
    // ARRAY UPDATE TYPES
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            ArrayUpdateTypes.PULL,
            ArrayUpdateTypes.PULL_ALL,
            ArrayUpdateTypes.POP,
            ArrayUpdateTypes.PUSH,
            ArrayUpdateTypes.ADD_TO_SET
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface ArrayUpdateTypes {
        String PULL = "PULL";
        String PULL_ALL = "PULL_ALL";
        String POP = "POP";
        String PUSH = "PUSH";
        String ADD_TO_SET = "ADD_TO_SET";
    }

    ///////////////////////////////////////////////////////////////////////////
    // PULL FILTERS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            PullFilters.LT,
            PullFilters.LTE,
            PullFilters.GT,
            PullFilters.GTE,
            PullFilters.NE,
            PullFilters.IN,
            PullFilters.NIN,
            PullFilters.OR
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface PullFilters {
        String LT = "lt";
        String LTE = "lte";
        String GT = "gt";
        String GTE = "gte";
        String NE = "ne";
        String IN = "in";
        String NIN = "nin";
        String OR = "or";
    }
}