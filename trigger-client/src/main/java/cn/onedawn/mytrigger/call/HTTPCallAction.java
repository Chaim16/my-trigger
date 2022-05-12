package cn.onedawn.mytrigger.call;

import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;
import cn.hutool.http.server.action.Action;
import cn.onedawn.mytrigger.excutor.ExecuteAndAck;
import cn.onedawn.mytrigger.excutor.TaskExecutor;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.CallRequest;
import cn.onedawn.mytrigger.utils.SpringBeanFactory;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;


/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName HTTPCallHandler.java
 * @Description TODO
 * @createTime 2021年10月29日 14:50:00
 */
public class HTTPCallAction implements Action {

    @SneakyThrows
    @Override
    public void doAction(HttpServerRequest request, HttpServerResponse response) {
        CallRequest callRequest = JSON.parseObject(request.getBody(), CallRequest.class);
        callRequest.check();

        Job job = callRequest.getJob();
        String callName = job.getCallName();

        Task task = (Task) SpringBeanFactory.getBean(callName);
        ExecuteAndAck executeAndAck = new ExecuteAndAck(task, callRequest);
        TaskExecutor.submit(executeAndAck);

        response.write("ok");
    }
}
