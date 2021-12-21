package cn.onedawn.mytrigger.triggercenter.tasks;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.App;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.CallRequest;
import cn.onedawn.mytrigger.response.Response;
import cn.onedawn.mytrigger.triggercenter.call.DubboCallServiceClient;
import cn.onedawn.mytrigger.triggercenter.call.HTTPCall;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import cn.onedawn.mytrigger.triggercenter.utils.ConstValue;
import cn.onedawn.mytrigger.type.CallType;
import cn.onedawn.mytrigger.type.JobStatusType;
import cn.onedawn.mytrigger.utils.CronUtil;
import cn.onedawn.mytrigger.utils.SpringBeanFactory;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName CallJobTask.java
 * @Description TODO
 * @createTime 2021年11月02日 12:41:00
 */
public class CallJobTask implements Callable {

    private List<Job> jobs;
    private JobService jobService;
    private boolean trigger;

    public CallJobTask(List<Job> jobs, boolean trigger) {
        this.jobs = jobs;
        this.jobService = SpringBeanFactory.getBeanByType(JobService.class);
        this.trigger = trigger;
    }

    public CallJobTask(Job job, boolean trigger) {
        this.jobs = new ArrayList<>();
        this.jobs.add(job);
        this.jobService = SpringBeanFactory.getBeanByType(JobService.class);
        this.trigger = trigger;
    }

    @Override
    public Object call() throws Exception {
        for (Job job : this.jobs) {
            try {
                doCall(job);
            } catch (Exception e) {
                if (jobService != null) {
                    jobService.updateStatusByJobId(job.getId(), JobStatusType.callerror);
                    job.setStatus(JobStatusType.callerror);
                }
            }
        }
        return true;
    }

    private void doCall(Job job) throws ParseException, MyTriggerException {
        App app = jobService.findAppById(job.getApp());
        jobService.updateStatusByJobId(job.getId(), JobStatusType.run);

        CallRequest callRequest = new CallRequest();
        callRequest.setJob(job);
        callRequest.setApp(app);

        // 立即重试机制
        int count = 0;
        boolean result = false;
        while (!result) {
            if (job.getCallType().equals(CallType.dubbo)) {
                result = callDubbo(callRequest);
            } else {
                result = callHttp(callRequest);
            }
            // 调度失败
            if (!result) {
                ++count;
                if (count > ConstValue.TRIGGER_RETRY_COUNT) {
                    throw new MyTriggerException("trigger job error, jobId:" + job.getId());
                }
            }
        }
    }

    private boolean callHttp(CallRequest callRequest) {
        return HTTPCall.call(callRequest).isSuccess();
    }

    private boolean callDubbo(CallRequest callRequest) throws MyTriggerException {
        DubboCallServiceClient client = SpringBeanFactory.getBean(DubboCallServiceClient.class);
        Response response = client.callback(callRequest);
        if (!response.isSuccess()) {
            throw new MyTriggerException("call dubbo error, msg:" + callRequest.getJob());
        }
        return response.isSuccess();
    }
}
