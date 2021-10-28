package cn.onedawn.mytrigger.submit;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.request.impl.RegisterRequest;
import cn.onedawn.mytrigger.utils.ConstValue;
import cn.onedawn.mytrigger.utils.StatusCode;
import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName HTTPSubmit.java
 * @Description TODO
 * @createTime 2021年10月28日 12:33:00
 */
public class HTTPStrategy {

    public static boolean submit(Job job) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setJob(job);
        Map<String, Object> formMap = new HashMap<>();
        formMap.put("data", JSON.toJSONString(registerRequest));
        String url = ConstValue.BASE_URL + "/job/register";
        HttpResponse response = HttpRequest.post(url)
                .form(formMap)
                .timeout(5000)
                .execute();
        return response.getStatus() == StatusCode.SUCCESS;
    }
}
