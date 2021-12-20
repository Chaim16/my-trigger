package cn.onedawn.mytrigger.call;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.server.SimpleServer;
import cn.onedawn.mytrigger.utils.ConstValue;

import java.io.IOException;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName HTTPCallListener.java
 * @Description TODO
 * @createTime 2021年10月29日 14:23:00
 */
public class HTTPCallListener {

    private SimpleServer server;

    public void init() throws IOException {
        server = HttpUtil.createServer(Integer.parseInt(ConstValue.HTTPCALL_SERVER_PORT));
        server.addAction("/call", new HTTPCallAction())
                .start();
        System.out.println("http call listener preapare ok");
    }

}
