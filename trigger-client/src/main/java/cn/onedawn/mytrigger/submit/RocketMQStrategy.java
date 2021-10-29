package cn.onedawn.mytrigger.submit;

import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.AckRequest;
import cn.onedawn.mytrigger.request.impl.RegisterRequest;
import cn.onedawn.mytrigger.utils.ConstValue;
import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName MQSubmit.java
 * @Description TODO
 * @createTime 2021年10月28日 12:50:00
 */
public class RocketMQStrategy {

    private static DefaultMQProducer producer;

    public void init() {
        producer = new DefaultMQProducer("my_trigger");
        producer.setNamesrvAddr(ConstValue.NAMESERVADDR);
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public static boolean submit(Job job) throws MQClientException, UnsupportedEncodingException, MQBrokerException, RemotingException, InterruptedException {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setJob(job);
        String messageBody = JSON.toJSONString(registerRequest);

        Message message = new Message(
                "job",
                "register",
                messageBody.getBytes(RemotingHelper.DEFAULT_CHARSET));
        SendResult sendResult = producer.send(message);

        return sendResult.getSendStatus() == SendStatus.SEND_OK;
    }

    public static boolean ack(Long jobId) throws UnsupportedEncodingException, MQBrokerException, RemotingException, InterruptedException, MQClientException {
        AckRequest ackRequest = new AckRequest();
        ackRequest.setJobId(jobId);
        String messageBody = JSON.toJSONString(ackRequest);

        Message message = new Message(
                "job",
                "ack",
                messageBody.getBytes(RemotingHelper.DEFAULT_CHARSET));

        SendResult sendResult = producer.send(message);
        return sendResult.getSendStatus() == SendStatus.SEND_OK;
    }

    public static void close(){
        producer.shutdown();
    }
}
