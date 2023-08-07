package me.priceconnelly.pctweaks.models;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Teleport {
    public static final List<Teleport> teleports = new LinkedList<Teleport>();
    private final UUID uuidPlayer;
    private final UUID uuidTarget;
    private final String id;
    private final long time;
    private final long MAX_TIME = 10000;
    public Teleport(Player player, Player target){
        if(player == null) throw new NullPointerException("Player cannot be null");
        if(target == null) throw new NullPointerException("Target cannot be null");
        uuidPlayer = player.getUniqueId();
        uuidTarget = target.getUniqueId();
        id = UUID.randomUUID().toString();
        time = System.currentTimeMillis();
        teleports.add(this);
        target.sendMessage(player.getDisplayName() + " offers a teleport");
        target.spigot().sendMessage(getTeleportMessage());
    }
    public void acceptTeleport(){
        Player player = Bukkit.getPlayer(uuidPlayer);
        Player target = Bukkit.getPlayer(uuidTarget);
        if(player == null){
            return;
        } else if(target == null){
            return;
        } else if(System.currentTimeMillis() - time > MAX_TIME){
            teleports.remove(this);
            target.sendMessage(ChatColor.RED + "Teleport has expired");
            return;
        }
        teleports.remove(this);
        //TODO: Add no damage ticks to prevent too much trolling
        target.setNoDamageTicks(100);
        target.teleport(player);
        player.sendMessage("Teleport" + ChatColor.GREEN + " accepted");
        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + target.getDisplayName() + ChatColor.RED + " accepted the teleport");
    }
    public void denyTeleport(){
        Player player = Bukkit.getPlayer(uuidPlayer);
        Player target = Bukkit.getPlayer(uuidTarget);
        if(player == null){

        } else if(target == null){

        } else if(System.currentTimeMillis() - time > MAX_TIME){
            target.sendMessage(ChatColor.RED + "Teleport has already expired");
        }
        teleports.remove(this);
        target.sendMessage("Teleport" + ChatColor.RED + " rejected");
        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + target.getDisplayName() + ChatColor.RED + " rejected the teleport");
    }
    public static Teleport getByID(String id){
        for(Teleport teleport : Teleport.teleports){
            if(teleport.getId().equals(id)) return teleport;
        }
        return null;
    }
    private TextComponent getTeleportMessage(){
        TextComponent message = new TextComponent();

        TextComponent yes = new TextComponent("[Accept]");
        yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/acceptTP " + id));
        yes.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        message.addExtra(yes);

        TextComponent no = new TextComponent("   [Reject]");
        no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rejectTP " + id));
        no.setColor(net.md_5.bungee.api.ChatColor.RED);
        message.addExtra(no);
        return message;
    }
    public String getId(){
        return id;
    }
    public static boolean isEmpty(){
        return teleports.isEmpty();
    }

    private class teleportGui extends Gui {

        public teleportGui(String name, int size){
            super(name, size);
        }
        @Override
        protected ItemStack getItem(ItemStack itemStack) {
            return null;
        }

        @Override
        protected ItemStack[] getItems() {
            return new ItemStack[0];
        }

        @Override
        protected void clicked(InventoryClickEvent e) {

        }
    }
}
