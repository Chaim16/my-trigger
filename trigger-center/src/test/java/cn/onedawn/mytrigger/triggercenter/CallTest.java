package cn.onedawn.mytrigger.triggercenter;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.Application;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.CallRequest;
import cn.onedawn.mytrigger.response.Response;
import cn.onedawn.mytrigger.triggercenter.call.DubboCallServiceClient;
import cn.onedawn.mytrigger.triggercenter.call.HTTPCall;
import cn.onedawn.mytrigger.type.CallType;
import cn.onedawn.mytrigger.type.JobStatusType;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName CallTest.java
 * @Description TODO
 * @createTime 2021年12月20日 12:24:00
 */
@SpringBootTest
public class CallTest {
    /**
     * Dubbo任务调度测试
     */
    @Test
    public void dubboCallTest() throws MyTriggerException {
        Job job = new Job();
        job.setStatus(JobStatusType.wait);
        job.setCron("* * * * * ?")
                .setRemove((byte) 0)
                .setApp(1L)
                .setCallName("dubboCallTest")
                .setCallType(CallType.dubbo);

        CallRequest callRequest = new CallRequest();
        callRequest.setJob(job)
                .setApp(new Application()
                        .setCreateTime("1")
                        .setName("mayi"));

        DubboCallServiceClient dubboCallServiceClient = new DubboCallServiceClient();
        Response response = dubboCallServiceClient.callback(callRequest);

        System.out.println(JSON.toJSONString(response));
    }

    /**
     * HTTP任务调度测试
     */
    @Test
    public void httpCallTest() {
        Job job = new Job();
        job.setStatus(JobStatusType.wait);
        job.setCron("* * * * * ?")
                .setRemove((byte) 0)
                .setApp(1L)
                .setCallName("dubboCallTest")
                .setCallType(CallType.http)
                .setCallHost("192.168.4.1");

        CallRequest callRequest = new CallRequest();
        callRequest.setJob(job)
                .setApp(new Application()
                        .setId(4L)
                        .setCreateTime("1")
                        .setName("mayi"));

        Response response = HTTPCall.call(callRequest);
        System.out.println(response);
    }
}
