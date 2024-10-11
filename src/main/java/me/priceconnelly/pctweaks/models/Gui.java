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

/**
 * <p>Abstract class to create guis for different uses. </p>
 * <p>The gui's can have multiple inventories (windows) with the ability to move forwards and backwards between them.</p>
 * <p>Items can be interacted with.</p>
 * <p>Gui's can be open across multiple instances and stay updated.</p>
 */
public abstract class Gui implements Listener{
    private final List<Inventory> _guis;
    private final String _name;
    private final int _size;
    //TODO: I guess this shouldn't be a static variable in theory
    private static final Material ARROW = Material.ARROW;
    private static final Material BLANK = Material.STAINED_GLASS_PANE;

    /**
     * Creates a gui
     * @param name The text displayed at the top of the inventory
     * @param size The number of rows in the inventory
     */
    public Gui(String name, int size){
        if (size < 18){
           size = 18;
           //TODO: Yell at them if the size is too small!!!
        }

        this._name = name;
        this._size = size;
        _guis = new LinkedList<Inventory>() {{add(Bukkit.createInventory(null, _size, _name));}};
        PCTweaks.getPlugin().getServer().getPluginManager().registerEvents(this, PCTweaks.getPlugin());
    }

    /**
     * Visually updates the gui
     */
    public void update(){
        ItemStack[] items = getItems();
        // Fills the bottom rows with BLANK
        for(Inventory gui : _guis) {
            gui.clear();
            for (int i = 0; i < 9; i++) {
                gui.setItem(_size - 9 + i, getBlank());
            }
        }
        // Does something???
        int size = 1;
        for (int i = 0; i < items.length; i++) {
            if(i % (size - 9) == 0 && i != 0){
                if(size > 1) _guis.get(size - 1).setItem(size - 2, getArrow("Back"));
                _guis.get(size - 1).setItem(size - 1, getArrow("Next"));
                size++;
            }
            if(size > _guis.size()) {
                Inventory gui = Bukkit.createInventory(null, size, _name);
                for (int j = 0; j < 9; j++) {
                    gui.setItem(size - 9 + j, getBlank());
                }
                this._guis.add(gui);
            }
            ItemStack item = new ItemStack(getItem(items[i]));
            _guis.get(size - 1).setItem(i % (size - 9), item);
        }
        // ????
        if(size > 1) _guis.get(size - 1).setItem(size - 2, getArrow("Back"));
        while(_guis.size() > size) {
            Inventory inventory = _guis.removeLast();
            for(HumanEntity humanEntity : inventory.getViewers()){
                humanEntity.openInventory(_guis.getLast());
            }
        }
    }

    /**
     * Abstract method to convert the item into a displayable form
     * @param itemStack The item to be displayed (needs decorations added)
     * @return The displayable item
     */
    protected abstract ItemStack getItem(ItemStack itemStack);

    /**
     * Abstract method to get all the items to be displayed
     * @return An array of items (not decorated)
     */
    protected abstract ItemStack[] getItems();

    /**
     * Creates an ARROW item
     * @param name The name of the item ex. "Back" "Next"
     * @return The item to be displayed
     */
    private ItemStack getArrow(String name){
        ItemStack itemStack = new ItemStack(ARROW);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Creates a BLANK item
     * @return The item to be displayed
     */
    private ItemStack getBlank(){
        ItemStack itemStack = new ItemStack(BLANK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Opens the gui
     * @param player The player to open the gui for
     */
    public void open(Player player){
        if(player == null) throw new NullPointerException("Player cannot be null");
        update();
        player.openInventory(_guis.getFirst());
    }

    /**
     * Move over one gui to the right ex. "Next"
     * @param player The player to move right for
     */
    private void moveRight(Player player){
        if(player == null) throw new NullPointerException("Player cannot be null");
        Inventory inventory = player.getOpenInventory().getTopInventory();
        int index = Math.min(_guis.indexOf(inventory) + 1, _guis.size());
        player.openInventory(_guis.get(index));
    }

    /**
     * Move over one gui to the left ex. "Back"
     * @param player The player to move left for
     */
    private void moveLeft(Player player){
        if(player == null) throw new NullPointerException("Player cannot be null");
        Inventory inventory = player.getOpenInventory().getTopInventory();
        int index = Math.max(_guis.indexOf(inventory) - 1, 0);
        player.openInventory(_guis.get(index));
    }

    /**
     * Checks if the gui contains a specific inventory (window)
     * @param inventory The inventory that is being searched for
     * @return {@code true} if the guis contains the specified inventory
     */
    public boolean contains(Inventory inventory){
        if(inventory == null) throw new NullPointerException("Inventory cannot be null");
        return _guis.contains(inventory);
    }

    /**
     *
     * @return The size of the gui
     */
    public int size(){
        return _size;
    }

    /**
     * Abstract method to handle an item being clicked within the gui
     * @param e The event where a valid item was clicked
     */
    protected abstract void clicked(InventoryClickEvent e);

    /**
     * Handles click events of item in within the gui: ARROWS, BLANKS, AIR and valid items
     * @param e Any click event within the gui
     */
    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e == null || e.getClickedInventory() == null) return;
        if(!contains(e.getView().getTopInventory())) return;
        e.setCancelled(true);
        ItemStack itemStack = e.getCurrentItem();
        if(itemStack.getType() == Material.AIR) return;
        if(itemStack.getType() == BLANK && _guis.size() - e.getSlot() <= 9) return;
        if(itemStack.getType() == ARROW) {
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
