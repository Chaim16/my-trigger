package cn.onedawn.mytrigger.exception;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName MyTriggerException.java
 * @Description TODO 封装的异常
 * @createTime 2021年10月26日 08:30:00
 */
public class MyTriggerException extends Exception{
    public MyTriggerException() {
    }

    public MyTriggerException(String message) {
        super(message);
    }

    public MyTriggerException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyTriggerException(Throwable cause) {
        super(cause);
    }

    public MyTriggerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
