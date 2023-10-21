package net.pullolo.magicarena.game;

import net.pullolo.magicarena.MagicArena;
import net.pullolo.magicarena.data.PlayerData;
import net.pullolo.magicarena.guis.GuiManager;
import net.pullolo.magicarena.players.ArenaEntity;
import net.pullolo.magicarena.players.ArenaPlayer;
import net.pullolo.magicarena.players.DungeonEntity;
import net.pullolo.magicarena.worlds.WorldManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

import static net.pullolo.magicarena.MagicArena.*;
import static net.pullolo.magicarena.data.PlayerData.getPlayerData;
import static net.pullolo.magicarena.players.ArenaEntity.arenaEntities;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;

public class Dungeon extends Game{


    public enum Difficulty{
        NORMAL,
        HARD,
        ULTRA
    }

    private final ArrayList<BlockState> foundSecrets = new ArrayList<>();
    private int score = 0;
    private int level;
    private int witherKeys = 0;
    private boolean bossKey = false;

    public Dungeon(ArrayList<Player> allPlayers, int level, Difficulty difficulty, boolean test){
        final double spawnX = 0.5, spawnZ = 0.5;
        World dungeon = createWorld();
        setWorld(dungeon);
        setAllPlayers(allPlayers);
        setTeam1(allPlayers);
        setTeam2(null);
        switch (difficulty){
            case HARD:
                level= (int) (level*1.5);
                break;
            case ULTRA:
                level=level*2;
                break;
        }
        int finalLevel = level;
        this.level = finalLevel;
        for (Player p : allPlayers){
            ArenaPlayer ap = new ArenaPlayer(p, getPlayerData(p).getLevel(), this);
            updatePlayerItemStats(p);
            ap.updateStats();
            ap.respawn();
            p.setGameMode(GameMode.SURVIVAL);
            p.teleport(new Location(dungeon, spawnX, config.getDouble("arenas-spawn-y"), spawnZ).setDirection(new Location(dungeon, spawnX, config.getDouble("arenas-spawn-y"), spawnZ).getDirection().multiply(-1)));
            if (test){
                p.sendMessage(ChatColor.YELLOW + "[Warning] Experimental=True");
            }
            //todo temp
            p.sendMessage(ChatColor.YELLOW + "[Warning] InDev=True, Some features may be incomplete!");
            //todo end temp
            p.sendMessage(ChatColor.GREEN + "Entered " + ChatColor.RED + difficulty.toString() + ChatColor.GREEN + " dungeon!");
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
                    convertArmorStandsToMobs(2, finalLevel);
                    for (Player p : allPlayers){
                        if (p!=null){
                            p.sendMessage(ChatColor.GREEN + "Game started!");
                            if (arenaPlayers.containsKey(p)) arenaPlayers.get(p).respawn();
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

    public void broadcastSound(Sound sound, float vol, float pitch){
        for (Player p : getAllPlayers()){
            if (p!=null){
                p.playSound(p.getLocation(), sound, vol, pitch);
            }
        }
    }

    public void convertArmorStandsToMobs(int blockOffset, int level){
        Random r = new Random();
        double x;
        double z;
        World w = getWorld();
        for (Entity en : w.getEntities()){
            if (!(en instanceof ArmorStand)){
                continue;
            }
            ArmorStand as = (ArmorStand) en;
            try {
                String cmd = as.getCustomName();
                if (cmd.regionMatches(0, "s: ", 0, 3)){
                    if (cmd.contains(",")){
                        String[] cmds = cmd.split(",");
                        int j = 0;
                        for (String s : cmds){
                            if (!s.regionMatches(0, "s: ", 0, 3)){
                                continue;
                            }
                            String[] spawnArgs = s.split(": ")[1].split(" ");
                            String mobType = spawnArgs[0];
                            int amount = Integer.parseInt(spawnArgs[1]);
                            boolean canHaveKey = true;
                            if (spawnArgs.length>2 && spawnArgs[2].equalsIgnoreCase("nokey")){
                                canHaveKey=false;
                            }
                            int mobWithKey=-1;
                            if (canHaveKey) mobWithKey=r.nextInt(amount);

                            for (int i = 0; i<amount; i++){
                                x = r.nextDouble()*blockOffset;
                                z = r.nextDouble()*blockOffset;
                                Location newLoc = as.getLocation().add(x,0,z);
                                as.remove();
                                if (!newLoc.getBlock().isPassable()){
                                    newLoc.add(0, 1, 0);
                                    if (newLoc.getBlock().getType().equals(Material.BEDROCK)){
                                        if (mobWithKey==i && i<amount-1){
                                            mobWithKey++;
                                            continue;
                                        }
                                    }
                                }
                                //temp todo same here
                                Entity e = w.spawnEntity(newLoc, EntityType.valueOf(mobType.toUpperCase()));
                                arenaEntities.put(e, new DungeonEntity(e, level, this, false));
                                if (i == mobWithKey) ((DungeonEntity) arenaEntities.get(e)).setWitherKey(true);
                            }
                            j++;
                        }
                    } else {
                        //String example = "s: zombie 10,s: skeleton 2";
                        String[] spawnArgs = cmd.split(": ")[1].split(" ");
                        String mobType = spawnArgs[0];
                        int amount = Integer.parseInt(spawnArgs[1]);
                        boolean canHaveKey = true;
                        if (spawnArgs.length>2 && spawnArgs[2].equalsIgnoreCase("nokey")){
                            canHaveKey=false;
                        }
                        int mobWithKey = -1;
                        if (canHaveKey) mobWithKey = r.nextInt(amount);
                        for (int i = 0; i<amount; i++){
                            x = r.nextDouble()*blockOffset;
                            z = r.nextDouble()*blockOffset;
                            Location newLoc = as.getLocation().add(x,0,z);
                            as.remove();
                            if (!newLoc.getBlock().isPassable()){
                                newLoc.add(0, 1, 0);
                                if (newLoc.getBlock().getType().equals(Material.BEDROCK)){
                                    if (mobWithKey==i && i<amount-1){
                                        mobWithKey++;
                                        continue;
                                    }
                                }
                            }
                            //temp todo make a method to spawn mobs to check their type and if they are custom, spawn custom
                            Entity e = w.spawnEntity(newLoc, EntityType.valueOf(mobType.toUpperCase()));
                            arenaEntities.put(e, new DungeonEntity(e, level, this, false));
                            if (i == mobWithKey) ((DungeonEntity) arenaEntities.get(e)).setWitherKey(true);
                        }
                    }
                }
            } catch (Exception e){
                continue;
            }
        }
        //create boss key
        if (getAllEntities().size()>0){
            Entity e = getAllEntities().get(getAllEntities().size()-1);
            if (arenaEntities.get(e) instanceof DungeonEntity){
                ((DungeonEntity) arenaEntities.get(e)).setBossKey(true);
            }
        }
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
        if (won){
            broadcast(ChatColor.DARK_RED + "[Dungeon] " + ChatColor.GREEN + "You have Won!" + ChatColor.GOLD + " " + score + " Score");
        } else broadcast(ChatColor.DARK_RED + "[Dungeon] " + ChatColor.RED + "You have Lost!" + ChatColor.GOLD + " " + score + " Score");
        for (Player p : allPlayers){
            if (p.getGameMode().equals(GameMode.SPECTATOR)){
                p.teleport(new Location(getWorld(), 0.5, config.getDouble("arenas-spawn-y"), 0.5).setDirection(new Location(getWorld(), 0.5, config.getDouble("arenas-spawn-y"), 0.5).getDirection().multiply(-1)));
                p.setGameMode(GameMode.SURVIVAL);
            }
        }
        for (Player p : allPlayers){
            guiManager.createDungeonRewardMenu(p, score, level).show(p);
        }
        //warn players
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : allPlayers){
                    if (p!=null){
                        p.sendMessage(ChatColor.GREEN + "You will be warped in 5 seconds!");
                    }
                }
            }
        }.runTaskLater(plugin, 200);
        super.finishDungeon(allPlayers, world, won);
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
    public void findWitherKey(Player p){
        witherKeys++;
        broadcast(ChatColor.DARK_RED + "Player " + ChatColor.RED + p.getDisplayName() + ChatColor.DARK_RED + " has found a" + ChatColor.DARK_GRAY + " Wither Key!");
        broadcastSound(Sound.ENTITY_ITEM_PICKUP, 1, 1.3f);
    }
    public void findBossKey(Player p){
        bossKey=true;
        broadcast(ChatColor.DARK_RED + "Player " + ChatColor.RED + p.getDisplayName() + ChatColor.DARK_RED + " has found a" + ChatColor.DARK_PURPLE + " Boss Key!");
        broadcastSound(Sound.ENTITY_ITEM_PICKUP, 1, 1.3f);
    }
    public void openWitherDoor(Location loc){
        witherKeys--;
        for (Location l: getAllNearBlocks(loc)){
            l.getBlock().setType(Material.AIR);
        }
        broadcast(ChatColor.DARK_RED + "[Dungeon] " + ChatColor.DARK_GRAY + "Wither Door " + ChatColor.DARK_RED + "has been opened!");
        broadcastSound(Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1, 0.5f);
    }
    public void openBossDoor(Location loc){
        for (Location l: getAllNearBlocks(loc)){
            l.getBlock().setType(Material.AIR);
        }
        broadcast(ChatColor.DARK_RED + "[Dungeon] " + ChatColor.DARK_PURPLE + "Boss Door " + ChatColor.DARK_RED + "has been opened!");
        broadcastSound(Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1, 0.3f);
        finishDungeon(getAllPlayers(), getWorld(), true);
    }
    public int getWitherKeys(){
        return witherKeys;
    }
    public boolean isBossKeyFound(){
        return bossKey;
    }
    public void addScore(int amount) {
        score+=amount;
    }

    public static Difficulty getRandomDifficulty(){
        switch (new Random().nextInt(Difficulty.values().length)){
            case 1:
                return Difficulty.HARD;
            case 2:
                return Difficulty.ULTRA;
            default:
                return Difficulty.NORMAL;
        }
    }
}
