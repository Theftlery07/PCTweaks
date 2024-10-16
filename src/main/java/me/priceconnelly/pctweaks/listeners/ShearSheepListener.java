package me.priceconnelly.pctweaks.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class ShearSheepListener implements Listener {
    @EventHandler
    public void onShearSheep(PlayerShearEntityEvent e){
        Player player = e.getPlayer();
        Entity entity = e.getEntity();
        if(entity.getType() == EntityType.SHEEP){
            player.sendMessage("Sheeeeeeeeep");
            e.setCancelled(true);
        } else{
            player.sendMessage("Sheep?");
        }
    }
}
