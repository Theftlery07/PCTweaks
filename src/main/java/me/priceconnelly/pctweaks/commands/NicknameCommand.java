package me.priceconnelly.pctweaks.commands;

import me.priceconnelly.pctweaks.models.PlayerData;
import me.priceconnelly.pctweaks.models.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NicknameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false){
            return true;
        }
        Player player = (Player) sender;
        if(!player.isOp()){
            player.sendMessage(ChatColor.RED + "You do not have permission to run this command");
            return true;
        }
        PlayerData playerData = PlayerData.getPlayerData(player);
        if(args.length == 0){
            player.sendMessage(ChatColor.RED + "Missing argument");
            return true;
        } else if(args[0].equalsIgnoreCase("clear")){
            if(args.length == 1){
                playerData.clearNickname();
                Rank.update(player);
                player.sendMessage(ChatColor.GREEN + "Your nickname has been reset");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if(target == null){
                player.sendMessage(ChatColor.RED + "" + target.getName() + "is not currently online");
                return true;
            }
            PlayerData.getPlayerData(target).clearNickname();
            Rank.update(target);
            target.sendMessage(ChatColor.RED + "Your nickname has been reset");
            player.sendMessage(ChatColor.GREEN + target.getName() + "'s nickname has been reset");
        } else{
            playerData.setNickname(args[0]);
            Rank.update(player);
            player.sendMessage("You title is now " + player.getDisplayName());
        }
        return true;
    }
}
