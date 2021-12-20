package cn.onedawn.mytrigger.triggercenter.task;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName SelectTriggerJobTask.java
 * @Description TODO
 * @createTime 2021年11月02日 23:05:00
 */
public class SelectTriggerJobTask extends AbstractSelectTask {
    @Override
    protected String getSelectSql() {
        return "select * from 任务 where status = wait and remove = false and trigger_time < now() order by trigger_time LIMIT 100";
    }
}
