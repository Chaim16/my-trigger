package cn.onedawn.mytrigger.triggercenter.tasks;

import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import cn.onedawn.mytrigger.utils.SpringBeanFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName CallEnter.java
 * @Description TODO
 * @createTime 2021年11月02日 18:27:00
 */
public class CallEnter {

    /**
     * 获取即将要调度的任务
     */
    public static List<Job> findTriggerJobs(JobService jobService) throws ExecutionException, InterruptedException {
        Future submit = TaskExecutor.getThreadFindJobPoolExecutor()
                .submit(new FindTriggerJobTask(jobService));
        List<Job> jobs = (List<Job>) submit.get();
        return jobs;
    }


    /**
     * 获取要即将要重试的任务
     */
    public static List<Job> findCallErrorJobs(JobService jobService, int retryCallErrorJobCountThreshold) throws ExecutionException, InterruptedException {
        Future submit = TaskExecutor.getThreadRetryCallErrorPoolExecutor()
                .submit(new FindCallErrorJobTask(jobService, retryCallErrorJobCountThreshold));
        List<Job> jobs = (List<Job>) submit.get();
        return jobs;
    }

    /**
     * 获取正在进行调度，但未完成的任务
     * @param jobService
     * @return
     */
    public static List<Job> findRunJobs(JobService jobService, boolean retried) throws ExecutionException, InterruptedException {
        Future submit = TaskExecutor.getThreadRetryRunPoolExecutor()
                .submit(new FindRunJobTask(jobService, retried));
        List<Job> jobs = (List<Job>) submit.get();
        return jobs;
    }

    /**
     * 提交任务给线程池执行
     *
     * 先将任务平均分给线程池中的各个线程，然后再进行提交
     * @param jobInfos 任务列表
     * @return 执行结果
     */
    public static List<Future> execute(List<Job> jobInfos) {
        // 获取线程池
        ServiceExecuteThreadPool serviceExecuteThreadPool = (ServiceExecuteThreadPool) SpringBeanFactory.getBean("serviceExecuteThreadPool");
        int max = serviceExecuteThreadPool.getMaxPoolSize();

        // 每个线程分配一个任务列表
        List<List<Job>> allJobLists = new ArrayList<>(max);
        int i;

        for (i = 0; i < max; i++) {
            allJobLists.add(i, new ArrayList<Job>());
        }

        i = 0;
        for (Job jobInfo : jobInfos) {
            if (i >= max) {
                i = 0;
            }
            allJobLists.get(i).add(jobInfo);
            i++;
        }

        List<Future> futures = new ArrayList<>();
        for (List<Job> jobs : allJobLists) {
            futures.add(serviceExecuteThreadPool.submit(new CallJobTask(jobs, false)));
        }
        return futures;
    }



}
