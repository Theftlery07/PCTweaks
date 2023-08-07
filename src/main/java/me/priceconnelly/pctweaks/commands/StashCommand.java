package me.priceconnelly.pctweaks.commands;

import me.pricec.myfirstplugin.Stash;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StashCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false){
            return true;
        }
        Player player = (Player) sender;
        Stash stash = Stash.getByOwner(player);
        if(args.length == 0){
            player.sendMessage(ChatColor.RED + "Missing argument");
        } else if(args[0].equalsIgnoreCase("empty")){
            if(stash.isEmpty()){
                player.sendMessage(ChatColor.GREEN + "Your stash is empty");
                return true;
            }
            stash.emptyStash();
            if(stash.isEmpty()){
                player.sendMessage(ChatColor.GREEN + "You have no remaining items");
            } else{
                player.sendMessage(ChatColor.GREEN + "Remaining items:");
                for(ItemStack itemStack : stash.getItemStacks()){
                    player.sendMessage(ChatColor.GREEN + "    " + itemStack.getAmount() + " " + itemStack.getType());
                }
            }
        } else if(args[0].equalsIgnoreCase("clear")){
            if(args.length == 1){
                stash.clear();
                player.sendMessage(ChatColor.GREEN + "Stash cleared");
                return true;
            }
            if(!player.isOp()){
                player.sendMessage(ChatColor.RED + "You do not have permission to clear someone else's stash");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if(target == null){
                player.sendMessage(ChatColor.RED + "" + target.getName() + "is not currently online");
                return true;
            }
            Stash.getByOwner(target).clear();
            target.sendMessage(ChatColor.RED + "Your stash has been cleared");
            player.sendMessage(ChatColor.GREEN + "Stash cleared");
        } else if(args[0].equalsIgnoreCase("view")){
            if(args.length == 1){
                stash.getGui().open(player);
                return true;
            }
            if(!player.isOp()){
                player.sendMessage(ChatColor.RED + "You do not have permission to view someone else's stash");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if(target == null){
                player.sendMessage(ChatColor.RED + "" + target.getName() + "is not currently online");
                return true;
            }
            Stash.getByOwner(target).getGui().open(player);
        }
        return true;
    }
}
