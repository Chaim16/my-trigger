package cn.onedawn.mytrigger;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName TestImpl.java
 * @Description TODO
 * @createTime 2021年10月30日 16:36:00
 */
@Component("test")
public class TestImpl implements Test {
    @Override
    public void run() {
        System.out.println("123");
    }
}
