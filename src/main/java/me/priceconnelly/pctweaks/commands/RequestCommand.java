package me.priceconnelly.pctweaks.commands;

import me.pricec.myfirstplugin.Cooldown;
import me.pricec.myfirstplugin.Request;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RequestCommand implements CommandExecutor {
    private static final Cooldown cooldown = new Cooldown(10);
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false){
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 1 && args[0].equalsIgnoreCase("view")){
            if(!player.isOp()){
                player.sendMessage(ChatColor.RED + "You do not have permission to preform this command");
                return true;
            }
            Request.getGui().open(player);
            return true;
        }
        if(args.length > 1){
            player.sendMessage(ChatColor.RED + "Too many arguments passed");
            return true;
        }
        ItemStack itemstack = player.getInventory().getItemInMainHand();
        if(itemstack.getType() == Material.AIR){
            player.sendMessage(ChatColor.RED + "Error 404: item not found");
            return true;
        }
        int count = 1;
        if(args.length == 1){
            try{
                count = Integer.parseInt(args[0]);
            } catch (Exception e){
                player.sendMessage(ChatColor.RED + "Invalid argument");
                return true;
            }
            if(count <= 0){
                player.sendMessage(ChatColor.RED + "Number of items must be greater than 0");
                return true;
            }
        }
        if(!player.isOp() && cooldown.onCooldown(player)) return true;
        Request request = new Request(player, itemstack, count);
        Request.getGui().update();
        TextComponent requestMessage = request.getRequestMessage();
        for(Player Op : Bukkit.getOnlinePlayers()){
            if(!Op.isOp()) continue;
            Op.spigot().sendMessage(requestMessage);
        }
        player.sendMessage(ChatColor.GREEN + "Request sent");
        return true;
    }
}
