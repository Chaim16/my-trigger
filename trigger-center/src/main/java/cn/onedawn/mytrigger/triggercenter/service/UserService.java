package cn.onedawn.mytrigger.triggercenter.service;

import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.pojo.User;
import cn.onedawn.mytrigger.triggercenter.tasks.CallEnter;
import lombok.SneakyThrows;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName UserService.java
 * @Description TODO
 * @createTime 2021年12月30日 15:07:00
 */
public interface UserService {
    boolean register(User user);

    void delete(HashSet ids);

    void updatePass(String username, String password);

    void update(User user);
}
