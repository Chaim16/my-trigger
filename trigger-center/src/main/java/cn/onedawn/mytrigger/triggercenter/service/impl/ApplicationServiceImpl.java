package cn.onedawn.mytrigger.triggercenter.service.impl;

import cn.onedawn.mytrigger.pojo.Application;
import cn.onedawn.mytrigger.triggercenter.mapper.ApplicationMapper;
import cn.onedawn.mytrigger.triggercenter.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName ApplicationServiceImpl.java
 * @Description TODO
 * @createTime 2021年10月26日 16:37:00
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    ApplicationMapper appMapper;

    @Override
    public boolean register(Application app) {
        return appMapper.register(app) > 0;
    }

    @Override
    public Long findAppIdByAppName(String appName) {
        return appMapper.selectAppIdByAppName(appName);
    }

    @Override
    public String findAppNameById(String appId) {
        return appMapper.selectAppNameById(appId);
    }
}
