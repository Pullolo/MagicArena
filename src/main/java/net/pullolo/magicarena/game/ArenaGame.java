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

public class ArenaGame extends Game {

    public ArenaGame(ArrayList<Player> team1, ArrayList<Player> team2, QueueManager.QueueType gameType, boolean ranked, boolean test){
        World arena = createWorld();
        setWorld(arena);
        ArrayList<Player> allPlayers = new ArrayList<>();
        allPlayers.addAll(team1);
        allPlayers.addAll(team2);
        setTeam1(team1);
        setTeam2(team2);
        setAllPlayers(allPlayers);
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
                    setStarted(true);
                    this.cancel();
                }
            }
        };
        startClock.runTaskTimer(MagicArena.plugin, 0, 1);
        setStartC(startClock);
        startNecessaryClocks(test, arena);
    }

    @Override
    public String pickRandomArena(){
        return WorldManager.getArenas().get(new Random().nextInt(WorldManager.getArenas().size()));
    }
}
