package cn.onedawn.mytrigger.triggercenter.controller;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.App;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.*;
import cn.onedawn.mytrigger.response.Response;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import cn.onedawn.mytrigger.type.ResponseType;
import cn.onedawn.mytrigger.utils.ConstValue;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName JobController.java
 * @Description TODO
 * @createTime 2021年10月26日 10:18:00
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @RequestMapping("/register")
    public Response register(HttpServletRequest request) throws MyTriggerException {
        Response response = new Response();
        long start = System.currentTimeMillis();
        boolean result = false;
        Job job = null;
        try {
            String requestData = request.getParameter(ConstValue.REQUEST_DATA);
            RegisterRequest registerRequest = JSON.parseObject(requestData, RegisterRequest.class);
            registerRequest.check();
            job = registerRequest.getJob();
            result = jobService.register(job);
        } catch (ParseException | MyTriggerException e) {
            response.setSuccess(result);
            response.setInfo(JSON.toJSONString(null));
            long end = System.currentTimeMillis();
            response.setTime(end - start);
            throw new MyTriggerException("[register job] faild");
        }
        long end = System.currentTimeMillis();

        response.setSuccess(result)
                .setInfo(JSON.toJSONString(job))
                .setType(ResponseType.register)
                .setTime(end - start);
        return response;
    }

    @RequestMapping("/modify")
    public Response modify(HttpServletRequest request) throws MyTriggerException{
        Response response = new Response();
        long start = System.currentTimeMillis();
        Job job = null;
        boolean result = false;
        try {
            String requestData = request.getParameter(ConstValue.REQUEST_DATA);
            ModifyRequest modifyRequest = JSON.parseObject(requestData, ModifyRequest.class);
            modifyRequest.check();
            job = modifyRequest.getJob();

            result = jobService.modify(job);
        } catch (MyTriggerException e) {
            throw new MyTriggerException("[modify] job faild");
        }
        long end = System.currentTimeMillis();

        response.setSuccess(result)
                .setInfo(JSON.toJSONString(job))
                .setType(ResponseType.modify)
                .setTime(end - start);
        return response;
    }

    @RequestMapping("remove")
    public Response remove(HttpServletRequest request) throws MyTriggerException {
        Response response = new Response();
        long start = System.currentTimeMillis();
        String requestData = request.getParameter(ConstValue.REQUEST_DATA);

        RemoveRequest removeRequest = JSON.parseObject(requestData, RemoveRequest.class);
        removeRequest.check();
        Long jobId = removeRequest.getJobId();

        boolean result = jobService.remove(jobId);
        long end = System.currentTimeMillis();

        response.setSuccess(result)
                .setInfo(null)
                .setType(ResponseType.remove)
                .setTime(end - start);
        return response;
    }

    @RequestMapping("pause")
    public Response pause(HttpServletRequest request) throws MyTriggerException {
        Response response = new Response();
        long start = System.currentTimeMillis();
        String requestData = request.getParameter(ConstValue.REQUEST_DATA);

        PauseRequest pauseRequest = JSON.parseObject(requestData, PauseRequest.class);
        pauseRequest.check();

        Long jobId = pauseRequest.getJobId();
        boolean result = jobService.pause(jobId);
        long end = System.currentTimeMillis();

        response.setSuccess(result)
                .setInfo(null)
                .setType(ResponseType.pause)
                .setTime(end - start);
        return response;
    }

    @RequestMapping("findAllJobByApp")
    public Response findAllJob(HttpServletRequest request) {
        Response response = new Response();
        long start = System.currentTimeMillis();
        String requestData = request.getParameter(ConstValue.REQUEST_DATA);
        App app = JSON.parseObject(requestData, CommonRequest.class).getApp();
        List<Job> jobs = jobService.findAllJobByApp(app.getId());
        long end = System.currentTimeMillis();

        response.setSuccess(jobs == null)
                .setTime(end - start)
                .setInfo(JSON.toJSONString(jobs));
        return response;
    }

}
