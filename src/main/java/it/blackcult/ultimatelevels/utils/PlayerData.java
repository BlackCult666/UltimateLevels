package it.blackcult.ultimatelevels.utils;

import it.blackcult.ultimatelevels.UltimateLevels;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerData {
    public static int getPlayerLevel(Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        return data.get(new NamespacedKey(UltimateLevels.getInstance(), "level"), PersistentDataType.INTEGER);
    }

    public static void setPlayerLevel(Player player, int level) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(new NamespacedKey(UltimateLevels.getInstance(), "level"), PersistentDataType.INTEGER, level);
    }
}
