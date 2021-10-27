package cn.onedawn.mytrigger.triggercenter.controller;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.RegisterRequest;
import cn.onedawn.mytrigger.response.Response;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import cn.onedawn.mytrigger.utils.ConstValue;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
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

    @RequestMapping("/register")
    public Response register(HttpServletRequest request) throws ParseException {
        Response response = new Response();
        long start = System.currentTimeMillis();
        boolean result = false;
        Job job = null;
        try {
            String requestData = request.getParameter(ConstValue.REQUEST_DATA);
            RegisterRequest registerRequest = JSON.parseObject(requestData, RegisterRequest.class);
            job = registerRequest.getJob();
            result = jobService.register(job);
        } catch (ParseException e) {
            response.setSuccess(result);
            response.setInfo(JSON.toJSONString(null));
            long end = System.currentTimeMillis();
            response.setTime(end - start);
            throw e;
        }
        response.setSuccess(result);
        response.setInfo(JSON.toJSONString(job));
        long end = System.currentTimeMillis();
        response.setTime(end - start);
        return response;
    }
}
