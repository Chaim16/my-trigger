package cn.onedawn.mytrigger.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class Application implements Serializable {
    private Long id;
    private String name;
    private String createTime;
    private String modifyTime;
}
