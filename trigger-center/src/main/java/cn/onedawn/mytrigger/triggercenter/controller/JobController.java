package cn.onedawn.mytrigger.triggercenter.controller;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.Application;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.*;
import cn.onedawn.mytrigger.response.Response;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import cn.onedawn.mytrigger.type.ResponseType;
import cn.onedawn.mytrigger.utils.ConstValue;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger logger = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobService jobService;

    @RequestMapping("/register")
    public Response register(HttpServletRequest request) {
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
            logger.error("[register job by http] failed, jobId:{}", job.getId());
        }
        long end = System.currentTimeMillis();

        response.setSuccess(result)
                .setInfo(JSON.toJSONString(job))
                .setType(ResponseType.register)
                .setTime(end - start);
        logger.info("[register job by http] success, jobId:{}, time consuming: {} ms", job.getId(), response.getTime());
        return response;
    }

    @RequestMapping("/modify")
    public Response modify(HttpServletRequest request) {
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
            logger.info("[modify job by http] failed, jobId:{}", job.getId());
        }
        long end = System.currentTimeMillis();

        response.setSuccess(result)
                .setInfo(JSON.toJSONString(job))
                .setType(ResponseType.modify)
                .setTime(end - start);
        logger.info("[modify job by http] success, jobId:{}, time consuming: {} ms", job.getId(), response.getTime());
        return response;
    }

    @RequestMapping("remove")
    public Response remove(HttpServletRequest request) {
        Response response = new Response();
        long start = 0, end = 0;
        RemoveRequest removeRequest = null;
        boolean result = false;
        try {
            start = System.currentTimeMillis();
            String requestData = request.getParameter(ConstValue.REQUEST_DATA);

            removeRequest = JSON.parseObject(requestData, RemoveRequest.class);
            removeRequest.check();
            Long jobId = removeRequest.getJobId();

            result = jobService.remove(jobId);
            end = System.currentTimeMillis();
        } catch (MyTriggerException e) {
            logger.info("[remove job by http] failed, jobId:{}", removeRequest.getJobId());
        }
        response.setSuccess(result)
                .setInfo(null)
                .setType(ResponseType.remove)
                .setTime(end - start);
        logger.info("[remove job by http] success, jobId:{}, time consuming:{} ms", removeRequest.getJobId(),response.getTime());
        return response;
    }

    @RequestMapping("pause")
    public Response pause(HttpServletRequest request) {
        Response response = new Response();
        long start = System.currentTimeMillis();
        boolean result = false;
        Long jobId = null;
        try {
            String requestData = request.getParameter(ConstValue.REQUEST_DATA);

            PauseRequest pauseRequest = JSON.parseObject(requestData, PauseRequest.class);
            pauseRequest.check();

            jobId = pauseRequest.getJobId();
            result = jobService.pause(jobId);
        } catch (MyTriggerException e) {
            logger.info("[pause job by http] failed, jobId:{}", jobId);
        }
        long end = System.currentTimeMillis();

        response.setSuccess(result)
                .setInfo(null)
                .setType(ResponseType.pause)
                .setTime(end - start);
        logger.info("[pause job by http] success, jobId:{}, time consuming:{} ms", jobId, response.getTime());
        return response;
    }

    @RequestMapping("findAllJobByApp")
    public Response findAllJob(HttpServletRequest request) {
        Response response = new Response();
        long start = System.currentTimeMillis();
        Application app = null;
        List<Job> jobs = null;
        try {
            String requestData = request.getParameter(ConstValue.REQUEST_DATA);
            app = JSON.parseObject(requestData, CommonRequest.class).getApp();
            jobs = jobService.findAllJobByApp(app.getId());
        } catch (Exception e) {
            logger.info("[findAppJobByApp] failed, app:{}", JSON.toJSONString(app));
        }
        long end = System.currentTimeMillis();

        response.setSuccess(jobs == null)
                .setTime(end - start)
                .setInfo(JSON.toJSONString(jobs));
        logger.info("[findAppJobByApp] success, app:{}, time consuming:{} ms", JSON.toJSONString(app), response.getTime());
        return response;
    }

}
