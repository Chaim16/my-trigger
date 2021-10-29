package cn.onedawn.mytrigger.excutor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName ThreadFactoryImpl.java
 * @Description TODO
 * @createTime 2021年10月29日 04:21:00
 */
public class ThreadFactoryImpl implements ThreadFactory {

    private static final AtomicInteger poolNumber = new AtomicInteger(1);

    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final String namePrefix;
    private final boolean daemon;

    public ThreadFactoryImpl(String namePrefix) {
        this(namePrefix, false);
    }

    public ThreadFactoryImpl(String namePrefix, boolean daemon) {
        SecurityManager securityManager = System.getSecurityManager();
        this.group = (securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = namePrefix + '-' + poolNumber.getAndIncrement() + "-thread-";
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        thread.setContextClassLoader(ThreadFactoryImpl.class.getClassLoader());
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.setDaemon(daemon);
        return thread;
    }
}
