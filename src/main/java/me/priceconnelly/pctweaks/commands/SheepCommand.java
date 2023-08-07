package me.priceconnelly.pctweaks.commands;

import me.pricec.myfirstplugin.Cooldown;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.util.Vector;

import java.util.Collection;

import static me.pricec.myfirstplugin.MyFirstPlugin.getPlugin;
import static me.pricec.myfirstplugin.MyFirstPlugin.getWorld;

public class SheepCommand implements CommandExecutor {
    private static final Cooldown cooldown = new Cooldown(60);
    private final int options = 17;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player == false) return true;
        Player player = (Player) sender;
        int rand = (int)Math.floor(Math.random() * options);
        if(player.isOp() && args.length == 1){
            try {
                rand = Integer.parseInt(args[0]);
            } catch (Exception e){
                player.sendMessage(ChatColor.RED + "Idiot");
            }
        }
        if(!player.isOp() && cooldown.onCooldown(player)) return true;
        switch (rand){
            case 0: {
                createSheep(player);
                break;
            } case 1: {
                getPlugin().getServer().broadcastMessage("<Sheep> Baa");
                break;
            } case 2: {
                Sheep sheep = getSheep(player);
                sheep.setInvulnerable(true);
                Location sheepLoc = sheep.getLocation();
                sheep.teleport(player);
                player.teleport(sheepLoc);
                player.getLocation().setDirection(sheepLoc.getDirection());
                break;
            } case 3: {
                //TODO: Controllable sheep :/
//                Sheep sheep = getSheep(player);
//                player.setSpectatorTarget(sheep);
                break;
            } case 4: {
                for(Sheep sheep : getWorld().getEntitiesByClass(Sheep.class)) {
                    sheep.teleport(player);
                    sheep.setInvulnerable(true);
                }
                break;
            } case 5: {
                Sheep[] sheeps = getWorld().getEntitiesByClass(Sheep.class).toArray(new Sheep[0]);
                if(sheeps.length == 0) break;
                double speed = 0.1;
                double angle = (Math.PI * 2) / sheeps.length;
                for (int i = 0; i < sheeps.length; i++) {
                    sheeps[i].teleport(player);
                    sheeps[i].setInvulnerable(true);
                }
                for (int i = 0; i < sheeps.length; i++) {
                    sheeps[i].setVelocity(new Vector(speed * Math.cos(angle * i), speed * 2, speed * Math.sin(angle * i)));
                }
                break;
            } case 6: {
                int speed = 1;
                Vector velocity = new Vector(0, speed, 0);
                for (int i = 0; i < 10; i++) {
                    Bukkit.getScheduler().runTaskLater(getPlugin(), () -> createSheep(player, 100, velocity), 20 * i);
                }
                break;
            } case 7: {
                int speed = 1;
                for (int i = 0; i < 20; i++) {
                    Bukkit.getScheduler().runTaskLater(getPlugin(), () -> { Vector velocity = player.getEyeLocation().getDirection().normalize().multiply(speed);
                        createSheep(player.getEyeLocation(), 80, velocity);}, 2 * i);
                }
                break;
            } case 8: {
                double speed = 0.25;
                for (int i = 0; i < 20; i++) {
                    Bukkit.getScheduler().runTaskLater(getPlugin(), () -> { Vector velocity = player.getEyeLocation().getDirection().normalize().multiply(speed);
                        createSheep(player.getEyeLocation(), 20, velocity);
                        recoil(player, velocity);}, 2 * i);
                }
                break;
            } case 9: {
                double speed = 0.25;
                Vector velocity = new Vector(0, -speed, 0);
                for (int i = 0; i < 20; i++) {
                    Bukkit.getScheduler().runTaskLater(getPlugin(), () -> { createSheep(player, 20, velocity);
                        recoil(player, velocity);}, 2 * i);
                }
                break;
            } case 10: {
                // :(
                for(Sheep sheep : getWorld().getEntitiesByClass(Sheep.class)) {
                    sheep.remove();
                }
                break;
            } case 11: {
                for(Sheep sheep : getWorld().getEntitiesByClass(Sheep.class)) {
                    sheep.setCustomName("jeb_");
                    sheep.setCustomNameVisible(false);
                }
                break;
            } case 12: {
                for (int i = 0; i < 20; i++) {
                    Bukkit.getScheduler().runTaskLater(getPlugin(), () -> player.playSound(player.getEyeLocation(), Sound.ENTITY_SHEEP_AMBIENT, 100, 1), 2 * i);
                }
                break;
            } case 13: {
                Player[] targets = Bukkit.getOnlinePlayers().toArray(new Player[0]);
                Player target = targets[(int)Math.floor(targets.length * Math.random())];
                createSheep(target);
                target.sendMessage("You got sheeped by " + player.getDisplayName());
            } case 14: {
                createSheep(getTop(player.getTargetBlock(null, 1000)));
                break;
            } case 15: {
                Location location = getTop(player.getTargetBlock(null, 1000));
                for (int i = 0; i < 20; i++) {
                    Bukkit.getScheduler().runTaskLater(getPlugin(), () -> createSheep(location, 20, new Vector(0, 1, 0)), 2 * i);
                }
                break;
            } case 16: {
                // Does absolutely nothing
            }
        }
        return true;
    }
    private static Location getTop(Block block){
        int offset = 1;
        while(block.getRelative(BlockFace.UP, offset).getType() != Material.AIR) offset++;
        return new Location(getWorld(), block.getX() + 0.5, block.getY() + offset, block.getZ() + 0.5);
    }
    private static Sheep getSheep(Entity entity){
        Collection<Sheep> sheeps = getWorld().getEntitiesByClass(Sheep.class);
        return sheeps.toArray(new Sheep[0])[(int)Math.floor(sheeps.size() * Math.random())];
    }

    private static Sheep createSheep(Entity entity){
        Sheep sheep = (Sheep) getWorld().spawnEntity(entity.getLocation(), EntityType.SHEEP);
        sheep.setInvulnerable(true);
        return sheep;
    }
    private static Sheep createSheep(Entity entity, int lifespan){
        Sheep sheep = createSheep(entity);
        if(lifespan > 0) Bukkit.getScheduler().runTaskLater(getPlugin(), sheep::remove, lifespan);
        return sheep;
    }
    private static Sheep createSheep(Entity entity, Vector vector){
        Sheep sheep = createSheep(entity);
        sheep.setVelocity(vector);
        return sheep;
    }
    private static Sheep createSheep(Entity entity, int lifespan, Vector vector){
        Sheep sheep = createSheep(entity);
        if(lifespan > 0) Bukkit.getScheduler().runTaskLater(getPlugin(), sheep::remove, lifespan);
        sheep.setVelocity(vector);
        return sheep;
    }
    private static Sheep createSheep(Location location){
        Sheep sheep = (Sheep) getWorld().spawnEntity(location, EntityType.SHEEP);
        sheep.setInvulnerable(true);
        return sheep;
    }
    private static Sheep createSheep(Location location, int lifespan){
        Sheep sheep = createSheep(location);
        if(lifespan > 0) Bukkit.getScheduler().runTaskLater(getPlugin(), sheep::remove, lifespan);
        return sheep;
    }
    private static Sheep createSheep(Location location, Vector vector){
        Sheep sheep = createSheep(location);
        sheep.setVelocity(vector);
        return sheep;
    }
    private static Sheep createSheep(Location location, int lifespan, Vector vector){
        Sheep sheep = createSheep(location);
        if(lifespan > 0) Bukkit.getScheduler().runTaskLater(getPlugin(), sheep::remove, lifespan);
        sheep.setVelocity(vector);
        return sheep;
    }
    private static void recoil(Entity entity, Vector vector){
        Vector velocity = entity.getVelocity();
        entity.setVelocity(new Vector(velocity.getX() - vector.getX(), velocity.getY() - vector.getY(), velocity.getZ() - vector.getZ()));
        entity.setFallDistance(-100);
    }
}
