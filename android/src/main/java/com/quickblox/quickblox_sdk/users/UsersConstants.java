package com.quickblox.quickblox_sdk.users;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class UsersConstants {

    private UsersConstants() {
        //empty
    }

    ///////////////////////////////////////////////////////////////////////////
    // SORTS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            Sorts.TYPE,
            Sorts.FIELD
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface Sorts {
        String TYPE = "type";
        String FIELD = "field";
    }

    ///////////////////////////////////////////////////////////////////////////
    // SORT TYPES
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            SortTypes.STRING,
            SortTypes.NUMBER,
            SortTypes.DATE
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface SortTypes {
        String STRING = "string";
        String NUMBER = "number";
        String DATE = "date";
    }

    ///////////////////////////////////////////////////////////////////////////
    // SORT FIELDS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            SortFields.ID,
            SortFields.FULL_NAME,
            SortFields.EMAIL,
            SortFields.LOGIN,
            SortFields.PHONE,
            SortFields.WEBSITE,
            SortFields.CREATED_AT,
            SortFields.UPDATED_AT,
            SortFields.LAST_REQUEST_AT,
            SortFields.EXTERNAL_USER_ID,
            SortFields.TWITTER_ID,
            SortFields.FACEBOOK_ID
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface SortFields {
        String ID = "id";
        String FULL_NAME = "full_name";
        String EMAIL = "email";
        String LOGIN = "login";
        String PHONE = "phone";
        String WEBSITE = "website";
        String CREATED_AT = "created_at";
        String UPDATED_AT = "updated_at";
        String LAST_REQUEST_AT = "last_request_at";
        String EXTERNAL_USER_ID = "external_user_id";
        String TWITTER_ID = "twitter_id";
        String FACEBOOK_ID = "facebook_id";
    }

    ///////////////////////////////////////////////////////////////////////////
    // FILTERS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            Filters.TYPE,
            Filters.FIELD,
            Filters.OPERATOR
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface Filters {
        String TYPE = "type";
        String FIELD = "field";
        String OPERATOR = "operator";
    }

    ///////////////////////////////////////////////////////////////////////////
    // FILTER TYPES
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            FilterTypes.STRING,
            FilterTypes.NUMBER,
            FilterTypes.DATE
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface FilterTypes {
        String STRING = "string";
        String NUMBER = "number";
        String DATE = "date";
    }

    ///////////////////////////////////////////////////////////////////////////
    // FILTER FIELDS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            FilterFields.ID,
            FilterFields.FULL_NAME,
            FilterFields.EMAIL,
            FilterFields.LOGIN,
            FilterFields.PHONE,
            FilterFields.WEBSITE,
            FilterFields.CREATED_AT,
            FilterFields.UPDATED_AT,
            FilterFields.LAST_REQUEST_AT,
            FilterFields.EXTERNAL_USER_ID,
            FilterFields.TWITTER_ID,
            FilterFields.FACEBOOK_ID
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface FilterFields {
        String ID = "id";
        String FULL_NAME = "full_name";
        String EMAIL = "email";
        String LOGIN = "login";
        String PHONE = "phone";
        String WEBSITE = "website";
        String CREATED_AT = "created_at";
        String UPDATED_AT = "updated_at";
        String LAST_REQUEST_AT = "last_request_at";
        String EXTERNAL_USER_ID = "external_user_id";
        String TWITTER_ID = "twitter_id";
        String FACEBOOK_ID = "facebook_id";
    }

    ///////////////////////////////////////////////////////////////////////////
    // FILTER OPERATORS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            FilterOperators.GT,
            FilterOperators.LT,
            FilterOperators.GE,
            FilterOperators.LE,
            FilterOperators.EQ,
            FilterOperators.NE,
            FilterOperators.BETWEEN,
            FilterOperators.IN
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface FilterOperators {
        String GT = "gt";
        String LT = "lt";
        String GE = "ge";
        String LE = "le";
        String EQ = "eq";
        String NE = "ne";
        String BETWEEN = "between";
        String IN = "in";
    }

    static String getAscDesc(boolean ascendingValue) {
        String ascDesc = "asc";
        if (!ascendingValue) {
            ascDesc = "desc";
        }
        return ascDesc;
    }
}