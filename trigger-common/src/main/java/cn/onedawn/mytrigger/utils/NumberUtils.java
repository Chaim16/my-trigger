package cn.onedawn.mytrigger.utils;

import cn.onedawn.mytrigger.exception.MyTriggerException;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName NumberUtils.java
 * @Description TODO
 * @createTime 2021年12月19日 22:55:00
 */
public class NumberUtils {

    public static boolean isValidLong(String str, String emsg) throws MyTriggerException {
        try{
            Long.parseLong(str);
            return true;
        }catch(NumberFormatException e){
            throw new MyTriggerException(emsg);
        }
    }
}
