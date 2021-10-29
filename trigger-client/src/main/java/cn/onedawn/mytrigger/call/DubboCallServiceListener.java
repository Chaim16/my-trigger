package cn.onedawn.mytrigger.call;

import cn.onedawn.mytrigger.api.DubboService;
import cn.onedawn.mytrigger.utils.ConstValue;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName DubboCallServiceConfig.java
 * @Description TODO
 * @createTime 2021年10月29日 05:37:00
 */
public class DubboCallServiceListener {

    private RegistryConfig registryConfig;
    private ServiceConfig<DubboService> serviceConfig;

    public void init() {
        registryConfig = new RegistryConfig();
        registryConfig.setAddress(ConstValue.ZOOKEEPER_ADDRESS);
        registryConfig.setUsername(ConstValue.ZOOKEEPER_USER);
        registryConfig.setPassword(ConstValue.ZOOKEEPER_PASSWORD);

        serviceConfig = new ServiceConfig<DubboService>();
        serviceConfig.setRegistry(registryConfig);
        serviceConfig.setProtocol(new ProtocolConfig(ConstValue.DUBBO_PROTOCOL, ConstValue.DUBBO_PROTOCOL_PORT));
        serviceConfig.setInterface(DubboService.class);
        serviceConfig.setRef(new DubboCallServiceImpl());
        serviceConfig.setVersion("1.0.0");
        serviceConfig.export();
    }
}
