package utils;

/**
 * 时间相关工具类
 *
 * @author fyn
 * @version 1.0
 */
public class CheckUtil {
    private static final String MONTH_REGEX = "[\\d]{4}-[\\d]{2}";
    private static final String DATE_REGEX = "[\\d]{4}-[\\d]{2}-[\\d]{2}";

    /**
     * 判断日期符不符合yyyy-MM-dd
     */
    public static boolean isDate(String date) {
        if (date == null || !date.matches(DATE_REGEX)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 判断日期符不符合yyyy-MM
     */
    public static boolean isMonth(String month) {
        if (month == null || !month.matches(MONTH_REGEX)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
