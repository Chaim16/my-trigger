package cn.onedawn.mytrigger.triggercenter.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName ConstValue.java
 * @Description TODO
 * @createTime 2021年11月02日 14:03:00
 */
@Component
public class ConstValue {
    public static final String HTTP_URI_HEAD_STR = "http://";
    public static final Integer HTTP_CALL_PORT = 9090;
    public static final String HTTP_CALL_URL = "/call";
    public static final long SCHED_TIME = 1000L;

    public static int TRIGGER_RETRY_COUNT = 3;

}
