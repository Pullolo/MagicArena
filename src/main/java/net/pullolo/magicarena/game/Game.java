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
import static net.pullolo.magicarena.MagicArena.mainWorld;
import static net.pullolo.magicarena.players.ArenaEntity.arenaEntities;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;
import static org.bukkit.Bukkit.getServer;

public abstract class Game {

    private ArrayList<Player> allPlayers;
    private ArrayList<Entity> allEntities = new ArrayList<>();
    private ArrayList<Player> team1;
    private ArrayList<Player> team2;

    private BukkitRunnable startC;
    private BukkitRunnable gameC;
    private BukkitRunnable gameCS;
    private World world;

    public World createWorld(){
        String arenaName = pickRandomArena().split("_")[1];
        String newArenaName = arenaName;
        while (doesArenaExist(newArenaName)){
            newArenaName+="-";
        }
        WorldManager.copyWorld(new File(getServer().getWorldContainer().getAbsolutePath().replace(".", "") + arenaName), "temp_" + newArenaName);
        WorldManager.saveWorld(Bukkit.getWorld("temp_" + newArenaName), false, false, false); //this results in saved name being temp_ the temp param cant be true
        return Bukkit.getWorld("temp_" + newArenaName);
    }
    public void startNecessaryClocks(boolean test, World arena){
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
                update1t();
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

                update1s();

                if (!test){
                    if (!isTeamAlive(team1) && isTeamAlive(team2)){
                        if (startC!=null) startC.cancel();
                        gameClock1t.cancel();
                        if (team2==null) finishDungeon(allPlayers, arena, false);
                        else finishGame(team2, team1, allPlayers, arena);
                        cancel();
                        return;
                    }
                    if (isTeamAlive(team1) && !isTeamAlive(team2)){
                        if (startC!=null) startC.cancel();
                        gameClock1t.cancel();
                        finishGame(team1, team2, allPlayers, arena);
                        cancel();
                        return;
                    }
                    if (!isTeamAlive(team1) && !isTeamAlive(team2)){
                        if (startC!=null) startC.cancel();
                        gameClock1t.cancel();
                        if (team2==null) finishDungeon(allPlayers, arena, false);
                        else finishGame(null, null, allPlayers, arena);
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

    public void update1s(){
        //do nothing
    }

    public void update1t(){
        //do nothing
    }

    public void finishGame(ArrayList<Player> winners, ArrayList<Player> losers, ArrayList<Player> allPlayers, World world){
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
                        if (p.isOp()) p.setGameMode(GameMode.CREATIVE);
                        else p.setGameMode(GameMode.SURVIVAL);
                        p.setHealth(p.getMaxHealth());
                    }
                }
                WorldManager.removeWorld(world);
            }
        }.runTaskLater(MagicArena.plugin, 100);
    }

    public void finishDungeon(ArrayList<Player> allPlayers, World world, boolean won){
        for (Player p : allPlayers){
            if (p!=null){
                p.sendMessage("[Arena] An error occurred in the match, neither side will be penalized for losing!");
                arenaPlayers.remove(p);
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
                        if (p.isOp()) p.setGameMode(GameMode.CREATIVE);
                        else p.setGameMode(GameMode.SURVIVAL);
                        p.setHealth(p.getMaxHealth());
                    }
                }
                WorldManager.removeWorld(world);
            }
        }.runTaskLater(MagicArena.plugin, 100);
    }

    public void forceEndGame(){
        if (startC!=null) startC.cancel();
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
                if (p.isOp()) p.setGameMode(GameMode.CREATIVE);
                else p.setGameMode(GameMode.SURVIVAL);
                p.setHealth(p.getMaxHealth());
            }
        }
        if (world!=null){
            WorldManager.removeWorld(world);
        }
    }

    public boolean isTeamAlive(ArrayList<Player> players){
        if (players==null) return true;
        for (Player p : players) {
            if (p!=null && arenaPlayers.containsKey(p)){
                return true;
            }
        }
        return false;
    }

    public abstract String pickRandomArena();

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
        else if (team2 !=null && team2.contains(p)) return team2;
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

    public void setWorld(World world) {
        this.world = world;
    }

    public void setTeam1(ArrayList<Player> team1) {
        this.team1 = team1;
    }

    public void setTeam2(ArrayList<Player> team2) {
        this.team2 = team2;
    }

    public void setAllPlayers(ArrayList<Player> allPlayers) {
        this.allPlayers = allPlayers;
    }

    public void setStartC(BukkitRunnable startC) {
        this.startC = startC;
    }
}
