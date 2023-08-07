package me.priceconnelly.pctweaks.commands;

import me.pricec.myfirstplugin.Teleport;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RejectTPCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false) return true;
        Player player = (Player) sender;
        if(args.length != 1){
            return true;
        }
        Teleport teleport = null;
        try{
            teleport = Teleport.getByID(args[0]);
        } catch (Exception e){
            player.sendMessage(ChatColor.RED + "Invalid id");
            return true;
        }
        if(teleport == null){
            player.sendMessage(ChatColor.RED + "This teleport has already been processed");
            return true;
        }
        teleport.denyTeleport();
        return true;
    }
}
