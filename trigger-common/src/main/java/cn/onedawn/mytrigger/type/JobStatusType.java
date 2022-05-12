package cn.onedawn.mytrigger.type;

import cn.onedawn.mytrigger.exception.MyTriggerException;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName JobStatusType.java
 * @Description TODO
 * @createTime 2021年11月02日 12:50:00
 */
public enum JobStatusType {
    /** 等待调度 */
    wait("wait"),
    /** 正在调度 */
    run("run"),
    /** 已经完成 */
    finished("finished"),
    /** 暂停调度 */
    pause("pause"),
    /** 调度失败 */
    callerror("callerror");

    private String str;
    JobStatusType(String str) {
        this.str = str;
    }

    @Override
    public String toString() { return str; }

    public static JobStatusType getType(String str) throws MyTriggerException {
        for(JobStatusType type : JobStatusType.values()) {
            if(type.str.equals(str)) {
                return type;
            }
        }

        throw new MyTriggerException("unknown type, type:" + str);
    }
}
