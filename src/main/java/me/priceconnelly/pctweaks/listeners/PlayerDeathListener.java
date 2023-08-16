package me.priceconnelly.pctweaks.listeners;

import me.priceconnelly.pctweaks.commands.BackCommand;
import me.priceconnelly.pctweaks.models.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;
import java.util.Random;

import static me.priceconnelly.pctweaks.PCTweaks.getPlugin;

public class PlayerDeathListener implements Listener {
    private static List<String> deathMessages = initializeMessages();
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        PlayerData.getPlayerData(e.getEntity()).addDeath();
        BackCommand.setLastLocation(e.getEntity());
        if (deathMessages.size() > 0){
            e.setDeathMessage(deathMessages.get(new Random().nextInt(deathMessages.size())));
        }
    }

    private static List<String> initializeMessages(){
        getPlugin().reloadConfig();
        return getPlugin().getConfig().getStringList("death_messages");
    }
    public static void reInit(){
        deathMessages = initializeMessages();
    }
    public static String[] getDeathMessages(){
        return deathMessages.toArray(new String[0]);
    }
}
