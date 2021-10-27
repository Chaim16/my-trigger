package cn.onedawn.mytrigger.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName APP.java
 * @Description TODO
 * @createTime 2021年10月26日 16:35:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class APP implements Serializable {
    private Long id;
    private String appName;
    private String appId;
}
