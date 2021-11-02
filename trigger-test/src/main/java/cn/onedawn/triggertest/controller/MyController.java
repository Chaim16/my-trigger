package cn.onedawn.triggertest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName MyController.java
 * @Description TODO
 * @createTime 2021年11月02日 17:04:00
 */
@RestController
public class MyController {
    @RequestMapping("/test")
    public String test() {
        return "ok";
    }
}
