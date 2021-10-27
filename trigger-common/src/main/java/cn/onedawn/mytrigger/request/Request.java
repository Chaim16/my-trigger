package cn.onedawn.mytrigger.request;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.type.RequestType;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName Request.java
 * @Description TODO
 * @createTime 2021年10月26日 08:53:00
 */
public abstract class Request {
    public RequestType type;
    public abstract void check() throws MyTriggerException;
    public abstract String mqKey();
}
