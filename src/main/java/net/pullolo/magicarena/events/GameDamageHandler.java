package net.pullolo.magicarena.events;

import net.pullolo.magicarena.items.Item;
import net.pullolo.magicarena.items.ItemsDefinitions;
import net.pullolo.magicarena.players.ArenaPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import static net.pullolo.magicarena.MagicArena.getLog;
import static net.pullolo.magicarena.items.ItemsDefinitions.getItemFromPlayer;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;
import static net.pullolo.magicarena.players.ArenaPlayer.isPlayerInGame;

public class GameDamageHandler implements Listener {

    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent event){
        if (event.isCancelled()){
            return;
        }
        if (!(event.getEntity() instanceof Player)){
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
        if (event.getDamage()<0.1){
            return;
        }

        if (event instanceof EntityDamageByEntityEvent){
            if (event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)){
                return;
            }
            if (!(((EntityDamageByEntityEvent) event).getDamager() instanceof Player)){
                return;
            }
            if (arenaPlayers.get((Player) ((EntityDamageByEntityEvent) event).getDamager()).getGame().getAllPlayersInPlayersTeam((Player) ((EntityDamageByEntityEvent) event).getDamager()).contains(damaged)){
                event.setCancelled(true);
                return;
            }
            arenaPlayers.get(damaged).damage((Player) ((EntityDamageByEntityEvent) event).getDamager(), damaged, calculateDamage(event.getDamage(), (Player) ((EntityDamageByEntityEvent) event).getDamager(), damaged), false);
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
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Player)){
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

    private double calculateDamage(double eventDamage, Player damager, Player damaged){
        //todo add scaling for weapons and stuff
        if (damager.getInventory().getItemInMainHand().getItemMeta()==null){
            return arenaPlayers.get(damager).getDamage()/1.3;
        }
        Double itemDamage = getItemFromPlayer(damager.getInventory().getItemInMainHand()).getDamage();
        double playerDamage = arenaPlayers.get(damager).getDamage();

        return playerDamage/1.3 * itemDamage/2;
    }

    private double calculateProjectileDamage(Player damager, Player damaged){
        if (damager.getInventory().getItemInMainHand().getItemMeta()==null){
            return arenaPlayers.get(damager).getDamage()/1.3;
        }
        Double itemDamage = getItemFromPlayer(damager.getInventory().getItemInMainHand()).getDamage();
        double playerDamage = arenaPlayers.get(damager).getDamage();

        return playerDamage/1.3 * itemDamage/1.8;
    }
}
