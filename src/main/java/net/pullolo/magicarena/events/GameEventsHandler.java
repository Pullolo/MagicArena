package net.pullolo.magicarena.events;

import dev.dbassett.skullcreator.SkullCreator;
import net.pullolo.magicarena.MagicArena;
import net.pullolo.magicarena.data.PlayerData;
import net.pullolo.magicarena.game.Dungeon;
import net.pullolo.magicarena.items.Item;
import net.pullolo.magicarena.players.ArenaEntity;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import static net.pullolo.magicarena.MagicArena.*;
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
    public void onEntityDrop(EntityDeathEvent event){
        if (event.getEntity().getWorld().getName().contains("temp_")){
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        PlayerData.savePlayerDataToDb(event.getPlayer(), dbManager);
        PlayerData.removePlayerData(event.getPlayer());
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
        PlayerData.setPlayerDataFromDb(event.getPlayer(), dbManager);
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

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if (!isPlayerInGame(event.getPlayer())){
            return;
        }
        if (!(arenaPlayers.get(event.getPlayer()).getGame() instanceof Dungeon)){
            return;
        }
        if (!event.getHand().equals(EquipmentSlot.HAND)){
            return;
        }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock()==null){
            return;
        }
        Dungeon d = (Dungeon) arenaPlayers.get(event.getPlayer()).getGame();
        if (event.getClickedBlock().getType().equals(Material.REDSTONE_BLOCK)){
            if (d.isBossKeyFound()){
                d.openBossDoor(event.getClickedBlock().getLocation());
            }
        } else if (event.getClickedBlock().getType().equals(Material.COAL_BLOCK)) {
            if (d.getWitherKeys()>0){
                d.openWitherDoor(event.getClickedBlock().getLocation());
            }
        }
        BlockState b = event.getClickedBlock().getState();

        if (b instanceof Skull){
            if (isSkullEqual((Skull) b, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmIwNTVjODEwYmRkZmQxNjI2NGVjOGQ0MzljNDMyODNlMzViY2E3MWE1MDk4M2UxNWUzNjRjZDhhYjdjNjY4ZiJ9fX0=")){
                if (!d.isSecretFound(b)){
                    d.findSecret(b, event.getPlayer());
                    d.broadcastSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 1.3f);
                }
            }
        }
    }

    private boolean isSkullEqual(Skull skull, String b64){
        try {
            if (((SkullMeta) getPlayerSkull(b64).getItemMeta()).getOwnerProfile().equals(skull.getOwnerProfile())){
                return true;
            }
        } catch (Exception e){
            return false;
        }
        return false;
    }

    private ItemStack getPlayerSkull(String base64){
        return SkullCreator.itemFromBase64(base64);
    }
}
