package me.priceconnelly.pctweaks.models;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Cooldown {
    private final HashMap<UUID, Long> playerCooldown = new HashMap<UUID, Long>();
    private final int time;
    public Cooldown(double time){
        this.time = (int)(time * 1000);
    }
    public boolean onCooldown(Player player){
        if(!playerCooldown.containsKey(player.getUniqueId())){
            playerCooldown.put(player.getUniqueId(), (long)0);
        }
        Long timeElapsed = System.currentTimeMillis() - playerCooldown.get(player.getUniqueId());
        if(timeElapsed <= time){
            player.sendMessage(ChatColor.RED + "Time remaining on cooldown " + (Math.ceil((time - timeElapsed) / 100) / 10) + "s");
            return true;
        }
        playerCooldown.put(player.getUniqueId(), System.currentTimeMillis());
        return false;
    }
}
