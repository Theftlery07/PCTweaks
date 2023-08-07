package me.priceconnelly.pctweaks.listeners;

import me.pricec.myfirstplugin.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        PlayerData.getPlayerData(e.getEntity()).addDeath();
    }
}
