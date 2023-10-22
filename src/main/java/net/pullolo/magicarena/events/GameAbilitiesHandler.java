package net.pullolo.magicarena.events;

import net.pullolo.magicarena.items.Item;
import net.pullolo.magicarena.misc.CooldownApi;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

import static net.pullolo.magicarena.MagicArena.getLog;
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
                if (arenaPlayers.get(p).getMana() >= calcBaseManaWithBonuses(10, p)){
                    CooldownApi.addCooldown("UT", p, 1.5);
                    arenaPlayers.get(p).setMana(arenaPlayers.get(p).getMana()-calcBaseManaWithBonuses(10, p));

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
                                                if (entity instanceof Player && !arenaPlayers.get(p).getGame().getAllPlayersInPlayersTeam(p).contains(entity)){
                                                    if (isKillable(p ,(Player) entity)){
                                                        arenaPlayers.get(entity).damage(p, entity, arenaPlayers.get(p).getMagicDamage()*1.1+20, true);
                                                        ((Damageable) entity).damage(0.01, p);
                                                    }
                                                } else {
                                                    if (arenaEntities.containsKey(entity)){
                                                        arenaEntities.get(entity).damage(p, entity, arenaPlayers.get(p).getMagicDamage()*1.1+20, true);
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
        if (item.getItemId().equalsIgnoreCase("storm_ruler")){
            if (!CooldownApi.isOnCooldown("SR", p)){
                CooldownApi.addCooldown("SR", p, 10);

                ArmorStand as = p.getWorld().spawn(p.getLocation().add(0, 1, 0), ArmorStand.class, en -> {
                    en.setVisible(false);
                    en.setGravity(false);
                    en.setSmall(true);
                    en.setMarker(true);
                });
                as.teleport(as.getLocation().setDirection(p.getLocation().getDirection().clone().setY(0)));

                Location dest = p.getLocation().add(p.getLocation().getDirection().clone().setY(0).multiply(10));
                Vector v = dest.subtract(p.getLocation()).toVector();

                double s = 0.4;
                int d = 20;

                new BukkitRunnable(){
                    final Random r = new Random();
                    final int distance = d;
                    final double speed = s;
                    int i = 1;
                    ArrayList<Entity> hit = new ArrayList<>();

                    @Override
                    public void run() {
                        if (p == null){
                            as.remove();
                            cancel();
                        }

                        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 255, 255), 1);
                        for (int j = 0; j<6; j++){
                            as.getWorld().spawnParticle(Particle.REDSTONE, as.getLocation().clone().add(rotateVector(as.getLocation().getDirection().clone(), 90).normalize().multiply(1.5-(j*0.5))).add(0, 0.3, 0), 1, 0, 0, 0, 1, dustOptions);
                        }
                        for (int j = 0; j<6; j++){
                            as.getWorld().spawnParticle(Particle.REDSTONE, as.getLocation().clone().add(0, j*0.5, 0), 1, 0, 0, 0, 1, dustOptions);
                        }
                        for (int j = 0; j<6; j++){
                            as.getWorld().spawnParticle(Particle.REDSTONE, as.getLocation().clone().add(as.getLocation().getDirection().clone().normalize().multiply(-1)).add(rotateVector(as.getLocation().getDirection().clone(), 90).normalize().multiply(1.5-(j*0.5))), 1, 0, 0, 0, 1, dustOptions);
                        }

                        as.teleport(as.getLocation().add(v.normalize().multiply(s)));

                        for (Entity entity : as.getLocation().getChunk().getEntities()){
                            if (!as.isDead() && !hit.contains(entity)){
                                if (entity.equals(as)){
                                    continue;
                                }
                                if (as.getLocation().distanceSquared(entity.getLocation()) <= 5.5){
                                    if (!entity.equals(p)){
                                        if (entity instanceof Damageable){
                                            if (entity instanceof Player && !arenaPlayers.get(p).getGame().getAllPlayersInPlayersTeam(p).contains(entity)){
                                                if (isKillable(p ,(Player) entity)){
                                                    arenaPlayers.get(entity).damage(p, entity, arenaPlayers.get(p).getMagicDamage()*1.12+110, true);
                                                    ((Damageable) entity).damage(0.01, p);
                                                }
                                            } else {
                                                if (arenaEntities.containsKey(entity)){
                                                    arenaEntities.get(entity).damage(p, entity, arenaPlayers.get(p).getDamage()*1.12+110, true);
                                                    ((Damageable) entity).damage(0.01, p);
                                                    if (arenaEntities.get(entity).getHealth()<=0){
                                                        new OnArenaEntityKilled(p, entity);
                                                    }
                                                }
                                            }
                                            hit.add(entity);
                                        }
                                    }
                                }
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

                event.setCancelled(true);
            } else p.sendMessage(ChatColor.RED + "This item is on Cooldown for " + (float) ((int) CooldownApi.getCooldownForPlayerLong("SR", p)/100)/10 + "s.");
        }
        if (item.getItemId().equalsIgnoreCase("healing_staff")){
            if (!CooldownApi.isOnCooldown("HS", p)){
                if (arenaPlayers.get(p).getMana() >= calcBaseManaWithBonuses(40, p)){
                    CooldownApi.addCooldown("HS", p, 15);
                    arenaPlayers.get(p).setMana(arenaPlayers.get(p).getMana()-calcBaseManaWithBonuses(40, p));

                    if (arenaPlayers.get(p).getBonusHpRegen().containsKey("hs")){
                        arenaPlayers.get(p).getBonusHpRegen().replace("hs", (double) (5+2*arenaPlayers.get(p).getLevel()));
                    } else {
                        arenaPlayers.get(p).getBonusHpRegen().put("hs", (double) (5+2*arenaPlayers.get(p).getLevel()));
                    }
                    BukkitRunnable healStop = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (arenaPlayers.containsKey(p)){
                                arenaPlayers.get(p).getBonusHpRegen().replace("hs", 0.0);
                            }
                        }
                    };
                    healStop.runTaskLater(plugin, 100);

                    event.setCancelled(true);

                    return;
                } else p.sendMessage(ChatColor.RED + "You dont have enough Mana!");
            } else p.sendMessage(ChatColor.RED + "This item is on Cooldown for " + (float) ((int) CooldownApi.getCooldownForPlayerLong("HS", p)/100)/10 + "s.");
        }
        if (item.getItemId().equalsIgnoreCase("golem_sword")){
            if (arenaPlayers.get(p).getMana() >= calcBaseManaWithBonuses(140, p)){
                arenaPlayers.get(p).setMana(arenaPlayers.get(p).getMana()-calcBaseManaWithBonuses(140, p));

                double itemDamage = item.getDamage();
                double playerDamage = arenaPlayers.get(p).getDamage();

                ArrayList<Entity> blocks = new ArrayList<>();
                Random rand = new Random();
                for (int i = 0; i<20; i++){
                    Entity e = p.getWorld().spawnFallingBlock(p.getLocation().add(rand.nextInt(11)-5, 5, rand.nextInt(11)-5), new MaterialData(Material.IRON_BLOCK));
                    e.setVelocity(new Vector(0, -1, 0));
                    FallingBlock en = (FallingBlock) e;
                    en.setDropItem(false);
                    blocks.add(e);
                }
                BukkitRunnable ironPunch = new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Entity entity : p.getWorld().getNearbyEntities(p.getLocation().add(0, 1, 0), 5, 3, 5)){
                            if (!entity.equals(p)){
                                if (entity instanceof Damageable){
                                    if (entity instanceof Player && !arenaPlayers.get(p).getGame().getAllPlayersInPlayersTeam(p).contains(entity)){
                                        if (isKillable(p ,(Player) entity)){
                                            arenaPlayers.get(entity).damage(p, entity, (5+itemDamage)*(1+playerDamage/100)*3, true);
                                            ((Damageable) entity).damage(0.01, p);
                                        }
                                    } else {
                                        if (arenaEntities.containsKey(entity)){
                                            arenaEntities.get(entity).damage(p, entity, (5+itemDamage)*(1+playerDamage/100)*3, true);
                                            ((Damageable) entity).damage(0.01, p);
                                            if (arenaEntities.get(entity).getHealth()<=0){
                                                new OnArenaEntityKilled(p, entity);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        p.getWorld().spawnParticle(Particle.WHITE_ASH, p.getLocation().add(0, 0.1, 0), 1000, 3, 0.1, 3, 0.1);
                        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1F, (float) 0.8);
                        for (Entity e:blocks){
                            FallingBlock en = (FallingBlock) e;
                            if (en.getLocation().getBlock().getType().equals(Material.IRON_BLOCK)){
                                en.getLocation().getBlock().setType(Material.AIR);
                            }
                            e.remove();
                        }
                        blocks.clear();
                    }
                };
                ironPunch.runTaskLater(plugin, 7);

                event.setCancelled(true);

                return;
            } else p.sendMessage(ChatColor.RED + "You dont have enough Mana!");
        }
        if (item.getItemId().equalsIgnoreCase("aspect_of_the_end")){
            if (arenaPlayers.get(p).getMana() >= calcBaseManaWithBonuses(50, p)){
                arenaPlayers.get(p).setMana(arenaPlayers.get(p).getMana()-calcBaseManaWithBonuses(50, p));

                Location loc = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 1, p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
                if (p.getWorld().getBlockAt(loc.add(loc.getDirection().multiply(1))).isPassable()) {
                    p.teleport(loc);
                    for (int i = 0; i < 8; i++) {
                        if (p.getWorld().getBlockAt(loc.add(loc.getDirection().multiply(1))).isPassable()) {
                            p.teleport(p.getLocation().add(p.getLocation().getDirection().multiply(1)));
                        } else {
                            break;
                        }
                    }
                }
                else p.sendMessage(ChatColor.RED + "There are blocks in the way!");
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

                event.setCancelled(true);

                return;
            } else p.sendMessage(ChatColor.RED + "You dont have enough Mana!");
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

    private Vector rotateVector(Vector vector, double whatAngle) {
        double sin = Math.sin(whatAngle);
        double cos = Math.cos(whatAngle);
        double x = vector.getX() * cos + vector.getZ() * sin;
        double z = vector.getX() * -sin + vector.getZ() * cos;

        return vector.setX(x).setZ(z);
    }
}
