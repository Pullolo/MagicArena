package net.pullolo.magicarena.game;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.pullolo.magicarena.MagicArena;
import net.pullolo.magicarena.items.Item;
import net.pullolo.magicarena.players.ArenaPlayer;
import net.pullolo.magicarena.worlds.WorldManager;
import org.bukkit.*;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import static net.pullolo.magicarena.MagicArena.*;
import static net.pullolo.magicarena.players.ArenaEntity.arenaEntities;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;
import static org.bukkit.Bukkit.getServer;

public class Game {

    private final ArrayList<Player> allPlayers;
    private final ArrayList<Entity> allEntities = new ArrayList<>();
    private final ArrayList<Player> team1;
    private final ArrayList<Player> team2;

    private final BukkitRunnable startC;
    private final BukkitRunnable gameC;
    private final BukkitRunnable gameCS;
    private final World world;

    public Game(ArrayList<Player> team1, ArrayList<Player> team2, QueueManager.QueueType gameType, boolean ranked, boolean test){
        String arenaName = pickRandomArena().split("_")[1];
        String newArenaName = arenaName;
        while (doesArenaExist(newArenaName)){
            newArenaName+="-";
        }
        WorldManager.copyWorld(new File(getServer().getWorldContainer().getAbsolutePath().replace(".", "") + arenaName), "temp_" + newArenaName);
        WorldManager.saveWorld(Bukkit.getWorld("temp_" + newArenaName), false, false); //this results in saved name being temp_ the temp param cant be true
        World arena = Bukkit.getWorld("temp_" + newArenaName);
        this.world=arena;
        ArrayList<Player> allPlayers = new ArrayList<>();
        allPlayers.addAll(team1);
        allPlayers.addAll(team2);
        this.team1 = team1;
        this.team2 = team2;
        this.allPlayers = allPlayers;
        for (Player p: team1){
            Location loc = new Location(arena, config.getDouble("arenas-spawn-x"), config.getDouble("arenas-spawn-y"), config.getDouble("arenas-spawn-z"));
            loc.setDirection(loc.getDirection().multiply(-1));
            p.teleport(loc);
        }
        for (Player p: team2){
            p.teleport(new Location(arena, config.getDouble("arenas-spawn-x"), config.getDouble("arenas-spawn-y"), -config.getDouble("arenas-spawn-z")));
        }
        for (Player p : allPlayers){
            new ArenaPlayer(p, 1, this);
            p.setGameMode(GameMode.SURVIVAL);
            if (test){
                p.sendMessage(ChatColor.YELLOW + "[Warning] Experimental=True");
            }
            p.sendMessage(ChatColor.GREEN + "Entered Game!");
            p.sendMessage(ChatColor.GREEN + "Match Starting in 20s...");
        }
        BukkitRunnable startClock = new BukkitRunnable() {
            int i = 20*20;
            @Override
            public void run() {
                i--;
                for (Player p : team1){
                    if (p!=null && p.getLocation().getZ()<config.getDouble("arenas-spawn-z")-1){
                        Location loc = new Location(arena, config.getDouble("arenas-spawn-x"), config.getDouble("arenas-spawn-y"), config.getDouble("arenas-spawn-z"));
                        loc.setDirection(loc.getDirection().multiply(-1));
                        p.teleport(loc);
                    }
                }
                for (Player p : team2){
                    if (p!=null && p.getLocation().getZ()>-config.getDouble("arenas-spawn-z")+2){
                        p.teleport(new Location(arena, config.getDouble("arenas-spawn-x"), config.getDouble("arenas-spawn-y"), -config.getDouble("arenas-spawn-z")));
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
        this.startC = startClock;
        BukkitRunnable gameClock1t = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : allPlayers){
                    if (p!=null && arenaPlayers.containsKey(p)){
                        updatePlayerStats(p);
                        float speed = (float) (arenaPlayers.get(p).getSpeed()/500);
                        p.setWalkSpeed(speed);
                        if (arenaPlayers.get(p).getHealth()<=0 || p.getLocation().getY() < -96){
                            //totem check
                            if (p.getInventory().getItemInMainHand().getItemMeta()!=null && p.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING) && arenaPlayers.get(p).getHealth()<=0){
                                p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                                arenaPlayers.get(p).setHealth(arenaPlayers.get(p).getMaxHealth()/20);
                                p.getWorld().spawnParticle(Particle.TOTEM, p.getLocation().add(0, 1, 0), 100, 0.1, 0, 0.1, 1);
                                p.getWorld().playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
                            } else if (p.getInventory().getItemInOffHand().getItemMeta()!=null && p.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING) && arenaPlayers.get(p).getHealth()<=0) {
                                p.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                                arenaPlayers.get(p).setHealth(arenaPlayers.get(p).getMaxHealth()/20);
                                p.getWorld().spawnParticle(Particle.TOTEM, p.getLocation().add(0, 1, 0), 100, 0.1, 0, 0.1, 1);
                                p.getWorld().playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
                            } else playerDied(p);
                        } else {
                            arenaPlayers.get(p).updateStats();

                            float h = (float) (arenaPlayers.get(p).getHealth() / arenaPlayers.get(p).getMaxHealth());
                            h = h * 20;
                            (p).setFoodLevel(20);
                            (p).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 255, false, false, false));
                            (p).setMaxHealth(20);
                            if (h >= 1){
                                (p).setHealth((int) h);
                            }
                            else {
                                (p).setHealth(1);
                            }
                        }
                    }
                }
                for (Entity e : allEntities){
                    ArrayList<Entity> toDel = new ArrayList<>();
                    if (e!=null && arenaEntities.containsKey(e)){
                        if (arenaEntities.get(e).getHealth()<=0 || e.getLocation().getY() < -96){
                            arenaEntities.get(e).updateStats();
                            arenaEntities.remove(e);
                            ((Damageable) e).setHealth(0);
                        } else {
                            arenaEntities.get(e).updateStats();
                            ((Damageable) e).setMaxHealth(40);
                            ((Damageable) e).setHealth(40);
                        }
                    }
                    for (Entity en : toDel){
                        allEntities.remove(en);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (en != null){
                                    en.remove();
                                }
                            }
                        }.runTaskLater(plugin, 20);
                    }
                }
            }
        };
        gameClock1t.runTaskTimer(MagicArena.plugin, 0, 1);
        this.gameC = gameClock1t;

        BukkitRunnable gameClock1s = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : allPlayers){
                    if (p!=null && arenaPlayers.containsKey(p)){
                        arenaPlayers.get(p).regen();
                        arenaPlayers.get(p).regenMana();
                        arenaPlayers.get(p).updateStats();
                        try{
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c" + Math.round(arenaPlayers.get(p).getHealth()) + "/" + Math.round(arenaPlayers.get(p).getMaxHealth())
                                    + "❤     §a" + Math.round(arenaPlayers.get(p).getDefence()) + "❈ Defence    §b" + Math.round(arenaPlayers.get(p).getMana())
                                    + "/" + Math.round(arenaPlayers.get(p).getMaxMana()) + "✎ Mana"));
                        } catch (Exception exception){
                            exception.printStackTrace();
                        }
                    }
                }
                for (Entity e : allEntities){
                    if (e!=null && arenaEntities.containsKey(e)){
                        arenaEntities.get(e).regen();
                        arenaEntities.get(e).regenMana();
                        arenaEntities.get(e).updateStats();
                    }
                }


                if (!test){
                    if (!isTeamAlive(team1) && isTeamAlive(team2)){
                        startClock.cancel();
                        gameClock1t.cancel();
                        finishGame(team2, team1, allPlayers, gameType, arena);
                        cancel();
                        return;
                    }
                    if (isTeamAlive(team1) && !isTeamAlive(team2)){
                        startClock.cancel();
                        gameClock1t.cancel();
                        finishGame(team1, team2, allPlayers, gameType, arena);
                        cancel();
                        return;
                    }
                    if (!isTeamAlive(team1) && !isTeamAlive(team2)){
                        startClock.cancel();
                        gameClock1t.cancel();
                        finishGame(null, null, allPlayers, gameType, arena);
                        cancel();
                        return;
                    }
                } else {
                    //Prevent an infinite Game
                    if (!isTeamAlive(allPlayers)){
                        for (Player p : allPlayers){
                            if (p!=null){
                                p.sendMessage(ChatColor.YELLOW + "[Warning] Due to no players being alive you will be warped in 3 seconds!");
                            }
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                forceEndGame();
                            }
                        }.runTaskLater(MagicArena.plugin, 60);
                        cancel();
                        return;
                    }
                }
            }
        };
        gameClock1s.runTaskTimer(MagicArena.plugin, 0, 20);
        this.gameCS = gameClock1s;
    }

    public void finishGame(ArrayList<Player> winners, ArrayList<Player> losers, ArrayList<Player> allPlayers, QueueManager.QueueType gameType, World world){
        removeAllEntitiesFromGame();
        for (Player p : allPlayers){
            if (p!=null){
                arenaPlayers.remove(p);
                p.setFireTicks(0);
            }
        }
        if (winners == null || losers == null){
            for (Player p : allPlayers){
                if (p!=null){
                    p.sendMessage("[Arena] An error occurred in the match, neither side will be penalized for losing!");
                    arenaPlayers.remove(p);
                }
            }
            return;
        }
        for (Player p : winners){
            if (p!=null){
                p.sendMessage(ChatColor.GREEN + "[Arena] You have won this game!");
            }
        }
        for (Player p : losers){
            if (p!=null){
                p.sendMessage(ChatColor.RED + "[Arena] You have lost this game!");
            }
        }
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
                        p.setGameMode(GameMode.SURVIVAL);
                        p.setHealth(p.getMaxHealth());
                    }
                }
                WorldManager.removeWorld(world);
            }
        }.runTaskLater(MagicArena.plugin, 100);
    }

    public void forceEndGame(){
        startC.cancel();
        gameC.cancel();
        gameCS.cancel();
        removeAllEntitiesFromGame();
        for (Player p : allPlayers){
            if (p!=null){
                arenaPlayers.remove(p);
                p.sendMessage(ChatColor.RED + "[Arena] This match was terminated, neither side will be penalized for losing!");
            }
        }
        for (Player p : allPlayers){
            if (p!=null){
                p.sendMessage(ChatColor.GREEN + "You have been warped!");
                p.teleport(Bukkit.getWorld(mainWorld).getSpawnLocation());
                p.setGameMode(GameMode.SURVIVAL);
                p.setHealth(p.getMaxHealth());
            }
        }
        if (world!=null){
            WorldManager.removeWorld(world);
        }
    }

    public boolean isTeamAlive(ArrayList<Player> players){
        for (Player p : players) {
            if (p!=null && arenaPlayers.containsKey(p)){
                return true;
            }
        }
        return false;
    }

    public String pickRandomArena(){
        return WorldManager.getArenas().get(new Random().nextInt(WorldManager.getArenas().size()));
    }

    public void playerDied(Player p){
        p.sendMessage(ChatColor.RED + "[Arena] You died!");
        arenaPlayers.remove(p);
        p.setGameMode(GameMode.SPECTATOR);
        p.teleport(new Location(p.getWorld(), 0, 65, 0));


        for (Player player : allPlayers){
            if (player!=null && !player.equals(p)){
                player.sendMessage(ChatColor.RED + "[Arena] Player " + p.getDisplayName() + " was slain!");
            }
        }

    }

    private boolean doesArenaExist(String arenaName){
        for (World w: Bukkit.getWorlds()){
            if (w.getName().equalsIgnoreCase("temp_" + arenaName)){
                return true;
            }
        }
        return false;
    }

    public void broadcast(String message){
        for (Player p : allPlayers){
            if (p!=null){
                p.sendMessage(message);
            }
        }
    }

    private void removeAllEntitiesFromGame(){
        ArrayList<Entity> toDel = new ArrayList<>();
        for (Entity e : allEntities){
            if (e != null && arenaEntities.containsKey(e)){
                if (arenaEntities.get(e).getGame().equals(this)){
                    toDel.add(e);
                }
            }
        }
        for (Entity e : toDel){
            allEntities.remove(e);
            arenaEntities.remove(e);
            e.remove();
        }
    }

    public ArrayList<Player> getAllPlayersInPlayersTeam(Player p){
        if (team1.contains(p)) return team1;
        else if (team2.contains(p)) return team2;
        return new ArrayList<>();
    }

    private void updatePlayerStats(Player p){
        updatePlayerStatsItemHeld(p);
    }

    private void updatePlayerStatsItemHeld(Player p){
        String key = "itemHeld";
        if (p.getInventory().getItemInMainHand().getItemMeta() != null){
            if (arenaPlayers.get(p).getBonusDefence().containsKey(key)){
                arenaPlayers.get(p).getBonusDefence().replace(key, new Item(p.getInventory().getItemInMainHand()).getDefence());
            }else {
                arenaPlayers.get(p).getBonusDefence().put(key, new Item(p.getInventory().getItemInMainHand()).getDefence());
            }
            if (arenaPlayers.get(p).getBonusMaxMana().containsKey(key)){
                arenaPlayers.get(p).getBonusMaxMana().replace(key, new Item(p.getInventory().getItemInMainHand()).getIntelligence());
            }else {
                arenaPlayers.get(p).getBonusMaxMana().put(key, new Item(p.getInventory().getItemInMainHand()).getIntelligence());
            }
            if (arenaPlayers.get(p).getBonusSpeed().containsKey(key)){
                arenaPlayers.get(p).getBonusSpeed().replace(key, new Item(p.getInventory().getItemInMainHand()).getSpeed());
            }else {
                arenaPlayers.get(p).getBonusSpeed().put(key, new Item(p.getInventory().getItemInMainHand()).getSpeed());
            }
            if (arenaPlayers.get(p).getBonusCritDamage().containsKey(key)){
                arenaPlayers.get(p).getBonusCritDamage().replace(key, new Item(p.getInventory().getItemInMainHand()).getCritDamage());
            }else {
                arenaPlayers.get(p).getBonusCritDamage().put(key, new Item(p.getInventory().getItemInMainHand()).getCritDamage());
            }
            if (arenaPlayers.get(p).getBonusCritChance().containsKey(key)){
                arenaPlayers.get(p).getBonusCritChance().replace(key, new Item(p.getInventory().getItemInMainHand()).getCritChance());
            }else {
                arenaPlayers.get(p).getBonusCritChance().put(key, new Item(p.getInventory().getItemInMainHand()).getCritChance());
            }
            if (arenaPlayers.get(p).getBonusMaxHealth().containsKey(key)){
                arenaPlayers.get(p).getBonusMaxHealth().replace(key, new Item(p.getInventory().getItemInMainHand()).getHealth());
            }else {
                arenaPlayers.get(p).getBonusMaxHealth().put(key, new Item(p.getInventory().getItemInMainHand()).getHealth());
            }
            if (arenaPlayers.get(p).getBonusMagicDamage().containsKey(key)){
                arenaPlayers.get(p).getBonusMagicDamage().replace(key, new Item(p.getInventory().getItemInMainHand()).getAbilityPower());
            }else {
                arenaPlayers.get(p).getBonusMagicDamage().put(key, new Item(p.getInventory().getItemInMainHand()).getAbilityPower());
            }
            if (arenaPlayers.get(p).getBonusMagicDefence().containsKey(key)){
                arenaPlayers.get(p).getBonusMagicDefence().replace(key, new Item(p.getInventory().getItemInMainHand()).getResistance());
            }else {
                arenaPlayers.get(p).getBonusMagicDefence().put(key, new Item(p.getInventory().getItemInMainHand()).getResistance());
            }
            return;
        }
        if (arenaPlayers.get(p).getBonusDefence().containsKey(key)){
            arenaPlayers.get(p).getBonusDefence().replace(key, 0.0);
        }else {
            arenaPlayers.get(p).getBonusDefence().put(key, 0.0);
        }
        if (arenaPlayers.get(p).getBonusMaxMana().containsKey(key)){
            arenaPlayers.get(p).getBonusMaxMana().replace(key, 0.0);
        }else {
            arenaPlayers.get(p).getBonusMaxMana().put(key, 0.0);
        }
        if (arenaPlayers.get(p).getBonusSpeed().containsKey(key)){
            arenaPlayers.get(p).getBonusSpeed().replace(key, 0.0);
        }else {
            arenaPlayers.get(p).getBonusSpeed().put(key, 0.0);
        }
        if (arenaPlayers.get(p).getBonusCritDamage().containsKey(key)){
            arenaPlayers.get(p).getBonusCritDamage().replace(key, 0.0);
        }else {
            arenaPlayers.get(p).getBonusCritDamage().put(key, 0.0);
        }
        if (arenaPlayers.get(p).getBonusCritChance().containsKey(key)){
            arenaPlayers.get(p).getBonusCritChance().replace(key, 0.0);
        }else {
            arenaPlayers.get(p).getBonusCritChance().put(key, 0.0);
        }
        if (arenaPlayers.get(p).getBonusMaxHealth().containsKey(key)){
            arenaPlayers.get(p).getBonusMaxHealth().replace(key, 0.0);
        }else {
            arenaPlayers.get(p).getBonusMaxHealth().put(key, 0.0);
        }
        if (arenaPlayers.get(p).getBonusMagicDamage().containsKey(key)){
            arenaPlayers.get(p).getBonusMagicDamage().replace(key, 0.0);
        }else {
            arenaPlayers.get(p).getBonusMagicDamage().put(key, 0.0);
        }
        if (arenaPlayers.get(p).getBonusMagicDefence().containsKey(key)){
            arenaPlayers.get(p).getBonusMagicDefence().replace(key, 0.0);
        }else {
            arenaPlayers.get(p).getBonusMagicDefence().put(key, 0.0);
        }
    }

    public void addEntity(Entity e){
        allEntities.add(e);
    }
    public void removeEntity(Entity e){
        allEntities.remove(e);
    }
}
