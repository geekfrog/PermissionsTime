package gg.frog.mc.permissionstime.utils;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import gg.frog.mc.permissionstime.config.LangCfg;
import gg.frog.mc.permissionstime.database.IPlayerDataDao;
import me.clip.placeholderapi.PlaceholderAPI;

public class StrUtil {

    private static final String dfs = "yyyy/MM/dd HH:mm:ss";
    private static final long dt = 24 * 60 * IPlayerDataDao.TIME_UNIT;
    private static final long ht = 60 * IPlayerDataDao.TIME_UNIT;
    private static final long mt = IPlayerDataDao.TIME_UNIT;
    private static final boolean placeholderAPI;
    
    static {
    	if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
    		placeholderAPI = true;    		
    	}else {
    		placeholderAPI = false;
    	}
    }

    public static String messageFormat(String src, Object... args) {
    	return MessageFormat.format(src, args).replace("&", "§").replace("\\n", "\n");
    }

    public static String messageFormat(Player player, String src, Object... args) {
    	String message = MessageFormat.format(src, args).replace("&", "§").replace("\\n", "\n").replace("%player%", player.getDisplayName());
    	if(placeholderAPI) {
    		message = PlaceholderAPI.setPlaceholders(player, message);
    	}
    	return message;
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

    public static String getLeftTime(long time) {
        long leftTime = time - new Date().getTime();
        long d = leftTime / dt;
        long h = (leftTime % dt) / ht;
        long m = (leftTime % ht) / mt;
        return messageFormat(LangCfg.LEFT_TIME, d, LangCfg.TIME_UNIT_D, h, LangCfg.TIME_UNIT_H, m, LangCfg.TIME_UNIT_M);
    }

}
