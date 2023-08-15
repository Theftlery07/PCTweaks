package me.priceconnelly.pctweaks.listeners;

import me.priceconnelly.pctweaks.commands.BackCommand;
import me.priceconnelly.pctweaks.models.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        PlayerData.getPlayerData(e.getEntity()).addDeath();
        BackCommand.setLastLocation(e.getEntity());
    }
}
