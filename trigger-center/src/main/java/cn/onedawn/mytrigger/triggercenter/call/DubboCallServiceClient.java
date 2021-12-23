package cn.onedawn.mytrigger.triggercenter.call;

import cn.onedawn.mytrigger.api.CallHandler;
import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.App;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.CallRequest;
import cn.onedawn.mytrigger.response.Response;
import cn.onedawn.mytrigger.utils.ConstValue;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
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

    private static Logger logger = LoggerFactory.getLogger(DubboCallServiceClient.class);

    private RegistryConfig registryConfig;
    private ApplicationConfig applicationConfig;

    public DubboCallServiceClient() {
        applicationConfig = new ApplicationConfig("my-trigger-center");
        registryConfig = new RegistryConfig();
        registryConfig.setAddress(ConstValue.ZOOKEEPER_ADDRESS);
        registryConfig.setProtocol("zookeeper");
        registryConfig.setTimeout(10000);
    }

    /* appName, ReferenceConfig*/
    private Map<String, ReferenceConfig<CallHandler>> referenceConfigMap = new HashMap<>();
    /* <appName, callHandler> */
    private volatile ConcurrentHashMap<String, CallHandler> callHandlerMap = new ConcurrentHashMap<>();

    private static final Object callHandlerMapLock = new Object();

    public synchronized ReferenceConfig getReferenceConfig(String appName, String callHost) {
        String key;
        if (StringUtils.isNotBlank(callHost)) {
            key = appName + "_" + callHost;
        } else {
            key = appName;
        }
        if (referenceConfigMap.containsKey(key)) {
            return referenceConfigMap.get(key);
        }
        /* 重新建立ReferenceConfig */
        ReferenceConfig referenceConfig = new ReferenceConfig<CallHandler>();
        referenceConfig.setInterface(CallHandler.class);
        referenceConfig.setVersion("1.0.0");
        referenceConfig.setTimeout(5000);

        if (StringUtils.isNotBlank(callHost)) {
            String url = ConstValue.DUBBO_PROTOCOL_HEAD + callHost + "/" + CallHandler.class.getCanonicalName();
            referenceConfig.setUrl(url);
        }
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setProtocol(ConstValue.DUBBO_PROTOCOL);
        referenceConfig.setRegistry(registryConfig);
        referenceConfigMap.put(key, referenceConfig);
        return referenceConfig;
    }

    public Response callback(CallRequest callRequest) throws MyTriggerException {
        App app = callRequest.getApp();
        Job job = callRequest.getJob();

        ReferenceConfig<CallHandler> referenceConfig = getReferenceConfig(app.getAppName(), job.getCallHost());
        CallHandler callbackService = null;
        callbackService = getCallHandler(referenceConfig, app.getAppName(), job.getCallHost());
        if (callbackService == null) {
            logger.error("mytrigger center callback by dubbo can't find service task: {}", JSON.toJSONString(callRequest));
            throw new MyTriggerException("mytrigger center callback by dubbo can't find service");
        }
        return callbackService.handle(callRequest);
    }

    private CallHandler getCallHandler(ReferenceConfig<CallHandler> referenceConfig, String appName, String callHost) {
        String key;
        if (StringUtils.isNotBlank(callHost)) {
            key = appName + "_" + callHost;
        } else {
            key = appName;
        }
        if (callHandlerMap.containsKey(key)) {
            return callHandlerMap.get(key);
        }
        synchronized (callHandlerMapLock) {
            if (callHandlerMap.containsKey(key)) {
                return callHandlerMap.get(key);
            }
            CallHandler callHandler = referenceConfig.get();
            callHandlerMap.put(key, callHandler);
            return callHandler;
        }
    }

}
