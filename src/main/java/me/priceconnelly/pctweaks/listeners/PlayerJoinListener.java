package me.priceconnelly.pctweaks.listeners;

import me.pricec.myfirstplugin.Rank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        Rank.update(player);
        e.setJoinMessage("Welcome " + player.getDisplayName());
    }
}
