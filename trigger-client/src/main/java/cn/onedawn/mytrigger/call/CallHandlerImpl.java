package cn.onedawn.mytrigger.call;

import cn.onedawn.mytrigger.api.CallHandler;
import cn.onedawn.mytrigger.excutor.ExecuteAndAck;
import cn.onedawn.mytrigger.excutor.TaskExecutor;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.CallRequest;
import cn.onedawn.mytrigger.response.Response;
import cn.onedawn.mytrigger.utils.SpringBeanFactory;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName DubboCallServiceImpl.java
 * @Description TODO
 * @createTime 2021年10月29日 04:51:00
 */
public class CallHandlerImpl implements CallHandler {
    @Override
    public Response handle(CallRequest callRequest) {
        long start = System.currentTimeMillis();
        Job job = callRequest.getJob();
        String callName = job.getCallName();
        Task task = (Task) SpringBeanFactory.getBean(callName);
        ExecuteAndAck executeAndAck = new ExecuteAndAck(task, callRequest);
        TaskExecutor.submit(executeAndAck);
        long end = System.currentTimeMillis();

        return new Response()
                .setSuccess(true)
                .setTime(end - start);
    }
}
