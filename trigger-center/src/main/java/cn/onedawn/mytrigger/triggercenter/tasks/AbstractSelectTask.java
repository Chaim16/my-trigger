package cn.onedawn.mytrigger.triggercenter.tasks;

import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.triggercenter.service.JobService;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName AbstractSelectTask.java
 * @Description TODO
 * @createTime 2021年11月02日 22:53:00
 */
public abstract class AbstractSelectTask implements Callable {

    protected JobService jobService;

    public AbstractSelectTask() {
    }

    public AbstractSelectTask(JobService jobService) {
        this.jobService = jobService;
    }

    @Override
    public Object call() throws Exception {
        List<Job> jobs;
        String sql = getSelectSql();
        jobs = jobService.findJob(sql);
        return jobs;
    }

    protected abstract String getSelectSql();
}
