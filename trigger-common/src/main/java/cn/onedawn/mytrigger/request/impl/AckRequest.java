package cn.onedawn.mytrigger.request.impl;

import cn.onedawn.mytrigger.pojo.App;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.pojo.User;
import cn.onedawn.mytrigger.request.Request;
import cn.onedawn.mytrigger.type.RequestType;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName AckRequest.java
 * @Description TODO
 * @createTime 2021年10月26日 09:08:00
 */
@Data
public class AckRequest extends Request {

    @JSONField(name = "type")
    private RequestType type = RequestType.ack;

    @JSONField(name = "job")
    private Long jobId;

    @Override
    public void check() {

    }

    @Override
    public String mqKey() {
        return null;
    }
}
