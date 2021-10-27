package cn.onedawn.mytrigger.request.impl;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.App;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.pojo.User;
import cn.onedawn.mytrigger.request.Request;
import cn.onedawn.mytrigger.type.RequestType;
import cn.onedawn.mytrigger.utils.CronUtil;
import cn.onedawn.mytrigger.utils.StringUtils;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName ModifyRequest.java
 * @Description TODO
 * @createTime 2021年10月27日 19:21:00
 */
@Data
public class ModifyRequest extends Request {
    @JSONField(name = "type")
    private RequestType type = RequestType.modify;

    @JSONField(name = "job")
    private Job job;

    @JSONField(name = "app")
    private App app;

    @JSONField(name = "user")
    private User user;

    @Override
    public void check() throws MyTriggerException {
        if (job != null) {
            StringUtils.isEmpty(String.valueOf(job.getApp()), "appId is empty");
            StringUtils.isEmpty(String.valueOf(job.getCallName()), "callName is empty");
            StringUtils.isEmpty(String.valueOf(job.getCron()), "cron is empty");
            StringUtils.isEmpty(String.valueOf(job.getCallHost()), "callHost is empty");
            Date cronDate = CronUtil.formatDateByCron(job.getCron(), System.currentTimeMillis());
            if (cronDate.getTime() < System.currentTimeMillis()) {
                throw new MyTriggerException("cron time before current time: date:" + cronDate);
            }
        }
    }

    @Override
    public String mqKey() {
        return null;
    }
}
