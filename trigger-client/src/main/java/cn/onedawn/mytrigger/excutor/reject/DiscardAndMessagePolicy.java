package cn.onedawn.mytrigger.excutor.reject;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import lombok.SneakyThrows;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName DiscarAndMessagePolicy.java
 * @Description TODO 拒绝策略，最大线程数满了之后直接抛弃，并给出提示
 * @createTime 2021年10月29日 04:15:00
 */
public class DiscardAndMessagePolicy implements RejectedExecutionHandler {

    @SneakyThrows
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("[threadpool] reject job execute");
    }
}
