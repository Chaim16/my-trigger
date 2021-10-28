package cn.onedawn.mytrigger;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.App;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.CommonRequest;
import cn.onedawn.mytrigger.request.impl.ModifyRequest;
import cn.onedawn.mytrigger.request.impl.RemoveRequest;
import cn.onedawn.mytrigger.response.Response;
import cn.onedawn.mytrigger.submit.HTTPStrategy;
import cn.onedawn.mytrigger.submit.RocketMQStrategy;
import cn.onedawn.mytrigger.type.AckType;
import cn.onedawn.mytrigger.type.CallType;
import cn.onedawn.mytrigger.type.SubmitType;
import cn.onedawn.mytrigger.utils.ConstValue;
import cn.onedawn.mytrigger.utils.StatusCode;
import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName MyTriggerClient.java
 * @Description TODO 客户端
 * @createTime 2021年10月28日 01:00:00
 */
public class MyTriggerClient {

    /**
     * 提交策略，默认使用MQ提交
     */
    private SubmitType submitType = SubmitType.MQ;
    /**
     * 回调方式，默认通过dubbo
     */
    private CallType callType = CallType.dubbo;
    /**
     * ack方式，默认通过mq
     */
    private AckType ackType = AckType.mq;

    private App app;

    private boolean closed;

    /**
     * 初始化客户端
     * 获取 服务端IP，端口号，查找AppId
     *
     * @param appName 应用名
     * @throws MyTriggerException 如果appName对应的appId不存在，抛出异常
     */
    public void init(String appName) throws MyTriggerException {
        // 查询appId
        String url = ConstValue.BASE_URL + "/app/findAppIdByAppName?appName=" + appName;
        HttpResponse response = HttpRequest.get(url).timeout(5000).execute();
        if (response.getStatus() == StatusCode.SUCCESS) {
            String body = response.body();
            app = new App();
            app.setId(Long.valueOf(body));
        } else {
            throw new MyTriggerException("find appId faild");
        }
        closed = false;
    }

    /**
     * 注册一个任务
     *
     * @param cron     时间表达式（可决定周期性还是一次性）
     * @param callName 回调接口名
     * @param callData 回调数据
     * @return 是否注册成功
     */
    public boolean register(String cron, String callName, String callData) throws MQBrokerException, RemotingException, UnsupportedEncodingException, InterruptedException, MQClientException {
        Job job = new Job();
        job.setStatus("wait")
                .setRemove((byte) 0)
                .setCallName(callName)
                .setCallData(callData)
                .setCallType(callType)
                .setCron(cron)
                .setApp(app.getId());
        if (submitType == SubmitType.MQ) {
            return RocketMQStrategy.submit(job);
        } else {
            return HTTPStrategy.submit(job);
        }
    }

    /**
     * 删除job
     */
    public boolean remove(Long jobId) {
        RemoveRequest request = new RemoveRequest();
        request.setJobId(jobId);
        Map<String, Object> formMap = new HashMap<>();
        formMap.put("data", JSON.toJSONString(request));
        String url = ConstValue.BASE_URL + "/job/remove";
        HttpResponse response = HttpRequest.post(url)
                .form(formMap)
                .timeout(5000)
                .execute();
        return response.getStatus() == StatusCode.SUCCESS;
    }

    /**
     * 修改job
     */
    public boolean modify(Job job) {
        ModifyRequest modifyRequest = new ModifyRequest();
        modifyRequest.setJob(job);
        Map<String, Object> formMap = new HashMap<>();
        formMap.put("data", JSON.toJSONString(job));
        String url = ConstValue.BASE_URL + "/job/modify";
        HttpResponse response = HttpRequest.post(url)
                .form(formMap)
                .timeout(5000)
                .execute();
        return response.getStatus() == StatusCode.SUCCESS;
    }

    /**
     * 获取当前应用下所有的Job信息
     */
    public List<Job> getAllJob() {
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setApp(app);
        Map<String, Object> formMap = new HashMap<>();
        formMap.put("data", JSON.toJSONString(commonRequest));
        String url = ConstValue.BASE_URL + "/job/findAllJobByApp";
        String body = HttpRequest.post(url)
                .form(formMap)
                .timeout(5000)
                .execute()
                .body();
        Response response = JSON.parseObject(body, Response.class);
        List<Job> jobs = JSON.parseObject(response.getInfo(), ArrayList.class);
        return jobs;
    }

    public static void main(String[] args) throws MyTriggerException, MQBrokerException, RemotingException, UnsupportedEncodingException, InterruptedException, MQClientException {
        MyTriggerClient myTriggerClient = new MyTriggerClient();
        myTriggerClient.init("543");
//        myTriggerClient.setSubmitType(SubmitType.HTTP);
        System.out.println(myTriggerClient.register("* * * * * ?", "call1", "123"));
//        System.out.println(myTriggerClient.remove(4L));
        System.out.println(myTriggerClient.getAllJob());
    }


    public SubmitType getSubmitType() {
        return submitType;
    }

    public void setSubmitType(SubmitType submitType) {
        this.submitType = submitType;
    }

    public CallType getCallType() {
        return callType;
    }

    public void setCallType(CallType callType) {
        this.callType = callType;
    }

    public AckType getAckType() {
        return ackType;
    }

    public void setAckType(AckType ackType) {
        this.ackType = ackType;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
