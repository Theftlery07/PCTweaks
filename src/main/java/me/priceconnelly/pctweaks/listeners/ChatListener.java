package me.priceconnelly.pctweaks.listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if(e.getPlayer() != null){
//            e.setCancelled(true);
//            TextComponent text = new TextComponent(e.getPlayer().getDisplayName() + ": " + e.getMessage());
//            if(!e.getPlayer().isOp()) text.setColor(ChatColor.DARK_GRAY);
//            e.getPlayer().chat(text.toString());
            if(!e.getPlayer().isOp()) e.setMessage(ChatColor.DARK_GRAY + e.getMessage());
        }
    }
}
