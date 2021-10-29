package cn.onedawn.mytrigger.type;

import cn.onedawn.mytrigger.exception.MyTriggerException;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName CallType.java
 * @Description TODO
 * @createTime 2021年10月29日 05:24:00
 */
public enum CallType {
    /** 回调方式 */
    dubbo("dubbo"),
    http("http");

    private String str;

    CallType(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

    public static CallType getType(String str) throws MyTriggerException {
        for (CallType type : CallType.values()) {
            if (type.str.equals(str)) {
                return type;
            }
        }

        throw new MyTriggerException("unknown type, type:" + str);
    }
}
