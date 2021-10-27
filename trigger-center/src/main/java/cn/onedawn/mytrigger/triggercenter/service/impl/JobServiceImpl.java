package cn.onedawn.mytrigger.triggercenter.service.impl;

import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.triggercenter.dao.mapper.JobMapper;
import cn.onedawn.mytrigger.triggercenter.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public boolean register(Job job) {
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
