package gg.frog.mc.permissionstime.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.LangCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.SqlManager;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class MainCommand implements CommandExecutor {

    private PluginMain pm = PluginMain.getInstance();
    private SqlManager sm = PluginMain.sm;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase(PluginMain.PLUGIN_NAME_LOWER_CASE)) {
            boolean isPlayer = false;
            if (sender instanceof Player) {
                isPlayer = true;
            }
            if (args.length == 0) {
                getHelp(sender, isPlayer);
                return true;
            } else {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (isPlayer) {
                        Player player = (Player) sender;
                        if (sender.isOp() || player.hasPermission("permissionstime.reload")) {
                            pm.getConfigManager().reloadConfig();
                            if (!sm.updateDatabase()) {
                                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "数据库异常"));
                            }
                            sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.CONFIG_RELOADED));
                            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.CONFIG_RELOADED));
                        } else {
                            sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.NO_PERMISSION));
                        }
                    } else {
                        pm.getConfigManager().reloadConfig();
                        if (!sm.updateDatabase()) {
                            sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "数据库异常"));
                        }
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.CONFIG_RELOADED));
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("me")) {
                    if (hasPermission(sender, isPlayer, "permissionstime.me")) {
                        return GiveCmd.onCommand(sender, isPlayer, args);
                    }
                } else if (args[0].equalsIgnoreCase("give")) {
                    if (hasPermission(sender, isPlayer, "permissionstime.give")) {
                        return GiveCmd.onCommand(sender, isPlayer, args);
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (hasPermission(sender, isPlayer, "permissionstime.set")) {
                        return GiveCmd.onCommand(sender, isPlayer, args);
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (hasPermission(sender, isPlayer, "permissionstime.remove")) {
                        return GiveCmd.onCommand(sender, isPlayer, args);
                    }
                } else if (args[0].equalsIgnoreCase("packages")) {
                    if (hasPermission(sender, isPlayer, "permissionstime.packages")) {
                        return PackagesCmd.onCommand(sender, isPlayer, args);
                    }
                } else {
                    getHelp(sender, isPlayer);
                    return true;
                }
            }
        }
        return false;
    }

    private void getHelp(CommandSender sender, boolean isPlayer) {
        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "&a===== " + PluginMain.PLUGIN_NAME + " Version:" + PluginMain.PLUGIN_VERSION + " ====="));
        if (!isPlayer || sender.isOp() || sender.hasPermission(PluginMain.PLUGIN_NAME_LOWER_CASE + ".reload")) {
            sender.sendMessage(StrUtil.messageFormat("/" + PluginMain.PLUGIN_NAME_LOWER_CASE + " reload \n\t-Reloads the config file."));
        }
        if (!isPlayer || sender.isOp() || sender.hasPermission(PluginMain.PLUGIN_NAME_LOWER_CASE + ".give")) {
            sender.sendMessage(StrUtil.messageFormat("/" + PluginMain.PLUGIN_NAME_LOWER_CASE + " give <playerName> <packageName> <time> \n\t- Give player package <time>day."));
        }
        if (!isPlayer || sender.isOp() || sender.hasPermission(PluginMain.PLUGIN_NAME_LOWER_CASE + ".set")) {
            sender.sendMessage(StrUtil.messageFormat("/" + PluginMain.PLUGIN_NAME_LOWER_CASE + " set <playerName> <packageName> <time> \n\t- Set player package <time>day."));
        }
        if (!isPlayer || sender.isOp() || sender.hasPermission(PluginMain.PLUGIN_NAME_LOWER_CASE + ".remove")) {
            sender.sendMessage(StrUtil.messageFormat("/" + PluginMain.PLUGIN_NAME_LOWER_CASE + " remove <playerName> <packageName> \n\t- Remove player package."));
        }
        if (!isPlayer || sender.isOp() || sender.hasPermission(PluginMain.PLUGIN_NAME_LOWER_CASE + ".packages")) {
            sender.sendMessage(StrUtil.messageFormat("/" + PluginMain.PLUGIN_NAME_LOWER_CASE + " packages [packageName] \n\t- View packages."));
        }
        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX));
    }

    private boolean hasPermission(CommandSender sender, boolean isPlayer, String permissionPath) {
        if (isPlayer) {
            Player player = (Player) sender;
            if (sender.isOp() || player.hasPermission(permissionPath)) {
            } else {
                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.NO_PERMISSION));
                return false;
            }
        }
        return true;
    }
}
