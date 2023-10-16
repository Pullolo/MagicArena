package net.pullolo.magicarena.game;

import net.pullolo.magicarena.MagicArena;
import net.pullolo.magicarena.players.ArenaPlayer;
import net.pullolo.magicarena.worlds.WorldManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

import static net.pullolo.magicarena.MagicArena.*;

public class Dungeon extends Game{

    public enum Difficulty{
        NORMAL,
        HARD,
        ULTRA
    }

    private final ArrayList<BlockState> foundSecrets = new ArrayList<>();
    private int score = 0;

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
            p.teleport(new Location(dungeon, spawnX, config.getDouble("arenas-spawn-y"), spawnZ).setDirection(new Location(dungeon, spawnX, config.getDouble("arenas-spawn-y"), spawnZ).getDirection().multiply(-1)));
            if (test){
                p.sendMessage(ChatColor.YELLOW + "[Warning] Experimental=True");
            }
            //todo temp
            p.sendMessage(ChatColor.YELLOW + "[Warning] InDev=True, Some features may be incomplete!");
            //todo end temp
            p.sendMessage(ChatColor.GREEN + "Entered Game!");
            p.sendMessage(ChatColor.GREEN + "Match Starting in 10s...");
        }
        BukkitRunnable startClock = new BukkitRunnable() {
            int i = 10*20;

            @Override
            public void run() {
                i--;
                for (Player p : allPlayers){
                    if (p!=null && (Math.abs(p.getLocation().getZ())>spawnZ+5 || Math.abs(p.getLocation().getX())>spawnX+5)){
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
                    setStarted(true);
                    for (Location loc : getAllNearBlocks(new Location(dungeon, 0, 62, -6))){
                        loc.getBlock().setType(Material.AIR);
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
    public void update1s(){

    }

    @Override
    public void update1t(){

    }

    public void findSecret(BlockState b, Player p){
        foundSecrets.add(b);
        broadcast(ChatColor.DARK_RED + "Player " + ChatColor.RED + p.getDisplayName() + ChatColor.DARK_RED + " has found a Secret!" + ChatColor.GOLD + " +100 Score");
        score+=100;
    }

    public boolean isSecretFound(BlockState b) {
        return foundSecrets.contains(b);
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

    public ArrayList<Location> getAllNearBlocks(Location loc){
        ArrayList<Location> blocks = new ArrayList<>();
        blocks.add(loc);
        Material m = loc.getBlock().getType();
        if (m.equals(Material.AIR)) return new ArrayList<>();

        return getAllBlocks(loc, blocks, m);
    }

    private ArrayList<Location> getAllBlocks(Location loc, ArrayList<Location> b, Material m){
        ArrayList<Location> blocks = new ArrayList<>(b);
        if (loc.clone().add(1, 0, 0).getBlock().getType().equals(m)){
            if (!doesArrayContainLoc(loc.clone().add(1, 0, 0), blocks)){
                blocks.add(loc.clone().add(1, 0, 0));
                addAll(loc.clone().add(1, 0, 0), blocks, m);
            }
        }
        if (loc.clone().add(-1, 0, 0).getBlock().getType().equals(m)){
            if (!doesArrayContainLoc(loc.clone().add(-1, 0, 0), blocks)){
                blocks.add(loc.clone().add(-1, 0, 0));
                addAll(loc.clone().add(-1, 0, 0), blocks, m);
            }
        }
        if (loc.clone().add(0, 1, 0).getBlock().getType().equals(m)){
            if (!doesArrayContainLoc(loc.clone().add(0, 1, 0), blocks)){
                blocks.add(loc.clone().add(0, 1, 0));
                addAll(loc.clone().add(0, 1, 0), blocks, m);
            }
        }
        if (loc.clone().add(0, -1, 0).getBlock().getType().equals(m)){
            if (!doesArrayContainLoc(loc.clone().add(0, -1, 0), blocks)){
                blocks.add(loc.clone().add(0, -1, 0));
                addAll(loc.clone().add(0, -1, 0), blocks, m);
            }
        }
        if (loc.clone().add(0, 0, 1).getBlock().getType().equals(m)){
            if (!doesArrayContainLoc(loc.clone().add(0, 0, 1), blocks)){
                blocks.add(loc.clone().add(0, 0, 1));
                addAll(loc.clone().add(0, 0, 1), blocks, m);
            }
        }
        if (loc.clone().add(0, 0, -1).getBlock().getType().equals(m)){
            if (!doesArrayContainLoc(loc.clone().add(0, 0, -1), blocks)){
                blocks.add(loc.clone().add(0, 0, -1));
                addAll(loc.clone().add(0, 0, -1), blocks, m);
            }
        }
        return blocks;
    }
    private void addAll(Location loc, ArrayList<Location> blocks, Material m){
        for (Location l : getAllBlocks(loc, blocks, m)){
            if (!blocks.contains(l)){
                blocks.add(l);
            }
        }
    }
    private boolean doesArrayContainLoc(Location loc, ArrayList<Location> locs){
        if (locs.size()>125) return true;
        for (Location l : locs){
            if (l.getX() == loc.getX() && l.getY() == loc.getY() && l.getZ() == loc.getZ()){
                return true;
            }
        }
        return false;
    }

    public int getScore() {
        return score;
    }
}
