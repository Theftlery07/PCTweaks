package me.priceconnelly.pctweaks.commands;

import me.pricec.myfirstplugin.PlayerData;
import me.pricec.myfirstplugin.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false){
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerData.getPlayerData(player);
        if(playerData.isPvp()){
            playerData.setPvp(false);
            player.sendMessage("pvp " + ChatColor.RED + "disabled");
        } else {
            playerData.setPvp(true);
            player.sendMessage("pvp " + ChatColor.GREEN + "enabled");
        }
        Rank.update(player);
        return true;
    }
}
