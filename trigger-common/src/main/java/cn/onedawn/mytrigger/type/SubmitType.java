package cn.onedawn.mytrigger.type;

import cn.onedawn.mytrigger.exception.MyTriggerException;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName SubmitType.java
 * @Description TODO
 * @createTime 2021年10月26日 10:07:00
 */
public enum SubmitType {
    /**
     * http提交
     */
    HTTP("http"),
    /**
     * mq提交
     */
    MQ("mq");

    private String str;

    SubmitType(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

    public static SubmitType getType(String str) throws MyTriggerException {
        for (SubmitType type : SubmitType.values()) {
            if (type.str.equals(str)) {
                return type;
            }
        }

        throw new MyTriggerException("unknown type, type:" + str);
    }
}
