package com.quickblox.quickblox_sdk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
public class DateUtil {
    private static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private DateUtil() {
        // empty
    }

    public static String convertDateToISO(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(ISO_DATE_FORMAT, Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String convertedDate = dateFormat.format(date);
        return convertedDate;
    }

    public static Date convertRawToDateISO(String raw) {
        Date expirationDate = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO_DATE_FORMAT, Locale.ENGLISH);
            String modifiedRaw = raw.replaceAll("\\+0([0-9]):00", "+0$100");
            expirationDate = simpleDateFormat.parse(modifiedRaw);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return expirationDate;
    }

    public static Date generateFutureDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 10);

        Date futureDate = calendar.getTime();

        return futureDate;
    }
}