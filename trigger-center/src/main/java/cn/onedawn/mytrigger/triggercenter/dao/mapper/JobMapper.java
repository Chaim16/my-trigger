package cn.onedawn.mytrigger.triggercenter.dao.mapper;

import cn.onedawn.mytrigger.pojo.Job;
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
}
