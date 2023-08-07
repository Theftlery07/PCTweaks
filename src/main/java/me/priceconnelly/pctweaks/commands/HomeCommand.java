package me.priceconnelly.pctweaks.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false){
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 0){
            if(player.getBedSpawnLocation() == null){
                player.sendMessage(ChatColor.RED + "Home not set");
                return true;
            }
            player.teleport(player.getBedSpawnLocation());
            player.sendMessage(ChatColor.GREEN + "Home sweet home");
        } else if(args[0].equalsIgnoreCase("set")){
            player.setBedSpawnLocation(player.getLocation(), true);
            player.sendMessage(ChatColor.GREEN + "Home set");
        }

        return true;
    }
}
