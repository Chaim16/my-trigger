package cn.onedawn.mytrigger.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
public class App implements Serializable {
    private Long id;
    private String appName;
    private String appId;
}
