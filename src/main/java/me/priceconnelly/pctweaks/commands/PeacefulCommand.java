package me.priceconnelly.pctweaks.commands;

import me.pricec.myfirstplugin.PlayerData;
import me.pricec.myfirstplugin.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import static me.pricec.myfirstplugin.MyFirstPlugin.getPlugin;

public class PeacefulCommand implements CommandExecutor {

    private static final int scanRadius = getPlugin().getConfig().getInt("peaceful_radius", 20);
    public static final int scanInterval = getPlugin().getConfig().getInt("peaceful_interval", 100);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false){
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerData.getPlayerData(player);
        if(playerData.isPeaceful()){
            playerData.setPeaceful(false);
            player.sendMessage("peaceful mode " + ChatColor.RED + "disabled");
        } else {
            playerData.setPeaceful(true);
            player.sendMessage("peaceful mode " + ChatColor.GREEN + "enabled");
        }
        Rank.update(player);
        return true;
    }
    public static void scan(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(!PlayerData.getPlayerData(player).isPeaceful()) continue;
            for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), scanRadius, scanRadius, scanRadius)){
                if(entity instanceof Monster) entity.remove();
            }
        }
    }
}
