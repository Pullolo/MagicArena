package net.pullolo.magicarena.events;

import net.pullolo.magicarena.MagicArena;
import net.pullolo.magicarena.items.Item;
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
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.*;

import static net.pullolo.magicarena.MagicArena.mainWorld;
import static net.pullolo.magicarena.MagicArena.partyManager;
import static net.pullolo.magicarena.items.ItemsDefinitions.itemIds;
import static net.pullolo.magicarena.players.ArenaEntity.arenaEntities;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;
import static net.pullolo.magicarena.players.ArenaPlayer.isPlayerInGame;

public class GameEventsHandler implements Listener {

    @EventHandler
    public void onPlayerBow(EntityShootBowEvent event){
        if (event.getConsumable()==null){
            return;
        }
        if (event.getConsumable().getItemMeta()==null){
            return;
        }
        if (!(event.getEntity() instanceof Player)){
            return;
        }
        if (itemIds.contains(new Item(event.getConsumable()).getItemId())){
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        partyManager.leaveParty(event.getPlayer());
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
            if (event.getPlayer().isOp()) event.getPlayer().setGameMode(GameMode.CREATIVE);
            else event.getPlayer().setGameMode(GameMode.SURVIVAL);
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
