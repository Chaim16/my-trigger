package cn.onedawn.mytrigger.triggercenter.controller;

import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.AckRequest;
import cn.onedawn.mytrigger.request.impl.RegisterRequest;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import cn.onedawn.mytrigger.utils.ConstValue;
import cn.onedawn.mytrigger.utils.NumberUtils;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName MQConsumer.java
 * @Description TODO 消费
 * @createTime 2021年10月28日 13:05:00
 */
@Component
public class RocketMQConsumer implements InitializingBean {
    private static DefaultMQPushConsumer consumer;

    @Autowired
    private JobService jobService;

    @Override
    public void afterPropertiesSet() throws Exception {
        consumer = new DefaultMQPushConsumer("my_trigger");
        consumer.setNamesrvAddr(ConstValue.NAMESERVADDR);
        try {
            consumer.subscribe("job", "register || ack");
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @SneakyThrows
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    for (MessageExt msg : msgs) {
                        String messageBody = new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET);
                        if ("register".equals(msg.getTags())) {
                            RegisterRequest registerRequest = JSON.parseObject(messageBody, RegisterRequest.class);
                            registerRequest.check();
                            Job job = registerRequest.getJob();
                            jobService.register(job);
                        } else if ("ack".equals(msg.getTags())) {
                            AckRequest ackRequest = JSON.parseObject(messageBody, AckRequest.class);
                            ackRequest.check();

                            jobService.ack(ackRequest.getJobId());
                        }
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        System.out.println("RocketMQ Consumer Started.");
    }
}
