package cn.onedawn.mytrigger.triggercenter.service;

import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.Request;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName JobService.java
 * @Description TODO
 * @createTime 2021年10月26日 10:20:00
 */
public interface JobService {
    boolean register(Job job);

    boolean remove(Long jobId);

    boolean trigger(Long jobId);

    boolean modify(Job job);

    boolean pause(Long jobId);

    boolean ack(Long jobId);

}
