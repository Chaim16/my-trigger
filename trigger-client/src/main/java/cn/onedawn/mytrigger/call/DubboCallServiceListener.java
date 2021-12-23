package cn.onedawn.mytrigger.call;

import cn.onedawn.mytrigger.api.CallHandler;
import cn.onedawn.mytrigger.pojo.App;
import cn.onedawn.mytrigger.utils.ConstValue;
import org.apache.dubbo.config.ApplicationConfig;
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
    private ServiceConfig<CallHandler> serviceConfig;
    private ApplicationConfig applicationConfig;
    private ProtocolConfig protocolConfig;

    private App app;

    public void init() {
        registryConfig = new RegistryConfig();
        registryConfig.setAddress(ConstValue.ZOOKEEPER_ADDRESS);
        registryConfig.setProtocol("zookeeper");
        registryConfig.setTimeout(10000);

        protocolConfig = new ProtocolConfig(ConstValue.DUBBO_PROTOCOL, ConstValue.DUBBO_PROTOCOL_PORT);
        applicationConfig = new ApplicationConfig("my-trigger-client-" + this.app.getAppName());

        serviceConfig = new ServiceConfig<CallHandler>();
        serviceConfig.setApplication(applicationConfig);
        serviceConfig.setRegistry(registryConfig);
        serviceConfig.setProtocol(protocolConfig);
        serviceConfig.setInterface(CallHandler.class);
        serviceConfig.setRef(new CallHandlerImpl());
        serviceConfig.setVersion("1.0.0");
        serviceConfig.export();

        System.out.println("dubbo call listener prepare ok");
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }
}
