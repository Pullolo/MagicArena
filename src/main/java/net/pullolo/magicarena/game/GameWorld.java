package net.pullolo.magicarena.game;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.ArrayList;
import java.util.HashMap;

import static net.pullolo.magicarena.MagicArena.getLog;
import static net.pullolo.magicarena.players.ArenaEntity.arenaEntities;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;

public class GameWorld extends Game{

    public static final HashMap<World, GameWorld> gameWorlds = new HashMap<>();

    public GameWorld(World w) {
        setWorld(w);
        gameWorlds.put(w, this);
        setAllPlayers((ArrayList<Player>) w.getPlayers());
        setTeam1(null);
        setTeam2(null);
        for (Player p : getAllPlayers()){
            p.sendMessage(ChatColor.GREEN + "You are now being processed by " + w.getName() + ".");
        }
        startNecessaryClocks(false, w);
        setStarted(true);
    }

    @Override
    public void update1s(){

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
}
