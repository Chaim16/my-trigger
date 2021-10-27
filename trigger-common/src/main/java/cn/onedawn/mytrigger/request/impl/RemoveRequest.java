package cn.onedawn.mytrigger.request.impl;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.App;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.pojo.User;
import cn.onedawn.mytrigger.request.Request;
import cn.onedawn.mytrigger.type.RequestType;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName RemoveRequest.java
 * @Description TODO
 * @createTime 2021年10月27日 19:40:00
 */
public class RemoveRequest extends Request {
    @JSONField(name = "type")
    private RequestType type = RequestType.remove;

    @JSONField(name = "jobId")
    private Long jobId;

    @JSONField(name = "appId")
    private Long appId;

    @JSONField(name = "userId")
    private Long userId;

    @Override
    public void check() throws MyTriggerException {

    }

    @Override
    public String mqKey() {
        return null;
    }
}
