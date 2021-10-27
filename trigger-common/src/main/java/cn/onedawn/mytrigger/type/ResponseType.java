package cn.onedawn.mytrigger.type;

import cn.onedawn.mytrigger.exception.MyTriggerException;
/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName ResponseType.java
 * @Description TODO
 * @createTime 2021年10月26日 08:39:00
 */
public enum ResponseType {
    /** 一般响应 */
    common("common"),
    /** 注册任务 */
    register("register"),
    /** 修改任务 */
    modify("modify"),
    /** 调度任务 */
    trigger("trigger"),
    /** 逻辑删除任务 */
    remove("remove"),
    /** 暂停任务 */
    pause("pause"),
    /** 回调任务 */
    callback("callback"),
    /** ack应答 */
    ack("ack");


    private String str;

    ResponseType(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

    public static ResponseType getType(String str) throws MyTriggerException {
        for (ResponseType type : ResponseType.values()) {
            if (type.str.equals(str)) {
                return type;
            }
        }
        throw new MyTriggerException("unknown type, type:" + str);
    }
}
