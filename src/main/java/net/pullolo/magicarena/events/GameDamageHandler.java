package net.pullolo.magicarena.events;

import net.pullolo.magicarena.items.Item;
import net.pullolo.magicarena.items.ItemsDefinitions;
import net.pullolo.magicarena.players.ArenaPlayer;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Random;

import static net.pullolo.magicarena.MagicArena.getLog;
import static net.pullolo.magicarena.items.ItemsDefinitions.getItemFromPlayer;
import static net.pullolo.magicarena.players.ArenaEntity.arenaEntities;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;
import static net.pullolo.magicarena.players.ArenaPlayer.isPlayerInGame;

public class GameDamageHandler implements Listener {

    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent event){
        if (event.isCancelled()){
            return;
        }
        if (event.getDamage()<0.1){
            return;
        }
        if (!(event.getEntity() instanceof Player)){
            if (event instanceof EntityDamageByEntityEvent){
                onEntityDamagedByEntity((EntityDamageByEntityEvent) event);
                return;
            }
            onEntityDamage(event.getEntity(), event.getDamage());
            return;
        }
        if (!isPlayerInGame((Player) event.getEntity())){
            return;
        }
        Player damaged = (Player) event.getEntity();
        if (damaged.getGameMode().equals(GameMode.CREATIVE)){
            return;
        }
        if (((Player) event.getEntity()).isBlocking()){
            return;
        }

        if (event instanceof EntityDamageByEntityEvent){
            if (event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)){
                return;
            }
            if (!(((EntityDamageByEntityEvent) event).getDamager() instanceof Player)){
                if (!arenaEntities.containsKey(((EntityDamageByEntityEvent) event).getDamager())){
                    return;
                }
            } else if (arenaPlayers.get((Player) ((EntityDamageByEntityEvent) event).getDamager()).getGame().getAllPlayersInPlayersTeam((Player) ((EntityDamageByEntityEvent) event).getDamager()).contains(damaged)){
                event.setCancelled(true);
                return;
            }
            arenaPlayers.get(damaged).damage(((EntityDamageByEntityEvent) event).getDamager(), damaged, calculateDamage(event.getDamage(), ((EntityDamageByEntityEvent) event).getDamager(), damaged), false);
            return;
        }

        arenaPlayers.get(damaged).damage(damaged, event.getDamage()*5, false);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event){
        if (event.isCancelled()){
            return;
        }
        if (!(event.getHitEntity() instanceof Player)){
            onEntityProjectileDamage(event);
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Player)){
            onProjectileDamagePlayer(event);
            return;
        }
        Player damaged = (Player) event.getHitEntity();
        Player damager = (Player) event.getEntity().getShooter();
        if (!(isPlayerInGame(damaged) && isPlayerInGame(damager))){
            return;
        }
        if (damaged.getGameMode().equals(GameMode.CREATIVE)){
            return;
        }
        if (damaged.isBlocking()){
            return;
        }
        if (arenaPlayers.get(damager).getGame().getAllPlayersInPlayersTeam(damager).contains(damaged)){
            event.setCancelled(true);
            return;
        }
        arenaPlayers.get(damaged).damage(damager, damaged, calculateProjectileDamage(damager, damaged), false);
    }

    public void onEntityProjectileDamage(ProjectileHitEvent event){
        Entity damaged = event.getHitEntity();
        Entity damager = (Entity) event.getEntity().getShooter();

        if (!(arenaEntities.containsKey(damaged))){
            return;
        }

        if (damager instanceof Player){
            if (!isPlayerInGame((Player) damager)){
                return;
            }
        } else {
            if (!arenaEntities.containsKey(damager)){
                return;
            }
        }

        arenaEntities.get(damaged).damage(damager, damaged, calculateProjectileDamage(damager, damaged), false);

        if (damager instanceof Player){
            checkIfKilled((Player) damager, damaged);
        }
    }

    public void onProjectileDamagePlayer(ProjectileHitEvent event){
        Player damaged = (Player) event.getHitEntity();
        Entity damager = (Entity) event.getEntity().getShooter();

        if (!(isPlayerInGame(damaged) && arenaEntities.containsKey(damager))){
            return;
        }
        if (damaged.getGameMode().equals(GameMode.CREATIVE)){
            return;
        }
        if (damaged.isBlocking()){
            return;
        }
        arenaPlayers.get(damaged).damage(damager, damaged, calculateProjectileDamage(damager, damaged), false);
    }

    public void onEntityDamage(Entity damaged, double damage){
        if (!arenaEntities.containsKey(damaged)){
            return;
        }
        arenaEntities.get(damaged).damage(damaged, damage*5, false);
    }

    public void onEntityDamagedByEntity(EntityDamageByEntityEvent event){
        Entity damaged = event.getEntity();
        if (!arenaEntities.containsKey(damaged)){
            return;
        }
        if (event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)){
            return;
        }
        Entity damager = event.getDamager();

        if (damager instanceof Player){
            if (!isPlayerInGame((Player) damager)){
                return;
            }
        } else {
            if (!arenaEntities.containsKey(damager)){
                return;
            }
        }

        arenaEntities.get(damaged).damage(damager, damaged, calculateDamage(event.getDamage(), damager, damaged), false);

        if (damager instanceof Player){
            checkIfKilled((Player) damager, damaged);
        }
    }



    private double calculateDamage(double eventDamage, Entity damager, Entity damaged){
        if (!(damager instanceof Player)){
            double entityDamage = arenaEntities.get(damager).getDamage();
            return entityDamage/1.3;
        }
        if (((Player) damager).getInventory().getItemInMainHand().getItemMeta()==null){
            return arenaPlayers.get(damager).getDamage()/1.3;
        }
        //todo add scaling for weapons and stuff
        Item playersItem = getItemFromPlayer(((Player) damager).getInventory().getItemInMainHand());
        if (playersItem.getItemId().equalsIgnoreCase("leeching_staff")){
            arenaPlayers.get(damager).setHealth(arenaPlayers.get(damager).getHealth()+2);
            damager.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, damager.getLocation().add(0, 1, 0), 20, 0.2, 0.6, 0.2, 1);
            ((Player) damager).playSound(damager, Sound.ENTITY_PLAYER_LEVELUP, 1, (1 + ((float) new Random().nextInt(2))/10));
        }
        Double itemDamage = playersItem.getDamage();
        double playerDamage = arenaPlayers.get(damager).getDamage();

        return playerDamage/1.3 * itemDamage/2;
    }

    private double calculateProjectileDamage(Entity damager, Entity damaged){
        if (!(damager instanceof Player)){
            double entityDamage = arenaEntities.get(damager).getDamage();
            return entityDamage*5;
        }
        if (((Player) damager).getInventory().getItemInMainHand().getItemMeta()==null){
            return arenaPlayers.get(damager).getDamage()/1.3;
        }
        //todo add scaling for weapons and stuff
        Double itemDamage = getItemFromPlayer(((Player) damager).getInventory().getItemInMainHand()).getDamage();
        double playerDamage = arenaPlayers.get(damager).getDamage();

        return playerDamage/1.3 * itemDamage/1.8;
    }

    private void checkIfKilled(Player p, Entity e){
        if (arenaEntities.get(e).getHealth()<=0){
            new OnArenaEntityKilled(p, e);
        }
    }
}
