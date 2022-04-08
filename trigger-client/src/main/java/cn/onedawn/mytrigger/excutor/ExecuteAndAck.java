package cn.onedawn.mytrigger.excutor;

import cn.onedawn.mytrigger.call.Task;
import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.request.impl.CallRequest;
import cn.onedawn.mytrigger.submit.RocketMQStrategy;
import lombok.SneakyThrows;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName ExcuteTask.java
 * @Description TODO 一个执行任务
 * @createTime 2021年10月29日 03:53:00
 */
public class ExecuteAndAck implements Runnable{
    private Task task;
    private CallRequest callRequest;

    public ExecuteAndAck(Task task, CallRequest callRequest) {
        this.task = task;
        this.callRequest = callRequest;
    }

    @SneakyThrows
    @Override
    public void run() {
        String str =  callRequest.getJob().getCallData();
        System.out.println(str);
        boolean success = task.run(callRequest.getJob().getCallData());
        if (!success) {
            throw new MyTriggerException("[call] task excute faild");
        }
        RocketMQStrategy.ack(callRequest.getJob().getId());
    }
}
