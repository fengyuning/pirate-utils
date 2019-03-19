package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CallableDemo {

    public static void main(String[] args) {
        //创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(4);
        //创建有多个返回值的任务
        List<Future> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            myCallable callable = new myCallable();
            Future future = pool.submit(callable);
            list.add(future);
        }
        pool.shutdown();

        list.forEach(future -> {
            try {
                System.out.println(future.get().toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}

class myCallable implements Callable{
    @Override
    public Object call() throws Exception {
        return 1;
    }
}