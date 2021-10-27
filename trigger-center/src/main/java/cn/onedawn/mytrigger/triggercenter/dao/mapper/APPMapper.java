package cn.onedawn.mytrigger.triggercenter.dao.mapper;

import cn.onedawn.mytrigger.pojo.APP;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName APPMapper.java
 * @Description TODO
 * @createTime 2021年10月26日 17:31:00
 */
@Mapper
public interface APPMapper {
    int register(APP app);
}
