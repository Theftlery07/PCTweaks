package me.priceconnelly.pctweaks.commands;

import me.priceconnelly.pctweaks.models.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TrustCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false){
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 0){
            player.sendMessage(ChatColor.RED + "Missing argument");
            return true;
        }
        if(args[0].equalsIgnoreCase("clear")){
            PlayerData.getPlayerData(player).clearTrustedPlayers();
            player.sendMessage(ChatColor.GREEN + "You trust no one");
        } else if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage(ChatColor.GREEN + "Trusted Players:");
            for(UUID uuid : PlayerData.getPlayerData(player).getTrustedPlayers()) player.sendMessage("   " + Bukkit.getPlayer(uuid));
        }
        if(args.length == 1){
            player.sendMessage(ChatColor.RED + "Missing argument");
            return true;
        }
        Player target = null;
        target = Bukkit.getPlayer(args[1]);
        if(target == null){
            player.sendMessage(ChatColor.RED + args[1] + " is not currently online");
            return true;
        } else if(player == target){
            player.sendMessage(ChatColor.RED + "You cannot target yourself");
            return true;
        }
        if(args[0].equalsIgnoreCase("add")){
            if(PlayerData.getPlayerData(player).addTrustedPlayer(target)){
                player.sendMessage(ChatColor.GREEN + "You trust " + ChatColor.RESET + "" + target.getDisplayName());
                target.sendMessage(target.getDisplayName() + "" + ChatColor.GREEN + " trusts you");
            } else{
                player.sendMessage(ChatColor.RED + "You already trust them");
            }
        } else if (args[0].equalsIgnoreCase("remove")){
            if(PlayerData.getPlayerData(player).removeTrustedPlayer(target)){
                player.sendMessage(ChatColor.GREEN + "You no longer trust " + ChatColor.RESET + "" + target.getDisplayName());
                target.sendMessage(target.getDisplayName() + "" + ChatColor.RED + " no longer trusts you");
            } else{
                player.sendMessage(ChatColor.RED + "You didn't trust them in the first place");
            }
        }
        return true;
    }
}
