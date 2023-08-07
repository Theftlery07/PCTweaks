package me.priceconnelly.pctweaks.commands;

import me.priceconnelly.pctweaks.models.Break;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BreakCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false){
            return true;
        }
        Player player = (Player) sender;
        if(!player.isOp()){
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
            return true;
        }
        if(args.length == 0){
            player.sendMessage(ChatColor.RED + "Missing argument");
            return true;
        } else if(args[0].equalsIgnoreCase("list")) {
            player.sendMessage("All available breaks:");
            for(String brk : Break.getBreaks()) player.sendMessage("   " + brk);
        } else if(args[0].equalsIgnoreCase("reload")) {
            Break.reInit();
            player.sendMessage(ChatColor.GREEN + "Breaks reloaded");
            return true;
        } else if(args[0].equalsIgnoreCase("start")) {
            if(Break.isRunning()){
                player.sendMessage(ChatColor.RED + "A break has already been initiated");
                return true;
            }
            if(args.length == 1){
                player.sendMessage(ChatColor.RED + "Missing argument");
                return true;
            }
            if(!Break.isValid(args[1])){
                player.sendMessage(ChatColor.RED + args[0] + " is not a valid break");
                return true;
            }
            Break.start(args[1], player);
            player.sendMessage(ChatColor.GREEN + "Break initiated");
            return true;
        } else if(args[0].equalsIgnoreCase("stop")) {
            if(!Break.isRunning()){
                player.sendMessage(ChatColor.RED + "There are no impending breaks");
                return true;
            }
            if(!Break.isOwner(player)){
                player.sendMessage(ChatColor.RED + "You are not the player who initiated the break");
                return true;
            }
            Break.stop(player);
            player.sendMessage(ChatColor.GREEN + "Break stopped");
            return true;
        } else if(args[0].equalsIgnoreCase("reopen")) {
            if(Break.isOpen()){
                player.sendMessage(ChatColor.RED + "The server is currently open");
                return true;
            }
            Break.reOpen();
            Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "The break is over");
            return true;
        } else{
            player.sendMessage(ChatColor.RED + "Invalid argument");
            return true;
        }
        return true;
    }
}
