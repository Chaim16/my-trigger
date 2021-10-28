package cn.onedawn.mytrigger.type;

import cn.onedawn.mytrigger.exception.MyTriggerException;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName AckType.java
 * @Description TODO
 * @createTime 2021年10月28日 01:09:00
 */
public enum AckType {
    /** dubbo 和 mq方式ack */
    dubbo("dubbo"),
    mq("mq");

    private String str;

    AckType(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

    public static AckType getType(String str) throws MyTriggerException {
        for (AckType type : AckType.values()) {
            if (type.str.equals(str)) {
                return type;
            }
        }

        throw new MyTriggerException("unknown type, type:" + str);
    }
}
