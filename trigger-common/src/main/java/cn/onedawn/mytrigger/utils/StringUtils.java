package cn.onedawn.mytrigger.utils;

import cn.onedawn.mytrigger.exception.MyTriggerException;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName StringUtils.java
 * @Description TODO
 * @createTime 2021年10月26日 15:36:00
 */
public class StringUtils {

    public static boolean isEmpty(String str, String emsg) throws MyTriggerException {
        if (str == null || str.length() == 0) {
            if (emsg == null) {
                return true;
            } else {
                throw new MyTriggerException(emsg);
            }
        }
        return false;
    }
}
