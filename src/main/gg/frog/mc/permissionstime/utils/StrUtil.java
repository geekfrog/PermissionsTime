package gg.frog.mc.permissionstime.utils;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

public class StrUtil {

    private static String dfs = "yyyy/MM/dd HH:mm:ss";

    public static String messageFormat(String src, Object... args) {
        return MessageFormat.format(src, args).replace("&", "§");
    }

    public static String timestampToString(long time) {
        return DateFormatUtils.format(new Date(time), dfs);
    }

    public static String dateToString(Date d) {
        return DateFormatUtils.format(d, dfs);
    }
    
    public static String nowTimeString() {
        return DateFormatUtils.format(new Date(), dfs);
    }
}
