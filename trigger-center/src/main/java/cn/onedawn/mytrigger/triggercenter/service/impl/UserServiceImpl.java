package cn.onedawn.mytrigger.triggercenter.service.impl;

import cn.onedawn.mytrigger.pojo.User;
import cn.onedawn.mytrigger.triggercenter.mapper.UserMapper;
import cn.onedawn.mytrigger.triggercenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName UserServiceImpl.java
 * @Description TODO
 * @createTime 2021年12月30日 15:08:00
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean register(User user) {
        return userMapper.register(user) > 0;
    }

    @Override
    public void delete(HashSet ids) {
        userMapper.delete(ids);
    }

    @Override
    public void updatePass(String username, String password) {
        userMapper.updatePass(username, password);
    }

    @Override
    public void update(User user) {
        userMapper.update(user);
    }
}
