package utils;


import Enums.DateTimeEnum;
import org.joda.time.DateTime;

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
}
