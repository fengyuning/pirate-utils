package utils;


import org.joda.time.DateTime;

/**
 * 时间相关工具类
 *
 * @author fyn
 * @version 1.0
 */
public class DateTimeUtil {
    private static final DateTime DATE_TIME = new DateTime();

    private static final String DATE_STR = "yyyy-MM-dd";
    private static final String DATE_TIME_STR = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_REX = "[\\d]{4}-[\\d]{2}";

    public static String dateTime() {
        return DATE_TIME.toString(DATE_TIME_STR);
    }

    public static String date() {
        return DATE_TIME.toString(DATE_STR);
    }

    public static Long timeStamp() {
        return DATE_TIME.getMillis();
    }

    public static String getLastDayOfMonth(String month) {
        if (month == null) {
            throw new IllegalArgumentException("month is null");
        }
        if (!month.matches(DATE_REX)) {
            throw new IllegalArgumentException("month的格式不符合yyyy-MM");
        }
        return new DateTime(month).dayOfMonth().withMaximumValue().toString(DATE_STR);
    }
}
