package net.pullolo.magicarena.game;

import net.pullolo.magicarena.players.DungeonEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.ArrayList;
import java.util.HashMap;

import static net.pullolo.magicarena.MagicArena.getLog;
import static net.pullolo.magicarena.data.PlayerData.getPlayerData;
import static net.pullolo.magicarena.players.ArenaEntity.arenaEntities;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;

public class GameWorld extends Game{

    public static final HashMap<World, GameWorld> gameWorlds = new HashMap<>();
    private long worldLevel = 1;

    public GameWorld(World w) {
        setWorld(w);
        gameWorlds.put(w, this);
        createEntities(w);
        setAllPlayers((ArrayList<Player>) w.getPlayers());
        setTeam1(null);
        setTeam2(null);
        for (Player p : getAllPlayers()){
            p.sendMessage(ChatColor.GREEN + "You are now being processed by " + w.getName() + ".");
        }
        startNecessaryClocks(false, w);
        setStarted(true);
    }

    private void createEntities(World w) {
        for (Entity e : w.getEntities()){
            if ((e instanceof LivingEntity) && !(e instanceof Player)){
                if (!arenaEntities.containsKey(e)){
                    arenaEntities.put(e, new DungeonEntity(e, (int) worldLevel, this, false));
                }
            }
        }
    }

    @Override
    public void update1s(){
        int i = 0;
        long lvl = 0;
        for (Player p : getAllPlayers()){
            i++;
            lvl += getPlayerData(p).getLevel();
        }
        if (i!=0) worldLevel = Math.round((double) lvl/i);
    }

    @Override
    public void update1t(){

    }

    @Override
    public void saveGameWorld(){
        getLog().info("Saving world " + getWorld().getName() + "...");
        games.remove(getWorld());
        for (Entity e : arenaEntities.keySet()){
            arenaEntities.get(e).save();
        }
        arenaEntities.clear();
        arenaPlayers.clear();
        setAllPlayers(new ArrayList<>());
        gameWorlds.remove(getWorld());
        getLog().info("Saved.");
    }

    public void broadcastSound(Sound sound, float vol, float pitch){
        for (Player p : getAllPlayers()){
            if (p!=null){
                p.playSound(p.getLocation(), sound, vol, pitch);
            }
        }
    }

    @Override
    public String pickRandomArena() {
        return null;
    }

    public int getWorldLevel() {
        return (int) worldLevel;
    }
}
