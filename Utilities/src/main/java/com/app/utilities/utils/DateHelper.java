package com.app.utilities.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @Auther Dhaval Jivani
 */

public final class DateHelper {

    private static final String TAG = "DateHelper";

    private static final String LONG_DATE_FORMAT = "yyyy-MM-dd";

    public static final long MILLISECONDS_IN_SECOND = 1000;

    public static final long MILLISECONDS_IN_MINUTE = 1000 * 60;

    private static final long MILLISECONDS_IN_HOUR = 1000 * 60 * 60;

    private static final long MILLISECONDS_IN_DAY = 1000 * 3600 * 24;




    /**
     * @param dateStr
     * @param format
     * @return
     */
    public static java.util.Date stringToDate(String dateStr, String format) {

        if (StringHelper.isEmpty(dateStr) || StringHelper.isEmpty(format)) return null;

        Date d = null;
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            formater.setLenient(false);
            d = formater.parse(dateStr);
        } catch (Exception e) {
            // log.error(e);
            Log.e(TAG, "error in stringToDate() : " + e.getMessage());
            d = null;
        }
        return d;
    }

    /**
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date, String format) {
        String result = "";
        if (date == null || StringHelper.isEmpty(format)) return result;

        SimpleDateFormat formater = new SimpleDateFormat(format);

        try {
            result = formater.format(date);
        } catch (Exception e) {
            Log.e(TAG, "error in dateToString() : " + e.getMessage());
        }
        return result;
    }

    /**
     *
     * @param format
     * @return
     */
    public static String getCurrDate(String format) {
        return dateToString(new Date(), format);
    }

    public static long getcurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static long dateToMillis(String date, String dateFormat) {

        if (StringHelper.isEmpty(date) || StringHelper.isEmpty(dateFormat)) return 0;

        SimpleDateFormat f = new SimpleDateFormat(dateFormat);
        try {
            Date d = f.parse(date);
            return d.getTime();
        } catch (ParseException e) {
            Log.e(TAG, "error in dateToMillis() : " + e.getMessage());
        }
        return 0;
    }

    public static String millisToString(long longDate, String dateFormat) {
        try {
            Calendar calendar = Calendar.getInstance();
            //calendar.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            calendar.setTimeInMillis(longDate);
            SimpleDateFormat df2 = new SimpleDateFormat(dateFormat);
            return df2.format(calendar.getTime());
        } catch (Exception e) {
            Log.e(TAG, "error in millisToString() : " + e.getMessage());
        }
        return "";
    }
    public static long getCurrentDateInLong() {
        return System.currentTimeMillis();
    }
    public static int getToday() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    public static int getToMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getToYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long dayDiff(Date date1, Date date2) {
        return (date2.getTime() - date1.getTime()) / 86400000;
    }

    /**
     *
     * @param before
     * @param after
     * @return
     */
    public static int yearDiff(String before, String after) {
        Date beforeDay = stringToDate(before, LONG_DATE_FORMAT);
        Date afterDay = stringToDate(after, LONG_DATE_FORMAT);
        return getYear(afterDay) - getYear(beforeDay);
    }

    /**
     *
     * @param after
     * @return
     */
    public static int yearDiffCurr(String after) {
        Date beforeDay = new Date();
        Date afterDay = stringToDate(after, LONG_DATE_FORMAT);
        return getYear(beforeDay) - getYear(afterDay);
    }

    /**
     *
     * @param before
     * @return
     */
    public static long dayDiffCurr(String before) {
        Date currDate = stringToDate(getCurrDate(LONG_DATE_FORMAT), LONG_DATE_FORMAT);
        Date beforeDate = stringToDate(before, LONG_DATE_FORMAT);
        return (currDate.getTime() - beforeDate.getTime()) / 86400000;

    }

    /**
     *
     * @param date
     * @param day
     * @return
     */
    public static Date nextDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.DAY_OF_YEAR, day);
        return cal.getTime();
    }

    /**
     *
     * @param date
     * @param week
     * @return
     */
    public static Date nextWeek(Date date, int week) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.WEEK_OF_MONTH, week);
        return cal.getTime();
    }

    /**
     *
     * @param date
     * @param months
     * @return
     */
    public static Date nextMonth(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    /**
     *
     * @param year
     * @param month
     * @return
     */
    public static int getFirstWeekdayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        c.set(year, month - 1, 1);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     *
     * @param year
     * @param month
     * @return
     */
    public static int getLastWeekdayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        c.set(year, month - 1, getDaysOfMonth(year, month));
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDaysOfMonth(String year, String month) {
        int days = 0;
        if (month.equals("1") || month.equals("3") || month.equals("5")
                || month.equals("7") || month.equals("8") || month.equals("10")
                || month.equals("12")) {
            days = 31;
        } else if (month.equals("4") || month.equals("6") || month.equals("9")
                || month.equals("11")) {
            days = 30;
        } else {
            if ((Integer.parseInt(year) % 4 == 0 && Integer.parseInt(year) % 100 != 0)
                    || Integer.parseInt(year) % 400 == 0) {
                days = 29;
            } else {
                days = 28;
            }
        }

        return days;
    }

    /**
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     *
     * @param format
     * @return
     */
    public static String getFirstDayOfMonth(String format) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        return dateToString(cal.getTime(), format);
    }

    /**
     *
     * @param format
     * @return
     */
    public static String getLastDayOfMonth(String format) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        return dateToString(cal.getTime(), format);
    }

    /**
     *
     * @param dateOne
     * @param dateTwo
     * @return
     */
    public static boolean isSameDay(Date dateOne, Date dateTwo)
    {
        if ((dateOne == null) || (dateTwo == null))
        {
            return false;
        }

        long milliDiff = dateTwo.getTime() - dateOne.getTime();
        long milliDiffAbsolute = (milliDiff < 0) ? (milliDiff * -1) : milliDiff;

        if (milliDiffAbsolute > MILLISECONDS_IN_DAY)
        {
            // more than one day difference
            return false;
        }
        else
        {
            // less than 24 hours difference. more checking required
            Calendar calOne = GregorianCalendar.getInstance();
            calOne.setTime(dateOne);

            int calOneDate = calOne.get(Calendar.DATE);
            calOne.add(Calendar.MILLISECOND, (int) milliDiff);

            int calTwoDate = calOne.get(Calendar.DATE);

            return calOneDate == calTwoDate;
        }
    }

    /**
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isDay1BeforeDay2(Date date1, Date date2)
    {
        return !isSameDay(date1, date2) && (date1.getTime() < date2.getTime());
    }

    /**
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isDay1AfterDay2(Date date1, Date date2)
    {
        return !isSameDay(date1, date2) && (date1.getTime() > date2.getTime());
    }

    public static boolean isTimeValidate(String inTime, String outTime, String sdf_format) {
        Date startTime;
        Date endTime;
        SimpleDateFormat sdf = new SimpleDateFormat(sdf_format);
        try {
            startTime = sdf.parse(inTime);
            endTime = sdf.parse(outTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        if (startTime.equals(endTime)) {
            return false;
        }
        return !endTime.before(startTime);
    }

    public static String changeDateFormat(String date , String dateFormatter ,String newDateFormatter){
        if (!StringHelper.isEmpty(date)) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(dateFormatter);
                Date parsedDate = format.parse(date);
                SimpleDateFormat sdf1 = new SimpleDateFormat(newDateFormatter);
                return sdf1.format(parsedDate);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "";
    }

}
