package me.priceconnelly.pctweaks.commands;

import me.priceconnelly.pctweaks.models.Cooldown;
import me.priceconnelly.pctweaks.models.Teleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportACommand implements CommandExecutor {
    private static final Cooldown cooldown = new Cooldown(5);
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
        Player target = null;
        target = Bukkit.getPlayer(args[0]);
        if(target == null){
            player.sendMessage(ChatColor.RED + args[0] + " is not currently online");
            return true;
        } else if(player == target){
            player.sendMessage(ChatColor.RED + "You cannot target yourself");
            return true;
        }
        if(!player.isOp() && cooldown.onCooldown(player)) return true;
        new Teleport(player, target, Teleport.TpType.A);
        return true;
    }
}
