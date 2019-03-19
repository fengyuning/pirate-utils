package utils;


import java.util.concurrent.*;

/**
 * 线程池工具
 *
 * @author fyn
 * @version 1.0
 */
public class ThreadPoolUtil {

    private static int DEFAULT_THREAD_NUM = 5;
    /**
     * 阿里巴巴强制不允许使用这种方式创建线程池
     * (1)FixedThreadPool和SingleThreadPool
     * 允许的请求队列的长度为Integer.MAX_VALUE,可能会大量堆积请求,导致OOM
     * (2)CachedTreadPool和ScheduledThreadPool
     * 允许创建Integer.MAX_VALUE个线程,可能会大量堆积线程,导致OOM
     * <p>
     * 主要就是避免直接使用Executors的默认实现
     */
    private static ExecutorService FIXED_THREAD_POOL = Executors.newFixedThreadPool(DEFAULT_THREAD_NUM);

    /**
     * 使用线程池的正确姿势
     */
    private static ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(
            5, 200,
            60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10));

    public static void destroy() {
        if (FIXED_THREAD_POOL != null) {
            FIXED_THREAD_POOL.shutdown();
        }
    }

    public static void execute(Runnable runnable) {
        FIXED_THREAD_POOL.execute(runnable);
    }

}
