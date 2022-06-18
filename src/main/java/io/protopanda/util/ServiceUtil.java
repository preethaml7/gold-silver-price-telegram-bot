package io.protopanda.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class ServiceUtil {
    public static String convertToIndianCurrencyFormat(String amount) {

        Locale indian = new Locale("en", "IN");
        NumberFormat indianFormat = NumberFormat.getCurrencyInstance(indian);

        return indianFormat.format(Double.parseDouble(amount)).replace("Rs.", "₹ ");
    }

    public static String getCurrentDateTimeInCentralTime() {

        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        formatDate.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
        return formatDate.format(new Date());
    }

    public static String getCurrentDateTimeInIndia() {

        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        formatDate.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return formatDate.format(new Date());
    }
}
