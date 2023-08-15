package me.priceconnelly.pctweaks.models;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static me.priceconnelly.pctweaks.PCTweaks.getPlugin;

public class PlayerData {
    private static HashMap<String, PlayerData> playerDatas;

    static {
        try {
            HashMap<String, PlayerData> result = loadData();
            System.out.println(result);
            if(result != null) playerDatas = result;
            else playerDatas = new HashMap<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String nickname = null;
    private List<String> ranks = new ArrayList<>();
    private int deaths = 0;
    private boolean peaceful = false;
    private boolean pvp = false;
    private List<UUID> trustedPlayers = new ArrayList<>();

    private static HashMap<String, PlayerData> initData(){
        return new HashMap<>();
    }
    public static PlayerData getPlayerData(Player player){
        String uuid = player.getUniqueId().toString();
        if(!playerDatas.containsKey(uuid)){
            playerDatas.put(uuid, new PlayerData());
        }
        return playerDatas.get(uuid);
    }
    public static void saveData() throws IOException {
        System.out.println("Saving Player Data");
        Gson gson = new Gson();
        File file = new File(getPlugin().getDataFolder().getAbsoluteFile() + "/playerData.json");
        file.getParentFile().mkdir();
        file.createNewFile();
        Writer writer = new FileWriter(file);
        gson.toJson(playerDatas, writer);
        writer.flush();
        writer.close();
        System.out.println("Saved Player Data");
    }
    public static HashMap<String, PlayerData> loadData() throws IOException {
        Gson gson = new Gson();
        File file = new File(getPlugin().getDataFolder().getAbsoluteFile() + "/playerData.json");
        if(!file.exists()) return new HashMap<>();
        Reader reader = new FileReader(file);
        return gson.fromJson(reader, new TypeToken<HashMap<String, PlayerData>>(){}.getType());
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname() {
        return nickname;
    }
    public void clearNickname(){
        nickname = null;
    }
    public String[] getRanks() {
        return ranks.toArray(new String[0]);
    }
    public void addRank(String rank){
        if(ranks.contains(rank)) return;
        ranks.add(rank);
    }
    public void removeRank(String rank){
        if(!ranks.contains(rank)) return;
        ranks.remove(rank);
    }
    public void clearRank(){
        ranks.clear();
    }
    public int getDeaths(){
        return deaths;
    }
    public void addDeath(){
        deaths++;
    }
    public boolean isPvp() {
        return pvp;
    }
    public void setPvp(boolean pvp){
        this.pvp = pvp;
    }
    public boolean isPeaceful() {
        return peaceful;
    }
    public void setPeaceful(boolean peaceful) {
        this.peaceful = peaceful;
    }
    public UUID[] getTrustedPlayers(){
        return trustedPlayers.toArray(new UUID[0]);
    }
    public boolean addTrustedPlayer(Player player){
        if(trustedPlayers.contains(player.getUniqueId())) return false;
        trustedPlayers.add(player.getUniqueId());
        return true;
    }
    public boolean removeTrustedPlayer(Player player){
        if(!trustedPlayers.contains(player.getUniqueId())) return false;
        trustedPlayers.add(player.getUniqueId());
        return true;
    }
    public void clearTrustedPlayers(){
        trustedPlayers.clear();
    }
    public boolean isTrusted(Player player){
        return trustedPlayers.contains(player.getUniqueId());
    }
}
