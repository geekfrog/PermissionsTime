package gg.frog.mc.permissionstime.utils;

import java.text.MessageFormat;

public class StrUtil {

    public static String messageFormat(String src, Object... args){
        return MessageFormat.format(src.replace("&", "§"), args);
    }
}
