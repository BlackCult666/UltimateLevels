package it.blackcult.ultimatelevels.utils;

import com.google.common.base.Strings;
import it.blackcult.ultimatelevels.UltimateLevels;
import org.bukkit.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

public class Methods {
    private static UltimateLevels plugin = UltimateLevels.getInstance();

    public static String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> format(List<String> mainLore) {
        List<String> lore = new ArrayList<>();
        for (String line : mainLore) {
            lore.add(format(line));
        }
        return lore;
    }

    public static ItemStack getCustomHead(OfflinePlayer player, String displayname, List<String> lore) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();

        meta.setOwningPlayer(player);
        meta.setDisplayName(format(displayname));
        meta.setLore(format(lore));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack getCustomItem(Material material, String displayname, List<String> lore, int amount) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(format(displayname));
        meta.setLore(format(lore));
        itemStack.setAmount(amount);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack getTippedArrow(PotionType potion, String displayname, List<String> lore) {
        ItemStack itemStack = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();

        meta.setDisplayName(format(displayname));
        meta.setLore(format(lore));
        meta.setBasePotionData(new PotionData(potion));
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static String getProgressBar(String symbol, int current, int max, int totalBars, String firstColor, String secondColor){
        float percentage = (float) current / max;
        int completedBars = (int) (totalBars * percentage);
        int leftBars = totalBars - completedBars;

        return Strings.repeat("" + ChatColor.translateAlternateColorCodes('&', firstColor) + symbol, completedBars) + Strings.repeat("" + ChatColor.translateAlternateColorCodes('&', secondColor) + symbol, leftBars);
    }

    public static String getPercentage(int current, int max) {
        if(current > max) {
            return "100%";
        }
        int percent = current * 100 / max;
        return Math.round((float) (percent * 10) / 10) + "%";
    }

    public static int getLevelSize() {
        List<Integer> levels = new ArrayList<>();
        for(String level : plugin.getConfig().getConfigurationSection("levels").getKeys(false)) {
            levels.add(Integer.parseInt(level));
        }
        return levels.size() + 1;
    }
}
