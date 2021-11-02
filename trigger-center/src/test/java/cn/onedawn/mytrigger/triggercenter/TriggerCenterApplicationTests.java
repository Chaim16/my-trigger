package cn.onedawn.mytrigger.triggercenter;

import cn.hutool.http.HttpRequest;
import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.App;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.CallRequest;
import cn.onedawn.mytrigger.request.impl.RegisterRequest;
import cn.onedawn.mytrigger.triggercenter.call.DubboCallServiceClient;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import cn.onedawn.mytrigger.triggercenter.task.CallJobTask;
import cn.onedawn.mytrigger.type.CallType;
import cn.onedawn.mytrigger.type.JobStatusType;
import cn.onedawn.mytrigger.utils.SpringBeanFactory;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class TriggerCenterApplicationTests {

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

    /**
     * 测试http调度
     */
    @Test
    void httpCallTest() throws Exception {
        Job job = new Job();
        job.setStatus(JobStatusType.wait);
        job.setCron("* * * * * ?")
                .setRemove((byte) 0)
                .setApp(1L)
                .setCallName("callName")
                .setCallType(CallType.http)
                .setCallHost("127.0.0.1")
                .setId(4L);
        CallJobTask callJobTask = new CallJobTask(job, false);
        callJobTask.call();
    }

    /**
     * 测试dubbo调度
     * @throws MyTriggerException
     */
    @Test
    void dubboCallTest() throws MyTriggerException {
        Job job = new Job();
        job.setStatus(JobStatusType.wait);
        job.setCron("* * * * * ?")
                .setRemove((byte) 0)
                .setApp(1L)
                .setCallName("callName")
                .setCallType(CallType.dubbo);
        CallRequest callRequest = new CallRequest();
        callRequest.setJob(job).setApp(new App().setAppId("1").setAppName("mayi"));
        DubboCallServiceClient dubboCallServiceClient = new DubboCallServiceClient();
        dubboCallServiceClient.callback(callRequest);
    }

    @Test
    void updateStatusByJobId() {
        JobService jobService = SpringBeanFactory.getBean(JobService.class);
        jobService.updateStatusByJobId(3L, JobStatusType.finish);
    }

}
