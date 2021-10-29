package cn.onedawn.mytrigger.request.impl;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.App;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.Request;
import cn.onedawn.mytrigger.type.RequestType;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName CallRequest.java
 * @Description TODO
 * @createTime 2021年10月29日 03:57:00
 */
@Data
@Accessors(chain = true)
public class CallRequest extends Request {
    @JSONField(name = "type")
    private RequestType type = RequestType.call;

    @JSONField(name = "job")
    private Job job;

    @JSONField(name = "app")
    private App app;

    @Override
    public void check() throws MyTriggerException {

    }

    @Override
    public String mqKey() {
        return null;
    }
}
