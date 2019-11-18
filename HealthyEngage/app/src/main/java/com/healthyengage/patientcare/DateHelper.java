package com.healthyengage.patientcare;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class DateHelper {
    public static String convertDateToString(Date date, String format) {
        String dateString = new SimpleDateFormat(format).format(date);
        return dateString;
    }
    public static Date convertStringToDate(String dateInString, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = formatter.parse(dateInString);
        return date;
    }

    public static String getDisplayFormat(String dateStr, String givenFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(givenFormat);
        Date date;
        try {
            date = formatter.parse(dateStr);
            SimpleDateFormat convertFormat = new SimpleDateFormat("dd-MMM-yyyy");
            return convertFormat.format(date);
        } catch (Throwable t) {
            return "";
        }
    }

    public static String getCurrentDate() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return currentDate;
    }

}
