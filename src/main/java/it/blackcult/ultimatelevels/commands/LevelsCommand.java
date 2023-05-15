package it.blackcult.ultimatelevels.commands;

import it.blackcult.ultimatelevels.inventories.LevelsInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)) {
            return false;
        }
        LevelsInventory levelsInventory = new LevelsInventory(player);
        levelsInventory.openMenu();
        return true;
    }
}
