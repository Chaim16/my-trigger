package cn.onedawn.mytrigger.utils;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName ConstValue.java
 * @Description TODO
 * @createTime 2021年10月26日 10:54:00
 */
public class ConstValue {
    public static final String REQUEST_DATA = "data";

    public static final String SERVER = "127.0.0.1";
    public static final String SERVER_PORT = "8080";
    public static final String BASE_URL = "http://" + ConstValue.SERVER + ":" + ConstValue.SERVER_PORT;

    public static final String NAMESERVADDR = "http://127.0.0.1:9876";

    public static final String ZOOKEEPER_ADDRESS = "http://127.0.0.1:2181";
    public static final String ZOOKEEPER_USER = "root";
    public static final String ZOOKEEPER_PASSWORD = "123456";

    public static final String DUBBO_PROTOCOL = "dubbo";
    public static final int DUBBO_PROTOCOL_PORT = 21816;

}
