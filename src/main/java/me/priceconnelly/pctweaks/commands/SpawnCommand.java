package me.priceconnelly.pctweaks.commands;

import me.priceconnelly.pctweaks.PCTweaks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false){
            return true;
        }
        Player player = (Player) sender;
        player.teleport(PCTweaks.getWorld().getSpawnLocation());
        return true;
    }
}
