package cn.onedawn.mytrigger.request.impl;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.Application;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.pojo.User;
import cn.onedawn.mytrigger.request.Request;
import cn.onedawn.mytrigger.type.RequestType;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName CommonRequest.java
 * @Description TODO
 * @createTime 2021年10月28日 11:25:00
 */
@Data
public class CommonRequest extends Request {
    @JSONField(name = "type")
    private RequestType type = RequestType.common;

    @JSONField(name = "job")
    private Job job;

    @JSONField(name = "app")
    private Application app;

    @JSONField(name = "user")
    private User user;

    @Override
    public void check() throws MyTriggerException {

    }

    @Override
    public String mqKey() {
        return null;
    }
}
