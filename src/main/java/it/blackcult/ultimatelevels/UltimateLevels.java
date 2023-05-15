package it.blackcult.ultimatelevels;

import it.blackcult.ultimatelevels.commands.LevelsCommand;
import it.blackcult.ultimatelevels.commands.ULevelsCommand;
import it.blackcult.ultimatelevels.listeners.InventoryListener;
import it.blackcult.ultimatelevels.listeners.ConnectionListener;
import it.blackcult.ultimatelevels.utils.CurrencyManager;
import net.milkbowl.vault.economy.Economy;
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

        if(!setupEconomy()) {
            getLogger().warning("Vault plugin not found.");
            getConfig().set("settings.use-vault", false);
            saveConfig();
        }
        getCommand("levels").setExecutor(new LevelsCommand());
        getCommand("ultimatelevels").setExecutor(new ULevelsCommand());

        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
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
