package cn.onedawn.mytrigger.triggercenter.service.impl;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.App;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.triggercenter.mapper.JobMapper;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import cn.onedawn.mytrigger.triggercenter.utils.ConstValue;
import cn.onedawn.mytrigger.type.JobStatusType;
import cn.onedawn.mytrigger.utils.CronUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName JobServiceImpl.java
 * @Description TODO
 * @createTime 2021年10月26日 10:26:00
 */
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobMapper jobMapper;

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
            try {
                throw new ParseException("[time convert] faild", 0);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        } catch (MyTriggerException e) {
            try {
                throw new MyTriggerException("[time] get onetime faild");
            } catch (MyTriggerException ex) {
                ex.printStackTrace();
            }
        }
        return jobMapper.register(job);
    }

    @Override
    public boolean remove(Long jobId) {
        return jobMapper.remove(jobId) > 0;
    }

    @Override
    public boolean trigger(Long jobId) {
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
            try {
                throw new ParseException("[time convert] faild", 0);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        } catch (MyTriggerException e) {
            try {
                throw new MyTriggerException("[time] get onetime faild");
            } catch (MyTriggerException ex) {
                ex.printStackTrace();
            }
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
    public boolean ack(Long jobId) throws MyTriggerException, ParseException {
        Job job = findJobById(jobId);
        // 一次性任务
        if (CronUtil.checkCronOneTime(job.getCron())) {
            remove(jobId);
            return jobMapper.ack(jobId) > 0;
        } else { // 周期性任务
            if (job.getRemove() == 1) {
                job.setStatus(JobStatusType.finish);
            } else {
                job.setStatus(JobStatusType.wait);
                // 设置下一次调度的时间
                job.setTriggerTime(dateFormat.format(CronUtil.getLoopTime(job.getCron(), System.currentTimeMillis())));
                job.setCallerrorRetryCount(0);
            }
            return jobMapper.modify(job) > 0;
        }
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
    public App findAppById(Long appId) {
        return jobMapper.selectAppById(appId);
    }
}
