package cn.onedawn.mytrigger.triggercenter.sched;

import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.threadpool.NamedThreadFactory;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import cn.onedawn.mytrigger.triggercenter.tasks.CallEnter;
import cn.onedawn.mytrigger.type.JobStatusType;
import cn.onedawn.mytrigger.utils.SpringBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName RetryRunJob.java
 * @Description TODO 定时重试调度之后没有ACK的任务（run）
 * @createTime 2021年12月21日 15:17:00
 */
@Service
@DependsOn("beanService")
public class RetryRunJob implements InitializingBean {

    Logger logger = LoggerFactory.getLogger(RetryRunJob.class);
    private JobService jobService;
    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("retry-run-thread"));
    private static int retryRunJobScheduleTime;

    @Value("${retry.run.job.schedule.time}")
    public void setRetryRunJobScheduleTime(int retryRunJobScheduleTime) {
        RetryRunJob.retryRunJobScheduleTime = retryRunJobScheduleTime;
    }

    private void updateJobStatus(List<Job> jobs, JobStatusType jobStatusType) {
        for (Job job : jobs) {
            logger.info("retry run job failed, now change the status to callError, jobId:{}", job.getId());
            job.setStatus(jobStatusType);
            jobService.modify(job);
        }
    }

    private void updateRunRetry(List<Job> jobs) {
        for (Job job : jobs) {
            logger.info("[retry run job], jobId:{}", job.getId());
            job.setRunRetry(1);
            jobService.modify(job);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("retry run job thread init");
        jobService = SpringBeanFactory.getBean(JobService.class);
        Runnable runnable = () -> {
            try {
                int retryCount;
                do {
                    long start, end;
                    start = System.currentTimeMillis();
                    List<Job> jobs = CallEnter.findRunJobs(jobService, false);
                    retryCount = jobs.size();
                    end = System.currentTimeMillis();
                    logger.info("retry run job thread get {} jobs, time consuming:{} ms", retryCount, end - start);

                    start = System.currentTimeMillis();
                    updateRunRetry(jobs);
                    List<Future> futures = CallEnter.execute(jobs);
                    for (Future future : futures) {
                        future.get();
                    }
                    end = System.currentTimeMillis();
                    logger.info("run jobs have been retried, time consuming: {} ms", end - start);

                    Thread.sleep(1000);
                    // 重试之后也失败的，状态改为 callerror
                    jobs = CallEnter.findRunJobs(jobService, true);
                    updateJobStatus(jobs, JobStatusType.callerror);
                } while (retryCount > 0);
            } catch (Exception e) {
                // 日志记录
                logger.error("retry run jobs failed, Exception: {}", e.getMessage());
            }
        };
        // 30分钟后重试
        int startTime = (int) (Math.random() * retryRunJobScheduleTime);
        executorService.scheduleAtFixedRate(runnable, startTime, retryRunJobScheduleTime, TimeUnit.SECONDS);
    }
}
