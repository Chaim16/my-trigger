package cn.onedawn.mytrigger.triggercenter.call;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.onedawn.mytrigger.request.impl.CallRequest;
import cn.onedawn.mytrigger.response.Response;
import cn.onedawn.mytrigger.utils.ConstValue;
import cn.onedawn.mytrigger.utils.StatusCode;
import com.alibaba.fastjson.JSON;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName HTTPCall.java
 * @Description TODO http方式进行回调
 * @createTime 2021年10月29日 14:11:00
 */
public class HTTPCall {

    public static Response call(CallRequest callRequest) {
        Response response = new Response();
        String[] hosts = callRequest.getJob().getCallHost().split(";");
        // 随机挑选一个主机进行调度
        Random random = new Random();
        int index = Math.abs(random.nextInt() % hosts.length);
        String callHost = hosts[index];
        String url = "http://" + callHost + ":" + ConstValue.HTTP_CALL_SERVER_PORT + "/call";

        long start = System.currentTimeMillis();
        HttpResponse execute = HttpRequest.post(url)
                .contentType("application/json;charset=utf-8")
                .body(JSON.toJSONString(callRequest).getBytes(StandardCharsets.UTF_8))
                .timeout(10000)
                .execute();
        long end = System.currentTimeMillis();

        response.setSuccess(execute.getStatus() == StatusCode.SUCCESS);
        response.setTime(end - start);
        return response;
    }

}
