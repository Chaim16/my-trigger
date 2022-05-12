package cn.onedawn.triggertest.tasks;

import cn.onedawn.mytrigger.call.Task;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    public boolean run(String callData) {
        if (callData != null) {
            System.out.println("callData ====> " + callData);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("成功调度" + simpleDateFormat.format(new Date()));
        return true;
    }
}
