package cn.onedawn.mytrigger;

import static org.junit.Assert.assertTrue;

import cn.onedawn.mytrigger.call.HTTPCallListener;
import org.apache.dubbo.config.*;
import org.junit.Test;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void httpCallTest() throws IOException {
        HTTPCallListener httpCallListener = new HTTPCallListener();
        httpCallListener.init();
    }

    @Test
    public void dubboCallTest() throws IOException {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("192.168.4.103:2181");
        registryConfig.setProtocol("zookeeper");
        registryConfig.setTimeout(10000);

        ProtocolConfig protocolConfig = new ProtocolConfig("dubbo", 21886);
        protocolConfig.setThreads(2);
        ApplicationConfig applicationConfig = new ApplicationConfig("test");

        ServiceConfig serviceConfig = new ServiceConfig<cn.onedawn.mytrigger.Test>();
        serviceConfig.setApplication(applicationConfig);
        serviceConfig.setVersion("1.0.0");
        serviceConfig.setRegistry(registryConfig);
        serviceConfig.setProtocol(protocolConfig);
        serviceConfig.setInterface(cn.onedawn.mytrigger.Test.class);
        serviceConfig.setRef(new TestImpl());
        serviceConfig.export();
        System.out.println("end");
    }
}
