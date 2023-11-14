package net.pullolo.magicarena.events;

import net.pullolo.magicarena.game.GameWorld;
import net.pullolo.magicarena.items.Item;
import net.pullolo.magicarena.misc.CooldownApi;
import net.pullolo.magicarena.misc.ParticleApi;
import net.pullolo.magicarena.players.ArenaPlayer;
import net.pullolo.magicarena.players.DungeonEntity;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

import static net.pullolo.magicarena.MagicArena.*;
import static net.pullolo.magicarena.items.ArmorDefinitions.armorItemIds;
import static net.pullolo.magicarena.items.ArmorDefinitions.armorItems;
import static net.pullolo.magicarena.items.ItemsDefinitions.getItemFromPlayer;
import static net.pullolo.magicarena.players.ArenaEntity.arenaEntities;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;
import static net.pullolo.magicarena.players.ArenaPlayer.isPlayerInGame;

public class GameDamageHandler implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (event.isCancelled()){
            return;
        }
        if (event instanceof EntityDamageByEntityEvent){
            if (((EntityDamageByEntityEvent) event).getDamager() instanceof Player && isPlayerInGame((Player) ((EntityDamageByEntityEvent) event).getDamager()) && !(arenaPlayers.get((Player) ((EntityDamageByEntityEvent) event).getDamager()).getGame() instanceof GameWorld)){
                if (event.getEntity() instanceof Hanging){
                    event.setCancelled(true);
                    return;
                }
            }
        }
        if (event.getDamage()<0.1){
            return;
        }
        if (!(event.getEntity() instanceof Player)){
            if (event instanceof EntityDamageByEntityEvent){
                onEntityDamagedByEntity((EntityDamageByEntityEvent) event);
                return;
            }
            onEntityDamage(event.getEntity(), event.getCause(), event.getDamage());
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
        if (arenaPlayers.get(damaged).isDead()){
            return;
        }

        if (event instanceof EntityDamageByEntityEvent){
            if (event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)){
                return;
            }
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)){
                if (damager instanceof Firework){
                    damager = (Entity) ((Firework) damager).getShooter();
                }
            }
            if (!(damager instanceof Player)){
                if (!arenaEntities.containsKey(damager)){
                    if (damager instanceof EvokerFangs){
                        if (!arenaEntities.containsKey(((EvokerFangs) damager).getOwner())){
                            return;
                        }
                        arenaPlayers.get(damaged).damage(((EvokerFangs) damager).getOwner(), damaged, calculateDamage(event.getDamage(), ((EvokerFangs) damager).getOwner(), damaged), true);
                    }
                    return;
                }
            } else if (arenaPlayers.get((Player) damager).getGame().getAllPlayersInPlayersTeam((Player) damager).contains(damaged)){
                event.setCancelled(true);
                return;
            }
            if (doesHaveFullSetBonus(damaged, "angel_armor")){
                if (!CooldownApi.isOnCooldown("ARM", damaged)){
                    CooldownApi.addCooldown("ARM", damaged, 20);
                    damaged.playSound(damaged, Sound.ITEM_SHIELD_BLOCK, 1, 1);
                    damaged.getWorld().spawnParticle(Particle.FLASH, damaged.getLocation(), 4, 0.1, 1, 0.1);
                    event.setCancelled(true);
                    return;
                }
            }
            arenaPlayers.get(damaged).damage(damager, damaged, calculateDamage(event.getDamage(), damager, damaged), false);
            return;
        }
        if (doesHaveFullSetBonus(damaged, "angel_armor")){
            if (!CooldownApi.isOnCooldown("ARM", damaged)){
                CooldownApi.addCooldown("ARM", damaged, 20);
                damaged.playSound(damaged, Sound.ITEM_SHIELD_BLOCK, 1, 1);
                damaged.getWorld().spawnParticle(Particle.FLASH, damaged.getLocation(), 4, 0.1, 1, 0.1);
                event.setCancelled(true);
                return;
            }
        }
        if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) arenaPlayers.get(damaged).damage(damaged, event.getDamage()*5, false);
        else arenaPlayers.get(damaged).trueDamage(damaged, arenaPlayers.get(damaged).getMaxHealth()/20);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event){
        if (event.isCancelled()){
            return;
        }
        if (event.getEntity().getShooter() instanceof Player && isPlayerInGame((Player) event.getEntity().getShooter())){
            if (event.getHitEntity() instanceof Hanging){
                event.setCancelled(true);
                return;
            }
        }
        if (!(event.getHitEntity() instanceof Player)){
            onEntityProjectileDamage(event);
            tryRemoveProjectile(event.getEntity());
            return;
        }
        Player damaged = (Player) event.getHitEntity();
        if (!(event.getEntity().getShooter() instanceof Player)){
            if (doesHaveFullSetBonus(damaged, "angel_armor")){
                if (!CooldownApi.isOnCooldown("ARM", damaged)){
                    CooldownApi.addCooldown("ARM", damaged, 20);
                    damaged.playSound(damaged, Sound.ITEM_SHIELD_BLOCK, 1, 1);
                    damaged.getWorld().spawnParticle(Particle.FLASH, damaged.getLocation(), 4, 0.1, 1, 0.1);
                    event.setCancelled(true);
                    return;
                }
            }
            onProjectileDamagePlayer(event);
            tryRemoveProjectile(event.getEntity());
            return;
        }
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
        if (doesHaveFullSetBonus(damaged, "angel_armor")){
            if (!CooldownApi.isOnCooldown("ARM", damaged)){
                CooldownApi.addCooldown("ARM", damaged, 20);
                damaged.playSound(damaged, Sound.ITEM_SHIELD_BLOCK, 1, 1);
                damaged.getWorld().spawnParticle(Particle.FLASH, damaged.getLocation(), 4, 0.1, 1, 0.1);
                event.setCancelled(true);
                return;
            }
        }

        arenaPlayers.get(damaged).damage(damager, damaged, calculateProjectileDamage(damager, damaged), false);
        tryRemoveProjectile(event.getEntity());
    }

    private void tryRemoveProjectile(Projectile projectile) {
        if (projectile instanceof Arrow && projectile.hasMetadata("terminator")){
            projectile.remove();
        }
    }

    public void onEntityProjectileDamage(ProjectileHitEvent event){
        if (event.getEntity() instanceof FishHook){
            return;
        }
        Entity damaged = event.getHitEntity();
        Entity damager = (Entity) event.getEntity().getShooter();

        if (damaged instanceof EnderDragonPart){
            damaged = ((EnderDragonPart) damaged).getParent();
        }
        if (!(arenaEntities.containsKey(damaged))){
            return;
        }

        if (damaged instanceof Shulker){
            if (((Shulker) damaged).getPeek()<=0){
                return;
            }
        }
        if (damaged instanceof Enderman){
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

    public void onEntityDamage(Entity damaged, EntityDamageEvent.DamageCause damageCause, double damage){
        if (damaged instanceof EnderDragonPart){
            damaged = ((EnderDragonPart) damaged).getParent();
        }
        if (!arenaEntities.containsKey(damaged)){
            return;
        }
        if (damageCause.equals(EntityDamageEvent.DamageCause.FALL)) arenaEntities.get(damaged).damage(damaged, damage*5, false);
        else arenaEntities.get(damaged).trueDamage(damaged, arenaEntities.get(damaged).getMaxHealth()/20);
        if (arenaEntities.get(damaged).getHealth()<=0){
            new OnArenaEntityKilled(damaged);
        }
    }

    public void onEntityDamagedByEntity(EntityDamageByEntityEvent event){
        Entity damaged = event.getEntity();
        if (damaged instanceof EnderDragonPart){
            damaged = ((EnderDragonPart) damaged).getParent();
        }
        if (!arenaEntities.containsKey(damaged)){
            return;
        }
        if (event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)){
            return;
        }
        Entity damager = event.getDamager();
        if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)){
            if (damager instanceof Firework){
                damager = (Entity) ((Firework) damager).getShooter();
            }
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

        arenaEntities.get(damaged).damage(damager, damaged, calculateDamage(event.getDamage(), damager, damaged), false);

        if (damager instanceof Player){
            checkIfKilled((Player) damager, damaged);
        }
    }

    private double calculateDamage(double eventDamage, Entity damager, Entity damaged){
        if (!(damager instanceof Player)){
            double entityDamage = arenaEntities.get(damager).getDamage();
            return entityDamage;
        }
        double playerDamage = arenaPlayers.get(damager).getDamage();
        if (((Player) damager).getInventory().getItemInMainHand().getItemMeta()==null || armorItemIds.contains(getItemFromPlayer(((Player) damager).getInventory().getItemInMainHand()).getItemId())){
            return 5*(1+playerDamage/100);
        }
        Item playersItem = getItemFromPlayer(((Player) damager).getInventory().getItemInMainHand());
        Double itemDamage = playersItem.getDamage();
        if (itemDamage==0) return (5*(1+playerDamage/100))+eventDamage*5;
        if (playersItem.getItemId().equalsIgnoreCase("leeching_staff")){
            arenaPlayers.get(damager).setHealth(arenaPlayers.get(damager).getHealth()+2);
            damager.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, damager.getLocation().add(0, 1, 0), 20, 0.2, 0.6, 0.2, 1);
            ((Player) damager).playSound(damager, Sound.ENTITY_PLAYER_LEVELUP, 1, (1 + ((float) new Random().nextInt(2))/10));
            //easter egg
            if(((Player) damager).getDisplayName().equalsIgnoreCase("yaemikujo") && new Random().nextInt(50)==0) damager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cKocham cię Słonko &7<&5od &aMikołaja&5 <3&7>"));
        }
        if (playersItem.getItemId().equalsIgnoreCase("undead_sword") && (damaged instanceof Zombie || damaged instanceof Skeleton)){
            return (5+itemDamage)*(1+playerDamage/100)*2;
        }
        if (playersItem.getItemId().equalsIgnoreCase("brutality_blade")){
            if (damaged instanceof Player){
                if (arenaPlayers.get(damaged).getHealth()<arenaPlayers.get(damaged).getMaxHealth()/2){
                    return (5+itemDamage)*(1+playerDamage/100)*2;
                }
            } else {
                if (arenaEntities.get(damaged).getHealth()<arenaEntities.get(damaged).getMaxHealth()/2){
                    return (5+itemDamage)*(1+playerDamage/100)*2;
                }
            }
        }
        if (playersItem.getItemId().equalsIgnoreCase("priscillas_dagger")){
            if (isBehind(damager, damaged)){
                return (5+itemDamage)*(1+playerDamage/100)*2;
            }
        }
        if (playersItem.getItemId().equalsIgnoreCase("atom_split_katana")){
            Random r = new Random();
            if (r.nextBoolean()){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Color[] colors = new Color[3];
                        colors[0] = Color.fromRGB(0, 145, 255);
                        colors[1] = Color.fromRGB(230, 0, 255);
                        colors[2] = Color.fromRGB(255, 0, 0);
                        if (damaged==null) return;
                        if (damaged.isDead()) return;
                        if (!arenaEntities.containsKey(damaged)) return;
                        if (arenaEntities.get(damaged).getHealth() <= 0) return;
                        ((Damageable) damaged).damage(0.01);
                        arenaEntities.get(damaged).damage(damager, damaged, (5+itemDamage)*(1+playerDamage/100)*(r.nextFloat()+1), false);
                        double rx = 0.2+r.nextFloat()*0.3;
                        double rz = 0.2+r.nextFloat()*0.3;
                        Location l1;
                        Location l2;
                        if (r.nextBoolean()){
                            l1 = damaged.getLocation().clone().add(rx, 2, rz);
                            l2 = damaged.getLocation().clone().add(-rx, 0, -rz);
                        } else {
                            l1 = damaged.getLocation().clone().add(-rx, 2, -rz);
                            l2 = damaged.getLocation().clone().add(rx, 0, rz);
                        }
                        particleApi.drawColoredLine(l1, l2, 1, colors[r.nextInt(colors.length)], 1, 0);
                    }
                }.runTaskLater(plugin, 1);
            }
        }
        if (playersItem.getItemId().equalsIgnoreCase("terminator")) return (5+1)*(1+playerDamage/100); //prevent term from dealing melee damage
        return (5+itemDamage)*(1+playerDamage/100);
    }

    private double calculateProjectileDamage(Entity damager, Entity damaged){
        if (!(damager instanceof Player)){
            double entityDamage = arenaEntities.get(damager).getDamage();
            return entityDamage;
        }
        double playerDamage = arenaPlayers.get(damager).getDamage();
        Item playerItem = getItemFromPlayer(((Player) damager).getInventory().getItemInMainHand());
        Double itemDamage = playerItem.getDamage();
        if (itemDamage==0) return (5*(1+playerDamage/100))+5*5;
        if (((Player) damager).getInventory().getItemInMainHand().getItemMeta()==null || armorItemIds.contains(getItemFromPlayer(((Player) damager).getInventory().getItemInMainHand()).getItemId())){
            return 5*(1+playerDamage/100);
        }
        //todo add scaling for weapons and stuff
        if (playerItem.getItemId().equalsIgnoreCase("flaming_bow")){
            damaged.setFireTicks(60);
        }

        return (5+itemDamage)*(1+playerDamage/100);
    }

    private void checkIfKilled(Player p, Entity e){
        if (arenaEntities.get(e).getHealth()<=0){
            new OnArenaEntityKilled(p, e);
        }
    }

    private boolean isBehind(Entity damager, Entity damaged){
        Vector pDir = damager.getLocation().getDirection();
        Vector eDir = damaged.getLocation().getDirection();
        double xv = pDir.getX() * eDir.getZ() - pDir.getZ() * eDir.getX();
        double zv = pDir.getX() * eDir.getX() + pDir.getZ() * eDir.getZ();
        double angle = Math.atan2(xv, zv); // Value between -π and +π
        double angleInDegrees = (angle * 180) / Math.PI;

        return angleInDegrees >= -32 && angleInDegrees <= 60;
    }

    private boolean doesHaveFullSetBonus(Player p, String armorSet){
        ItemStack helmetItem = p.getInventory().getHelmet();
        ItemStack chestplateItem = p.getInventory().getChestplate();
        ItemStack leggingsItem = p.getInventory().getLeggings();
        ItemStack bootsItem = p.getInventory().getBoots();

        if (helmetItem==null || helmetItem.getItemMeta()==null){
            return false;
        }
        if (chestplateItem==null || chestplateItem.getItemMeta()==null){
            return false;
        }
        if (leggingsItem==null || leggingsItem.getItemMeta()==null){
            return false;
        }
        if (bootsItem==null || bootsItem.getItemMeta()==null){
            return false;
        }

        Item helmet = new Item(helmetItem);
        Item chestplate = new Item(chestplateItem);
        Item leggings = new Item(leggingsItem);
        Item boots = new Item(bootsItem);

        for (Item i : armorItems.get(armorSet)){
            if (i.getItemId().equalsIgnoreCase(helmet.getItemId())
            || i.getItemId().equalsIgnoreCase(chestplate.getItemId())
            || i.getItemId().equalsIgnoreCase(leggings.getItemId())
            || i.getItemId().equalsIgnoreCase(boots.getItemId())){
                continue;
            }
            return false;
        }
        return true;
    }
}
