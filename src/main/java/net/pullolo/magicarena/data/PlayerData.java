package net.pullolo.magicarena.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static net.pullolo.magicarena.MagicArena.getLog;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;
import static net.pullolo.magicarena.players.ArenaPlayer.isPlayerInGame;

public class PlayerData {

    private static final HashMap<Player, PlayerData> playerData = new HashMap<>();
    private final String name;
    private int level, star_essence, wishes, dungeon_essence;
    private double xp;
    private boolean updated;

    public PlayerData(String name, int level, double xp, int star_essence, int wishes, int dungeon_essence, boolean updated) {
        this.name = name;
        this.level = level;
        this.xp = xp;
        this.star_essence = star_essence;
        this.wishes = wishes;
        this.dungeon_essence = dungeon_essence;
        this.updated = updated;
    }

    public static PlayerData getPlayerData(Player p){
        return playerData.get(p);
    }

    public static void setPlayerDataFromDb(Player p, DbManager db){
        String playerName = p.getName();
        playerData.put(p, db.getPlayerData(playerName));
    }

    public static void savePlayerDataToDb(Player p, DbManager db){
        String playerName = p.getName();
        db.updatePlayer(playerName, playerData.get(p).getLevel(), playerData.get(p).getXp(), playerData.get(p).getStarEssence(), playerData.get(p).getWishes(), playerData.get(p).getDungeonEssence(), playerData.get(p).isUpdated());
    }

    public static void removePlayerData(Player p){
        playerData.remove(p);
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    public int getStarEssence() {
        return star_essence;
    }

    public int getWishes() {
        return wishes;
    }

    public int getDungeonEssence() {
        return dungeon_essence;
    }

    public void setLevel(int level) {
        this.level = level;
        try {
            Player p = Bukkit.getPlayer(name);
            if (isPlayerInGame(p)){
                arenaPlayers.get(p).setLevel(this.level);
            }
        } catch (Exception e){
            getLog().warning("Couldn't set Player level!");
        }
    }

    public void setStarEssence(int star_essence) {
        this.star_essence = star_essence;
    }

    public void setWishes(int wishes) {
        this.wishes = wishes;
    }

    public void setDungeonEssence(int dungeon_essence) {
        this.dungeon_essence = dungeon_essence;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}
