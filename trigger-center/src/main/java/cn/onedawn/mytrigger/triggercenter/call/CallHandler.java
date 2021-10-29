package cn.onedawn.mytrigger.triggercenter.call;

import cn.onedawn.mytrigger.request.impl.CallRequest;
import cn.onedawn.mytrigger.response.Response;

public interface CallHandler {
    Response handle(CallRequest request);
}
