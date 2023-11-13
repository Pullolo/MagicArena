package net.pullolo.magicarena.events;

import net.pullolo.magicarena.items.Item;
import net.pullolo.magicarena.misc.CooldownApi;
import net.pullolo.magicarena.misc.ParticleApi;
import net.pullolo.magicarena.quests.QuestManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

import static net.pullolo.magicarena.MagicArena.*;
import static net.pullolo.magicarena.items.ArmorDefinitions.armorItems;
import static net.pullolo.magicarena.items.ArmorDefinitions.shadowweaveShroudBoots;
import static net.pullolo.magicarena.items.ItemsDefinitions.itemIds;
import static net.pullolo.magicarena.players.ArenaEntity.arenaEntities;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;
import static net.pullolo.magicarena.players.ArenaPlayer.isPlayerInGame;
import static org.bukkit.Bukkit.getServer;

public class GameAbilitiesHandler implements Listener {

    private final ArrayList<Player> shotCustomProjectile = new ArrayList<>();

    @EventHandler
    public void onProjectileLand(ProjectileHitEvent event){
        Projectile projectile = event.getEntity();
        if (projectile.hasMetadata("projectile_explode_heal")){
            projectile.getWorld().playSound(projectile.getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK, 1f, 1.2f);
            particleApi.spawnParticles(projectile.getLocation(), Particle.HEART, 50, 2, 0, 2, 2);
            particleApi.spawnParticles(projectile.getLocation(), Particle.VILLAGER_HAPPY, 50, 2, 0.5, 2, 2);
            particleApi.spawnColoredParticles(projectile.getLocation(), Color.RED, 1, 50, 2, 0, 2);
            for (Entity e : projectile.getNearbyEntities(2, 2, 2)){
                if (!(e instanceof Player)){
                    continue;
                }
                Player p = (Player) e;
                if (!arenaPlayers.containsKey(p)){
                    continue;
                }
                arenaPlayers.get(p).setHealth(arenaPlayers.get(p).getHealth()+arenaPlayers.get(p).getMaxHealth()*0.4);
                p.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, p.getLocation().add(0, 1, 0), 20, 0.2, 0.6, 0.2, 1);
                p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1, (1 + ((float) new Random().nextInt(2))/10));
            }
        }
    }

    @EventHandler
    public void onBow(EntityShootBowEvent event){
        if (!(event.getEntity() instanceof Player)){
            return;
        }
        if (event.getBow()==null) return;
        if (event.getBow().getItemMeta()==null) return;
        Item item = new Item(event.getBow());
        if (item.getItemId().equalsIgnoreCase("terminator")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onProjectile(ProjectileLaunchEvent event){
        if (!(event.getEntity().getShooter() instanceof Player)){
            return;
        }
        Player p = (Player) event.getEntity().getShooter();
        Projectile projectile = event.getEntity();
        Item item = new Item(p.getInventory().getItemInMainHand());
        if (projectile instanceof Snowball && !shotCustomProjectile.contains(p)){
            if (item.getItemId().equalsIgnoreCase("bacta_nade")){
                event.setCancelled(true);
                if (isPlayerInGame(p) && !CooldownApi.isOnCooldown("BNADE", p)){
                    CooldownApi.addCooldown("BNADE", p, 30);
                    shotCustomProjectile.add(p);
                    p.launchProjectile(Snowball.class).setMetadata("projectile_explode_heal", new FixedMetadataValue(plugin, p));
                    shotCustomProjectile.remove(p);
                } else if (CooldownApi.isOnCooldown("BNADE", p)) p.sendMessage(ChatColor.RED + "This item is on Cooldown for " + (float) ((int) CooldownApi.getCooldownForPlayerLong("BNADE", p)/100)/10 + "s.");
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event){
        Player p = event.getPlayer();
        if (!isPlayerInGame(p)){
            return;
        }
        if (!arenaPlayers.get(event.getPlayer()).getGame().hasStarted()){
            return;
        }
        if (event.getHook().getHookedEntity()==null){
            return;
        }
        Entity e = event.getHook().getHookedEntity();
        if (!arenaEntities.containsKey(e)){
            return;
        }
        if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta()==null){
            return;
        }
        Item item = new Item(event.getPlayer().getInventory().getItemInMainHand());
        if (!itemIds.contains(item.getItemId())){
            return;
        }
        if (item.getItemId().equalsIgnoreCase("scorpion_chain_dart")){
            if (e instanceof Player && !arenaPlayers.get(p).getGame().getAllPlayersInPlayersTeam(p).contains(e)){
                arenaPlayers.get(e).damage(p, e, arenaPlayers.get(p).getMagicDamage()*3+400, true);
                ((Player) e).damage(0.01, p);
            } else {
                arenaEntities.get(e).damage(p, e, arenaPlayers.get(p).getMagicDamage()*3+400,true);
                ((Damageable) e).damage(0.01, p);
                if (arenaEntities.get(e).getHealth()<=0){
                    new OnArenaEntityKilled(p, e);
                }
            }
            return;
        }
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event){
        if (!event.isSneaking()){
            return;
        }
        Player p = event.getPlayer();
        if (!isPlayerInGame(p)){
            return;
        }
        if (!arenaPlayers.get(event.getPlayer()).getGame().hasStarted()){
            return;
        }
        if (p.getInventory().getBoots()!=null && p.getInventory().getBoots().getItemMeta()!=null){
            Item boots = new Item(p.getInventory().getBoots());
            if (boots.getItemId().equals(shadowweaveShroudBoots.getItemId())){
                if (!CooldownApi.isOnCooldown("SSTE", p)){
                    if (arenaPlayers.get(p).getMana() >= calcBaseManaWithBonuses(50, p)) {
                        CooldownApi.addCooldown("SSTE", p, 1);
                        arenaPlayers.get(p).setMana(arenaPlayers.get(p).getMana()-calcBaseManaWithBonuses(50, p));

                        Location loc = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 1, p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
                        if (p.getWorld().getBlockAt(loc.add(loc.getDirection().multiply(1))).isPassable()) {
                            p.teleport(loc);
                            for (int i = 0; i < 4; i++) {
                                if (p.getWorld().getBlockAt(loc.add(loc.getDirection().multiply(1))).isPassable()) {
                                    p.teleport(p.getLocation().add(p.getLocation().getDirection().multiply(1)));
                                } else {
                                    break;
                                }
                            }
                        }
                        else p.sendMessage(ChatColor.RED + "There are blocks in the way!");
                        QuestManager.onTeleportAbility(p);
                        p.playSound(p, Sound.ENTITY_WITHER_SHOOT, 0.5f, 1.1f+(((float) new Random().nextInt(6))/10));
                        p.getWorld().spawnParticle(Particle.SMOKE_LARGE, p.getLocation(), 10, 0, 0, 0, 0.1);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event){
        if (!isPlayerInGame(event.getPlayer())){
            return;
        }
        if (!arenaPlayers.get(event.getPlayer()).getGame().hasStarted()){
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

        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
            if (item.getItemId().equalsIgnoreCase("terminator")){
                if (!CooldownApi.isOnCooldown("SHORTBOW", p)){
                    CooldownApi.addCooldown("SHORTBOW", p, 0.2);
                    double force = 2;
                    if (p.getInventory().contains(Material.ARROW, 3) || p.getGameMode().equals(GameMode.CREATIVE)){
                        if (!p.getGameMode().equals(GameMode.CREATIVE)){
                            p.getInventory().removeItem(new ItemStack(Material.ARROW, 3));
                        }
                        Arrow a1 = p.launchProjectile(Arrow.class, rotateVector(p.getLocation().getDirection().multiply(force), 0.2));
                        Arrow a2 = p.launchProjectile(Arrow.class, rotateVector(p.getLocation().getDirection().multiply(force), -0.2));
                        Arrow a3 = p.launchProjectile(Arrow.class, rotateVector(p.getLocation().getDirection().multiply(force), 0));
                        a1.setMetadata(item.getItemId(), new FixedMetadataValue(plugin, p));
                        a1.setBounce(false);
                        a2.setMetadata(item.getItemId(), new FixedMetadataValue(plugin, p));
                        a3.setMetadata(item.getItemId(), new FixedMetadataValue(plugin, p));
                        event.setCancelled(true);
                    }
                }
            }
            return;
        }

        if (!(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR))){
            return;
        }

        if (item.getItemId().equalsIgnoreCase("terminator")){
            event.setCancelled(true);
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
                return;
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
                        areaDamage(p, p.getWorld().getNearbyEntities(p.getLocation().add(0, 1, 0), 5, 3, 5), (5+itemDamage)*(1+playerDamage/100)*3, true);
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
                QuestManager.onTeleportAbility(p);
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

                event.setCancelled(true);

                return;
            } else p.sendMessage(ChatColor.RED + "You dont have enough Mana!");
        }
        if (item.getItemId().equalsIgnoreCase("leaping_sword")){
            if (!CooldownApi.isOnCooldown("LS", p)){
                if (arenaPlayers.get(p).getMana() >= calcBaseManaWithBonuses(100, p)){
                    CooldownApi.addCooldown("LS", p, 10);
                    arenaPlayers.get(p).setMana(arenaPlayers.get(p).getMana()-calcBaseManaWithBonuses(100, p));

                    double itemDamage = item.getDamage();
                    double playerDamage = arenaPlayers.get(p).getDamage();

                    p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
                    p.setVelocity(p.getLocation().getDirection().multiply(2.8));
                    List<Entity> nearbyEntites = (List<Entity>) getServer().getWorld(p.getWorld().getName()).getNearbyEntities(p.getLocation(), 8, 6, 8);

                    p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, (p.getLocation()), 4, 0.2, 0.1, 0.2, 0.3);
                    p.getWorld().spawnParticle(Particle.FLAME, (p.getLocation()), 100, 0.6, 0.6, 0.6, 0.2);

                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 255));

                    areaDamage(p, nearbyEntites, (5+itemDamage)*(1+playerDamage/100)*1.8, false);

                    event.setCancelled(true);

                    return;
                } else p.sendMessage(ChatColor.RED + "You dont have enough Mana!");
            } else p.sendMessage(ChatColor.RED + "This item is on Cooldown for " + (float) ((int) CooldownApi.getCooldownForPlayerLong("LS", p)/100)/10 + "s.");
        }
        if (item.getItemId().equalsIgnoreCase("aurora_staff")){
            if (!CooldownApi.isOnCooldown("AS", p)){
                if (arenaPlayers.get(p).getMana() >= calcBaseManaWithBonuses(10, p)){
                    CooldownApi.addCooldown("AS", p, 1);
                    arenaPlayers.get(p).setMana(arenaPlayers.get(p).getMana()-calcBaseManaWithBonuses(10, p));

                    ArmorStand as = p.getWorld().spawn(p.getLocation().add(0, 1.5, 0), ArmorStand.class, en -> {
                        en.setVisible(false);
                        en.setGravity(false);
                        en.setSmall(true);
                        en.setMarker(true);
                    });

                    Location dest = p.getLocation().add(p.getLocation().getDirection().multiply(10));
                    Vector v = dest.subtract(p.getLocation()).toVector();

                    double s = 0.3;
                    int d = 21;

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

                            Particle.DustOptions dustOptions1 = new Particle.DustOptions(Color.fromRGB(37, 112, 232), 1);
                            Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.fromRGB(54, 150, 194), 1);
                            as.getWorld().spawnParticle(Particle.REDSTONE, as.getLocation(), 1, 0, 0, 0, 1, dustOptions1);
                            as.teleport(as.getLocation().add(v.normalize().multiply(s)));
                            as.getWorld().spawnParticle(Particle.REDSTONE, as.getLocation(), 1, 0, 0, 0, 1, dustOptions2);
                            as.teleport(as.getLocation().add(v.normalize()).add((r.nextInt(3)-1)*0.1, (r.nextInt(3)-1)*0.1, (r.nextInt(3)-1)*0.1));

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
                                                        arenaPlayers.get(entity).damage(p, entity, arenaPlayers.get(p).getMagicDamage()*3+300/Math.sqrt(i), true);
                                                        ((Damageable) entity).damage(0.01, p);
                                                    }
                                                } else {
                                                    if (arenaEntities.containsKey(entity)){
                                                        arenaEntities.get(entity).damage(p, entity, arenaPlayers.get(p).getMagicDamage()*3+300/Math.sqrt(i), true);
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
            } else p.sendMessage(ChatColor.RED + "This item is on Cooldown for " + (float) ((int) CooldownApi.getCooldownForPlayerLong("AS", p)/100)/10 + "s.");
        }
        if (item.getItemId().equalsIgnoreCase("kusumibaru")){
            if (!CooldownApi.isOnCooldown("KU", p)){
                CooldownApi.addCooldown("KU", p, 6);

                double itemDamage = item.getDamage();
                double playerDamage = arenaPlayers.get(p).getDamage();

                p.setVelocity(p.getLocation().getDirection().normalize().multiply(2).setY(0.3));
                BukkitRunnable r = new BukkitRunnable() {
                    final ArrayList<Entity> alreadyDamaged = new ArrayList<>();
                    int i = 0;
                    @Override
                    public void run() {
                        i++;

                        for (Entity entity : p.getNearbyEntities(1, 1, 1)){
                            if (!entity.equals(p) && !alreadyDamaged.contains(entity)){
                                if (entity instanceof Damageable){
                                    if (entity instanceof Player && !arenaPlayers.get(p).getGame().getAllPlayersInPlayersTeam(p).contains(entity)){
                                        if (isKillable(p ,(Player) entity)){
                                            arenaPlayers.get(entity).damage(p, entity, (5+itemDamage)*(1+playerDamage/100)*5, false);
                                            ((Damageable) entity).damage(0.01, p);
                                        }
                                    } else {
                                        if (arenaEntities.containsKey(entity)){
                                            arenaEntities.get(entity).damage(p, entity, (5+itemDamage)*(1+playerDamage/100)*5, false);
                                            ((Damageable) entity).damage(0.01, p);
                                            if (arenaEntities.get(entity).getHealth()<=0){
                                                new OnArenaEntityKilled(p, entity);
                                            }
                                        }
                                    }
                                    alreadyDamaged.add(entity);
                                }
                            }
                        }

                        if (i>14){
                            cancel();
                        }
                    }
                };
                r.runTaskTimer(plugin, 0, 1);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1F, 0.8F);

                event.setCancelled(true);

                return;
            } else p.sendMessage(ChatColor.RED + "This item is on Cooldown for " + (float) ((int) CooldownApi.getCooldownForPlayerLong("KU", p)/100)/10 + "s.");
        }
        if (item.getItemId().equalsIgnoreCase("rod_of_chaos")){
            if (!CooldownApi.isOnCooldown("ROC", p)){
                if (arenaPlayers.get(p).getMana() >= calcBaseManaWithBonuses((int) (arenaPlayers.get(p).getMaxMana()/2), p)){
                    CooldownApi.addCooldown("ROC", p, 0.5);
                    arenaPlayers.get(p).setMana(arenaPlayers.get(p).getMana()-calcBaseManaWithBonuses((int) (arenaPlayers.get(p).getMaxMana()/2), p));

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

                            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(115, 0, 0), 1);
                            as.getWorld().spawnParticle(Particle.REDSTONE, as.getLocation(), 1, 0, 0, 0, 1, dustOptions);
                            as.teleport(as.getLocation().add(v.normalize().multiply(s)));

                            double damageAmount = arenaPlayers.get(p).getMagicDamage()+25;

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
                                                        arenaPlayers.get(entity).damage(p, entity, damageAmount, true);
                                                        ((Damageable) entity).damage(0.01, p);
                                                        arenaPlayers.get(p).setHealth(arenaPlayers.get(p).getHealth() + arenaPlayers.get(entity).getMaxHealth()/10);
                                                    }
                                                } else {
                                                    if (arenaEntities.containsKey(entity)){
                                                        arenaEntities.get(entity).damage(p, entity, damageAmount, true);
                                                        ((Damageable) entity).damage(0.01, p);
                                                        arenaPlayers.get(p).setHealth(arenaPlayers.get(p).getHealth() + arenaEntities.get(entity).getMaxHealth()/10);
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
            } else p.sendMessage(ChatColor.RED + "This item is on Cooldown for " + (float) ((int) CooldownApi.getCooldownForPlayerLong("ROC", p)/100)/10 + "s.");
        }
        if (item.getItemId().equalsIgnoreCase("atom_split_katana")){
            if (arenaPlayers.get(p).getMana() >= calcBaseManaWithBonuses((int) (arenaPlayers.get(p).getMaxMana()/2), p)){
                arenaPlayers.get(p).setMana(arenaPlayers.get(p).getMana()-calcBaseManaWithBonuses((int) (arenaPlayers.get(p).getMaxMana()/2), p));

                double itemDamage = item.getDamage();
                double playerDamage = arenaPlayers.get(p).getDamage();

                Location l1 = p.getLocation().clone();
                Location loc = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 1, p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
                if (p.getWorld().getBlockAt(loc.add(loc.getDirection().multiply(1))).isPassable()) {
                    p.teleport(loc);
                    for (int i = 0; i < 7; i++) {
                        if (p.getWorld().getBlockAt(loc.add(loc.getDirection().multiply(1))).isPassable()) {
                            p.teleport(p.getLocation().add(p.getLocation().getDirection().multiply(1)));
                        } else {
                            break;
                        }
                    }
                }
                QuestManager.onTeleportAbility(p);
                p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1f, 2f);
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1.2f);
                Location l2 = p.getLocation().clone();
                Color[] colors = new Color[3];
                colors[0] = Color.fromRGB(0, 145, 255);
                colors[1] = Color.fromRGB(230, 0, 255);
                colors[2] = Color.fromRGB(255, 0, 0);
                areaDamage(p, particleApi.drawColoredLine(l1, l2, 1, colors[new Random().nextInt(colors.length)], 1, 0),
                        (5+itemDamage)*(1+playerDamage/100)*12, false);

                event.setCancelled(true);

                return;
            } else p.sendMessage(ChatColor.RED + "You dont have enough Mana!");
        }
        if (item.getItemId().equalsIgnoreCase("considered_judgment")){
            if (!CooldownApi.isOnCooldown("CJ", p)){
                CooldownApi.addCooldown("CJ", p, 20);

                if (arenaPlayers.get(p).getBonusDamage().containsKey("CJ-active")){
                    arenaPlayers.get(p).getBonusDamage().remove("CJ-active");
                    arenaPlayers.get(p).updateStats();
                    arenaPlayers.get(p).getBonusDamage().put("CJ-active", arenaPlayers.get(p).getDamage()*0.18);
                } else {
                    arenaPlayers.get(p).getBonusDamage().put("CJ-active", arenaPlayers.get(p).getDamage()*0.18);
                }
                p.playSound(p, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 0.6f, 2f);
                BukkitRunnable removeBonusDamage = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (arenaPlayers.containsKey(p)){
                            arenaPlayers.get(p).getBonusDamage().remove("CJ-active");
                            p.playSound(p, Sound.ENTITY_ZOMBIE_INFECT, 1f, 1f);
                        }
                    }
                };
                removeBonusDamage.runTaskLater(plugin, 200);

                event.setCancelled(true);
                return;
            } else p.sendMessage(ChatColor.RED + "This item is on Cooldown for " + (float) ((int) CooldownApi.getCooldownForPlayerLong("CJ", p)/100)/10 + "s.");
        }
        if (item.getItemId().equalsIgnoreCase("hyperion")){
            if (arenaPlayers.get(p).getMana() >= calcBaseManaWithBonuses(300, p)){
                arenaPlayers.get(p).setMana(arenaPlayers.get(p).getMana()-calcBaseManaWithBonuses(300, p));

                double itemDamage = item.getDamage();
                double playerDamage = arenaPlayers.get(p).getDamage();
                double playerAp = arenaPlayers.get(p).getMagicDamage();
                double playerIntelligence = arenaPlayers.get(p).getMaxMana();

                Location loc = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 1, p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
                if (p.getWorld().getBlockAt(loc.add(loc.getDirection().multiply(1))).isPassable()) {
                    p.teleport(loc);
                    for (int i = 0; i < 10; i++) {
                        if (p.getWorld().getBlockAt(loc.add(loc.getDirection().multiply(1))).isPassable()) {
                            p.teleport(p.getLocation().add(p.getLocation().getDirection().multiply(1)));
                        } else {
                            break;
                        }
                    }
                }
                else p.sendMessage(ChatColor.RED + "There are blocks in the way!");
                QuestManager.onTeleportAbility(p);
                p.playSound(p.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1f, 1f);
                p.playSound(p.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1f, 1f);
                p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, (p.getLocation().add(p.getLocation().getDirection().multiply(-3))), 10, 2, 0.1, 2, 0.3);
                p.getWorld().spawnParticle(Particle.FLAME, (p.getLocation().add(p.getLocation().getDirection().multiply(-3))), 10, 0.6, 0.6, 0.6, 0.2);
                areaDamage(p, p.getNearbyEntities(6, 5, 6),
                        ((5+itemDamage)*(1+playerDamage/100)*0.02)*(playerAp+playerIntelligence*0.2), true);
            }
        }
    }

    private void areaDamage(Player damager, Collection<Entity> entities, double amount, boolean isMagic){
        for (Entity entity : entities){
            if (!entity.equals(damager)){
                if (entity instanceof Damageable){
                    if (entity instanceof Player && !arenaPlayers.get(damager).getGame().getAllPlayersInPlayersTeam(damager).contains(entity)){
                        if (isKillable(damager ,(Player) entity)){
                            arenaPlayers.get(entity).damage(damager, entity, amount, isMagic);
                            ((Damageable) entity).damage(0.01, damager);
                        }
                    } else {
                        if (arenaEntities.containsKey(entity)){
                            arenaEntities.get(entity).damage(damager, entity, amount, isMagic);
                            ((Damageable) entity).damage(0.01, damager);
                            if (arenaEntities.get(entity).getHealth()<=0){
                                new OnArenaEntityKilled(damager, entity);
                            }
                        }
                    }
                }
            }
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
        if (doesHaveFullSetBonus(damaged, "angel_armor")){
            if (!CooldownApi.isOnCooldown("ARM", damaged)){
                CooldownApi.addCooldown("ARM", damaged, 20);
                damaged.playSound(damaged, Sound.ITEM_SHIELD_BLOCK, 1, 1);
                damaged.getWorld().spawnParticle(Particle.FLASH, damaged.getLocation(), 4, 0.1, 1, 0.1);
                return false;
            }
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
