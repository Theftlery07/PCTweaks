package me.priceconnelly.pctweaks.listeners;

import me.pricec.myfirstplugin.commands.FreezeCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if(FreezeCommand.frozen && !e.getPlayer().isOp()){
            e.setCancelled(true);
        }
    }
}
