package gg.frog.mc.permissionstime.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import gg.frog.mc.permissionstime.PluginMain;
import gg.frog.mc.permissionstime.config.LangCfg;
import gg.frog.mc.permissionstime.config.PackagesCfg;
import gg.frog.mc.permissionstime.config.PluginCfg;
import gg.frog.mc.permissionstime.database.SqlManager;
import gg.frog.mc.permissionstime.utils.StrUtil;

public class MainCommand implements CommandExecutor, TabCompleter {

    private PluginMain pm;
    private SqlManager sm;

    public MainCommand(PluginMain pm) {
        this.pm = pm;
        this.sm = pm.getSqlManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase(pm.PLUGIN_NAME_LOWER_CASE) || commandLabel.equalsIgnoreCase("pt")) {
            boolean isPlayer = false;
            if (sender instanceof Player) {
                isPlayer = true;
            }
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                getHelp(sender, isPlayer);
                return true;
            } else {
                if (args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_PROCESSING));
                    if (isPlayer) {
                        Player player = (Player) sender;
                        if (sender.isOp() || player.hasPermission("permissionstime.reload")) {
                            for (Player p : pm.getServer().getOnlinePlayers()) {
                                InventoryView inventory = p.getOpenInventory();
                                if (StrUtil.messageFormat(LangCfg.INVENTORY_NAME + "&r&5&9&2&0&r").equals(inventory.getTitle())) {
                                    inventory.close();
                                }
                            }
                            pm.getConfigManager().reloadConfig();
                            if (!sm.updateDatabase()) {
                                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "&4Database exceptions."));
                            }
                            sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_CONFIG_RELOADED));
                            pm.getServer().getConsoleSender().sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_CONFIG_RELOADED));
                        } else {
                            sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_NO_PERMISSION));
                        }
                    } else {
                        for (Player p : pm.getServer().getOnlinePlayers()) {
                            InventoryView inventory = p.getOpenInventory();
                            if (inventory != null) {
                                if (StrUtil.messageFormat(LangCfg.INVENTORY_NAME + "&r&5&9&2&0&r").equals(inventory.getTitle())) {
                                    inventory.close();
                                }
                            }
                        }
                        pm.getConfigManager().reloadConfig();
                        if (!sm.updateDatabase()) {
                            sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "&4Database exceptions."));
                        }
                        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_CONFIG_RELOADED));
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("me")) {
                    if (hasPermission(sender, isPlayer, "permissionstime.me")) {
                        MeCmd meCmd = new MeCmd(pm, sender, isPlayer, args);
                        new Thread(meCmd).start();
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
                } else if (args[0].equalsIgnoreCase("get")) {
                    if (hasPermission(sender, isPlayer, "permissionstime.get")) {
                        GetCmd getCmd = new GetCmd(pm, sender, args);
                        new Thread(getCmd).start();
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
                    sender.sendMessage(StrUtil.messageFormat(LangCfg.CMD_HELP, pm.PLUGIN_NAME_LOWER_CASE));
                }
                return true;
            }
        }
        return false;
    }

    private void getHelp(CommandSender sender, boolean isPlayer) {
        sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "\n&a===== " + pm.PLUGIN_NAME + " Version:" + pm.PLUGIN_VERSION + (pm.getDescription().getCommands().containsKey("pt") ? " Aliases:/pt" : "") + " ====="));
        if (isPlayer && (sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".me"))) {
            sender.sendMessage(StrUtil.messageFormat(LangCfg.CMD_ME, pm.PLUGIN_NAME_LOWER_CASE));
        }
        if (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".packages")) {
            sender.sendMessage(StrUtil.messageFormat(LangCfg.CMD_PACKAGES, pm.PLUGIN_NAME_LOWER_CASE));
        }
        if (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".get")) {
            sender.sendMessage(StrUtil.messageFormat(LangCfg.CMD_GET, pm.PLUGIN_NAME_LOWER_CASE));
        }
        if (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".give")) {
            sender.sendMessage(StrUtil.messageFormat(LangCfg.CMD_GIVE, pm.PLUGIN_NAME_LOWER_CASE));
        }
        if (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".set")) {
            sender.sendMessage(StrUtil.messageFormat(LangCfg.CMD_SET, pm.PLUGIN_NAME_LOWER_CASE));
        }
        if (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".remove")) {
            sender.sendMessage(StrUtil.messageFormat(LangCfg.CMD_REMOVE, pm.PLUGIN_NAME_LOWER_CASE));
        }
        if (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".removeall")) {
            sender.sendMessage(StrUtil.messageFormat(LangCfg.CMD_REMOVEALL, pm.PLUGIN_NAME_LOWER_CASE));
        }
        if (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".reload")) {
            sender.sendMessage(StrUtil.messageFormat(LangCfg.CMD_RELOAD, pm.PLUGIN_NAME_LOWER_CASE));
        }
    }

    private boolean hasPermission(CommandSender sender, boolean isPlayer, String permissionPath) {
        if (isPlayer) {
            Player player = (Player) sender;
            if (sender.isOp() || player.hasPermission(permissionPath)) {
            } else {
                sender.sendMessage(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + LangCfg.MSG_NO_PERMISSION));
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tipList = new ArrayList<String>();
        boolean isPlayer = false;
        if (sender instanceof Player) {
            isPlayer = true;
        }
        if (args.length == 1) {
            args[0] = args[0].toLowerCase(Locale.ENGLISH);
            if ("me".startsWith(args[0]) && (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".me"))) {
                tipList.add("me");
            }
            if ("packages".startsWith(args[0]) && (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".packages"))) {
                tipList.add("packages");
            }
            if ("give".startsWith(args[0]) && (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".give"))) {
                tipList.add("give");
            }
            if ("set".startsWith(args[0]) && (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".set"))) {
                tipList.add("set");
            }
            if ("get".startsWith(args[0]) && (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".get"))) {
                tipList.add("get");
            }
            if ("remove".startsWith(args[0]) && (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".remove"))) {
                tipList.add("remove");
            }
            if ("removeall".startsWith(args[0]) && (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".removeall"))) {
                tipList.add("removeall");
            }
            if ("reload".startsWith(args[0]) && (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".reload"))) {
                tipList.add("reload");
            }
        } else if (args.length == 2) {
            args[0] = args[0].toLowerCase(Locale.ENGLISH);
            args[1] = args[1].toLowerCase(Locale.ENGLISH);
            if ("packages".equalsIgnoreCase(args[0]) && (!isPlayer || sender.isOp() || sender.hasPermission(pm.PLUGIN_NAME_LOWER_CASE + ".me"))) {
                for (String name : PackagesCfg.PACKAGES.keySet()) {
                    if (name.startsWith(args[1])) {
                        tipList.add(name);
                    }
                }
            } else if ("give".equalsIgnoreCase(args[0]) || "set".equalsIgnoreCase(args[0]) || "get".equalsIgnoreCase(args[0]) || "remove".equalsIgnoreCase(args[0]) || "removeall".equalsIgnoreCase(args[0])) {
                return null;
            }
        } else if (args.length == 3) {
            args[0] = args[0].toLowerCase(Locale.ENGLISH);
            args[2] = args[2].toLowerCase(Locale.ENGLISH);
            if ("give".equalsIgnoreCase(args[0]) || "set".equalsIgnoreCase(args[0]) || "remove".equalsIgnoreCase(args[0])) {
                for (String name : PackagesCfg.PACKAGES.keySet()) {
                    if (name.startsWith(args[2])) {
                        tipList.add(name);
                    }
                }
            }
        }
        return tipList;
    }
}
