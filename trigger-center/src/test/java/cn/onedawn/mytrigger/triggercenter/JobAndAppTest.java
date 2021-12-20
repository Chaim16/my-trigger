package cn.onedawn.mytrigger.triggercenter;

import cn.hutool.http.HttpRequest;
import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.App;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.*;
import cn.onedawn.mytrigger.type.CallType;
import cn.onedawn.mytrigger.type.JobStatusType;
import cn.onedawn.mytrigger.utils.ConstValue;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
class JobAndAppTest {

    /**
     * 应用注册测试
     */
    @Test
    public void appRegisterTest() throws MyTriggerException {
        Map<String, Object> formMap = new HashMap<>();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setApp(new App(1L, "mayi", String.valueOf(UUID.randomUUID())));
        registerRequest.check();
        formMap.put(ConstValue.REQUEST_DATA, JSON.toJSONString(registerRequest));

        String body = HttpRequest.post("http://localhost:8080/app/register")
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
        Job job = new Job();
        job.setStatus(JobStatusType.wait);
        job.setCron("1 * * * * ?")
                .setRemove((byte) 0)
                .setApp(6L)
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

    /**
     * HTTP注册
     */
    @Test
    void contextLoads() {
        Map<String, Object> formMap = new HashMap<>();
        RegisterRequest registerRequest = new RegisterRequest();
        Job job = new Job();
        job.setStatus(JobStatusType.wait);
        job.setCron("* * * * * ?")
                .setRemove((byte) 0)
                .setApp(1L)
                .setCallName("call")
                .setCallType(CallType.dubbo);
        registerRequest.setJob(job);
        formMap.put("data", JSON.toJSONString(registerRequest));
        String body = HttpRequest.post("http://localhost:8080/job/register")
                .form(formMap)
                .timeout(5000)
                .execute()
                .body();
        System.out.println(body);
    }

}
