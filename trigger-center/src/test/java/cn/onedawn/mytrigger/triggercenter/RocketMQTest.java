package cn.onedawn.mytrigger.triggercenter;

import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.RegisterRequest;
import cn.onedawn.mytrigger.type.CallType;
import cn.onedawn.mytrigger.type.JobStatusType;
import cn.onedawn.mytrigger.utils.ConstValue;
import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName RocketMQTest.java
 * @Description TODO
 * @createTime 2021年12月20日 11:26:00
 */
@SpringBootTest
public class RocketMQTest {

    /**
     * 任务注册测试
     */
    @Test
    public void jobRegisterTest() throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("my_trigger");
        producer.setNamesrvAddr(ConstValue.NAMESERVADDR);
        producer.start();

        Job job = new Job();
        job.setStatus(JobStatusType.wait);
        job.setCron("* * * * * ?")
                .setRemove((byte) 0)
                .setApp(1L)
                .setCallName("call")
                .setCallType(CallType.dubbo);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setJob(job);
        String messageBody = JSON.toJSONString(registerRequest);

        Message message = new Message("job",
                "register",
                messageBody.getBytes(StandardCharsets.UTF_8));
        SendResult sendResult = producer.send(message);

        System.out.println(sendResult.getSendStatus());
    }
}
