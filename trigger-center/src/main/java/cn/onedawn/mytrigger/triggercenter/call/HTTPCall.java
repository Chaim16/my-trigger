package cn.onedawn.mytrigger.triggercenter.call;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.onedawn.mytrigger.pojo.App;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.CallRequest;
import cn.onedawn.mytrigger.response.Response;
import cn.onedawn.mytrigger.type.CallType;
import cn.onedawn.mytrigger.utils.ConstValue;
import cn.onedawn.mytrigger.utils.StatusCode;
import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, Object> formMap = new HashMap<>();
        formMap.put(ConstValue.REQUEST_DATA, JSON.toJSONString(callRequest));
        String url = "http://" + callRequest.getJob().getCallHost() + ":" + ConstValue.HTTPCALL_SERVER_PORT + "/call";

        long start = System.currentTimeMillis();
//        String url = "http://" + callRequest.getJob().getCallHost() + ":" + ConstValue.HTTPCALL_SERVER_PORT;
        HttpResponse execute = HttpRequest.post(url)
//                .form(formMap)
                .contentType("multipart/form-data;charset=utf-8")
                .timeout(3000)
                .execute();
        long end = System.currentTimeMillis();

        response.setSuccess(execute.getStatus() == StatusCode.SUCCESS);
        response.setTime(end - start);
        return response;
    }

    public static void main(String[] args) {
        Job job = new Job()
                .setId(5L)
                .setCallType(CallType.http)
                .setCallName("789")
                .setCallHost("127.0.0.1")
                .setRemove((byte) 0)
                .setCron("* * * * * ?")
                .setStatus("run");
        CallRequest callRequest = new CallRequest();
        App app = new App().setId(1L).setAppName("89").setAppId("890");
        callRequest.setApp(app).setJob(job);

        Response call = HTTPCall.call(callRequest);
        System.out.println(call.isSuccess());
        System.out.println("-------------------");
    }

}
