package cn.onedawn.mytrigger.pojo;

import cn.onedawn.mytrigger.type.CallType;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName Job.java
 * @Description TODO 任务实体类
 * @createTime 2021年10月26日 09:11:00
 */
@Data
public class Job implements Serializable {
    @JSONField(name = "id")
    private Long id;

    @JSONField(name = "status")
    private String status;

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

}
