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
    public static String EXPIRATION_DATE = null;
    public static String MSG_PARAMETER_MISMATCH = null;
    public static String MSG_DAYS_PARAMETER_INCORRECT = null;
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

    public LangCfg(String fileName, PluginMain pm) {
        super(fileName, pm);
    }

    @Override
    protected void init() {}

    @Override
    protected void loadToDo() {
        INVENTORY_NAME = getConfig().getString("inventoryName", "&4===Permissions Packages===");
        EXPIRATION_DATE = getConfig().getString("expirationDate", "&4Expiration date: {0}");
        MSG_PARAMETER_MISMATCH = getConfig().getString("msg.parameterMismatch", "&4Parameter mismatch.");
        MSG_DAYS_PARAMETER_INCORRECT = getConfig().getString("msg.daysParameterIncorrect", "&4The number of days is incorrect. Please enter a positive integer.");
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
        MSG_TELL_GIVE_PACKAGE = getConfig().getString("msg.tellGivePackage", "&2{0} give you {1}days package of {2}");
        MSG_GIVE_PACKAGE = getConfig().getString("msg.givePackage", "&2Give the player {0} {1}days package of {2}");
        MSG_GIVE_PACKAGE_FAIL = getConfig().getString("msg.givePackageFail", "&4Failed to give players {0} {1}days package of {2}");
        MSG_TELL_SET_PACKAGE = getConfig().getString("msg.tellSetPackage", "&2{0} set your package of {2} to {1}days");
        MSG_SET_PACKAGE = getConfig().getString("msg.setPackage", "&2Set the player {0} {1}days package of {2}");
        MSG_SET_PACKAGE_FAIL = getConfig().getString("msg.setPackageFail", "&4Failed to set the player {0} {1}days package of {2}");
        MSG_TELL_DEL_PACKAGE = getConfig().getString("msg.tellDelPackage", "&4{0} deleted your package of {1}");
        MSG_DEL_PACKAGE = getConfig().getString("msg.delPackage", "&2Remove package of {1}&2 for player {0}");
        MSG_DEL_PACKAGE_FAIL = getConfig().getString("msg.delPackageFail", "&4Failed to delete package of {1}&2 for player {0}");
        MSG_TELL_DEL_ALL = getConfig().getString("msg.tellDelAll", "&4{0} remove all your packages");
        MSG_DEL_ALL = getConfig().getString("msg.delAll", "&2Remove all packages for player {0}");
        MSG_DEL_ALL_FAIL = getConfig().getString("msg.delAllFail", "&4Failed to remove all packages for player {0}");
    }
}
