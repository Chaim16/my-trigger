package cn.onedawn.mytrigger.triggercenter.sched;

import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.threadpool.NamedThreadFactory;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import cn.onedawn.mytrigger.utils.SpringBeanFactory;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName DeleteJob.java
 * @Description TODO 定时物理删除 Remove = 1 的任务，存放到ES中
 * @createTime 2021年12月26日 21:20:00
 */
@Service
@DependsOn("beanService")
public class DeleteJob implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(DeleteJob.class);
    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("delete-thread"));
    private JobService jobService;
    private static long deleteJobScheduleTime;

    @Value("${delete.job.schedule.time}")
    public void setDeleteJobScheduleTime(long deleteJobScheduleTime) {
        DeleteJob.deleteJobScheduleTime = deleteJobScheduleTime;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("delete job thread init");
        jobService = SpringBeanFactory.getBeanByType(JobService.class);
        Runnable runnable = () -> {
            try {
                int updateCount;
                long start, end;
                do {
                    start = System.currentTimeMillis();
                    updateCount = jobService.deleteRemoveJobs();
                    end = System.currentTimeMillis();
                    logger.info("delete-thread has deleted {} jobs, time consuming: {} ms", updateCount, end - start);
                } while (updateCount > 0);
            } catch (Exception e) {
                logger.error("delete removed jobs failed, Exception: {}", e.getMessage());
            }
        };
        int startTime = (int) (Math.random() * deleteJobScheduleTime);
//        executorService.scheduleAtFixedRate(runnable, startTime, deleteJobScheduleTime, TimeUnit.SECONDS);
    }
}
