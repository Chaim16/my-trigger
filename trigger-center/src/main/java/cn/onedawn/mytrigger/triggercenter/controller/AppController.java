package cn.onedawn.mytrigger.triggercenter.controller;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.APP;
import cn.onedawn.mytrigger.response.Response;
import cn.onedawn.mytrigger.triggercenter.service.AppService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName AppController.java
 * @Description TODO
 * @createTime 2021年10月26日 16:34:00
 */
@RestController
@RequestMapping("/app")
public class AppController {
    @Autowired
    private AppService appService;

    @RequestMapping("/register")
    public Response register(@PathVariable(name = "app") APP app) throws MyTriggerException {
        Response response = new Response();
        long start = System.currentTimeMillis();
        boolean result = appService.register(app);
        long end = System.currentTimeMillis();
        response.setSuccess(result);
        response.setTime(end - start);
        return response;
    }

}
