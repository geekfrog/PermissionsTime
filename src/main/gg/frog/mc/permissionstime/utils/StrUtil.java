package gg.frog.mc.permissionstime.utils;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StrUtil {

    public static String messageFormat(String src, Object... args) {
        return MessageFormat.format(src, args).replace("&", "§");
    }

    public static String timestampToString(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(new Date(time));
    }
}
