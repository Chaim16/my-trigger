package cn.onedawn.mytrigger.triggercenter.service;

import cn.onedawn.mytrigger.pojo.Application;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName ApplicationService.java
 * @Description TODO
 * @createTime 2021年10月26日 16:35:00
 */
public interface ApplicationService {
    boolean register(Application app);

    Long findAppIdByAppName(String appName);
}
