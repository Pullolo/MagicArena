package net.pullolo.magicarena.events;

import dev.dbassett.skullcreator.SkullCreator;
import net.pullolo.magicarena.MagicArena;
import net.pullolo.magicarena.data.PlayerData;
import net.pullolo.magicarena.game.Dungeon;
import net.pullolo.magicarena.game.Game;
import net.pullolo.magicarena.game.GameWorld;
import net.pullolo.magicarena.items.Item;
import net.pullolo.magicarena.players.ArenaEntity;
import net.pullolo.magicarena.players.ArenaPlayer;
import net.pullolo.magicarena.players.DungeonEntity;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import static net.pullolo.magicarena.MagicArena.*;
import static net.pullolo.magicarena.data.PlayerData.getPlayerData;
import static net.pullolo.magicarena.data.PlayerData.setPlayerDataFromDb;
import static net.pullolo.magicarena.game.Game.games;
import static net.pullolo.magicarena.game.GameWorld.gameWorlds;
import static net.pullolo.magicarena.items.ItemsDefinitions.itemIds;
import static net.pullolo.magicarena.players.ArenaEntity.arenaEntities;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;
import static net.pullolo.magicarena.players.ArenaPlayer.isPlayerInGame;
import static net.pullolo.magicarena.players.UpdateManager.updatePlayer;
import static net.pullolo.magicarena.wish.WishSystem.lastArmorSet;

public class GameEventsHandler implements Listener {

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event){
        if (!(event.getWorld().getName().contains("temp_") || gameWorlds.containsKey(event.getChunk().getWorld()))){
            return;
        }
        for (Entity e : event.getChunk().getEntities()){
            if (!arenaEntities.containsKey(e)){
                continue;
            }
            arenaEntities.get(e).setLoaded(false);
            if (e instanceof Monster || e.isDead()){
                arenaEntities.remove(e);
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event){
        if (!(event.getWorld().getName().contains("temp_") || gameWorlds.containsKey(event.getChunk().getWorld()))){
            return;
        }
        for (Entity e : event.getChunk().getEntities()){
            if (!arenaEntities.containsKey(e)) {
                if (gameWorlds.containsKey(e.getWorld()) && e instanceof LivingEntity){
                    GameWorld g = gameWorlds.get(e.getWorld());
                    int level = 1;
                    arenaEntities.put(e, new ArenaEntity(e, level, g, false));
                }
                continue;
            }
            arenaEntities.get(e).setLoaded(true);
        }
        Game g = games.get(event.getWorld());
        if (!(g instanceof Dungeon)){
            return;
        }
        ((Dungeon) g).convertArmorStandsToMobs(2, event.getChunk().getEntities());
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event){
        if (!(event.getEntity().getWorld().getName().contains("temp_") || gameWorlds.containsKey(event.getEntity().getWorld()))){
            return;
        }
        if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPELL)){
            Game g = null;
            for (Player p : event.getEntity().getWorld().getPlayers()){
                if (isPlayerInGame(p)){
                    g = arenaPlayers.get(p).getGame();
                    break;
                }
            }
            if (g==null){
                event.getEntity().remove();
                return;
            }
            if (event.getEntity() instanceof Vex){
                int level = 1;
                if (g instanceof Dungeon){
                    level = ((Dungeon) g).getLevel();
                }
                arenaEntities.put(event.getEntity(), new DungeonEntity(event.getEntity(), level, g, false));
            }
            return;
        }
        if (gameWorlds.containsKey(event.getEntity().getWorld())){
            if (event.getEntity() instanceof Player || event.getEntity() instanceof ArmorStand || event.getEntity() instanceof EnderDragonPart){
                return;
            }
            GameWorld g = gameWorlds.get(event.getEntity().getWorld());
            int level = 1;
            arenaEntities.put(event.getEntity(), new ArenaEntity(event.getEntity(), level, g, false));
            return;
        }
    }

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
            if (!(arenaPlayers.get(event.getPlayer()).getGame() instanceof GameWorld)){
                arenaPlayers.get(event.getPlayer()).getGame().playerDied(event.getPlayer());
                event.getPlayer().teleport(Bukkit.getWorld(mainWorld).getSpawnLocation());
            } else {
                arenaPlayers.get(event.getPlayer()).getGame().getAllPlayers().remove(event.getPlayer());
                arenaPlayers.remove(event.getPlayer());
            }
        }

        MagicArena.gameManager.getQueueManager().removePlayerFromQueue(event.getPlayer());
        lastArmorSet.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if (event.getPlayer().getName().contains(" ")){
            event.getPlayer().kickPlayer("Invalid Name!");
        }
        event.getPlayer().setInvulnerable(false);
        setPlayerDataFromDb(event.getPlayer(), dbManager);
        if (!getPlayerData(event.getPlayer()).isUpdated()){
            updatePlayer(event.getPlayer());
        }
        if (event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
            if (event.getPlayer().isOp()) event.getPlayer().setGameMode(GameMode.CREATIVE);
            else event.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
        if (event.getPlayer().getWorld().getName().regionMatches(0, "temp_", 0, 5)){
            event.getPlayer().teleport(Bukkit.getWorld(mainWorld).getSpawnLocation());
        }
        if (gameWorlds.containsKey(event.getPlayer().getWorld())){
            new ArenaPlayer(event.getPlayer(), getPlayerData(event.getPlayer()).getLevel(), gameWorlds.get(event.getPlayer().getWorld()));
            gameWorlds.get(event.getPlayer().getWorld()).getAllPlayers().add(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerBuild(BlockPlaceEvent event){
        if (isPlayerInGame(event.getPlayer()) || event.getPlayer().getWorld().getName().contains("temp_") && !(arenaPlayers.get(event.getPlayer()).getGame() instanceof GameWorld)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent event){
        if (isPlayerInGame(event.getPlayer()) || event.getPlayer().getWorld().getName().contains("temp_") && !(arenaPlayers.get(event.getPlayer()).getGame() instanceof GameWorld)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemFrameBreak(HangingBreakByEntityEvent event){
        if (!(event.getRemover() instanceof Player)){
            return;
        }
        if (!isPlayerInGame((Player) event.getRemover()) || (arenaPlayers.get((Player) event.getRemover()).getGame() instanceof GameWorld)){
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemFrameInteract(PlayerInteractEntityEvent event){
        if (!isPlayerInGame(event.getPlayer()) || (arenaPlayers.get(event.getPlayer()).getGame() instanceof GameWorld)){
            return;
        }
        if (event.getRightClicked() instanceof Hanging){
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if (!isPlayerInGame(event.getPlayer()) || (arenaPlayers.get(event.getPlayer()).getGame() instanceof GameWorld)){
            return;
        }
        if (event.getItem()!=null){
            if (event.getItem().getType().equals(Material.ITEM_FRAME) || event.getItem().getType().equals(Material.GLOW_ITEM_FRAME)){
                event.setCancelled(true);
                return;
            }
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
            if (isSkullEqual((Skull) b, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGI2OTc1YWY3MDcyNGQ2YTQ0ZmQ1OTQ2ZTYwYjI3MTc3MzdkZmRiNTQ1YjRkYWIxODkzMzUxYTljOWRkMTgzYyJ9fX0=")){
                event.getClickedBlock().setType(Material.AIR);
                d.reviveAll(event.getPlayer(), event.getClickedBlock().getLocation());
                d.broadcastSound(Sound.BLOCK_AMETHYST_CLUSTER_HIT, 1, 0.3f);
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
