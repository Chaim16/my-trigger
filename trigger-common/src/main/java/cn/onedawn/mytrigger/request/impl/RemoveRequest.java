package cn.onedawn.mytrigger.request.impl;

import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.request.Request;
import cn.onedawn.mytrigger.type.RequestType;
import cn.onedawn.mytrigger.utils.NumberUtils;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName RemoveRequest.java
 * @Description TODO
 * @createTime 2021年10月27日 19:40:00
 */
@Data
public class RemoveRequest extends Request {
    @JSONField(name = "type")
    private RequestType type = RequestType.remove;

    @JSONField(name = "jobId")
    private Long jobId;

    @Override
    public void check() throws MyTriggerException {
        NumberUtils.isValidLong(String.valueOf(jobId), "jobId isn't valid Long");
    }

    @Override
    public String mqKey() {
        return null;
    }
}
