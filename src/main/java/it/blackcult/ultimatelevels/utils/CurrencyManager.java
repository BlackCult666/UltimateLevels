package it.blackcult.ultimatelevels.utils;

import it.blackcult.ultimatelevels.UltimateLevels;
import it.blackcult.ultimatelevels.inventories.LevelsInventory;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class CurrencyManager {
    private UltimateLevels plugin = UltimateLevels.getInstance();

    public int getTotalExp(Player player) {
        int experience;
        int level = player.getLevel();

        if(level >= 0 && level <= 15) {
            experience = (int) Math.ceil(Math.pow(level, 2) + (6 * level));
            int requiredExperience = 2 * level + 7;
            double currentExp = Double.parseDouble(Float.toString(player.getExp()));
            experience += Math.ceil(currentExp * requiredExperience);
            return experience;
        } else if(level > 15 && level <= 30) {
            experience = (int) Math.ceil((2.5 * Math.pow(level, 2) - (40.5 * level) + 360));
            int requiredExperience = 5 * level - 38;
            double currentExp = Double.parseDouble(Float.toString(player.getExp()));
            experience += Math.ceil(currentExp * requiredExperience);
            return experience;
        } else {
            experience = (int) Math.ceil((4.5 * Math.pow(level, 2) - (162.5 * level) + 2220));
            int requiredExperience = 9 * level - 158;
            double currentExp = Double.parseDouble(Float.toString(player.getExp()));
            experience += Math.ceil(currentExp * requiredExperience);
            return experience;
        }
    }

    public void takeExp(Player player, int amount) {
        if(player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        int total = getTotalExp(player) - amount;
        player.setTotalExperience(0);
        player.setTotalExperience(total);
        player.setLevel(0);
        player.setExp(0);

        while(total > player.getExpToLevel()) {
            total -= player.getExpToLevel();
            player.setLevel(player.getLevel() + 1);
        }
        float xp = (float) total / (float) player.getExpToLevel();
        player.setExp(xp);
    }

    public void updateLevel(Player player, int playerLevel, int price) {
        LevelsInventory levelsInventory = LevelsInventory.players.get(player.getUniqueId());
        int newLevel = playerLevel + 1;
        if(!plugin.getConfig().getBoolean("settings.use-vault")) {
            if(getTotalExp(player) < price) {
                return;
            }
            takeExp(player, price);
        } else {
            if(!plugin.getEconomy().has(player, price)) {
                return;
            }
            plugin.getEconomy().withdrawPlayer(player, price);
        }
        PlayerData.setPlayerLevel(player, newLevel);
        levelsInventory.openMenu();
        player.playSound(player.getLocation(), Sound.valueOf(plugin.getConfig().getString("settings.level-up-sound")), 1, 1);

        for(String reward : plugin.getConfig().getStringList("levels." + newLevel + ".rewards.prizes")) {
            if(!plugin.getConfig().getBoolean("levels." + newLevel + ".rewards.enabled")) {
                break;
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward.replaceAll("%player%", player.getName()).replaceAll("%level%", String.valueOf(newLevel)));
        }
    }
}
