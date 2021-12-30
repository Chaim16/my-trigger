package cn.onedawn.mytrigger.triggercenter.controller;

import cn.onedawn.mytrigger.pojo.User;
import cn.onedawn.mytrigger.request.impl.RegisterRequest;
import cn.onedawn.mytrigger.response.Response;
import cn.onedawn.mytrigger.triggercenter.service.UserService;
import cn.onedawn.mytrigger.type.ResponseType;
import cn.onedawn.mytrigger.utils.ConstValue;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName UserController.java
 * @Description TODO
 * @createTime 2021年12月30日 15:07:00
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 注册用户
     */
    @PostMapping
    public Response register(HttpServletRequest request) {
        Response response = new Response();
        boolean result = false;
        long start = System.currentTimeMillis();
        User user = null;
        try {
            String data = request.getParameter(ConstValue.REQUEST_DATA);
            RegisterRequest registerRequest = JSON.parseObject(data, RegisterRequest.class);
            user = registerRequest.getUser();
            result = userService.register(user);
        } catch (Exception e) {
            logger.error("[register user] failed, userId: {}, userName: {}", user.getId(), user.getUsername());
        }
        long end = System.currentTimeMillis();
        response.setSuccess(result)
                .setType(ResponseType.register)
                .setInfo(null)
                .setTime(end - start);
        return response;
    }

    /**
     * 删除一个或多个用户
     */
    @DeleteMapping
    public Response delete(HttpServletRequest request) {
        Response response = new Response();
        String requestString = request.getParameter(ConstValue.REQUEST_DATA);
        long start = System.currentTimeMillis();
        try {
            HashSet ids = JSON.parseObject(requestString, HashSet.class);
            userService.delete(ids);
        } catch (Exception e) {
            logger.error("[delete user] failed, ids: {}", requestString);
        }
        long end = System.currentTimeMillis();
        response.setSuccess(true)
                .setTime(end - start)
                .setInfo(null)
                .setType(ResponseType.remove);
        return response;
    }

    /**
     * 修改密码，参数已经在管理后台校验，这里直接更新就行
     */
    @PostMapping("/updatePass")
    public Response updatePass(HttpServletRequest request) {
        Response response = new Response();
        String username = request.getParameter("username");
        String password = request.getParameter("newPass");
        long start = System.currentTimeMillis();
        try {
            userService.updatePass(username, password);
        } catch (Exception e) {
            logger.error("[update passwd] failed, username: {}", username);
        }
        long end = System.currentTimeMillis();
        response.setSuccess(true)
                .setTime(end - start)
                .setInfo(null);
        return response;
    }

    /**
     * 修改信息
     */
    @PutMapping
    public Response update(HttpServletRequest request) {
        Response response = new Response();
        String requestString = request.getParameter(ConstValue.REQUEST_DATA);
        User user = JSON.parseObject(requestString, User.class);
        long start = System.currentTimeMillis();
        try {
            userService.update(user);
        } catch (Exception e) {
            logger.error("[update user] faild, username: {}, Exception, {}", user.getUsername(), e.getMessage());
        }
        long end = System.currentTimeMillis();
        response.setSuccess(true)
                .setTime(end - start)
                .setInfo(null);
        return response;
    }

}
