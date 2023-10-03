package net.pullolo.magicarena.events;

import net.pullolo.magicarena.MagicArena;
import net.pullolo.magicarena.players.ArenaEntity;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;

import static net.pullolo.magicarena.MagicArena.mainWorld;
import static net.pullolo.magicarena.players.ArenaEntity.arenaEntities;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;
import static net.pullolo.magicarena.players.ArenaPlayer.isPlayerInGame;

public class GameEventsHandler implements Listener {

    @EventHandler
    public void onDummySpawn(PlayerCommandPreprocessEvent event){
        if (!(event.getMessage().equalsIgnoreCase("/summon minecraft:creeper") && isPlayerInGame(event.getPlayer()))){
            return;
        }
        Player p = event.getPlayer();
        Creeper creeper = (Creeper) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.CREEPER);
        creeper.setAI(false);
        arenaEntities.put(creeper, new ArenaEntity(creeper, 1, arenaPlayers.get(p).getGame(), true));
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        if (arenaPlayers.containsKey(event.getPlayer())){
            arenaPlayers.get(event.getPlayer()).getGame().broadcast("[Arena] Player " + event.getPlayer().getDisplayName() + " has left the game!");
            arenaPlayers.get(event.getPlayer()).getGame().playerDied(event.getPlayer());
            event.getPlayer().teleport(Bukkit.getWorld(mainWorld).getSpawnLocation());
        }

        MagicArena.gameManager.getQueueManager().removePlayerFromQueue(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if (event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
            event.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
        if (event.getPlayer().getWorld().getName().regionMatches(0, "temp_", 0, 5)){
            event.getPlayer().teleport(Bukkit.getWorld(mainWorld).getSpawnLocation());
        }
    }

    @EventHandler
    public void onPlayerBuild(BlockPlaceEvent event){
        if (isPlayerInGame(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent event){
        if (isPlayerInGame(event.getPlayer())){
            event.setCancelled(true);
        }
    }
}
