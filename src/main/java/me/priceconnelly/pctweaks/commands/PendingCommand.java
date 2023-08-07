package me.priceconnelly.pctweaks.commands;

import me.priceconnelly.pctweaks.models.Request;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PendingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false){
            return true;
        }
        Player player = (Player) sender;
        if(!player.isOp()){
            boolean pending = false;
            for(Request request : Request.getRequests()){
                if(request.getPlayer() != player) continue;
                if(!pending) player.sendMessage("Your pending requests:");
                pending = true;
                player.sendMessage(ChatColor.BOLD + "    " + request.getItem().getAmount() + " " + request.getItem().getType());
            }
            if(!pending){
                player.sendMessage(ChatColor.GREEN + "You have no pending requests");
            }
            return true;
        }
        if(Request.isEmpty()){
            player.sendMessage(ChatColor.GREEN + "There are no pending requests");
            return true;
        }
        if(args.length == 0){
            for(Request request : Request.getRequests()){
                player.spigot().sendMessage(request.getRequestMessage());
            }
        } else{
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null){
                player.sendMessage(ChatColor.RED + args[0] + " is not currently online");
                return true;
            }
            boolean pending = false;
            for(Request request : Request.getRequests()){
                //TODO: Find a better way to do this
                if(!request.getPlayer().getUniqueId().equals(target.getUniqueId())) continue;
                pending = true;
                player.spigot().sendMessage(request.getRequestMessage());
            }
            if(!pending){
                player.sendMessage(ChatColor.GREEN + target.getDisplayName() + " has no pending requests");
            }
        }
        return true;
    }
}
