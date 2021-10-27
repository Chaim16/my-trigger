package cn.onedawn.mytrigger.triggercenter.service.impl;

import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.triggercenter.dao.mapper.JobMapper;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import cn.onedawn.mytrigger.utils.CronUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public boolean register(Job job) throws ParseException {
        Date date = new Date();
        String datetime = dateFormat.format(date);
        job.setCreateTime(datetime);
        job.setModifyTime(datetime);
        job.setTriggerTime(dateFormat.format(CronUtil.getLoopTime(job.getCron(), System.currentTimeMillis())));
        return jobMapper.register(job);
    }

    @Override
    public boolean remove(Long jobId) {
        return false;
    }

    @Override
    public boolean trigger(Long jobId) {
        return false;
    }

    @Override
    public boolean modify(Job job) {
        return false;
    }

    @Override
    public boolean pause(Long jobId) {
        return false;
    }

    @Override
    public boolean ack(Long jobId) {
        return false;
    }
}
