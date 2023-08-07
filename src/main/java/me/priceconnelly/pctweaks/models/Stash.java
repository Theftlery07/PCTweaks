package me.priceconnelly.pctweaks.models;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Stash {
    private static final HashMap<UUID, Stash> stashes = new HashMap<UUID, Stash>();
    private final UUID uuid;
    private final List<StashedItem> items = new LinkedList<StashedItem>();
    private final Gui gui;
    public int id = 0;
    public Stash(Player owner){
        if(owner == null) throw  new NullPointerException("The owner of a stash cannot be null");
        uuid = owner.getUniqueId();
        stashes.put(owner.getUniqueId(), this);
        gui = new stashGui(owner.getName(), 54);
    }
    public void add(ItemStack itemStack){
        if(itemStack == null) throw new NullPointerException("Items added to a stash cannot be null");
        items.add(new StashedItem(itemStack, id++));
        gui.update();
    }
    private StashedItem[] getStash(){
        return items.toArray(new StashedItem[0]);
    }
    public ItemStack[] getItemStacks(){
        List<ItemStack> items = new LinkedList<ItemStack>();
        for(StashedItem stashedItem : getStash()) items.add(stashedItem.itemStack);
        return items.toArray(new ItemStack[0]);
    }
    public void emptyStash(){
        for(StashedItem stashedItem : getStash()) dumpItem(stashedItem);
    }
    private void dumpItem(StashedItem stashedItem){
        Player owner = Bukkit.getPlayer(uuid);
        ItemStack itemStack = stashedItem.itemStack;
        ItemStack[] inventory = owner.getInventory().getContents();
        int availableSpace = 0;
        for (int i = 0; i < 36; i++) {
            if(inventory[i] == null){
                availableSpace += itemStack.getMaxStackSize();
            }
//                else if(inventory[i].getType() == itemStack.getType()){
//                    availableSpace += itemStack.getType().getMaxStackSize() - inventory[i].getAmount();
//                }
        }
        if(availableSpace >= itemStack.getAmount()){
            while(itemStack.getAmount() > 0){
                ItemStack newItem = new ItemStack(itemStack);
                newItem.setAmount(Math.min(itemStack.getAmount(), itemStack.getMaxStackSize()));
                owner.getInventory().addItem(newItem);
                itemStack.setAmount(itemStack.getAmount() - itemStack.getMaxStackSize());
            }
            owner.getInventory().addItem(itemStack);
            items.remove(stashedItem);
        } else if (availableSpace > 0) {
            itemStack.setAmount(itemStack.getAmount() - availableSpace);
            ItemStack newItemStack = new ItemStack(itemStack);
            newItemStack.setAmount(availableSpace);
            owner.getInventory().addItem(newItemStack);
        }
    }
    public boolean isEmpty(){return items.isEmpty();}
    public void clear(){
        items.clear();
    }
    public static Stash getByOwner(Player player){
        if(player == null) throw new NullPointerException("Player cannot be null");
        if(!stashes.containsKey(player.getUniqueId())) stashes.put(player.getUniqueId(), new Stash(player));
        return stashes.get(player.getUniqueId());
    }
    //TODO: Maybe make a guiable class
    public StashedItem getByItemStack(ItemStack itemStack){
        for(StashedItem stashedItem : getStash()) if(stashedItem.itemStack.equals(itemStack)) return stashedItem;
        return null;
    }
    public StashedItem getById(int id){
        for(StashedItem stashedItem : getStash()) if(stashedItem.id == id) return stashedItem;
        return null;
    }
    public Gui getGui() {
        return gui;
    }
    private static class StashedItem{
        public final ItemStack itemStack;
        public final int id;
        public StashedItem(ItemStack itemStack, int id){
            this.itemStack = itemStack;
            this.id = id;
        }
    }
    private class stashGui extends Gui {
        public stashGui(String name, int size) {
            super(name, size);
        }

        @Override
        protected ItemStack getItem(ItemStack itemStack) {
            StashedItem stashedItem = getByItemStack(itemStack);
            ItemStack item = new ItemStack(stashedItem.itemStack);
            item.setAmount(1);
            ItemMeta itemMeta = item.getItemMeta();
            List<String> lore = new LinkedList<String>();
            lore.add(ChatColor.RESET + "Count: " + ChatColor.GOLD + stashedItem.itemStack.getAmount());
            lore.add(ChatColor.DARK_GRAY + "ID: " + stashedItem.id);
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            return item;
        }

        @Override
        protected ItemStack[] getItems() {
            return getItemStacks();
        }

        @Override
        protected void clicked(InventoryClickEvent e) {
            List<String> lore = e.getCurrentItem().getItemMeta().getLore();
            int id = 0;
            try{
                id = Integer.parseInt(lore.get(lore.size() - 1).split(" ")[1]);
            } catch (Exception exception){
                throw new IllegalStateException("Ahhhhhhhhhhhhhhh, something else is horribly wrong");
            }
            dumpItem(getById(id));
            update();
        }
    }
}
