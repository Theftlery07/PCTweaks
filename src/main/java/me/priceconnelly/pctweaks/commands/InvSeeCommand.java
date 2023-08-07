package me.priceconnelly.pctweaks.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvSeeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false){
            return true;
        }
        Player player = (Player) sender;
        if(!player.isOp()){
            player.sendMessage(ChatColor.RED + "You do not have permission to preform this command");
            return true;
        }
        if(args.length > 1){
            player.sendMessage(ChatColor.RED + "Too many arguments");
            return true;
        } else if(args.length == 0){
            player.sendMessage(ChatColor.RED + "Missing arguments");
            return true;
        }
        //TODO: Be able to see into tile entities; maybe make its own toggle?
        //TODO: Can I at least hide the interaction but still interact with it?
        Player target = Bukkit.getPlayer(args[0]);
        if(target == null){
            player.sendMessage(ChatColor.RED + args[0] + " is not currently online");
            return true;
        }
        player.openInventory(target.getInventory());
        return true;
    }
}
