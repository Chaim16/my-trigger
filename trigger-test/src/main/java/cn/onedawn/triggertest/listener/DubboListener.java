package cn.onedawn.triggertest.listener;

import cn.onedawn.mytrigger.MyTriggerClient;
import cn.onedawn.mytrigger.exception.MyTriggerException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName Test.java
 * @Description TODO
 * @createTime 2021年11月02日 16:48:00
 */
@Component()
public class DubboListener implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            MyTriggerClient myTriggerClient = new MyTriggerClient();
            System.out.println("mytrigger-client initing");
            myTriggerClient.init("mayi");
            System.out.println("mytrigger-client inited");
        } catch (MyTriggerException e) {
            e.printStackTrace();
            System.out.println("message: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("message: " + e.getMessage());
        }
    }
}
