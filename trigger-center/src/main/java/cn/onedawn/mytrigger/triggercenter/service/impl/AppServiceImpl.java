package cn.onedawn.mytrigger.triggercenter.service.impl;

import cn.onedawn.mytrigger.pojo.APP;
import cn.onedawn.mytrigger.triggercenter.dao.mapper.APPMapper;
import cn.onedawn.mytrigger.triggercenter.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName AppServiceImpl.java
 * @Description TODO
 * @createTime 2021年10月26日 16:37:00
 */
@Service
public class AppServiceImpl implements AppService {
    @Autowired
    APPMapper appMapper;

    @Override
    public boolean register(APP app) {
        return appMapper.register(app) > 0;
    }
}
