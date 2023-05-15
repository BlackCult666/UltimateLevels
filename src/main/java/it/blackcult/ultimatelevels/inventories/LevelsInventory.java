package it.blackcult.ultimatelevels.inventories;

import it.blackcult.ultimatelevels.UltimateLevels;
import it.blackcult.ultimatelevels.utils.Methods;
import it.blackcult.ultimatelevels.utils.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class LevelsInventory {
    public static HashMap<UUID, LevelsInventory> players = new HashMap<>();

    private UltimateLevels plugin = UltimateLevels.getInstance();
    private Inventory inv;
    private Player player;
    private int itemsPerPage = 9;
    private int currentPage = 1;

    public LevelsInventory(Player player) {
        this.player = player;
    }

    public void handleClick(InventoryClickEvent e) {
        if(e.getCurrentItem() == null) {
            return;
        }
        e.setCancelled(true);
        if(e.getSlot() == 38) {
            previousPage();

        } else if(e.getSlot() == 42) {
            nextPage();
        }
        if(e.getSlot() > 26 && e.getSlot() < 36) {
            if (e.getCurrentItem().getType() == Material.valueOf(plugin.getConfig().getString("settings.materials.locked-level"))) {
                int playerLevel = PlayerData.getPlayerLevel(player);
                int clickedLevel = e.getCurrentItem().getAmount();

                if (clickedLevel > (playerLevel + 1)) {
                    return;
                }
                int price = plugin.getConfig().getInt("levels." + clickedLevel + ".price");
                plugin.getCurrencyManager().updateLevel(player, playerLevel, price);
            }
        }
    }

    public void openMenu() {
        int startIndex = getStartIndex();
        int endIndex = Math.min(startIndex + itemsPerPage, Methods.getLevelSize());

        inv = Bukkit.createInventory(null, 45, Methods.format(plugin.getConfig().getString("settings.gui-title")));
        players.put(player.getUniqueId(), this);
        addGlass();

        boolean currency = plugin.getConfig().getBoolean("settings.use-vault");
        int playerLevel = PlayerData.getPlayerLevel(player);
        int playerCurrency = (!currency) ? plugin.getCurrencyManager().getTotalExp(player) : (int) plugin.getEconomy().getBalance(player);
        for (int i = startIndex; i < endIndex; i++) {
            int price = (playerLevel >= i) ? 0 : plugin.getConfig().getInt("levels." + i + ".price");
            int actualLevel = i;

            ItemStack lockedLevels = Methods.getCustomItem(Material.valueOf(plugin.getConfig().getString("settings.materials.locked-level")), plugin.getConfig().getString("levels." + i + ".level.locked-name").replaceAll("%level%", String.valueOf(i)), plugin.getConfig().getStringList("levels." + i + ".level.lore").stream().map(s -> s.replace("%progressbar%", getProgressBar(playerCurrency, price)).replace("%percentage%", Methods.getPercentage(playerCurrency, price)).replace("%price%", String.valueOf(price)).replace("%player%", player.getName()).replace("%level%", String.valueOf(actualLevel))).collect(Collectors.toList()), i);
            ItemStack completedLevels = Methods.getCustomItem(Material.valueOf(plugin.getConfig().getString("settings.materials.unlocked-level")), plugin.getConfig().getString("levels." + i + ".level.unlocked-name").replaceAll("%level%", String.valueOf(i)), plugin.getConfig().getStringList("levels." + i + ".level.lore").stream().map(s -> s.replace("%progressbar%", getProgressBar(playerCurrency, price)).replace("%percentage%", Methods.getPercentage(playerCurrency, price)).replace("%price%", String.valueOf(price)).replace("%player%", player.getName()).replace("%level%", String.valueOf(actualLevel))).collect(Collectors.toList()), i);
            ItemStack emptyMinecart = Methods.getCustomItem(Material.valueOf(plugin.getConfig().getString("settings.materials.unlocked-reward")), plugin.getConfig().getString("levels." + i + ".rewards.name").replaceAll("%level%", String.valueOf(i)), plugin.getConfig().getStringList("levels." + i + ".rewards.lore").stream().map(s -> s.replace("%progressbar%", getProgressBar(playerCurrency, price)).replace("%percentage%", Methods.getPercentage(playerCurrency, price)).replace("%price%", String.valueOf(price)).replace("%player%", player.getName()).replace("%level%", String.valueOf(actualLevel))).collect(Collectors.toList()), 1);
            ItemStack minecart = Methods.getCustomItem(Material.valueOf(plugin.getConfig().getString("settings.materials.locked-reward")), plugin.getConfig().getString("levels." + i + ".rewards.name").replaceAll("%level%", String.valueOf(i)), plugin.getConfig().getStringList("levels." + i + ".rewards.lore").stream().map(s -> s.replace("%progressbar%", getProgressBar(playerCurrency, price)).replace("%percentage%", Methods.getPercentage(playerCurrency, price)).replace("%price%", String.valueOf(price)).replace("%player%", player.getName()).replace("%level%", String.valueOf(actualLevel))).collect(Collectors.toList()), 1);

            if (playerLevel < i) {
                inv.setItem(i - startIndex + 27, lockedLevels);
                if(plugin.getConfig().getBoolean("levels." + i + ".rewards.enabled")) {
                    inv.setItem(i - startIndex + 18, minecart);
                }
            } else {
                inv.setItem(i - startIndex + 27, completedLevels);
                if(plugin.getConfig().getBoolean("levels." + i + ".rewards.enabled")) {
                    inv.setItem(i - startIndex + 18, emptyMinecart);
                }
            }
        }
        ItemStack skull = Methods.getCustomHead(player, plugin.getConfig().getString("stats-item.name").replaceAll("%player%", player.getName()).replaceAll("%level%", String.valueOf(playerLevel)).replaceAll("%nextlevel%", getNextLevel(playerLevel)), plugin.getConfig().getStringList("stats-item.lore").stream().map(s -> s.replace("%player%", player.getName()).replace("%level%", String.valueOf(playerLevel)).replace("%nextlevel%", getNextLevel(playerLevel))).collect(Collectors.toList()));
        inv.setItem(4, skull);

        inv.setItem(38, Methods.getTippedArrow(PotionType.INSTANT_HEAL, plugin.getConfig().getString("back-button.name"), plugin.getConfig().getStringList("back-button.lore")));
        inv.setItem(42, Methods.getTippedArrow(PotionType.JUMP, plugin.getConfig().getString("next-button.name"), plugin.getConfig().getStringList("next-button.lore")));

        player.openInventory(inv);
    }

    private void addGlass() {
        ItemStack grayGlass = Methods.getCustomItem(Material.valueOf(plugin.getConfig().getString("settings.materials.decorative-block")), plugin.getConfig().getString("decorative-block.name"), plugin.getConfig().getStringList("decorative-block.lore"), 1);
        for(int i = 0; i < 27; i++) {
            if(i == 4) {
                continue;
            }
            if(i > 17) {
                if(inv.getItem(i + 9) != null && plugin.getConfig().getBoolean("levels." + inv.getItem(i + 9).getAmount() + ".rewards.enabled")) {
                    continue;
                }
            }

            inv.setItem(i, grayGlass);
        }
        for(int i = 36; i < 45; i++) {
            if(i == 38 || i == 42) {
                continue;
            }
            inv.setItem(i, grayGlass);
        }
    }

    public void nextPage() {
        if ((currentPage * itemsPerPage) < Methods.getLevelSize() - 1) {
            currentPage++;
            openMenu();
        }
    }

    public void previousPage() {
        if (currentPage > 1) {
            currentPage--;
            openMenu();
        }
    }

    private int getStartIndex() {
        int startIndex = 1 + (currentPage - 1) * itemsPerPage;
        if(currentPage == 1) {
            startIndex = 1;
        }
        return startIndex;
    }

    private String getProgressBar(int playerCurrency, int neededCurrency) {
        String progressBar;
        if(playerCurrency > neededCurrency) {
            progressBar = Methods.getProgressBar(plugin.getConfig().getString("progress-bar.symbol"), 100, 100, 25, plugin.getConfig().getString("progress-bar.completed-color"), plugin.getConfig().getString("progress-bar.uncompleted-color"));
        } else {
            progressBar = Methods.getProgressBar(plugin.getConfig().getString("progress-bar.symbol"), playerCurrency, neededCurrency, 25, plugin.getConfig().getString("progress-bar.completed-color"), plugin.getConfig().getString("progress-bar.uncompleted-color"));
        }
        return progressBar;
    }

    private String getNextLevel(int actualLevel) {
        if(actualLevel == Methods.getLevelSize() - 1) {
            return plugin.getConfig().getString("stats-item.maxed-string");
        }
        return String.valueOf(actualLevel + 1);
    }

}
