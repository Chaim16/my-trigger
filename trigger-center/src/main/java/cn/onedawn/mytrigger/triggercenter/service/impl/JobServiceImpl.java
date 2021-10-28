package cn.onedawn.mytrigger.triggercenter.service.impl;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.triggercenter.dao.mapper.JobMapper;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
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

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

    @Override
    public boolean ack(Long jobId) {
        return false;
    }

    @Override
    public List<Job> findAllJobByApp(Long id) {
        return jobMapper.selectAllJobByAppId(id);
    }
}
