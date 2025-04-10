package cn.onedawn.mytrigger.triggercenter;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.Application;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.pojo.User;
import cn.onedawn.mytrigger.request.impl.*;
import cn.onedawn.mytrigger.response.Response;
import cn.onedawn.mytrigger.type.CallType;
import cn.onedawn.mytrigger.type.JobStatusType;
import cn.onedawn.mytrigger.utils.ConstValue;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
class JobAndAppAndUserTest {

    SimpleDateFormat dateFormat = new SimpleDateFormat(ConstValue.TIME_PATTERN);

    /**
     * 应用注册测试
     */
    @Test
    public void appRegisterTest() throws MyTriggerException {
        Map<String, Object> formMap = new HashMap<>();
        RegisterRequest registerRequest = new RegisterRequest();
        Date date = new Date();
        registerRequest.setApp(new Application(null, "mayi", dateFormat.format(date), dateFormat.format(date)));
        registerRequest.check();
        formMap.put(ConstValue.REQUEST_DATA, JSON.toJSONString(registerRequest));

        String body = HttpRequest.post("http://localhost:8080/application/register")
                .form(formMap)
                .timeout(5000)
                .execute()
                .body();
        System.out.println(body);
    }

    /**
     * 任务注册测试
     */
    @Test
    public void jobRegisterTest() throws MyTriggerException {
        for (int i = 1; i < 50000; i++) {
            Job job = new Job();
            job.setStatus(JobStatusType.wait);
            job.setCron("*/3 * * * * ?")
                    .setRemove((byte) 0)
                    .setApp(2L)
                    .setCallName("dubboCallTest")
                    .setCallType(CallType.dubbo);

            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setJob(job);
            registerRequest.check();

            Map<String, Object> formMap = new HashMap<>();
            formMap.put(ConstValue.REQUEST_DATA, JSON.toJSONString(registerRequest));

            String body = HttpRequest.post("http://localhost:8080/job/register")
                    .form(formMap)
                    .timeout(5000)
                    .execute()
                    .body();
            System.out.println(body);
        }
    }

    /**
     * 任务修改测试
     */
    @Test
    public void jobModiyTest() throws MyTriggerException {
        Job job = new Job();
        job.setStatus(JobStatusType.wait)
                .setId(23L)
                .setCron("1 * * * * ?")
                .setRemove((byte) 0)
                .setApp(1L)
                .setCallName("call")
                .setCallType(CallType.dubbo);

        ModifyRequest modifyRequest = new ModifyRequest();
        modifyRequest.setJob(job);
        modifyRequest.check();

        Map<String, Object> formMap = new HashMap<>();
        formMap.put(ConstValue.REQUEST_DATA, JSON.toJSONString(modifyRequest));

        String body = HttpRequest.post("http://localhost:8080/job/modify")
                .form(formMap)
                .timeout(5000)
                .execute()
                .body();
        System.out.println(body);
    }

    /**
     * 任务删除测试
     * @throws MyTriggerException
     */
    @Test
    public void jobRemoveTest() throws MyTriggerException {
        RemoveRequest removeRequest = new RemoveRequest();
        removeRequest.setJobId(23L);
        removeRequest.check();

        Map<String, Object> formMap = new HashMap<>();
        formMap.put(ConstValue.REQUEST_DATA, JSON.toJSONString(removeRequest));

        String body = HttpRequest.post("http://localhost:8080/job/remove")
                .timeout(5000)
                .form(formMap)
                .execute()
                .body();
        System.out.println(body);
    }

    /**
     * 任务暂停测试
     */
    @Test
    public void jobPauseTest() {
        PauseRequest pauseRequest = new PauseRequest();
        pauseRequest.setJobId(22L);

        Map<String, Object> formMap = new HashMap<>();
        formMap.put(ConstValue.REQUEST_DATA, JSON.toJSONString(pauseRequest));
        String body = HttpRequest.post("http://localhost:8080/job/pause")
                .form(formMap)
                .timeout(5000)
                .execute()
                .body();
        System.out.println(body);
    }


    @Test
    public void userRegisterTest() {
        User user = new User();
        user.setId(1L);
        user.setUsername("qingming");
        user.setPassword("789");
        user.setProhibit((byte) 0);
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUser(user);

        String url = "http://127.0.0.1:8080/user/register";
        Map<String, Object> formMap = new HashMap<>();
        formMap.put(ConstValue.REQUEST_DATA, JSON.toJSONString(registerRequest));
        HttpResponse insertToCenterResponse = HttpRequest.post(url)
                .form(formMap)
                .timeout(5000)
                .execute();
        Response response = JSON.parseObject(insertToCenterResponse.body(), Response.class);
        System.out.println(JSON.toJSONString(response));
    }

}
