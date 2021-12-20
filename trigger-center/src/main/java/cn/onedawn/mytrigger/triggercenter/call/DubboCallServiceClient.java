package cn.onedawn.mytrigger.triggercenter.call;

import cn.onedawn.mytrigger.api.DubboService;
import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.App;
import cn.onedawn.mytrigger.request.impl.CallRequest;
import cn.onedawn.mytrigger.response.Response;
import cn.onedawn.mytrigger.utils.ConstValue;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName DubboCallServiceConsumer.java
 * @Description TODO
 * @createTime 2021年10月29日 06:14:00
 */
@Component
public class DubboCallServiceClient {

    private ReferenceConfig<DubboService> referenceConfig;
    private RegistryConfig registryConfig;
    private ApplicationConfig applicationConfig;

    /* <appName, callHandler> */
    private volatile ConcurrentHashMap<String, DubboService> callHandlerMap = new ConcurrentHashMap<>();

    private static final Object callHandlerMapLock = new Object();

    public synchronized ReferenceConfig getReferenceConfig() {
        applicationConfig = new ApplicationConfig("my-trigger-center");

        registryConfig = new RegistryConfig();
        registryConfig.setAddress(ConstValue.ZOOKEEPER_ADDRESS);
        registryConfig.setProtocol("zookeeper");
        registryConfig.setTimeout(10000);

        referenceConfig = new ReferenceConfig<DubboService>();
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setProtocol(ConstValue.DUBBO_PROTOCOL);
        referenceConfig.setInterface(DubboService.class);
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setVersion("1.0.0");
        referenceConfig.setTimeout(5000);

        return referenceConfig;
    }

    public Response callback(CallRequest callRequest) throws MyTriggerException {
        ReferenceConfig<DubboService> referenceConfig = getReferenceConfig();
        DubboService callbackService = null;
        callbackService = getCallHandler(referenceConfig, callRequest.getApp());

        if (callbackService != null) {
            return callbackService.handle(callRequest);
        } else {
            throw new MyTriggerException("[callback] faild");
        }
    }

    private DubboService getCallHandler(ReferenceConfig<DubboService> referenceConfig, App app) {
        if (callHandlerMap.containsKey(app.getAppName())) {
            return callHandlerMap.get(app.getAppName());
        }
        synchronized (callHandlerMapLock) {
            if (callHandlerMap.containsKey(app.getAppName())) {
                return callHandlerMap.get(app.getAppName());
            }
            DubboService dubboService = referenceConfig.get();
            callHandlerMap.put(app.getAppName(), dubboService);
            return dubboService;
        }
    }

}
