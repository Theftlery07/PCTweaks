package me.priceconnelly.pctweaks.commands;

import me.priceconnelly.pctweaks.listeners.PlayerDeathListener;
import me.priceconnelly.pctweaks.models.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeathCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false) return true;
        Player player = (Player) sender;
        if(args.length == 0){
            player.sendMessage(ChatColor.RED + "" + PlayerData.getPlayerData(player).getDeaths() + " Deaths");
            return true;
        }
        if(!player.isOp()){
            player.sendMessage(ChatColor.RED + "You do not have permission to preform this command");
            return true;
        }
        if (args[1].equalsIgnoreCase("reload")){
            PlayerDeathListener.reInit();
            return true;
        } else if (args[1].equalsIgnoreCase("list")){
            player.sendMessage("Possible death messages");
            for(String s : PlayerDeathListener.getDeathMessages()) player.sendMessage("   " + s);
        }
        return true;
    }
}
