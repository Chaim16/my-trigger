package cn.onedawn.mytrigger.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName User.java
 * @Description TODO
 * @createTime 2021年10月26日 16:41:00
 */
@Data
public class User implements Serializable {
    @JSONField(name = "id")
    private Long id;

    @JSONField(name = "username")
    private String username;

    @JSONField(name = "password")
    private String password;

    @JSONField(name = "prohibit")
    private byte prohibit;
}
