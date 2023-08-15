package me.priceconnelly.pctweaks.models;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Request {
    private static final List<Request> requests = new LinkedList<Request>();
    private static final Gui gui = new RequestGui();

    private final UUID uuid;
    private ItemStack item;
    private final String id;
    public Request(Player player, ItemStack itemStack, int count){
        if(player == null) throw new NullPointerException("Player cannot be null");
        if(itemStack == null) throw new NullPointerException("Item cannot be null");
        if(count <= 0) throw new IllegalArgumentException("Request count must be greater than 0");

        uuid = player.getUniqueId();
        this.item = new ItemStack(itemStack);
        this.item.setAmount(count);
        this.id = UUID.randomUUID().toString();
        requests.add(this);
    }
    public TextComponent getRequestMessage(){
        Player player = Bukkit.getPlayer(uuid);
        if(player == null) return null;
        TextComponent message = new TextComponent();

        TextComponent yes = new TextComponent("[✔]");
        yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + id));
        yes.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(net.md_5.bungee.api.ChatColor.GREEN + "Accept").create()));
        yes.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        message.addExtra(yes);

        TextComponent no = new TextComponent("  [X] ");
        no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reject " + id));
        no.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(net.md_5.bungee.api.ChatColor.RED + "Deny").create()));
        no.setColor(net.md_5.bungee.api.ChatColor.RED);
        message.addExtra(no);

        String text = ChatColor.BOLD + player.getDisplayName() + ChatColor.RESET + " is requesting "  + ChatColor.BOLD + item.getAmount() + " " + item.getType();
        TextComponent item = new TextComponent(text);
        item.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/requestGui"));
        item.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Open in Gui").create()));
        //item.setColor(net.md_5.bungee.api.ChatColor.RED);
        message.addExtra(item);

        return message;
    }
    public boolean fillRequest(){
        Player player = Bukkit.getPlayer(uuid);
        //TODO: uhhhhhh, stack sizes how?
        if(player == null) return false;
        requests.remove(this);
        gui.update();
        ItemStack[] itemStack = player.getInventory().getContents();
        int availableSpace = 0;
        for (int i = 0; i < 36; i++) {
            if(itemStack[i] == null){
                availableSpace += item.getMaxStackSize();
            }
//                else if(itemStack[i].getType() == material){
//                    availableSpace += item.getMaxStackSize() - itemStack[i].getAmount();
//                }
        }
        if(availableSpace >= item.getAmount()){
            player.sendMessage(ChatColor.GREEN + "Your request for " + ChatColor.BOLD + item.getAmount() + " " + item.getType() + ChatColor.GREEN + " has been accepted");
            while(item.getAmount() > 0){
                ItemStack newItem = new ItemStack(item);
                newItem.setAmount(Math.min(item.getAmount(), item.getMaxStackSize()));
                player.getInventory().addItem(newItem);
                item.setAmount(item.getAmount() - item.getMaxStackSize());
            }
//            player.getInventory().addItem(new ItemStack(item));
            return true;
        }
        else{
            player.sendMessage(ChatColor.GREEN + "Your request for " + ChatColor.BOLD + item.getAmount() + " " + item.getType() + ChatColor.GREEN + " has been accepted\nand moved to your stash");
            Stash stash = Stash.getByOwner(player);
            stash.add(new ItemStack(item));
            return true;
        }
//        return true;
    }
    public boolean denyRequest(){
        Player player = Bukkit.getPlayer(uuid);
        if(player == null){
            return false;
        }
        requests.remove(this);
        gui.update();
        player.sendMessage(ChatColor.RED + "Your request for " + ChatColor.BOLD + item.getAmount() + " " + item.getType() + ChatColor.RED + " has been denied");
        return true;
    }
    public static Request getByID(String id){
        for(Request request : requests){
            if(request.getId().equals(id)) return request;
        }
        return null;
    }
    public static Request getByItemStack(ItemStack itemStack){
        for(Request request : requests) if(request.getItem() == itemStack) return request;
        return null;
    }
    public Player getPlayer(){
        return Bukkit.getPlayer(uuid);
    }
    public ItemStack getItem(){
        return item;
    }
    public String getId(){
        return id;
    }
    public static Request[] getRequests(){
        return requests.toArray(new Request[0]);
    }
    public static boolean isEmpty(){return requests.isEmpty();}
    public static int size(){
        return requests.size();
    }
    public static Gui getGui(){
        return gui;
    }

    private static class RequestGui extends Gui{
        public RequestGui() {
            super("Request Handler", 54);
        }
        @Override
        protected ItemStack getItem(ItemStack itemStack) {
            Request request = getByItemStack(itemStack);
            ItemStack item = new ItemStack(request.getItem());
            item.setAmount(1);
            ItemMeta itemMeta = item.getItemMeta();
            List<String> lore = new LinkedList<String>();
            lore.add(ChatColor.GREEN + "[LMB]" + ChatColor.RED + "   [RMB]");
            if(request.getPlayer() != null){
                lore.add(ChatColor.RESET + "Player: " + ChatColor.GOLD + request.getPlayer().getDisplayName());
            } else {
                lore.add(ChatColor.RED + "Player is not Online");
            }
            lore.add(ChatColor.RESET + "Count: " + ChatColor.GOLD + request.getItem().getAmount());
            lore.add(ChatColor.DARK_GRAY + "ID: " + request.getId());
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            return item;
        }

        @Override
        protected ItemStack[] getItems() {
            Request[] requests = Request.getRequests();
            ItemStack[] items = new ItemStack[requests.length];
            for (int i = 0; i < requests.length; i++) {
                items[i] = requests[i].getItem();
            }
            return items;
        }

        @Override
        protected void clicked(InventoryClickEvent e) {
            List<String> lore = e.getCurrentItem().getItemMeta().getLore();
            String id = "";
            try{
                id = lore.get(lore.size() - 1).split(" ")[1];
            } catch (Exception exception){
                throw new IllegalStateException("Ahhhhhhhhhhhhhhh, something is horribly wrong");
            }
            Request request = Request.getByID(id);
            if(request == null){
                e.getWhoClicked().sendMessage("This request has already been processed");
                return;
            }
            if(e.isLeftClick()){
                if(request.fillRequest()){
                    e.getWhoClicked().sendMessage(ChatColor.GREEN + "Request accepted");
                } else{
                    e.getWhoClicked().sendMessage(ChatColor.RED + request.getPlayer().getDisplayName() + " is not currently online");
                    return;
                }
            } else if (e.isRightClick()) {
                request.denyRequest();
            }
        }
    }
}
