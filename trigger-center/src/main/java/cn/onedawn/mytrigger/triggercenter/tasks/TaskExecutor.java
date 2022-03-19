package cn.onedawn.mytrigger.triggercenter.tasks;


import cn.onedawn.mytrigger.threadpool.DiscardAndMessagePolicy;
import cn.onedawn.mytrigger.threadpool.NamedThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName TaskExecutor.java
 * @Description TODO
 * @createTime 2021年11月02日 21:50:00
 */
public class TaskExecutor {

    private static final int CORE_POOL_SIZE = 16;
    private static final int MAX_POOL_SIZE = 16;

    private static ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE,60, TimeUnit.SECONDS,
                    new LinkedBlockingQueue(5000),
                    new NamedThreadFactory("mytrigger-center"),
                    new DiscardAndMessagePolicy()
            );

    private static ThreadPoolExecutor threadRetryRunPoolExecutor =
            new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE,60, TimeUnit.SECONDS,
                    new LinkedBlockingQueue(5000),
                    new NamedThreadFactory("mytrigger-center-retry-run"),
                    new DiscardAndMessagePolicy()
            );

    private static ThreadPoolExecutor threadRetryCallErrorPoolExecutor =
            new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE,60, TimeUnit.SECONDS,
                    new LinkedBlockingQueue(5000),
                    new NamedThreadFactory("mytrigger-center-retry-callerror"),
                    new DiscardAndMessagePolicy()
            );

    private static ThreadPoolExecutor threadDeleteJobPoolExecutor =
            new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE,60, TimeUnit.SECONDS,
                    new LinkedBlockingQueue(5000),
                    new NamedThreadFactory("mytrigger-center-delete"),
                    new DiscardAndMessagePolicy()
            );

    private static ThreadPoolExecutor threadFindJobPoolExecutor =
            new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE,60, TimeUnit.SECONDS,
                    new LinkedBlockingQueue(5000),
                    new NamedThreadFactory("mytrigger-center"),
                    new DiscardAndMessagePolicy()
            );

    public static int getMaxPoolSize() {
        return MAX_POOL_SIZE;
    }

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public static ThreadPoolExecutor getThreadRetryRunPoolExecutor() {
        return threadRetryRunPoolExecutor;
    }

    public static ThreadPoolExecutor getThreadRetryCallErrorPoolExecutor() {
        return threadRetryCallErrorPoolExecutor;
    }

    public static ThreadPoolExecutor getThreadDeleteJobPoolExecutor() {
        return threadDeleteJobPoolExecutor;
    }


    public static ThreadPoolExecutor getThreadFindJobPoolExecutor() {
        return threadFindJobPoolExecutor;
    }
}
