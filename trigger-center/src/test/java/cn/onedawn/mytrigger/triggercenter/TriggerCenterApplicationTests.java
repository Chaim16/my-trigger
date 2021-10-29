package cn.onedawn.mytrigger.triggercenter;

import cn.hutool.http.HttpRequest;
import cn.onedawn.mytrigger.pojo.App;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.CallRequest;
import cn.onedawn.mytrigger.request.impl.RegisterRequest;
import cn.onedawn.mytrigger.response.Response;
import cn.onedawn.mytrigger.triggercenter.call.HTTPCall;
import cn.onedawn.mytrigger.type.CallType;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class TriggerCenterApplicationTests {

    @Test
    void contextLoads() {
        Map<String, Object> formMap = new HashMap<>();
        RegisterRequest registerRequest = new RegisterRequest();
        Job job = new Job();
        job.setStatus("wait");
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

    @Test
    void httpCallTest() {

    }

}
