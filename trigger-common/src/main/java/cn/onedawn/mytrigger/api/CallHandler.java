package cn.onedawn.mytrigger.api;

import cn.onedawn.mytrigger.request.impl.CallRequest;
import cn.onedawn.mytrigger.response.Response;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName CallService.java
 * @Description TODO dubbo接口
 * @createTime 2021年10月29日 04:47:00
 */
public interface CallHandler {
    Response handle(CallRequest callRequest);
}
