package com.quickblox.quickblox_sdk.chat;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
public class ChatConstants {

    private ChatConstants() {
        //empty
    }

    ///////////////////////////////////////////////////////////////////////////
    // DIALOG TYPES
    ///////////////////////////////////////////////////////////////////////////
    @IntDef({
            DialogTypes.PUBLIC_CHAT,
            DialogTypes.GROUP_CHAT,
            DialogTypes.CHAT
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface DialogTypes {
        int PUBLIC_CHAT = 1;
        int GROUP_CHAT = 2;
        int CHAT = 3;
    }

    ///////////////////////////////////////////////////////////////////////////
    // EVENTS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            Events.CONNECTED,
            Events.CONNECTION_CLOSED,
            Events.RECONNECTION_FAILED,
            Events.RECONNECTION_SUCCESSFUL,
            Events.RECEIVED_NEW_MESSAGE,
            Events.RECEIVED_SYSTEM_MESSAGE,
            Events.MESSAGE_DELIVERED,
            Events.MESSAGE_READ,
            Events.USER_IS_TYPING,
            Events.USER_STOPPED_TYPING
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Events {
        String CONNECTED = ChatModule.CHANNEL_NAME + "/CONNECTED";
        String CONNECTION_CLOSED = ChatModule.CHANNEL_NAME + "/CONNECTION_CLOSED";
        String RECONNECTION_FAILED = ChatModule.CHANNEL_NAME + "/RECONNECTION_FAILED";
        String RECONNECTION_SUCCESSFUL = ChatModule.CHANNEL_NAME + "/RECONNECTION_SUCCESSFUL";
        String RECEIVED_NEW_MESSAGE = ChatModule.CHANNEL_NAME + "/RECEIVED_NEW_MESSAGE";
        String RECEIVED_SYSTEM_MESSAGE = ChatModule.CHANNEL_NAME + "/RECEIVED_SYSTEM_MESSAGE";
        String MESSAGE_DELIVERED = ChatModule.CHANNEL_NAME + "/MESSAGE_DELIVERED";
        String MESSAGE_READ = ChatModule.CHANNEL_NAME + "/MESSAGE_READ";
        String USER_IS_TYPING = ChatModule.CHANNEL_NAME + "/USER_IS_TYPING";
        String USER_STOPPED_TYPING = ChatModule.CHANNEL_NAME + "/USER_STOPPED_TYPING";
    }

    public static List<String> getAllEvents() {
        List<String> events = new ArrayList<>();

        events.add(Events.CONNECTED);
        events.add(Events.CONNECTION_CLOSED);
        events.add(Events.RECONNECTION_FAILED);
        events.add(Events.RECONNECTION_SUCCESSFUL);
        events.add(Events.RECEIVED_NEW_MESSAGE);
        events.add(Events.RECEIVED_SYSTEM_MESSAGE);
        events.add(Events.MESSAGE_DELIVERED);
        events.add(Events.MESSAGE_READ);
        events.add(Events.USER_IS_TYPING);
        events.add(Events.USER_STOPPED_TYPING);

        return events;
    }

    ///////////////////////////////////////////////////////////////////////////
    // DIALOG SORTS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            DialogSorts.LAST_MESSAGE_DATE_SENT
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface DialogSorts {
        String LAST_MESSAGE_DATE_SENT = "last_message_date_sent";
    }

    ///////////////////////////////////////////////////////////////////////////
    // MESSAGE SORTS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            MessageSorts.DATE_SENT
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface MessageSorts {
        String DATE_SENT = "date_sent";
    }

    ///////////////////////////////////////////////////////////////////////////
    // MESSAGE FILTERS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            MessageFilters.FIELD,
            MessageFilters.OPERATOR
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface MessageFilters {
        String FIELD = "field";
        String OPERATOR = "operator";
    }

    ///////////////////////////////////////////////////////////////////////////
    // MESSAGE FILTER FIELDS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            MessageFilterFields.ID,
            MessageFilterFields.BODY,
            MessageFilterFields.DATE_SENT,
            MessageFilterFields.SENDER_ID,
            MessageFilterFields.RECIPIENT_ID,
            MessageFilterFields.ATTACHMENTS_TYPE,
            MessageFilterFields.UPDATED_AT
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface MessageFilterFields {
        String ID = "_id";
        String BODY = "message";
        String DATE_SENT = "date_sent";
        String SENDER_ID = "sender_id";
        String RECIPIENT_ID = "recipient_id";
        String ATTACHMENTS_TYPE = "attachments_type";
        String UPDATED_AT = "updated_at";
    }

    ///////////////////////////////////////////////////////////////////////////
    // MESSAGE FILTER OPERATORS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            MessageFilterOperators.LT,
            MessageFilterOperators.LTE,
            MessageFilterOperators.GT,
            MessageFilterOperators.GTE,
            MessageFilterOperators.NE,
            MessageFilterOperators.IN,
            MessageFilterOperators.NIN,
            MessageFilterOperators.OR,
            MessageFilterOperators.CTN,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface MessageFilterOperators {
        String LT = "lt";
        String LTE = "lte";
        String GT = "gt";
        String GTE = "gte";
        String NE = "ne";
        String IN = "in";
        String NIN = "nin";
        String OR = "or";
        String CTN = "ctn";
    }

    ///////////////////////////////////////////////////////////////////////////
    // DIALOG FILTERS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            DialogFilters.FIELD,
            DialogFilters.OPERATOR
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface DialogFilters {
        String FIELD = "field";
        String OPERATOR = "operator";
    }

    ///////////////////////////////////////////////////////////////////////////
    // DIALOG FILTER FIELDS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            DialogFilterFields.ID,
            DialogFilterFields.TYPE,
            DialogFilterFields.NAME,
            DialogFilterFields.LAST_MESSAGE_DATE_SENT,
            DialogFilterFields.CREATED_AT,
            DialogFilterFields.UPDATED_AT
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface DialogFilterFields {
        String ID = "_id";
        String TYPE = "type";
        String NAME = "name";
        String LAST_MESSAGE_DATE_SENT = "last_message_date_sent";
        String CREATED_AT = "created_at";
        String UPDATED_AT = "updated_at";
    }

    ///////////////////////////////////////////////////////////////////////////
    // DIALOG FILTER OPERATORS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({DialogFilterOperators.LT,
            DialogFilterOperators.LTE,
            DialogFilterOperators.GT,
            DialogFilterOperators.GTE,
            DialogFilterOperators.NE,
            DialogFilterOperators.IN,
            DialogFilterOperators.NIN,
            DialogFilterOperators.ALL,
            DialogFilterOperators.CTN
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface DialogFilterOperators {
        String LT = "lt";
        String LTE = "lte";
        String GT = "gt";
        String GTE = "gte";
        String NE = "ne";
        String IN = "in";
        String NIN = "nin";
        String ALL = "all";
        String CTN = "ctn";
    }
}