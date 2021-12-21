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
public class ConstValue extends cn.onedawn.mytrigger.utils.ConstValue {
    public static final String HTTP_URI_HEAD_STR = "http://";
    public static final Integer HTTP_CALL_PORT = 9090;
    public static final String HTTP_CALL_URL = "/call";
    public static final long TRIGGER_SCHED_TIME = 1000L;

    public static int TRIGGER_RETRY_COUNT = 3;

    // 10分钟重试
    public static int RETRY_CALL_ERROR_JOB_SCHEDULE_TIME = 600;
    public static int RETRY_CALL_ERROR_JOB_THRESHOLD_TIME = 300;
    public static int RETRY_CALL_ERROR_JOB_COUNT_THRESHOLD = 10;

    // 5分钟重试
    public static int RETRY_RUN_JOB_SCHEDULE_TIME = 5;
}
