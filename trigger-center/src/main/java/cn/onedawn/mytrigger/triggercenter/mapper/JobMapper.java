package cn.onedawn.mytrigger.triggercenter.mapper;

import cn.onedawn.mytrigger.pojo.App;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.type.JobStatusType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName JobMapper.java
 * @Description TODO
 * @createTime 2021年10月26日 15:48:00
 */
@Mapper
public interface JobMapper {
    boolean register(Job job);

    int modify(Job job);

    int remove(Long jobId);

    int pause(Long jobId);

    List<Job> selectAllJobByAppId(Long id);

    int updateStatusByJobId(Long id, JobStatusType status);

    List<Job> selectJob(String sql);

    int ack(Long jobId);

    App selectAppById(Long appId);

    Job selectJobById(Long jobId);

    int deleteRemovejob();
}
