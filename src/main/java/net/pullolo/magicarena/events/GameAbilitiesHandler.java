package net.pullolo.magicarena.events;

import net.pullolo.magicarena.items.Item;
import net.pullolo.magicarena.misc.CooldownApi;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

import static net.pullolo.magicarena.MagicArena.plugin;
import static net.pullolo.magicarena.items.ItemsDefinitions.itemIds;
import static net.pullolo.magicarena.players.ArenaEntity.arenaEntities;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;
import static net.pullolo.magicarena.players.ArenaPlayer.isPlayerInGame;

public class GameAbilitiesHandler implements Listener {

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event){
        if (!isPlayerInGame(event.getPlayer())){
            return;
        }
        if (!arenaPlayers.get(event.getPlayer()).getGame().hasStarted()){
            return;
        }
        if (!(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR))){
            return;
        }
        if (event.getItem()==null || event.getItem().getItemMeta()==null){
            return;
        }
        Item item = new Item(event.getItem());
        Player p = event.getPlayer();
        if (!itemIds.contains(item.getItemId())){
            return;
        }

        if (item.getItemId().equalsIgnoreCase("unstable_tome")){
            if (!CooldownApi.isOnCooldown("UT", p)){
                if (arenaPlayers.get(p).getMana() >= calcBaseManaWithBonuses(30, p)){
                    CooldownApi.addCooldown("UT", p, 1.5);
                    arenaPlayers.get(p).setMana(arenaPlayers.get(p).getMana()-calcBaseManaWithBonuses(30, p));

                    ArmorStand as = p.getWorld().spawn(p.getLocation().add(0, 1.5, 0), ArmorStand.class, en -> {
                        en.setVisible(false);
                        en.setGravity(false);
                        en.setSmall(true);
                        en.setMarker(true);
                    });

                    Location dest = p.getLocation().add(p.getLocation().getDirection().multiply(10));
                    Vector v = dest.subtract(p.getLocation()).toVector();

                    double s = 0.8;
                    int d = 40;

                    new BukkitRunnable(){
                        final Random r = new Random();
                        final int distance = d;
                        final double speed = s;
                        int i = 1;

                        @Override
                        public void run() {
                            if (p == null){
                                as.remove();
                                cancel();
                            }

                            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(66, 117, 245), 1);
                            as.getWorld().spawnParticle(Particle.REDSTONE, as.getLocation(), 1, 0, 0, 0, 1, dustOptions);
                            as.teleport(as.getLocation().add(v.normalize().multiply(s)));

                            for (Entity entity : as.getLocation().getChunk().getEntities()){
                                if (!as.isDead()){
                                    if (entity.equals(as)){
                                        continue;
                                    }
                                    if (as.getLocation().distanceSquared(entity.getLocation()) <= 3.5){
                                        if (!entity.equals(p)){
                                            if (entity instanceof Damageable){
                                                if (entity instanceof Player){
                                                    if (isKillable(p ,(Player) entity)){
                                                        arenaPlayers.get(entity).damage(p, entity, arenaPlayers.get(p).getMagicDamage()*1.1, true);
                                                        ((Damageable) entity).damage(0.01, p);
                                                    }
                                                } else {
                                                    if (arenaEntities.containsKey(entity)){
                                                        arenaEntities.get(entity).damage(p, entity, arenaPlayers.get(p).getMagicDamage()*1.1, true);
                                                        ((Damageable) entity).damage(0.01, p);
                                                        if (arenaEntities.get(entity).getHealth()<=0){
                                                            new OnArenaEntityKilled(p, entity);
                                                        }
                                                    }
                                                }
                                                as.remove();
                                                cancel();
                                            }
                                        }
                                    }
                                }
                            }

                            if (!as.getLocation().add(0, 1, 0).getBlock().isPassable()){
                                if (!as.isDead()){
                                    as.remove();
                                    cancel();
                                }
                            }

                            if (i > distance){
                                if (!as.isDead()){
                                    as.remove();
                                    cancel();
                                }
                            }
                            i++;
                        }
                    }.runTaskTimer(plugin, 0, 1);
                    event.setCancelled(true);

                    return;
                } else p.sendMessage(ChatColor.RED + "You dont have enough Mana!");
            } else p.sendMessage(ChatColor.RED + "This item is on Cooldown for " + (float) ((int) CooldownApi.getCooldownForPlayerLong("UT", p)/100)/10 + "s.");
        }
    }

    public static int calcBaseManaWithBonuses(int mana, Player player){
        if (player.getGameMode().equals(GameMode.CREATIVE)){
            return 0;
        }
        return mana;
    }

    private boolean isKillable(Player damager, Player damaged){
        if (!isPlayerInGame(damaged)){
            return false;
        }
        if (damaged.getGameMode().equals(GameMode.CREATIVE)){
            return false;
        }
        if (arenaPlayers.get(damager).getGame().getAllPlayersInPlayersTeam(damager).contains(damaged)){
            return false;
        }
        return true;
    }
}
