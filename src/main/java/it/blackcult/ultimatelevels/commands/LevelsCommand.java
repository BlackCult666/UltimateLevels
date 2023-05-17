package it.blackcult.ultimatelevels.commands;

import it.blackcult.ultimatelevels.UltimateLevels;
import it.blackcult.ultimatelevels.inventories.LevelsInventory;
import it.blackcult.ultimatelevels.utils.Methods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class LevelsCommand implements CommandExecutor {
    private UltimateLevels plugin = UltimateLevels.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)) {
            return false;
        }
        if(!player.hasPermission("ultimatelevels.use")) {
            player.sendMessage(Methods.format(plugin.getConfig().getString("no-permission")));
            return false;
        }
        List<String> disabledWorlds = plugin.getConfig().getStringList("disabled-worlds");
        if(disabledWorlds.contains(player.getWorld().getName())) {
            player.sendMessage(Methods.format(plugin.getConfig().getString("command-disabled")));
            return false;
        }
        LevelsInventory levelsInventory = new LevelsInventory(player);
        levelsInventory.openMenu();
        return true;
    }
}
