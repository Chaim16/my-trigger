package cn.onedawn.triggertest.listener;

import cn.onedawn.mytrigger.MyTriggerClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

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
        MyTriggerClient myTriggerClient = new MyTriggerClient();
        myTriggerClient.init("mayi");
        System.out.println("mytrigger-client inited");
    }
}
