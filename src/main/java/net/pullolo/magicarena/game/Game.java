package net.pullolo.magicarena.game;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.pullolo.magicarena.MagicArena;
import net.pullolo.magicarena.players.ArenaPlayer;
import net.pullolo.magicarena.worlds.WorldManager;
import org.bukkit.*;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import static net.pullolo.magicarena.MagicArena.config;
import static net.pullolo.magicarena.MagicArena.mainWorld;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;
import static org.bukkit.Bukkit.getServer;

public class Game {

    private final ArrayList<Player> allPlayers;
    private final ArrayList<Player> team1;
    private final ArrayList<Player> team2;

    public Game(ArrayList<Player> team1, ArrayList<Player> team2, QueueManager.QueueType gameType){
        String arenaName = pickRandomArena().split("_")[1];
        WorldManager.copyWorld(new File(getServer().getWorldContainer().getAbsolutePath().replace(".", "") + arenaName), "temp_" + arenaName);
        WorldManager.saveWorld(Bukkit.getWorld("temp_" + arenaName), false, false); //this results in saved name being temp_ the temp param does cant be true
        World arena = Bukkit.getWorld("temp_" + arenaName);
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

        BukkitRunnable gameClock1t = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : allPlayers){
                    if (p!=null && arenaPlayers.containsKey(p)){
                        float speed = (float) (arenaPlayers.get(p).getSpeed()/500);
                        p.setWalkSpeed(speed);
                        if (arenaPlayers.get(p).getHealth()<=0 || p.getLocation().getY() < -96){
                            playerDied(p);
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
            }
        };
        gameClock1t.runTaskTimer(MagicArena.plugin, 0, 1);

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
            }
        };
        gameClock1s.runTaskTimer(MagicArena.plugin, 0, 20);
    }

    public void finishGame(ArrayList<Player> winners, ArrayList<Player> losers, ArrayList<Player> allPlayers, QueueManager.QueueType gameType, World world){

        for (Player p : allPlayers){
            if (p!=null){
                arenaPlayers.remove(p);
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

    public boolean isTeamAlive(ArrayList<Player> players){
        for (Player p : players) {
            if (p!=null && arenaPlayers.containsKey(p)){
                return true;
            }
        }
        return false;
    }
    //todo add check to end game and see who won

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

    public void broadcast(String message){
        for (Player p : allPlayers){
            if (p!=null){
                p.sendMessage(message);
            }
        }
    }

    public ArrayList<Player> getAllPlayersInPlayersTeam(Player p){
        if (team1.contains(p)) return team1;
        else if (team2.contains(p)) return team2;
        return new ArrayList<>();
    }
}
