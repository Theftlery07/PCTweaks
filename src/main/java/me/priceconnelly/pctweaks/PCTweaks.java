package me.priceconnelly.pctweaks;

import me.priceconnelly.pctweaks.commands.*;
import me.priceconnelly.pctweaks.listeners.*;
import me.priceconnelly.pctweaks.models.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class PCTweaks extends JavaPlugin {
    private static PCTweaks plugin;
    private static World world;
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        world = Bukkit.getWorlds().get(0);

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        System.out.println("Hello World!");

        //Misc.
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new ShearSheepListener(), this);
        // Request
        //TODO: Compress all these stupid commands into 1 - 2
        getCommand("request").setExecutor(new RequestCommand());
        getCommand("accept").setExecutor(new AcceptCommand());
        getCommand("acceptAll").setExecutor(new AcceptAllCommand());
        getCommand("reject").setExecutor(new RejectCommand());
        getCommand("rejectAll").setExecutor(new RejectAllCommand());
        getCommand("pending").setExecutor(new PendingCommand());
        // Stash
        getCommand("stash").setExecutor(new StashCommand());
        // Chat
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        // InvSee
        getCommand("inventorySee").setExecutor(new InvSeeCommand());
        // Home
        getCommand("home").setExecutor(new HomeCommand());
        // Spawn
        getCommand("spawn").setExecutor(new SpawnCommand());
        // Freeze
        getCommand("freeze").setExecutor(new FreezeCommand());
        getServer().getPluginManager().registerEvents(new MoveListener(), this);
        // Rank
        getCommand("rank").setExecutor(new RankCommand());
        // Teleport
        //TODO: Can probably compress this too
        getCommand("tpa").setExecutor(new TeleportACommand());
        getCommand("tpr").setExecutor(new TeleportRCommand());
        getCommand("acceptTP").setExecutor(new AcceptTPCommand());
        getCommand("rejectTP").setExecutor(new RejectTPCommand());
        getServer().getPluginManager().registerEvents(new PlayerTeleportListener(), this);
        // Sheep
        getCommand("sheep").setExecutor(new SheepCommand());
        // Pvp
        getCommand("pvp").setExecutor(new PvpCommand());
        getServer().getPluginManager().registerEvents(new EntityHitListener(), this);
        // Break
        getCommand("break").setExecutor(new BreakCommand());
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(), this);
        // Peaceful
        getCommand("peaceful").setExecutor(new PeacefulCommand());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> PeacefulCommand.scan(), 0, PeacefulCommand.scanInterval);
        // Nickname
        getCommand("nickname").setExecutor(new NicknameCommand());
        // HCF
        getCommand("hcf").setExecutor(new HCFCommand());
        // Death Counter
        getCommand("deaths").setExecutor(new DeathsCommand());
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        // Back
        getCommand("back").setExecutor(new BackCommand());
        // Trust
        getCommand("trust").setExecutor(new TrustCommand());
        // Player Data
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            try {
                PlayerData.saveData();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 6000, 6000);
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Come Back Soon!!!");
        // PlayerData
        try {
            PlayerData.saveData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static PCTweaks getPlugin(){
        return plugin;
    }
    public static World getWorld(){
        return world;
    }
}
