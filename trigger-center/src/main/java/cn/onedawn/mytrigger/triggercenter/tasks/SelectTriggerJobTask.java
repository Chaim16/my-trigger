package cn.onedawn.mytrigger.triggercenter.tasks;

import cn.onedawn.mytrigger.triggercenter.service.JobService;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName SelectTriggerJobTask.java
 * @Description TODO
 * @createTime 2021年11月02日 23:05:00
 */
public class SelectTriggerJobTask extends AbstractSelectTask {

    public SelectTriggerJobTask(JobService jobService) {
        super(jobService);
    }

    public SelectTriggerJobTask() {}

    @Override
    protected String getSelectSql() {
        return "select * from 任务 where status = 'wait' and remove = 0 and trigger_time < now() order by trigger_time LIMIT 100";
    }
}
