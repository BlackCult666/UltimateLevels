package it.blackcult.ultimatelevels.listeners;

import it.blackcult.ultimatelevels.UltimateLevels;
import it.blackcult.ultimatelevels.inventories.LevelsInventory;
import it.blackcult.ultimatelevels.utils.Methods;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {
    private UltimateLevels plugin = UltimateLevels.getInstance();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(!e.getView().getTitle().equals(Methods.format(plugin.getConfig().getString("settings.gui-title")))) {
            return;
        }
        LevelsInventory levelsInventory = LevelsInventory.players.get(e.getWhoClicked().getUniqueId());
        levelsInventory.handleClick(e);
    }
}
