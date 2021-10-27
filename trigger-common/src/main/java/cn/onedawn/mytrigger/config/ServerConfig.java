package cn.onedawn.mytrigger.config;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName ServerConfig.java
 * @Description TODO 服务配置类
 * @createTime 2021年10月26日 09:19:00
 */
@Data
public class ServerConfig {

    @JSONField(name = "dubboRegisterIp")
    private String dubboRegisterIp;

    @JSONField(name = "dubboTimeout")
    private int dubboTimeout;

    @JSONField(name = "ackDubboServerPort")
    private int ackDubboServerPort;

    @JSONField(name = "httpServerHost")
    private String httpServerHost;

    @JSONField(name = "submitMQZkServer")
    private String submitMQZkServer;

    @JSONField(name = "submitMQTopic")
    private String submitMQTopic;

    @JSONField(name = "nameserverAddress")
    private String nameserverAddress;

    @JSONField(name = "dubboThreadCount")
    private int dubboThreadCount;
}
