package net.pullolo.magicarena.game;

import net.pullolo.magicarena.MagicArena;
import net.pullolo.magicarena.players.ArenaPlayer;
import net.pullolo.magicarena.worlds.WorldManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

import static net.pullolo.magicarena.MagicArena.config;
import static net.pullolo.magicarena.MagicArena.mainWorld;

public class Dungeon extends Game{

    public enum Difficulty{
        NORMAL,
        HARD,
        ULTRA
    }

    public Dungeon(ArrayList<Player> allPlayers, int level, Difficulty difficulty, boolean test){
        final double spawnX = 0.5, spawnZ = 0.5;
        World dungeon = createWorld();
        setWorld(dungeon);
        setAllPlayers(allPlayers);
        setTeam1(allPlayers);
        setTeam2(null);
        for (Player p : allPlayers){
            new ArenaPlayer(p, 1, this);
            p.setGameMode(GameMode.SURVIVAL);
            p.teleport(new Location(dungeon, spawnX, config.getDouble("arenas-spawn-y"), spawnZ));
            if (test){
                p.sendMessage(ChatColor.YELLOW + "[Warning] Experimental=True");
            }
            p.sendMessage(ChatColor.GREEN + "Entered Game!");
            p.sendMessage(ChatColor.GREEN + "Match Starting in 10s...");
        }
        BukkitRunnable startClock = new BukkitRunnable() {
            int i = 10*20;

            @Override
            public void run() {
                i--;
                for (Player p : allPlayers){
                    if (p!=null && (Math.abs(p.getLocation().getZ())>spawnZ+2 || Math.abs(p.getLocation().getX())>spawnX+2)){
                        Location loc = new Location(dungeon, spawnX, config.getDouble("arenas-spawn-y"), spawnZ);
                        loc.setDirection(loc.getDirection().multiply(-1));
                        p.teleport(loc);
                    }
                }
                if (i<5*20 && i%20==0){
                    for (Player p : allPlayers){
                        if (p!=null){
                            p.sendMessage(ChatColor.GREEN + "Starting in " + i/20);
                        }
                    }
                }
                if (i<1){
                    for (Player p : allPlayers){
                        if (p!=null){
                            p.sendMessage(ChatColor.GREEN + "Game started!");
                        }
                    }
                    this.cancel();
                }
            }
        };
        startClock.runTaskTimer(MagicArena.plugin, 0, 1);
        setStartC(startClock);
        startNecessaryClocks(test, dungeon);
    }

    @Override
    public void finishDungeon(ArrayList<Player> allPlayers, World world, boolean won) {
        for (Player p : allPlayers){
            if (p!=null){
                p.sendMessage(ChatColor.GREEN + "You will be warped out in 5 seconds!");
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : allPlayers){
                    if (p!=null){
                        p.sendMessage(ChatColor.GREEN + "You have been warped!");
                        p.teleport(Bukkit.getWorld(mainWorld).getSpawnLocation());
                        if (p.isOp()) p.setGameMode(GameMode.CREATIVE);
                        else p.setGameMode(GameMode.SURVIVAL);
                        p.setHealth(p.getMaxHealth());
                    }
                }
                WorldManager.removeWorld(world);
            }
        }.runTaskLater(MagicArena.plugin, 100);
    }

    @Override
    public String pickRandomArena() {
        return WorldManager.getDungeons().get(new Random().nextInt(WorldManager.getDungeons().size()));
    }
}
