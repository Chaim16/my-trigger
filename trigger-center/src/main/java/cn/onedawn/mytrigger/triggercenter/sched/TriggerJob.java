package cn.onedawn.mytrigger.triggercenter.sched;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import cn.onedawn.mytrigger.triggercenter.tasks.CallEnter;
import cn.onedawn.mytrigger.triggercenter.utils.ConstValue;
import cn.onedawn.mytrigger.utils.SpringBeanFactory;
import lombok.SneakyThrows;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName TriggerJob.java
 * @Description TODO
 * @createTime 2021年11月02日 18:08:00
 */
@Service
@DependsOn("beanService")
public class TriggerJob {

    private JobService jobService;
    private static final long schedTime = ConstValue.SCHED_TIME;

    public TriggerJob() {
        jobService = SpringBeanFactory.getBeanByType(JobService.class);

        Runnable runnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    try {
                        long start = System.currentTimeMillis();
                        processCore();
                        long end = System.currentTimeMillis();

                        long sleepTime = schedTime - (end - start);
                        if (sleepTime > 0) {
                          Thread.sleep(sleepTime);
                        }
                    } catch (InterruptedException e) {
                        throw new MyTriggerException(String.valueOf(e));
                    }
                }
            }

            /**
             * 获取任务并执行
             * @throws ExecutionException
             * @throws InterruptedException
             */
            private void processCore() throws ExecutionException, InterruptedException {
                do {
                    long start = System.currentTimeMillis();
                    // 获取要即将调度的任务
                    List<Job> jobs = CallEnter.selectTriggerJobs(jobService);
                    long end = System.currentTimeMillis();

                    // 日志提示时间

                    if (jobs.size() == 0) {
                        break;
                    }
                    start = System.currentTimeMillis();
                    List<Future> futures = CallEnter.execute(jobs);
                    for (Future future : futures) {
                        future.get();
                    }
                    end = System.currentTimeMillis();

                    // 日志提示时间

                } while (true);
            }
        };
        Thread triggerThread = new Thread(runnable);
        triggerThread.setDaemon(true);
        triggerThread.start();
    }
}
