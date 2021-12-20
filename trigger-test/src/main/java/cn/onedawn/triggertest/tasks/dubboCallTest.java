package cn.onedawn.triggertest.tasks;

import cn.onedawn.mytrigger.call.Task;
import org.springframework.stereotype.Component;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName Call.java
 * @Description TODO
 * @createTime 2021年10月30日 13:53:00
 */
@Component("dubboCallTest")
public class dubboCallTest implements Task {

    @Override
    public boolean run() {
        System.out.println("成功调度");
        return true;
    }
}
