package cn.onedawn.mytrigger.triggercenter.task;

import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.triggercenter.service.JobService;

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

/*
    public static List<Future> execute(List<Job> jobInfos) {
        ServiceExecuteThreadPool serviceExecuteThreadPool = (ServiceExecuteThreadPool) TCSpringBeanFactory.getBean("serviceExecuteThreadPool");
        int max = serviceExecuteThreadPool.getMaxPoolSize();

        List<List<JobInfo>> ll = new ArrayList<List<JobInfo>>(max);
        int i;

        for(i=0;i<max;i++) {
            ll.add(i, new ArrayList<JobInfo>());
        }

        i=0;
        for(JobInfo jobInfo : jobInfos) {
            if(i>=max) {
                i=0;
            }

            ll.get(i).add(jobInfo);
            i++;
        }


        List<Future> futures = new ArrayList<Future>();
        for(List<JobInfo> l : ll) {
            futures.add(serviceExecuteThreadPool.submit(new CallJobTask(l, false)));
        }
        return futures;
    }


    */
/**
     * 选择即将要调度的任务
     *//*

    public static List<Job> selectTriggerJobs(JobService jobService) throws ExecutionException, InterruptedException {
        Future submit = TaskExecutor.getThreadPoolExecutor().submit(new SelectTriggerJobTask());
        List<Job> jobs = (List<Job>) submit.get();
        return jobs;
    }
*/


}
