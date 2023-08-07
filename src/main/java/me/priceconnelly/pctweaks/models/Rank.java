package me.priceconnelly.pctweaks.models;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

import static me.priceconnelly.pctweaks.PCTweaks.getPlugin;

public abstract class Rank {
    private static HashMap<String, rank> ranks = initializeRanks();
    private static String defaultNoOp = initializeDefNoOp();
    private static String defaultOp = initializeDefOp();
    public static void update(Player player){
        StringJoiner sj = new StringJoiner(" ");
        PlayerData playerData = PlayerData.getPlayerData(player);
        if(player.isOp()) sj.add(defaultOp);
        else sj.add(defaultNoOp);
        for(String rank : playerData.getRanks()) {
            if(!ranks.containsKey(rank)) continue;
            sj.add(ranks.get(rank).name);
        }
        if(playerData.isPvp()) sj.add(ChatColor.AQUA + "[pvp]" + ChatColor.RESET);
        if(playerData.isPeaceful()) sj.add(ChatColor.GREEN + "[peaceful]" + ChatColor.RESET);
        if(playerData.getNickname() != null) sj.add(playerData.getNickname());
        else sj.add(player.getName());

        String name = sj.toString();
        player.setDisplayName(name);
        player.setPlayerListName(name);
    }
    public static void giveRank(Player player, String rank){
        if(!ranks.containsKey(rank)) return;
        if(ranks.get(rank).restricted && !player.isOp()) return;
        PlayerData.getPlayerData(player).addRank(rank);
        update(player);
    }
    public static void giveRank(Player player, String[] rs){
        for(String posRank : rs){
            posRank = posRank.toLowerCase();
            if(!ranks.containsKey(posRank)) continue;
            if(ranks.get(posRank).restricted && !player.isOp()) continue;
            PlayerData.getPlayerData(player).addRank(posRank);
        }
        update(player);
    }
    public static void removeRank(Player player, String rank){
        if(!ranks.containsKey(rank)) return;
        PlayerData.getPlayerData(player).removeRank(rank);
        update(player);
    }
    public static void removeRank(Player player, String[] rs){
        for(String posRank : rs){
            posRank = posRank.toLowerCase();
            if(!ranks.containsKey(posRank)) continue;
            PlayerData.getPlayerData(player).removeRank(posRank);
        }
        update(player);
    }
    public static String[] getRanks(){
        List<String> rankNames = new LinkedList<String>();
        for(rank r : ranks.values()) rankNames.add(r.name);
        return rankNames.toArray(new String[0]);
    }
    public static String[] getRanks(Player player){
        List<String> rankNames = new LinkedList<String>();
        for(rank r : ranks.values()){
            if(!player.isOp() && (r.restricted || r.hidden)) continue;
            if(r.restricted && !player.isOp()) continue;
            rankNames.add(r.name);
        }
        return rankNames.toArray(new String[0]);
    }
    public static void clearRank(Player player){
        PlayerData.getPlayerData(player).clearRank();
        update(player);
    }
    private static String configToRank(String string){
        String[] args = string.split(",");
        StringBuilder out = new StringBuilder();
        try{
            out.append(ChatColor.valueOf(args[1].toUpperCase().replaceAll("\\s", "")));
        } catch (Exception e){
            if(args.length > 1){
                System.out.println("Invalid color: " + args[1]);
            }
            else{
                System.out.println("Missing color argument");
            }
        }
        out.append("[");
        out.append(args[0]);
        out.append("]");
        out.append(ChatColor.RESET);
        return out.toString();
    }
    private static HashMap<String, rank> initializeRanks() {
        getPlugin().reloadConfig();
        List<String> input = getPlugin().getConfig().getStringList("ranks");
        HashMap<String, rank> output = new HashMap<>();
        for(String string : input){
            String[] args = string.split(",");
            boolean restricted = false;
            try{
                restricted = Boolean.parseBoolean(args[2].toLowerCase().replaceAll("\\s", ""));
            } catch (Exception e){
                if(args.length > 2){
                    System.out.println("Invalid argument: " + args[2]);
                }
            }
            boolean hidden = false;
            try{
                hidden = Boolean.parseBoolean(args[3].toLowerCase().replaceAll("\\s", ""));
            } catch (Exception e){
                if(args.length > 3){
                    System.out.println("Invalid argument: " + args[3]);
                }
            }
            output.put(args[0].toLowerCase(), new rank(configToRank(string), restricted, hidden));
        }
        return output;
    }
    public static String initializeDefNoOp(){
        return configToRank(getPlugin().getConfig().getString("default_rank_no_op"));
    }
    public static String initializeDefOp(){
        return configToRank(getPlugin().getConfig().getString("default_rank_op"));
    }
    public static void reInit(){
        ranks = initializeRanks();
        defaultNoOp = initializeDefNoOp();
        defaultOp = initializeDefOp();
    }

    private static class rank{
        public final String name;
        public final boolean restricted;
        public final boolean hidden;
        public rank(String name, boolean restricted, boolean hidden){
            this.name = name;
            this.restricted = restricted;
            this.hidden = hidden;
        }
    }
}
