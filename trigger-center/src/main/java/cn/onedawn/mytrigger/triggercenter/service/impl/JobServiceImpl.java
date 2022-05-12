package cn.onedawn.mytrigger.triggercenter.service.impl;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.Application;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.triggercenter.mapper.ApplicationMapper;
import cn.onedawn.mytrigger.triggercenter.mapper.JobMapper;
import cn.onedawn.mytrigger.triggercenter.service.ElasticsearchService;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import cn.onedawn.mytrigger.triggercenter.tasks.CallJobTask;
import cn.onedawn.mytrigger.type.JobStatusType;
import cn.onedawn.mytrigger.utils.ConstValue;
import cn.onedawn.mytrigger.utils.CronUtil;
import cn.onedawn.mytrigger.utils.SpringBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName JobServiceImpl.java
 * @Description TODO
 * @createTime 2021年10月26日 10:26:00
 */
@Service
public class JobServiceImpl implements JobService {

    private static Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    SimpleDateFormat dateFormat = new SimpleDateFormat(ConstValue.TIME_PATTERN);

    @Override
    public boolean register(Job job) {
        Date date = new Date();
        String datetime = dateFormat.format(date);
        job.setCreateTime(datetime);
        job.setModifyTime(datetime);
        try {
            setTriggerTime(job);
        } catch (ParseException e) {
            logger.error("[register job] time convert failed, jobId:{}", job.getId());
        } catch (MyTriggerException e) {
            logger.error("[register job] get onetime failed, jobId:{}", job.getId());
        }
        return jobMapper.register(job) > 0;
    }

    @Override
    public boolean remove(Long jobId) {
        return jobMapper.remove(jobId) > 0;
    }

    @Override
    @Transactional
    public boolean trigger(Long jobId) {
        Job job = jobMapper.selectJobById(jobId);
        if (job == null) {
            logger.error("[trigger job] cann`t find job by id: {}", jobId);
            return false;
        }
        try {
            job.setTrigger(1);
            FutureTask futureTask = new FutureTask(new CallJobTask(job, true));
            Thread callJobTask = new Thread(futureTask);
            callJobTask.start();
            return (boolean) futureTask.get();
        } catch (InterruptedException e) {
            logger.error("[trigger job] failed trigger job by id: {}", jobId);
            logger.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean modify(Job job) {
        Date date = new Date();
        String datetime = dateFormat.format(date);
        job.setModifyTime(datetime);
        try {
            setTriggerTime(job);
        } catch (ParseException e) {
            logger.error("[modify job] time convert failed, jobId:{}", job.getId());
        } catch (MyTriggerException e) {
            logger.error("[modify job] time get onetime failed, jobId:{}", job.getId());
        }
        return jobMapper.modify(job) > 0;
    }

    /**
     * 设置下一次调度时间，区分周期性任务和一次性任务
     */
    private void setTriggerTime(Job job) throws MyTriggerException, ParseException {
        String cron = job.getCron();
        if (CronUtil.checkCronOneTime(cron)) {
            job.setTriggerTime(dateFormat.format(CronUtil.getOnceTime(cron)));
        } else {
            job.setTriggerTime(dateFormat.format(CronUtil.getLoopTime(job.getCron(), System.currentTimeMillis())));
        }
    }

    @Override
    public boolean pause(Long jobId) {
        return jobMapper.pause(jobId) > 0;
    }

    /**
     * 找出Job，判断是否为周期性调度
     * 如果是周期性调度，则更新状态和下一次调度时间
     */
    @Override
    @Transactional
    public boolean ack(Long jobId) throws MyTriggerException, ParseException, IOException, CloneNotSupportedException {
        try {
            Job job = findJobById(jobId);
            Application app = applicationMapper.selectAppById(job.getApp());
            // 一次性任务
            if (CronUtil.checkCronOneTime(job.getCron())) {
                remove(jobId);
                boolean result = jobMapper.ack(jobId) > 0;
                if (result) {
                    // 往ES里面存
                    ElasticsearchService elasticsearchService = (ElasticsearchService) SpringBeanFactory.getBean("elasticsearchService");
                    elasticsearchService.Index(job, app);
                }
                return result;
            } else { // 周期性任务
                if (job.getRemove() == 1) {
                    job.setStatus(JobStatusType.finished);
                } else {
                    // ES内存储本次调度
                    Job finishJob = job.clone();
                    finishJob.setStatus(JobStatusType.finished);
                    ElasticsearchService elasticsearchService = (ElasticsearchService) SpringBeanFactory.getBean("elasticsearchService");
                    elasticsearchService.Index(job, app);

                    // 更新MySQL数据
                    job.setStatus(JobStatusType.wait);
                    job.setTriggerTime(dateFormat.format(CronUtil.getLoopTime(job.getCron(), System.currentTimeMillis())));
                    job.setCallerrorRetryCount(0);
                    job.setRunRetry(0);
                }
                return jobMapper.modify(job) > 0;
            }
        } catch (MyTriggerException e) {
            logger.error("[ack job] failed ack! jobId:{}", jobId);
            logger.error(String.valueOf(e.getMessage()));
            e.printStackTrace();
        }
        return false;
    }

    private Job findJobById(Long jobId) {
        return jobMapper.selectJobById(jobId);
    }

    @Override
    public List<Job> findAllJobByApp(Long id) {
        return jobMapper.selectAllJobByAppId(id);
    }

    @Override
    public int updateStatusByJobId(Long id, JobStatusType status) {
        return jobMapper.updateStatusByJobId(id, status);
    }

    @Override
    public List<Job> findJob(String sql) {
        return jobMapper.selectJob(sql);
    }

    @Override
    public Application findAppById(Long appId) {
        return applicationMapper.selectAppById(appId);
    }

    @Override
    public int deleteRemoveJobs() {
        return jobMapper.deleteRemovejob();
    }

    @Override
    public List<Job> findRemoveJobs() {
        return jobMapper.selectRemoveJobs();
    }

    @Override
    public void insertMultiJob(Integer num) {
        String sql = "INSERT INTO trigger_job\n" +
                "(status, trigger_time, remove, call_name, call_data, call_type, call_host, cron, create_time, modify_time, app, callerror_retry_count, run_retry)\n" +
                "VALUES('wait', '2022-05-02 02:22:15', 1, 'dubboCallTest', 'call_data', 'dubbo', '', '*/3 * * * * ?', '2021-12-20 20:40:21', '2022-05-02 02:22:13', 1, 0, 0)\n";
        String values_str = ",('wait', '2022-05-02 02:22:15', 1, 'dubboCallTest', 'call_data', 'dubbo', '', '*/3 * * * * ?', '2021-12-20 20:40:21', '2022-05-02 02:22:13', 1, 0, 0)\n";
        StringBuffer stringBuffer = new StringBuffer(100000);
        stringBuffer.append(sql);
        num -= 1;
        for (int i = 0; i < num; i++) {
            stringBuffer.append(values_str);
        }
        stringBuffer.append(';');
        sql = stringBuffer.toString();
        jobMapper.insertMultiJob(sql);
    }

    @Override
    public void readyAllJob() {
        String sql = "update trigger_job set status = 'wait', remove = 0;";
        jobMapper.readyAllJob(sql);
    }


}
