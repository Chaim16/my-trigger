package cn.onedawn.mytrigger.pojo;

import cn.onedawn.mytrigger.type.CallType;
import cn.onedawn.mytrigger.type.JobStatusType;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName Job.java
 * @Description TODO 任务实体类
 * @createTime 2021年10月26日 09:11:00
 */
@Data
@Accessors(chain = true)
public class Job implements Serializable, Cloneable {
    @JSONField(name = "id")
    private Long id;

    @JSONField(name = "status")
    private JobStatusType status;

    @JSONField(name = "triggerTime")
    private String triggerTime;

    @JSONField(name = "remove")
    private Byte remove;

    @JSONField(name = "callName")
    private String callName;

    @JSONField(name = "callType")
    private CallType callType;

    @JSONField(name = "callData")
    private String callData;

    @JSONField(name = "callHost")
    private String callHost;

    @JSONField(name = "cron")
    private String cron;

    @JSONField(name = "createTime")
    private String createTime;

    @JSONField(name = "modifyTime")
    private String modifyTime;

    @JSONField(name = "app")
    private Long app;

    @JSONField(name = "callerrorRetryCount")
    private Integer callerrorRetryCount;

    @JSONField(name = "runRetry")
    private Integer runRetry;

    /**
     * 是否手动触发调度：0否，1是
     */
    @JSONField(name = "trigger")
    private Integer trigger;

    @Override
    public Job clone() throws CloneNotSupportedException {
        return (Job) super.clone();
    }

}
