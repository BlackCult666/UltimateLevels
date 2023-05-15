package it.blackcult.ultimatelevels.listeners;

import it.blackcult.ultimatelevels.UltimateLevels;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        PersistentDataContainer data = player.getPersistentDataContainer();

        if(!data.has(new NamespacedKey(UltimateLevels.getInstance(), "level"), PersistentDataType.INTEGER)) {
            data.set(new NamespacedKey(UltimateLevels.getInstance(), "level"), PersistentDataType.INTEGER, 0);
        }
    }
}
