package cn.onea.kill;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class catkill extends JavaPlugin {
    private boolean featureEnabled = true;
    private static final String ADMIN_PERMISSION = "killme.admin";
    private static final String CONFIG_KEY = "enabled";

    public void onEnable() {
        this.saveDefaultConfig();
        this.featureEnabled = this.getConfig().getBoolean(CONFIG_KEY, true);
        this.getLogger().info("主人!喵喵牌kill插件已开启! Feature status: " + this.featureEnabled);
    }

    public void onDisable() {
        this.getConfig().set(CONFIG_KEY, this.featureEnabled);
        this.saveConfig();
        this.getLogger().info("主人!喵喵牌kill插件已卸载");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("catkill")) {
            if (args.length == 0) {
                this.handlePlayerCommand(sender);
                return true;
            }
            if (args.length == 1) {
                return this.handleAdminCommand(sender, args[0]);
            }
        }
        return false;
    }

    private void handlePlayerCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("只有玩家可以使用这个命令喵！");
            return;
        }
        Player player = (Player)((Object)sender);
        if (!this.featureEnabled) {
            player.sendMessage("§c当前自杀功能已被管理员关闭喵！");
            return;
        }
        player.setHealth(0.0);
        player.sendMessage("§6你已成功自杀喵");
        Bukkit.broadcastMessage("§e玩家 " + player.getName() + " 选择了自我了断喵，可惜捏");
    }

    private boolean handleAdminCommand(CommandSender sender, String arg) {
        if (!sender.hasPermission(ADMIN_PERMISSION)) {
            sender.sendMessage("§c你没有权限使用此命令！");
            return true;
        }
        switch (arg.toLowerCase()) {
            case "on": {
                this.featureEnabled = true;
                this.saveConfigChange();
                sender.sendMessage("§a已开启自杀功能");
                return true;
            }
            case "off": {
                this.featureEnabled = false;
                this.saveConfigChange();
                sender.sendMessage("§c已关闭自杀功能");
                return true;
            }
        }
        sender.sendMessage("§6用法: /catkill [on|off]");
        return true;
    }

    private void saveConfigChange() {
        this.getConfig().set(CONFIG_KEY, this.featureEnabled);
        this.saveConfig();
    }
}
