package cn.onedawn.mytrigger.utils;

import cn.onedawn.mytrigger.cron.CronExpression;
import cn.onedawn.mytrigger.exception.MyTriggerException;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName CronUtil.java
 * @Description TODO
 * @createTime 2021年10月26日 11:17:00
 */
public class CronUtil {

    /**
     * 根据表达式转换成时间
     */
    public static Date formatDateByCron(String cron, long time) throws MyTriggerException {
        try {
            // 如果是周期任务，则设置后面的时间，防止被后台线程拿到
            if (checkCronOneTime(cron)) {
                return getOnceTime(cron);
            } else {
                return getLoopTime(cron, time);
            }

        } catch (Exception e) {
            throw new MyTriggerException("cron:" + cron, e);
        }
    }

    /** 获取一次性时间 */
    public static Date getOnceTime(String cron) throws ParseException {
        String dateFormat = "ss mm HH dd MM ? yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.parse(cron);
    }

    /** 获取周期性时间 */
    public static Date getLoopTime(String cron, long time) throws ParseException {
        CronExpression cronExpression = new CronExpression(cron);
        return cronExpression.getNextValidTimeAfter(new Date(time));
    }


    /**
     * 判断用户提交的表达式类型是周期性的还是一次性的
     */
    public static boolean checkCronOneTime(String cron) throws MyTriggerException {
        if (StringUtils.isBlank(cron)) {
            return false;
        }
        String[] items = cron.split(" ");
        int length = items.length;
        if (items.length < 6) {
            throw new MyTriggerException("error cron expression format");
        }
        boolean result = true;
        for (int i = 0; i < length; i++) {
            result &= !containsCommonChar(items[i]);
        }
        return result;
    }

    /**
     * 判断单个cron字段是否含有周期性特征
     */
    private static boolean containsCommonChar(String cron) {
        char[] invalidCharList = new char[] {',', '-', '*', '/'};
        if (cron == null) {
            return false;
        }
        for (char chr : invalidCharList) {
            if (cron.indexOf(chr) > -1) {
                return true;
            }
        }
        return false;
    }
}
