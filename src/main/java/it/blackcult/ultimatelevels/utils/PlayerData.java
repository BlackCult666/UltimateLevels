package it.blackcult.ultimatelevels.utils;

import com.sun.tools.javac.Main;
import it.blackcult.ultimatelevels.UltimateLevels;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerData {
    private static UltimateLevels plugin = UltimateLevels.getInstance();

    public static int getPlayerLevel(Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        return data.get(new NamespacedKey(UltimateLevels.getInstance(), "level"), PersistentDataType.INTEGER);
    }

    public static String getNextLevel(Player player) {
        if(getPlayerLevel(player) == Methods.getLevelSize() - 1) {
            return plugin.getConfig().getString("stats-item.maxed-string");
        }
        return String.valueOf(getPlayerLevel(player) + 1);
    }

    public static void setPlayerLevel(Player player, int level) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(new NamespacedKey(UltimateLevels.getInstance(), "level"), PersistentDataType.INTEGER, level);
    }
}
