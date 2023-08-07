package me.priceconnelly.pctweaks.commands;

import me.pricec.myfirstplugin.Request;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptAllCommand implements CommandExecutor {
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
        if(Request.isEmpty()){
            player.sendMessage(ChatColor.RED + "There are no pending requests");
        } else{
            for(Request request : Request.getRequests()){
                request.fillRequest();
            }
            player.sendMessage("All pending requests " + ChatColor.GREEN +"accepted");
        }
        return true;
    }
}
