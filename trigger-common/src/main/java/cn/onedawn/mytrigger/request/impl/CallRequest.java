package cn.onedawn.mytrigger.request.impl;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.Application;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.Request;
import cn.onedawn.mytrigger.type.CallType;
import cn.onedawn.mytrigger.type.RequestType;
import cn.onedawn.mytrigger.utils.CronUtil;
import cn.onedawn.mytrigger.utils.StringUtils;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName CallRequest.java
 * @Description TODO
 * @createTime 2021年10月29日 03:57:00
 */
@Data
@Accessors(chain = true)
public class CallRequest extends Request implements Serializable {
    @JSONField(name = "type")
    private RequestType type = RequestType.call;

    @JSONField(name = "job")
    private Job job;

    @JSONField(name = "app")
    private Application app;

    @Override
    public void check() throws MyTriggerException {
        if (job != null) {
            StringUtils.isEmpty(String.valueOf(job.getApp()), "app is empty");
            StringUtils.isEmpty(String.valueOf(job.getCallName()), "callName is empty");
            StringUtils.isEmpty(String.valueOf(job.getCron()), "cron is empty");
            StringUtils.isEmpty(String.valueOf(job.getCallType()), "callType is empty");
            if (job.getCallType() == CallType.http) {
                StringUtils.isEmpty(String.valueOf(job.getCallHost()), "callHost is empty");
            }
        }
        if (app != null) {
            StringUtils.isEmpty(String.valueOf(app.getName()), "appName is empty");
            StringUtils.isEmpty(String.valueOf(app.getCreateTime()), "appId is empty");
        }
    }

    @Override
    public String mqKey() {
        return null;
    }
}
