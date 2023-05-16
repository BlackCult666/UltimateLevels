package it.blackcult.ultimatelevels.commands;

import it.blackcult.ultimatelevels.UltimateLevels;
import it.blackcult.ultimatelevels.utils.Methods;
import it.blackcult.ultimatelevels.utils.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ULevelsCommand implements CommandExecutor {
    private UltimateLevels plugin = UltimateLevels.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)) {
            return false;
        }
        if(!player.hasPermission("ultimatelevels.admin")) {
            player.sendMessage(Methods.format(plugin.getConfig().getString("no-permission")));
            return false;
        }
        if(!(args.length > 0)) {
            sendHelp(player);
            return false;
        }
        switch(args[0]) {
            case "reload":
                if(!player.hasPermission("ultimatelevels.reload")) {
                    player.sendMessage(Methods.format(plugin.getConfig().getString("no-permission")));
                    return false;
                }
                plugin.reloadConfig();
                player.sendMessage(Methods.format(plugin.getConfig().getString("reload-message")));
                break;
            case "help":
                if(!player.hasPermission("ultimatelevels.use")) {
                    player.sendMessage(Methods.format(plugin.getConfig().getString("no-permission")));
                    return false;
                }
                sendHelp(player);
                break;
            case "set":
                if(!player.hasPermission("ultimatelevels.set")) {
                    player.sendMessage(Methods.format(plugin.getConfig().getString("no-permission")));
                    return false;
                }
                if(args.length != 3) {
                    player.sendMessage(Methods.format(plugin.getConfig().getString("set-level-usage")));
                    return false;
                }
                setLevel(args, args[1], player);
                break;
        }
        return true;
    }

    private void sendHelp(Player player) {
        List<String> messages = plugin.getConfig().getStringList("help-message");
        for (String message : messages) {
            player.sendMessage(Methods.format(message));
        }
    }

    private void setLevel(String[] args, String target, Player player) {
        for(OfflinePlayer targetPlayer : Bukkit.getOfflinePlayers()) {
            if(!target.equals(targetPlayer.getName())) {
                player.sendMessage(Methods.format(plugin.getConfig().getString("player-not-found").replaceAll("%player%", target)));
                return;
            }
            try {
                int level = Integer.parseInt(args[2]);
                if(level < 0 || level > Methods.getLevelSize()) {
                    player.sendMessage(Methods.format(plugin.getConfig().getString("invalid-level").replaceAll("%maxlevel%", String.valueOf(Methods.getLevelSize() - 1))));
                    return;
                }
                PlayerData.setPlayerLevel((Player) targetPlayer, level);
                player.sendMessage(Methods.format(plugin.getConfig().getString("changed-level").replaceAll("%player%", target).replaceAll("%level%", String.valueOf(level))));
            } catch (NumberFormatException e) {
                player.sendMessage(Methods.format(plugin.getConfig().getString("invalid-level").replaceAll("%maxlevel%", String.valueOf(Methods.getLevelSize() - 1))));
            }
        }
    }
}
