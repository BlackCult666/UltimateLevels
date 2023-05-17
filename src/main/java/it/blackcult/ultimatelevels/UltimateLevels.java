package it.blackcult.ultimatelevels;

import it.blackcult.ultimatelevels.commands.LevelsCommand;
import it.blackcult.ultimatelevels.commands.ULevelsCommand;
import it.blackcult.ultimatelevels.listeners.InventoryListener;
import it.blackcult.ultimatelevels.listeners.ConnectionListener;
import it.blackcult.ultimatelevels.papi.LevelsExpansion;
import it.blackcult.ultimatelevels.utils.CurrencyManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class UltimateLevels extends JavaPlugin {
    private static UltimateLevels instance;
    private CurrencyManager currencyManager;
    private Economy economy;

    @Override
    public void onEnable() {
        instance = this;
        currencyManager = new CurrencyManager();

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        if(!setupEconomy()) {
            getLogger().warning("Vault plugin not found.");
            getConfig().set("settings.use-vault", false);
            saveConfig();
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().info("PlaceholderAPI plugin not found.");
        }
        registerCommands();
        registerListeners();

        new LevelsExpansion().register();
    }

    private boolean setupEconomy() {
        if(getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if(economyProvider == null) {
            return false;
        }
        economy = economyProvider.getProvider();
        return (economy != null);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
    }

    private void registerCommands() {
        getCommand("levels").setExecutor(new LevelsCommand());
        getCommand("ultimatelevels").setExecutor(new ULevelsCommand());
    }

    public Economy getEconomy() {
        return economy;
    }

    public CurrencyManager getCurrencyManager() {
        return currencyManager;
    }

    public static UltimateLevels getInstance() {
        return instance;
    }
}
