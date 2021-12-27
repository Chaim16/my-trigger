package cn.onedawn.mytrigger.triggercenter.sched;

import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.threadpool.NamedThreadFactory;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import cn.onedawn.mytrigger.triggercenter.tasks.CallEnter;
import cn.onedawn.mytrigger.utils.SpringBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName RetryCallErrorJob.java
 * @Description TODO 定时重试调度失败的任务
 * @createTime 2021年12月21日 13:19:00
 */
@Service
@DependsOn("beanService")
public class RetryCallErrorJob implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(RetryCallErrorJob.class);
    private JobService jobService;
    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("retry-callerror-thread"));

    private static int retryCallErrorJobCountThreshold;
    private static int retryCallErrorJobScheduleTime;

    @Value("${retry.call.error.job.count.threshold}")
    public void setRetryCallErrorJobCountThreshold(int retryCallErrorJobCountThreshold) {
        RetryCallErrorJob.retryCallErrorJobCountThreshold = retryCallErrorJobCountThreshold;
    }

    @Value("${retry.call.error.job.schedule.time}")
    public void setRetryCallErrorJobScheduleTime(int retryCallErrorJobScheduleTime) {
        RetryCallErrorJob.retryCallErrorJobScheduleTime = retryCallErrorJobScheduleTime;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("retry callError job thread init");
        jobService = SpringBeanFactory.getBean(JobService.class);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    int retryCount = 0;
                    long start, end;
                    do {
                        List<Future> futures;
                        start = System.currentTimeMillis();
                        List<Job> jobs = CallEnter.findCallErrorJobs(jobService, retryCallErrorJobScheduleTime);
                        retryCount = jobs.size();
                        end = System.currentTimeMillis();
                        logger.info("retry callError job thread get {} jobs, time consuming:{} ms", retryCount, end - start);

                        start = System.currentTimeMillis();
                        futures = CallEnter.execute(jobs);
                        for (Future future : futures) {
                            future.get();
                        }
                        updateCallErrorJobRetryCount(jobs);
                        end = System.currentTimeMillis();
                        // 日志记录时间
                        logger.info("callError jobs have been retried, time consuming: {} ms", end - start);
                    } while (retryCount > 0);
                } catch (Exception e) {
                    // 日志记录
                    logger.error("retry callError jobs failed, Exception: {}", e.getMessage());
                }
            }

            private void updateCallErrorJobRetryCount(List<Job> jobs) {
                for (Job job : jobs) {
                    logger.info("[retry callError job], jobId:{}", job.getId());
                    int retryCount = job.getCallerrorRetryCount() + 1;
                    job.setCallerrorRetryCount(retryCount);
                    // 最多重试十次，还不成功就删除
                    if (retryCount > retryCallErrorJobCountThreshold) {
                        jobService.remove(job.getId());
                        // 日志记录
                        logger.error("find a job has retried 10 counts but failed, now remove it, jobId : {}", job.getId());
                    } else {
                        jobService.modify(job);
                    }
                }
            }
        };
        // 10分钟后重试
        int startTime = (int) (Math.random() * retryCallErrorJobScheduleTime);
        executorService.scheduleAtFixedRate(runnable, startTime, retryCallErrorJobScheduleTime, TimeUnit.SECONDS);
    }
}
