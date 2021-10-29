package cn.onedawn.mytrigger.call;

import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;
import cn.onedawn.mytrigger.excutor.ExecuteAndAck;
import cn.onedawn.mytrigger.excutor.TaskExecutor;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.CallRequest;
import cn.onedawn.mytrigger.response.Response;
import cn.onedawn.mytrigger.type.ResponseType;
import cn.onedawn.mytrigger.utils.ConstValue;
import cn.onedawn.mytrigger.utils.SpringBeanFactory;
import com.alibaba.fastjson.JSON;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName HTTPCallHandler.java
 * @Description TODO
 * @createTime 2021年10月29日 14:50:00
 */
public class HTTPCallHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpServerRequest request = new HttpServerRequest(httpExchange);

        String requestData = request.getParam(ConstValue.REQUEST_DATA);
        System.out.println(requestData);
        CallRequest callRequest = JSON.parseObject(requestData, CallRequest.class);
        Job job = callRequest.getJob();
        String callName = job.getCallName();

        Task task = (Task) SpringBeanFactory.getBean(callName);
        ExecuteAndAck executeAndAck = new ExecuteAndAck(task, callRequest);
        TaskExecutor.submit(executeAndAck);

        HttpServerResponse httpServerResponse = new HttpServerResponse(httpExchange);
        httpServerResponse.write("");

    }
}
