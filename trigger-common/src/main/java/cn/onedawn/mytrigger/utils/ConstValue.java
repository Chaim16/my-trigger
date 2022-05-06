package cn.onedawn.mytrigger.utils;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName ConstValue.java
 * @Description TODO
 * @createTime 2021年10月26日 10:54:00
 */
public class ConstValue {
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String REQUEST_DATA = "data";

    public static final String SERVER = "192.168.8.1";
    public static final String SERVER_PORT = "8080";
    public static final String BASE_URL = "http://" + ConstValue.SERVER + ":" + ConstValue.SERVER_PORT;

    public static final String NAMESERVADDR = "http://192.168.8.130:9876";

    public static final String ZOOKEEPER_ADDRESS = "192.168.8.122:2181";
    public static final String ZOOKEEPER_USER = "root";
    public static final String ZOOKEEPER_PASSWORD = "123456";

    public static final String DUBBO_PROTOCOL = "dubbo";
    public static final int DUBBO_PROTOCOL_PORT = 21886;
    public static final String DUBBO_PROTOCOL_HEAD = "dubbo://";

    public static final String HTTP_CALL_SERVER_PORT = "9090";
}
