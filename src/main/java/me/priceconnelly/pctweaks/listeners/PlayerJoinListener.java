package me.priceconnelly.pctweaks.listeners;

import me.priceconnelly.pctweaks.models.Rank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        Rank.update(player);
        if(!player.hasPlayedBefore()){
            e.setJoinMessage("Welcome " + player.getDisplayName());
        } else {
            e.setJoinMessage(player.getDisplayName() + " came back from the moooooooooooon!");
        }
    }
}
