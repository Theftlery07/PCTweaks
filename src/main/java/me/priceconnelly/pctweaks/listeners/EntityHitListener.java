package me.priceconnelly.pctweaks.listeners;

import me.pricec.myfirstplugin.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityHitListener implements Listener {
    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player attacker = (Player) e.getDamager();
            Player attacked = (Player) e.getEntity();
            // if the attacked doesn't have pvp enabled
            if(!PlayerData.getPlayerData(attacked).isPvp()){
                e.setCancelled(true);
                attacker.sendMessage(ChatColor.RED + attacked.getDisplayName() + " does not have pvp enabled");
            }
            // if the attacker doesn't have pvp enabled
            if(!PlayerData.getPlayerData(attacker).isPvp()){
                e.setCancelled(true);
                attacker.sendMessage(ChatColor.RED + "You do not have pvp enabled");
            }
        }
    }
}
