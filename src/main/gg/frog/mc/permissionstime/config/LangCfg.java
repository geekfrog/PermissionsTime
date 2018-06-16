package gg.frog.mc.permissionstime.config;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.utils.config.PluginConfig;

/**
 * 语言支持
 * 
 * @author QiaoPengyu
 *
 */
public class LangCfg extends PluginConfig {

    public static String INVENTORY_NAME = null;
    public static String TAG_INVENTORY_NAME = null;
    public static String EXPIRATION_TIME = null;
    public static String LEFT_TIME = null;
    public static String TIME_UNIT_D = null;
    public static String TIME_UNIT_H = null;
    public static String TIME_UNIT_M = null;
    public static String TIME_FOREVER = null;
    public static String TAG = null;

    public static String MSG_PARAMETER_MISMATCH = null;
    public static String MSG_TIME_PARAMETER_INCORRECT = null;
    public static String MSG_TIME_UNIT_PARAMETER_INCORRECT = null;
    public static String MSG_PROCESSING = null;
    public static String MSG_NO_DATA = null;
    public static String MSG_PACKAGE_NUM = null;
    public static String MSG_PACKAGE_LIST = null;
    public static String MSG_PACKAGE_DETAIL = null;
    public static String MSG_NO_PERMISSION = null;
    public static String MSG_CONFIG_RELOADED = null;
    public static String MSG_FAIL_SET_PERMISSION = null;
    public static String MSG_NO_FIND_PLAYER = null;
    public static String MSG_NO_FIND_PACKAGE = null;
    public static String MSG_TELL_GIVE_PACKAGE = null;
    public static String MSG_GIVE_PACKAGE = null;
    public static String MSG_GIVE_PACKAGE_FAIL = null;
    public static String MSG_TELL_SET_PACKAGE = null;
    public static String MSG_SET_PACKAGE = null;
    public static String MSG_SET_PACKAGE_FAIL = null;
    public static String MSG_TELL_DEL_PACKAGE = null;
    public static String MSG_DEL_PACKAGE = null;
    public static String MSG_DEL_PACKAGE_FAIL = null;
    public static String MSG_TELL_DEL_ALL = null;
    public static String MSG_DEL_ALL = null;
    public static String MSG_DEL_ALL_FAIL = null;
    public static String MSG_NUM_OF_PACKAGES = null;
    public static String MSG_EXPIRATION_DATE = null;
    public static String MSG_UNKNOWN_PACKAGE = null;
    public static String MSG_IS_EXPIRATION_DATE = null;
    public static String MSG_FUNC_DISABLED = null;
    public static String MSG_TAG_SET_SUCCESS = null;

    public static String CMD_HELP = null;
    public static String CMD_ME = null;
    public static String CMD_TAG = null;
    public static String CMD_PACKAGES = null;
    public static String CMD_GET = null;
    public static String CMD_GIVE = null;
    public static String CMD_SET = null;
    public static String CMD_REMOVE = null;
    public static String CMD_REMOVEALL = null;
    public static String CMD_RELOAD = null;

    public LangCfg(String fileName, PluginMain pm) {
        super(fileName, pm);
    }

    @Override
    protected void init() {}

