package cn.onedawn.mytrigger.triggercenter.controller;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.RegisterRequest;
import cn.onedawn.mytrigger.response.Response;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequestMapping("/register")
    public Response register(RegisterRequest request) throws MyTriggerException {
        Response response = new Response();
        long start = System.currentTimeMillis();
        boolean result = false;
        Job job = null;
        try {
            request.check();
            job = request.getJob();
            Date date = new Date();
            job.setCreateTime(dateFormat.format(date));
            job.setModifyTime(dateFormat.format(date));
            result = jobService.register(job);
        } catch (MyTriggerException e) {
            response.setSuccess(result);
            response.setInfo(JSON.toJSONString(null));
            long end = System.currentTimeMillis();
            response.setTime(end - start);
            throw new MyTriggerException("register faild");
        }
        response.setSuccess(result);
        response.setInfo(JSON.toJSONString(job));
        long end = System.currentTimeMillis();
        response.setTime(end - start);
        return response;
    }
}
