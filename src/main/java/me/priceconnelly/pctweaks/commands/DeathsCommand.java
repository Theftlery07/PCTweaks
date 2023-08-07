package me.priceconnelly.pctweaks.commands;

import me.pricec.myfirstplugin.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeathsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false) return true;
        Player player = (Player) sender;
        player.sendMessage(ChatColor.RED + "" + PlayerData.getPlayerData(player).getDeaths() + " Deaths");
        return true;
    }
}