    @Override
    protected void loadToDo() {
        INVENTORY_NAME = getConfig().getString("inventoryName", "&4===Permissions Packages===");
        TAG_INVENTORY_NAME = getConfig().getString("tagInventoryName", "&4===Tag Packages===");
        EXPIRATION_TIME = getConfig().getString("expirationTime", "&4Expiration time: {0}");
        LEFT_TIME = getConfig().getString("leftTime", "&4Left time: About {0}{1} {2}{3} {4}{5}");
        TIME_UNIT_D = getConfig().getString("timeUnitD", "day(s)");
        TIME_UNIT_H = getConfig().getString("timeUnitH", "hour(s)");
        TIME_UNIT_M = getConfig().getString("timeUnitM", "minute(s)");
        TIME_FOREVER = getConfig().getString("timeForever", "Forever");
        TAG = getConfig().getString("tag", "Tag/Prefix");

        MSG_PARAMETER_MISMATCH = getConfig().getString("msg.parameterMismatch", "&4Parameter mismatch.");
        MSG_TIME_PARAMETER_INCORRECT = getConfig().getString("msg.timeParameterIncorrect", "&4The number of time is incorrect. Please enter a nonzero integer.");
        MSG_TIME_UNIT_PARAMETER_INCORRECT = getConfig().getString("msg.timeUnitParameterIncorrect", "&4The number of time unit is incorrect. Please use d/h/m. (d=day, h=hour, m=minute)");
        MSG_PROCESSING = getConfig().getString("msg.processing", "&2Please wait for processing...");
        MSG_NO_DATA = getConfig().getString("msg.noData", "&4No data for packages.");
        MSG_PACKAGE_NUM = getConfig().getString("msg.packageNum", "&4There are {0} kinds of permissions packages.");
        MSG_PACKAGE_LIST = getConfig().getString("msg.packageList", "{0}packageName: {1}, displayName: {2}");
        MSG_PACKAGE_DETAIL = getConfig().getString("msg.packageDetail", "packageName: {0}, displayName: {1}&r\\nPermissions: {2}\\nGroups: {3}");
        MSG_NO_PERMISSION = getConfig().getString("msg.nopermission", "&4You do not have permission to do this.");
        MSG_CONFIG_RELOADED = getConfig().getString("msg.configReloaded", "&2Configuration overload is complete.");
        MSG_FAIL_SET_PERMISSION = getConfig().getString("msg.failSetPermission", "&4Failed to modify permissions. Please re-enter the server!");
        MSG_NO_FIND_PLAYER = getConfig().getString("msg.noFindPlayer", "&4Can not find player named &2{0}");
        MSG_NO_FIND_PACKAGE = getConfig().getString("msg.noFindPackage", "&4Can not find package named &2{0}");
        MSG_TELL_GIVE_PACKAGE = getConfig().getString("msg.tellGivePackage", "&2{0} give you {1} package of {2}");
        MSG_GIVE_PACKAGE = getConfig().getString("msg.givePackage", "&2Give the player {0} {1} package of {2}");
        MSG_GIVE_PACKAGE_FAIL = getConfig().getString("msg.givePackageFail", "&4Failed to give players {0} {1} package of {2}");
        MSG_TELL_SET_PACKAGE = getConfig().getString("msg.tellSetPackage", "&2{0} set your package of {2} to {1}");
        MSG_SET_PACKAGE = getConfig().getString("msg.setPackage", "&2Set the player {0} {1} package of {2}");
        MSG_SET_PACKAGE_FAIL = getConfig().getString("msg.setPackageFail", "&4Failed to set the player {0} {1} package of {2}");
        MSG_TELL_DEL_PACKAGE = getConfig().getString("msg.tellDelPackage", "&4{0} deleted your package of {1}");
        MSG_DEL_PACKAGE = getConfig().getString("msg.delPackage", "&2Remove package of {1}&2 for player {0}");
        MSG_DEL_PACKAGE_FAIL = getConfig().getString("msg.delPackageFail", "&4Failed to delete package of {1}&2 for player {0}");
        MSG_TELL_DEL_ALL = getConfig().getString("msg.tellDelAll", "&4{0} remove all your packages");
        MSG_DEL_ALL = getConfig().getString("msg.delAll", "&2Remove all packages for player {0}");
        MSG_DEL_ALL_FAIL = getConfig().getString("msg.delAllFail", "&4Failed to remove all packages for player {0}");
        MSG_NUM_OF_PACKAGES = getConfig().getString("msg.numOfPackages", "====={0} has {1} packages=====");
        MSG_EXPIRATION_DATE = getConfig().getString("msg.expirationDate", "{0}packages: {1}({2}), Expiration date: {3}, {4}");
        MSG_UNKNOWN_PACKAGE = getConfig().getString("msg.unknownPackage", "Unknown Packages");
        MSG_IS_EXPIRATION_DATE = getConfig().getString("msg.isExpirationDate", "Your package: {0}({1})&r has expired.");
        MSG_FUNC_DISABLED = getConfig().getString("msg.funcDisabled", "{0} functionality disabled.");
        MSG_TAG_SET_SUCCESS = getConfig().getString("msg.tagSetSuccess", "&2Tag Set Success.");

        CMD_HELP = getConfig().getString("cmd.help", "/{0} help \\n&7  - Show commands.");
        CMD_ME = getConfig().getString("cmd.me", "&6/{0} me \\n&7  - View your packages.");
        CMD_TAG = getConfig().getString("cmd.tag", "&6/{0} tag <c/p/s> \\n&7  - View your color/prefix/suffix tags.");
        CMD_PACKAGES = getConfig().getString("cmd.packages", "&6/{0} packages [packageName] \\n&7  - View packages.");
        CMD_GET = getConfig().getString("cmd.get", "&6/{0} get <playerName> \\n&7  - View player packages.");
        CMD_GIVE = getConfig().getString("cmd.give", "&6/{0} give <playerName> <packageName> <time> <timeUnit:d/h/m> \\n&7  - Give player package some time. (Time accumulation.) \\n&7    timeUnit: d=day, h=hour, m=minute");
        CMD_SET = getConfig().getString("cmd.set", "&6/{0} set <playerName> <packageName> <time> <timeUnit:d/h/m> \\n&7  - Set player package some time. \\n&7    timeUnit: d=day, h=hour, m=minute");
        CMD_REMOVE = getConfig().getString("cmd.remove", "&6/{0} remove <playerName> <packageName> [t/f] \\n&7  - Remove player package. (t: Delete global package.)");
        CMD_REMOVEALL = getConfig().getString("cmd.removeall", "&6/{0} removeall <playerName> [t/f] \\n&7  - Remove player all package. (t: Delete global packages.)");
        CMD_RELOAD = getConfig().getString("cmd.reload", "&6/{0} reload \\n&7  - Reloads the config file.");
    }
}
