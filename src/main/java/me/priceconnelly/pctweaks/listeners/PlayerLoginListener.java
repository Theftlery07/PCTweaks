package me.priceconnelly.pctweaks.listeners;

import me.priceconnelly.pctweaks.models.Break;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e){
        if(!Break.isOpen() && !e.getPlayer().isOp()) e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.AQUA + "Break time");
    }


}
