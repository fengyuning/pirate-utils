

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {

    private static AtomicInteger count;

    private static final List<String> NO_CHECK_LIST = new ArrayList(){{
        add("timing-email-data");               //爬虫检查
        add("notice/timing_email_new");         //发送邮件
        add("refresh/notice-analytic-data");     //campain任务计算数据
    }};

    public static void main(String[] args) throws Exception {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

        cyclicBarrier.await();


    }


    public static void swap(List<Integer> list, int a, int b) {
        int num = list.get(a);
        list.set(a, list.get(b));
        list.set(b, num);
    }


}