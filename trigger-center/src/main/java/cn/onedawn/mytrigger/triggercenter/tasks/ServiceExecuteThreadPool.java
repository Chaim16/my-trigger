package cn.onedawn.mytrigger.triggercenter.tasks;

import cn.onedawn.mytrigger.threadpool.NamedThreadFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName ServiceExecuteThreadPool.java
 * @Description TODO 执行任务的线程池以及监控
 * @createTime 2021年12月20日 15:57:00
 */
@Component(value = "serviceExecuteThreadPool")
public class ServiceExecuteThreadPool {
    private static final int DEFAULT_CORE_POOL_SIZE = 50;
    private static final int DEFAULT_MAX_POOL_SIZE = 50;
    private static final int DEFAULT_MONITOR_PERIOD = 60;

    private int corePoolSize;
    private int maxPoolSize;
    /** 监控时间间隔，单位s */
    private int monitorPeriod;

    private AtomicLong rejectedTaskCount = new AtomicLong();
    private AtomicBoolean initFlag = new AtomicBoolean(false);
    private ConcurrentMap<Long, AtomicLong> checkMap = new ConcurrentHashMap<>();

    private ThreadPoolExecutor threadPoolExecutor;
    private ScheduledExecutorService monitor;

    private Runnable monitorTask = new Runnable() {
        @Override
        public void run() {
            try {
                // 正在执行任务的线程数
                int activeCount = threadPoolExecutor.getActiveCount();
                // 已完成的任务数
                long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
                // 总任务数
                long totalTaskCount = threadPoolExecutor.getTaskCount();
                // 最大线程数
                int largestPoolSize = threadPoolExecutor.getLargestPoolSize();
                int poolSize = threadPoolExecutor.getPoolSize();
                // 监控日志记录

            } catch (Exception e) {
                // 监控日志记录
            }
        }
    };

    public ServiceExecuteThreadPool() {
        // 防止重复初始化
        if (!initFlag.compareAndSet(false, true)) {
            return;
        }
        corePoolSize = corePoolSize <= 0 ? DEFAULT_CORE_POOL_SIZE : corePoolSize;
        maxPoolSize = maxPoolSize <= 0 ? DEFAULT_MAX_POOL_SIZE : maxPoolSize;

        threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                5L,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(500),
                new NamedThreadFactory("ServiceProcessor"),
                new ThreadPoolExecutor.AbortPolicy());

        monitorPeriod = monitorPeriod <= 0 ? DEFAULT_MONITOR_PERIOD : monitorPeriod;
        // 一个线程来监控
        monitor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("solution-processor-monitor", true));
        monitor.scheduleAtFixedRate(monitorTask, monitorPeriod, monitorPeriod, TimeUnit.SECONDS);
    }

    public <T> Future <T> submit(Callable<T> task) {
        if (task == null) {
            throw new NullPointerException();
        }
        return threadPoolExecutor.submit(task);
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void destroy() {
        threadPoolExecutor.shutdown();
    }
}
