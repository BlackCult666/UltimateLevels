package it.blackcult.ultimatelevels.papi;

import it.blackcult.ultimatelevels.utils.PlayerData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class LevelsExpansion extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "ultimatelevels";
    }

    @Override
    public String getAuthor() {
        return "BlackCult666";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if(p == null) {
            return "";
        }
        switch(params){
            case "level" -> {
                return String.valueOf(PlayerData.getPlayerLevel(p));
            }
            case "nextlevel" -> {
                return PlayerData.getNextLevel(p);
            }
        }
        return null;
    }
}
