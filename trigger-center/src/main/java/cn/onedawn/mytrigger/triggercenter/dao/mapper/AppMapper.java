package cn.onedawn.mytrigger.triggercenter.dao.mapper;

import cn.onedawn.mytrigger.pojo.App;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName APPMapper.java
 * @Description TODO
 * @createTime 2021年10月26日 17:31:00
 */
@Mapper
public interface AppMapper {
    int register(App app);
}
