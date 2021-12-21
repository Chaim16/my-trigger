package cn.onedawn.mytrigger.triggercenter.sched;

import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import cn.onedawn.mytrigger.triggercenter.tasks.CallEnter;
import cn.onedawn.mytrigger.triggercenter.utils.ConstValue;
import cn.onedawn.mytrigger.type.JobStatusType;
import cn.onedawn.mytrigger.utils.SpringBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName RetryRunJob.java
 * @Description TODO
 * @createTime 2021年12月21日 15:17:00
 */
@Service
@DependsOn("beanService")
public class RetryRunJob {

    Logger logger = LoggerFactory.getLogger(RetryRunJob.class);

    private JobService jobService;

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private static final int retryRunJobScheduleTime = ConstValue.RETRY_RUN_JOB_SCHEDULE_TIME;

    public RetryRunJob() {
        jobService = SpringBeanFactory.getBean(JobService.class);
        Runnable runnable = () -> {
            try {
                int retryCount;
                do {
                    retryCount = 0;
                    long start, end;
                    start = System.currentTimeMillis();
                    List<Job> jobs = CallEnter.findRunJobs(jobService, false);

                    retryCount = jobs.size();
                    end = System.currentTimeMillis();

                    start = System.currentTimeMillis();
                    updateRunRetry(jobs);
                    List<Future> futures = CallEnter.execute(jobs);
                    for (Future future : futures) {
                        future.get();
                    }
                    end = System.currentTimeMillis();

                    // 日志记录

                    Thread.sleep(1000);

                    // 重试之后也失败的，状态改为 callerror
                    jobs = CallEnter.findRunJobs(jobService, true);
                    updateJobStatus(jobs, JobStatusType.callerror);
                } while (retryCount > 0);
            } catch (Exception e) {
                // 日志记录
            }
        };
        // 30分钟后重试
        int startTime = (int)(Math.random() * retryRunJobScheduleTime);
        executorService.scheduleAtFixedRate(runnable, startTime, retryRunJobScheduleTime, TimeUnit.SECONDS);
    }

    private void updateJobStatus(List<Job> jobs, JobStatusType jobStatusType) {
        for (Job job : jobs) {
            job.setStatus(jobStatusType);
            jobService.modify(job);
        }
    }

    private void updateRunRetry(List<Job> jobs) {
        for (Job job : jobs) {
            job.setRunRetry(1);
            jobService.modify(job);
        }
    }

}
