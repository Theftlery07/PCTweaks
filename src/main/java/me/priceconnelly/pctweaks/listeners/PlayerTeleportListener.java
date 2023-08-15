package me.priceconnelly.pctweaks.listeners;

import me.priceconnelly.pctweaks.commands.BackCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e){
        BackCommand.setLastLocation(e.getPlayer());
    }
}
