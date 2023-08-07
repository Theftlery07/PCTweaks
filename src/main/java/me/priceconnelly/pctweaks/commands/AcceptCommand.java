package me.priceconnelly.pctweaks.commands;

import me.pricec.myfirstplugin.Request;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false) return true;
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
        Request request = null;
        try{
            request = Request.getByID(args[0]);
        } catch (Exception e){
            player.sendMessage(ChatColor.RED + "Invalid id");
            return true;
        }
        if(Request.isEmpty()){
            player.sendMessage(ChatColor.GREEN + "There are no pending requests");
            return true;
        }
        if(request == null){
            player.sendMessage(ChatColor.RED + "This request has already been processed");
            return true;
        }
        if(request.fillRequest()){
            player.sendMessage("Request" + ChatColor.GREEN + " accepted");
        } else{
            player.sendMessage(ChatColor.RED + request.getPlayer().getDisplayName() + " is not currently online");
        }
        return true;
    }
}
