package cn.onedawn.mytrigger.response;

import cn.onedawn.mytrigger.type.ResponseType;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName Reseponse.java
 * @Description TODO
 * @createTime 2021年10月26日 08:45:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Response implements Serializable {
    @JSONField(name = "type")
    private ResponseType type = ResponseType.common;

    @JSONField(name = "success")
    private boolean success = false;

    @JSONField(name = "info")
    private String info;

    @JSONField(name = "time")
    private Long time;

    public Response(ResponseType type, boolean success) {
        this.type = type;
        this.success = success;
    }
}
