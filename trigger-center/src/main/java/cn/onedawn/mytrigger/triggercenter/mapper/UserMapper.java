package cn.onedawn.mytrigger.triggercenter.mapper;

import cn.onedawn.mytrigger.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashSet;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName UserMapper.java
 * @Description TODO
 * @createTime 2021年12月30日 15:10:00
 */
@Mapper
public interface UserMapper {
    int register(User user);

    void delete(HashSet ids);

    void updatePass(String username, String password);

    void update(User user);
}
