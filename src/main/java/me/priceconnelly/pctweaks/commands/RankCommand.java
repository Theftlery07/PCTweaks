package me.priceconnelly.pctweaks.commands;

import me.pricec.myfirstplugin.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false){
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 0){
            player.sendMessage(ChatColor.RED + "Missing argument");
            return true;
        } else if(args[0].equalsIgnoreCase("list")){
            player.sendMessage("All available ranks:");
            for(String rank : Rank.getRanks(player)) player.sendMessage("   " + rank);
        } else if (args[0].equalsIgnoreCase("reload") && player.isOp()) {
            Rank.reInit();
            player.sendMessage(ChatColor.GREEN + "Ranks reloaded");
        } else if(args[0].equalsIgnoreCase("clear")){
            if(args.length == 1){
                Rank.clearRank(player);
                player.sendMessage(ChatColor.GREEN + "Your rank has been reset");
                return true;
            }
            if(!player.isOp()){
                player.sendMessage(ChatColor.RED + "You do not have permission to reset someone else's rank");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if(target == null){
                player.sendMessage(ChatColor.RED + "" + target.getName() + "is not currently online");
                return true;
            }
            Rank.clearRank(target);
            target.sendMessage(ChatColor.RED + "Your rank has been reset");
            player.sendMessage(ChatColor.GREEN + target.getName() + "'s rank has been reset");
        } else if (args[0].equalsIgnoreCase("remove")) {
            //TODO: Same issue here
            Rank.removeRank(player, args);
            player.sendMessage("You title is now " + player.getDisplayName());
        } else if (args[0].equalsIgnoreCase("add")) {
            //TODO: Oopsie, it also taking in the first argument 😬
            Rank.giveRank(player, args);
            player.sendMessage("You title is now " + player.getDisplayName());
        } else{
            player.sendMessage(ChatColor.RED + "Invalid argument");
        }
        return true;
    }
}
