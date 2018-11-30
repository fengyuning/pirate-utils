package utils;


import Enums.DateTimeEnum;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间相关工具类
 *
 * @author fyn
 * @version 1.0
 */
public class DateTimeUtil {
    private static final DateTime DATE_TIME = new DateTime();

    public static String dateTime() {
        return DATE_TIME.toString(DateTimeEnum.DATE_TIME.getStyle());
    }

    public static String date() {
        return DATE_TIME.toString(DateTimeEnum.DATE.getStyle());
    }

    public static Long timeStamp() {
        return DATE_TIME.getMillis();
    }

    public static String getLastDayOfMonth(String month) {
        if (!CheckUtil.isMonth(month)) {
            throw new IllegalArgumentException("month参数错误");
        }
        return new DateTime(month).dayOfMonth().withMaximumValue()
                .toString(DateTimeEnum.DATE.getStyle());
    }

    /**
     * 返回该月的每一天
     *
     * @param month yyyy-MM格式的字符串
     * @return
     */
    public static List<DateTime> getDateListByMonth(String month) {
        if (!CheckUtil.isMonth(month)) {
            throw new IllegalArgumentException("month参数有问题");
        }
        DateTime dateTime = DateTime.parse(month, DateTimeEnum.MONTH.getFormat());
        DateTime start = dateTime.dayOfMonth().withMinimumValue();
        DateTime end = dateTime.dayOfMonth().withMaximumValue();

        int days = end.getDayOfMonth() - start.getDayOfMonth();
        List<DateTime> dateList = new ArrayList<>();
        for (int i = 0; i <= days; i++) {
            dateList.add(start.plusDays(i));
        }
        return dateList;
    }

    /**
     * 用时间戳获取相差的时间间隔
     */
    public static int getDays(String startDate, String endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("startDate or endDate is null");
        }
        if (!CheckUtil.isDate(startDate)) {
            throw new IllegalArgumentException("startDate格式错误(应为yyyy-MM-dd)");
        }
        if (!CheckUtil.isDate(endDate)) {
            throw new IllegalArgumentException("endDate格式错误(应为yyyy-MM-dd)");
        }
        DateTime startTime = DateTime.parse(startDate, DateTimeEnum.DATE.getFormat());
        DateTime endTime = DateTime.parse(endDate, DateTimeEnum.DATE.getFormat());

        Long days = (endTime.getMillis() - startTime.getMillis()) / 86400000;
        return days.intValue() + 1;
    }

    public static void main(String[] args) {

        System.out.println(getDays("2017-11-01","2018-11-01"));
    }
}
