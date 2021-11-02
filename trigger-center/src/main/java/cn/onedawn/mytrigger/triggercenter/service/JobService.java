package cn.onedawn.mytrigger.triggercenter.service;

import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.Request;
import cn.onedawn.mytrigger.type.JobStatusType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName JobService.java
 * @Description TODO
 * @createTime 2021年10月26日 10:20:00
 */
@Component
public interface JobService {
    boolean register(Job job) throws ParseException;

    boolean remove(Long jobId);

    boolean trigger(Long jobId);

    boolean modify(Job job);

    boolean pause(Long jobId);

    boolean ack(Long jobId);

    List<Job> findAllJobByApp(Long id);

    int updateStatusByJobId(Long id, JobStatusType status);
}
