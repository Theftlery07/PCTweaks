package me.priceconnelly.pctweaks.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand implements CommandExecutor {
    public static boolean frozen = false;
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
        //TODO: test invulnerability and make sure no kid can get away with it
        if(!frozen){
            Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.AQUA + "Freeze");
            for(Player p : Bukkit.getOnlinePlayers()) if(!p.isOp()) p.setInvulnerable(true);
        } else{
            Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.AQUA + "Unfreeze");
            for(Player p : Bukkit.getOnlinePlayers()) if(!p.isOp()) p.setInvulnerable(false);
        }
        frozen = !frozen;
        return true;
    }
}
