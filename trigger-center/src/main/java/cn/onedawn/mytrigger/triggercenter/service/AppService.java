package cn.onedawn.mytrigger.triggercenter.service;

import cn.onedawn.mytrigger.pojo.App;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName AppService.java
 * @Description TODO
 * @createTime 2021年10月26日 16:35:00
 */
public interface AppService {
    boolean register(App app);

    Long findAppIdByAppName(String appName);
}
