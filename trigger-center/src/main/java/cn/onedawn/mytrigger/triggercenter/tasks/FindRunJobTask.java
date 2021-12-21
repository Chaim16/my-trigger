package cn.onedawn.mytrigger.triggercenter.tasks;

import cn.onedawn.mytrigger.triggercenter.service.JobService;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName FindRunJobTask.java
 * @Description TODO
 * @createTime 2021年12月21日 15:28:00
 */
public class FindRunJobTask extends AbstractSelectTask{

    private boolean retried;

    public FindRunJobTask(JobService jobService, boolean retried) {
        super(jobService);
        this.retried = retried;
    }

    @Override
    protected String getSelectSql() {
        return "select * from 任务 where status = 'run' and remove = 0 and modify_time < now() and run_retry = " + (retried ? 1 : 0);
    }
}
