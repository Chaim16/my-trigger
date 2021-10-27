package cn.onedawn.mytrigger.request.impl;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.APP;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.pojo.User;
import cn.onedawn.mytrigger.request.Request;
import cn.onedawn.mytrigger.type.RequestType;
import cn.onedawn.mytrigger.utils.CronUtil;
import cn.onedawn.mytrigger.utils.StringUtils;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName RegisterRequest.java
 * @Description TODO
 * @createTime 2021年10月26日 11:07:00
 */
@Data
public class RegisterRequest extends Request implements Serializable {
    @JSONField(name = "type")
    private RequestType type = RequestType.register;

    @JSONField(name = "data")
    private Job job;

    @JSONField(name = "app")
    private APP app;

    @JSONField(name = "user")
    private User user;

    @Override
    public void check() throws MyTriggerException {
        StringUtils.isEmpty(String.valueOf(job.getApp()), "appId is empty");
        StringUtils.isEmpty(String.valueOf(job.getCallName()), "callName is empty");
        StringUtils.isEmpty(String.valueOf(job.getCron()), "cron is empty");
        StringUtils.isEmpty(String.valueOf(job.getCallHost()), "callHost is empty");
        Date cronDate = CronUtil.formatDateByCron(job.getCron(), System.currentTimeMillis());
        if (cronDate.getTime() < System.currentTimeMillis()) {
            throw new MyTriggerException("cron time before current time: date:" + cronDate);
        }
    }

    @Override
    public String mqKey() {
        return null;
    }
}
