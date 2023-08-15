package me.priceconnelly.pctweaks.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class BackCommand implements CommandExecutor {
    private static HashMap<UUID, Location> lastLocation = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false){
            return true;
        }
        Player player = (Player) sender;
        if(!lastLocation.containsKey(player.getUniqueId())){
            player.sendMessage(ChatColor.RED + "There is no where to go back to");
            return true;
        }
        player.teleport(lastLocation.get(player.getUniqueId()));
        player.sendMessage(ChatColor.GREEN + "Backed");
        return true;
    }
    public static void setLastLocation(Player player){
        lastLocation.put(player.getUniqueId(), player.getLocation());
    }
}
