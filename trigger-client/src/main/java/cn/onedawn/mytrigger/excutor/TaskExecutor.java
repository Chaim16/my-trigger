package cn.onedawn.mytrigger.excutor;

import cn.onedawn.mytrigger.threadpool.DiscardAndMessagePolicy;
import cn.onedawn.mytrigger.threadpool.NamedThreadFactory;

import java.util.concurrent.*;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName TaskExcutor.java
 * @Description TODO 存放了个线程池
 * @createTime 2021年10月29日 04:08:00
 */
public class TaskExecutor {

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            50, 100, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(5000),
            new NamedThreadFactory("my_trigger"),
            new DiscardAndMessagePolicy());

    public static void submit(Runnable task) {
        threadPoolExecutor.submit(task);
    }

}
