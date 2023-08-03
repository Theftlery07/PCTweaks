package me.priceconnelly.pctweaks;

import org.bukkit.plugin.java.JavaPlugin;

public final class PCTweaks extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Thank you for choosing PCTweaks!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Come Back Soon!!!");
    }
}
