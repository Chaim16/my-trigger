package cn.onedawn.mytrigger.triggercenter.sched;

import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import cn.onedawn.mytrigger.triggercenter.tasks.CallEnter;
import cn.onedawn.mytrigger.utils.SpringBeanFactory;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName TriggerJob.java
 * @Description TODO 持续扫表，拉起任务进行调度
 * @createTime 2021年11月02日 18:08:00
 */
@Service
@DependsOn("beanService")
public class TriggerJob implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(TriggerJob.class);

    private JobService jobService;
    private static long triggerScheduleTime;

    @Value("${trigger.schedule.time}")
    public void setTriggerScheduleTime(long triggerScheduleTime) {
        TriggerJob.triggerScheduleTime = triggerScheduleTime;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("trigger job thread init");
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

                        long sleepTime = triggerScheduleTime - (end - start);
                        if (sleepTime > 0) {
                            Thread.sleep(sleepTime);
                        }
                    } catch (InterruptedException e) {
                        // 日志记录
                        logger.error("trigger jobs failed, Exception: {}", e.getMessage());
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
                    List<Job> jobs = CallEnter.findTriggerJobs(jobService);
                    long end = System.currentTimeMillis();
                    // 日志提示时间
                    logger.info("trigger job thread get {} jobs, time consuming:{} ms", jobs.size(), end - start);

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
                    logger.info("[trigger jobs] {} jobs have been trigger, time consuming:{}", jobs.size(), end - start);
                } while (true);
            }
        };
        Thread triggerThread = new Thread(runnable, "trigger daemon thread");
        triggerThread.setDaemon(true);
        triggerThread.start();
    }
}
