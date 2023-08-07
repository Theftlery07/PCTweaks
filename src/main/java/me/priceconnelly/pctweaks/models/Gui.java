package me.priceconnelly.pctweaks.models;

import me.priceconnelly.pctweaks.PCTweaks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

public abstract class Gui implements Listener{
    private final List<Inventory> gui;
    private final String name;
    private final int size;
    private static final Material Arrow = Material.ARROW;
    private static final Material Blank = Material.STAINED_GLASS_PANE;
    public Gui(String name, int size){
        this.name = name;
        this.size = size;
        gui = new LinkedList<Inventory>() {{add(Bukkit.createInventory(null, size, name));}};
        PCTweaks.getPlugin().getServer().getPluginManager().registerEvents(this, PCTweaks.getPlugin());
    }
    public void update(){
        ItemStack[] items = getItems();
        for(Inventory gui : gui) {
            gui.clear();
            for (int i = 0; i < 9; i++) {
                gui.setItem(size - 9 + i, getBlank());
            }
        }
        int size = 1;
        for (int i = 0; i < items.length; i++) {
            if(i % (size - 9) == 0 && i != 0){
                if(size > 1) gui.get(size - 1).setItem(size - 2, getArrow("Back"));
                gui.get(size - 1).setItem(size - 1, getArrow("Next"));
                size++;
            }
            if(size > gui.size()) {
                Inventory gui = Bukkit.createInventory(null, size, name);
                for (int j = 0; j < 9; j++) {
                    gui.setItem(size - 9 + j, getBlank());
                }
                this.gui.add(gui);
            }
            ItemStack item = new ItemStack(getItem(items[i]));
            gui.get(size - 1).setItem(i % (size - 9), item);
        }
        if(size > 1) gui.get(size - 1).setItem(size - 2, getArrow("Back"));
        while(gui.size() > size) {
            Inventory inventory = gui.remove(gui.size() - 1);
            for(HumanEntity humanEntity : inventory.getViewers()){
                humanEntity.openInventory(gui.get(gui.size() - 1));
            }
        }
    }
    protected abstract ItemStack getItem(ItemStack itemStack);
    protected abstract ItemStack[] getItems();
    private ItemStack getArrow(String name){
        ItemStack itemStack = new ItemStack(Arrow);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    private ItemStack getBlank(){
        ItemStack itemStack = new ItemStack(Blank);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public void open(Player player){
        if(player == null) throw new NullPointerException("Player cannot be null");
        update();
        player.openInventory(gui.get(0));
    }
    public void moveRight(Player player){
        if(player == null) throw new NullPointerException("Player cannot be null");
        Inventory inventory = player.getOpenInventory().getTopInventory();
        int index = Math.min(gui.indexOf(inventory) + 1, gui.size());
        player.openInventory(gui.get(index));
    }
    public void moveLeft(Player player){
        if(player == null) throw new NullPointerException("Player cannot be null");
        Inventory inventory = player.getOpenInventory().getTopInventory();
        int index = Math.max(gui.indexOf(inventory) - 1, 0);
        player.openInventory(gui.get(index));
    }
    public boolean contains(Inventory inventory){
        if(inventory == null) throw new NullPointerException("Inventory cannot be null");
        return gui.contains(inventory);
    }
    public int size(){
        return size;
    }

    protected abstract void clicked(InventoryClickEvent e);

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e == null || e.getClickedInventory() == null) return;
        if(!contains(e.getView().getTopInventory())) return;
        e.setCancelled(true);
        ItemStack itemStack = e.getCurrentItem();
        if(itemStack.getType() == Material.AIR) return;
        if(itemStack.getType() == Blank && gui.size() - e.getSlot() <= 9) return;
        if(itemStack.getType() == Arrow) {
            if(e.getSlot() % 9 == 8){
                moveRight((Player)e.getWhoClicked());
                return;
            } else if (e.getSlot() % 9 == 7){
                moveLeft((Player)e.getWhoClicked());
                return;
            }
        }
        clicked(e);
    }
}
