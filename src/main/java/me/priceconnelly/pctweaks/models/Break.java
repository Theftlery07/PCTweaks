package me.priceconnelly.pctweaks.models;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static me.priceconnelly.pctweaks.PCTweaks.getPlugin;

public abstract class Break {
    private static HashMap<String, brk> breaks = initializeBreaks();
    private static brk curBreak;
    private static UUID curOwner;
    private static brk onBreak;
    private static List<Integer> taskIds = new LinkedList<Integer>();
    public static boolean isRunning(){
        return curBreak != null;
    }
    public static boolean isOpen(){
        return onBreak == null;
    }
    public static boolean isValid(String name){
        return breaks.containsKey(name.toLowerCase());
    }
    public static boolean isOwner(Player player){
        return player.getUniqueId().equals(curOwner);
    }
    public static String[] getBreaks(){
        List<String> breakDesc = new LinkedList<>();
        for(brk b : breaks.values()) breakDesc.add(b.name + " | " + b.time + " min");
        return breakDesc.toArray(new String[0]);
    }
    public static void start(String name, Player player){
        if(!isValid(name)) throw new IllegalArgumentException("Haha nice try bozo");
        if(isRunning()) throw new IllegalStateException("Really dog?");
        curBreak = breaks.get(name.toLowerCase());
        curOwner = player.getUniqueId();
        getPlugin().getServer().broadcastMessage(ChatColor.RED + "Warning Break Approaching!!!");
        for (int i = (int)Math.floor(curBreak.time); i > 0; i--) {
            String message = ChatColor.AQUA + curBreak.name + " will occur in " + i + " minute" + (i != 1 ? "s" : "");
            taskIds.add(Bukkit.getScheduler().runTaskLater(getPlugin(), () -> getPlugin().getServer().broadcastMessage(message), (long)((curBreak.time - i) * 20 * 60)).getTaskId());
        }
        if(curBreak.time >= 0.5){
            String message = ChatColor.AQUA + curBreak.name + " will occur in 30 seconds";
            taskIds.add(Bukkit.getScheduler().runTaskLater(getPlugin(), () -> getPlugin().getServer().broadcastMessage(message), (long)(((curBreak.time * 60) - 30) * 20)).getTaskId());
        }
        for (int i = 5; i > 0; i--) {
            String message = ChatColor.AQUA + curBreak.name + " will occur in " + i + " second" + (i != 1 ? "s" : "");
            taskIds.add(Bukkit.getScheduler().runTaskLater(getPlugin(), () -> getPlugin().getServer().broadcastMessage(message), (long)(((curBreak.time * 60) - i) * 20)).getTaskId());
        }
        taskIds.add(Bukkit.getScheduler().runTaskLater(getPlugin(), () -> complete(), (long)(curBreak.time * 20 * 60)).getTaskId());

    }
    public static void stop(Player player){
        if(!isRunning()) throw new IllegalStateException("Use your eyes, nothing is running!!!");
        if(!isOwner(player)) throw new IllegalArgumentException("Imagine");
        for(int id : taskIds) Bukkit.getScheduler().cancelTask(id);
        curBreak = null;
        curOwner = null;
    }
    private static void complete(){
        getPlugin().getServer().broadcastMessage("The World Ended");
        for(Player player : Bukkit.getOnlinePlayers()) if(!player.isOp()) player.kickPlayer(ChatColor.AQUA + curBreak.name);
        onBreak = curBreak;
        curBreak = null;
        curOwner = null;
    }

    public static void reOpen(){
        if(isOpen()) throw new IllegalStateException("Clown alert");
        onBreak = null;
    }

    private static HashMap<String, brk> initializeBreaks(){
        getPlugin().reloadConfig();
        List<String> input = getPlugin().getConfig().getStringList("breaks");
        HashMap<String, brk> output = new HashMap<>();
        for(String string : input){
            String[] args = string.split(",");
            double time = 5;
            try{
                time = Double.parseDouble(args[1]);
            } catch (Exception e){
                if(args.length > 1){
                    System.out.println("Invalid argument: " + args[1]);
                }
            }
            output.put(args[0].toLowerCase(), new brk(args[0], time));
        }
        return output;
    }

    public static void reInit(){
        breaks = initializeBreaks();
    }

    private static class brk{
        public final double time;
        public final String name;
        public brk(String name, double time){
            this.time = time;
            this.name = name;
        }
    }

}
