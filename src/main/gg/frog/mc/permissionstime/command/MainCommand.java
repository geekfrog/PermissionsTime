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

    private PluginMain pm;
    private SqlManager sm;

    public MainCommand(PluginMain pm) {
        this.pm = pm;
        this.sm = pm.getSqlManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase(pm.PLUGIN_NAME_LOWER_CASE)) {
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
                        GiveCmd giveCmd = new GiveCmd(pm, sender, args);
                        new Thread(giveCmd).start();
                    }
                } else if (args[0].equalsIgnoreCase("give")) {
                    if (hasPermission(sender, isPlayer, "permissionstime.give")) {
                        GiveCmd giveCmd = new GiveCmd(pm, sender, args);
                        new Thread(giveCmd).start();
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (hasPermission(sender, isPlayer, "permissionstime.set")) {
                        SetCmd setCmd = new SetCmd(pm, sender, args);
                        new Thread(setCmd).start();
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (hasPermission(sender, isPlayer, "permissionstime.remove")) {
                        RemoveCmd removeCmd = new RemoveCmd(pm, sender, args);
                        new Thread(removeCmd).start();
                    }
                } else if (args[0].equalsIgnoreCase("removeall")) {
                    if (hasPermission(sender, isPlayer, "permissionstime.removeall")) {
                        RemoveAllCmd removeAllCmd = new RemoveAllCmd(pm, sender, args);
                        new Thread(removeAllCmd).start();
                    }
                } else if (args[0].equalsIgnoreCase("packages")) {
                    if (hasPermission(sender, isPlayer, "permissionstime.packages")) {
                        PackagesCmd packagesCmd = new PackagesCmd(pm, sender, args);
                        new Thread(packagesCmd).start();
                    }
                } else {
                    getHelp(sender, isPlayer);
                }
                return true;
            }
        }
        return false;
    }

    private void getHelp(CommandSender sender, boolean isPlayer) {
        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "&a===== " + pm.PLUGIN_NAME + " Version:" + pm.PLUGIN_VERSION + " ====="));
        if (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".me")) {
            sender.sendMessage(StrUtil.messageFormat("/" + pm.PLUGIN_NAME_LOWER_CASE + " .me \n  - View self package."));
        }
        if (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".packages")) {
            sender.sendMessage(StrUtil.messageFormat("/" + pm.PLUGIN_NAME_LOWER_CASE + " packages [packageName] \n  - View packages."));
        }
        if (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".give")) {
            sender.sendMessage(StrUtil.messageFormat("/" + pm.PLUGIN_NAME_LOWER_CASE + " give <playerName> <packageName> <time> \n  - Give player package <time>day."));
        }
        if (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".set")) {
            sender.sendMessage(StrUtil.messageFormat("/" + pm.PLUGIN_NAME_LOWER_CASE + " set <playerName> <packageName> <time> \n  - Set player package <time>day."));
        }
        if (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".remove")) {
            sender.sendMessage(StrUtil.messageFormat("/" + pm.PLUGIN_NAME_LOWER_CASE + " remove <playerName> <packageName> \n  - Remove player package."));
        }
        if (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".removeall")) {
            sender.sendMessage(StrUtil.messageFormat("/" + pm.PLUGIN_NAME_LOWER_CASE + " removeall <playerName> \n  - Remove player all package."));
        }
        if (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".reload")) {
            sender.sendMessage(StrUtil.messageFormat("/" + pm.PLUGIN_NAME_LOWER_CASE + " reload \n  -Reloads the config file."));
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
