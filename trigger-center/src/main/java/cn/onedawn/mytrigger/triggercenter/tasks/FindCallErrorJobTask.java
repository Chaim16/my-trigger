package cn.onedawn.mytrigger.triggercenter.tasks;

import cn.onedawn.mytrigger.triggercenter.service.JobService;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName FindCallErrorJobTask.java
 * @Description TODO
 * @createTime 2021年12月21日 13:43:00
 */
public class FindCallErrorJobTask extends AbstractSelectTask{
    private int callerrorRetryCountThreshold;

    public FindCallErrorJobTask(JobService jobService, int callerrorRetryCountThreshold) {
        super(jobService);
        this.callerrorRetryCountThreshold = callerrorRetryCountThreshold;
    }
    @Override
    protected String getSelectSql() {
        return "select * from 任务 where status = 'callerror' and remove = 0 and callerror_retry_count < " + this.callerrorRetryCountThreshold + " and modify_time < now() order by trigger_time limit 10";
    }
}
