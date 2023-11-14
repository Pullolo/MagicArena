package net.pullolo.magicarena.game;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.pullolo.magicarena.MagicArena;
import net.pullolo.magicarena.items.Item;
import net.pullolo.magicarena.misc.CooldownApi;
import net.pullolo.magicarena.players.ArenaPlayer;
import net.pullolo.magicarena.quests.QuestManager;
import net.pullolo.magicarena.worlds.WorldManager;
import org.bukkit.*;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static net.pullolo.magicarena.MagicArena.*;
import static net.pullolo.magicarena.MagicArena.mainWorld;
import static net.pullolo.magicarena.items.ArmorDefinitions.armorItemIds;
import static net.pullolo.magicarena.items.ArmorDefinitions.armorItems;
import static net.pullolo.magicarena.items.ItemsDefinitions.itemIds;
import static net.pullolo.magicarena.players.ArenaEntity.arenaEntities;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;
import static org.bukkit.Bukkit.getServer;

public abstract class Game {

    public static final HashMap<World, Game> games = new HashMap<>();

    private ArrayList<Player> allPlayers;
    private final ArrayList<Entity> allEntities = new ArrayList<>();
    private ArrayList<Player> team1;
    private ArrayList<Player> team2;

    private BukkitRunnable startC;
    private BukkitRunnable gameC;
    private BukkitRunnable gameCS;
    private World world;
    private boolean started = false;

    public World createWorld(){
        String arenaName = pickRandomArena().split("_")[1];
        String newArenaName = arenaName;
        while (doesArenaExist(newArenaName)){
            newArenaName+="-";
        }
        WorldManager.copyWorld(new File(getServer().getWorldContainer().getAbsolutePath().replace(".", "") + arenaName), "temp_" + newArenaName);
        WorldManager.saveWorld(Bukkit.getWorld("temp_" + newArenaName), false, false, false); //this results in saved name being temp_ the temp param cant be true
        games.put(Bukkit.getWorld("temp_" + newArenaName), this);
        return Bukkit.getWorld("temp_" + newArenaName);
    }
    public void startNecessaryClocks(boolean test, World arena){
        BukkitRunnable gameClock1t = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : allPlayers){
                    if (p!=null && arenaPlayers.containsKey(p)){
                        updatePlayerItemStats(p, false);
                        float speed = (float) (arenaPlayers.get(p).getSpeed()/500);
                        p.setWalkSpeed(speed);
                        if (arenaPlayers.get(p).getHealth()<=0 || p.getLocation().getY() < -96){
                            //totem check
                            if (p.getInventory().getItemInMainHand().getItemMeta()!=null && p.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING) && arenaPlayers.get(p).getHealth()<=0){
                                p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                                arenaPlayers.get(p).setHealth(arenaPlayers.get(p).getMaxHealth()/20);
                                p.getWorld().spawnParticle(Particle.TOTEM, p.getLocation().add(0, 1, 0), 100, 0.1, 0, 0.1, 1);
                                p.getWorld().playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
                            } else if (p.getInventory().getItemInOffHand().getItemMeta()!=null && p.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING) && arenaPlayers.get(p).getHealth()<=0) {
                                p.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                                arenaPlayers.get(p).setHealth(arenaPlayers.get(p).getMaxHealth()/20);
                                p.getWorld().spawnParticle(Particle.TOTEM, p.getLocation().add(0, 1, 0), 100, 0.1, 0, 0.1, 1);
                                p.getWorld().playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
                            } else {
                                if (doesHaveFullSetBonus(p, "shadowweave_shroud") && !CooldownApi.isOnCooldown("SSA", p)){
                                    CooldownApi.addCooldown("SSA", p, 120);
                                    Player saver;
                                    do {
                                        saver=allPlayers.get(new Random().nextInt(allPlayers.size()));
                                    } while (saver.equals(p) && allPlayers.size()>1);
                                    p.teleport(saver);
                                    arenaPlayers.get(p).setHealth(arenaPlayers.get(p).getMaxHealth()/20);
                                    p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().add(0, 1, 0), 100, 0.1, 0.1, 0.1, 0.1);
                                    p.getWorld().playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
                                } else {
                                    playerDied(p);
                                }
                            }
                        } else {
                            arenaPlayers.get(p).updateStats();
                            try{
                                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c" + Math.round(arenaPlayers.get(p).getHealth()) + "/" + Math.round(arenaPlayers.get(p).getMaxHealth())
                                        + "❤     §a" + Math.round(arenaPlayers.get(p).getDefence()) + "❈ Defence    §b" + Math.round(arenaPlayers.get(p).getMana())
                                        + "/" + Math.round(arenaPlayers.get(p).getMaxMana()) + "✎ Mana"));
                            } catch (Exception exception){
                                exception.printStackTrace();
                            }
                            float h = (float) (arenaPlayers.get(p).getHealth() / arenaPlayers.get(p).getMaxHealth());
                            h = h * 20;
                            (p).setFoodLevel(20);
                            (p).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 255, false, false, false));
                            (p).setMaxHealth(20);
                            if (h >= 1){
                                (p).setHealth((int) h);
                            }
                            else {
                                (p).setHealth(1);
                            }
                        }
                    }
                }
                ArrayList<Entity> toDel = new ArrayList<>();
                for (Entity e : allEntities){

                    if (e!=null && arenaEntities.containsKey(e)){
                        if (arenaEntities.get(e).getHealth()<=0 || e.getLocation().getY() < -96){
                            arenaEntities.get(e).updateStats();
                            arenaEntities.remove(e);
                            ((Damageable) e).setHealth(0);
                        } else {
                            arenaEntities.get(e).updateStats();
                            ((Damageable) e).setMaxHealth(40);
                            ((Damageable) e).setHealth(40);
                        }
                    }
                }
                for (Entity en : toDel){
                    allEntities.remove(en);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (en != null){
                                en.remove();
                            }
                        }
                    }.runTaskLater(plugin, 20);
                }
                update1t();
            }
        };
        gameClock1t.runTaskTimer(MagicArena.plugin, 0, 1);
        this.gameC = gameClock1t;

        BukkitRunnable gameClock1s = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : allPlayers){
                    if (p!=null && arenaPlayers.containsKey(p)){
                        arenaPlayers.get(p).regen();
                        arenaPlayers.get(p).regenMana();
                        arenaPlayers.get(p).updateStats();
                    }
                }
                for (Entity e : allEntities){
                    if (e!=null && arenaEntities.containsKey(e)){
                        arenaEntities.get(e).regen();
                        arenaEntities.get(e).regenMana();
                        arenaEntities.get(e).updateStats();
                    }
                }

                update1s();

                if (!test){
                    if (!isTeamAlive(team1) && isTeamAlive(team2)){
                        if (startC!=null) startC.cancel();
                        gameClock1t.cancel();
                        if (team2==null) finishDungeon(allPlayers, arena, false);
                        else finishGame(team2, team1, allPlayers, arena);
                        cancel();
                        return;
                    }
                    if (isTeamAlive(team1) && !isTeamAlive(team2)){
                        if (startC!=null) startC.cancel();
                        gameClock1t.cancel();
                        finishGame(team1, team2, allPlayers, arena);
                        cancel();
                        return;
                    }
                    if (!isTeamAlive(team1) && !isTeamAlive(team2)){
                        if (startC!=null) startC.cancel();
                        gameClock1t.cancel();
                        if (team2==null) finishDungeon(allPlayers, arena, false);
                        else finishGame(null, null, allPlayers, arena);
                        cancel();
                        return;
                    }
                } else {
                    //Prevent an infinite Game
                    if (!isTeamAlive(allPlayers)){
                        for (Player p : allPlayers){
                            if (p!=null){
                                p.sendMessage(ChatColor.YELLOW + "[Warning] Due to no players being alive you will be warped in 3 seconds!");
                            }
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                forceEndGame();
                            }
                        }.runTaskLater(MagicArena.plugin, 60);
                        cancel();
                        return;
                    }
                }
            }
        };
        gameClock1s.runTaskTimer(MagicArena.plugin, 0, 20);
        this.gameCS = gameClock1s;
    }

    public void update1s(){
        //do nothing
    }

    public void update1t(){
        //do nothing
    }

    public void finishGame(ArrayList<Player> winners, ArrayList<Player> losers, ArrayList<Player> allPlayers, World world){
        if (startC!=null) startC.cancel();
        if (gameC!=null) gameC.cancel();
        if (gameCS!=null) gameCS.cancel();
        removeAllEntitiesFromGame();
        for (Player p : allPlayers){
            if (p!=null){
                arenaPlayers.remove(p);
                p.setFireTicks(0);
                p.setInvulnerable(true);
            }
        }
        if (winners == null || losers == null){
            for (Player p : allPlayers){
                if (p!=null){
                    p.sendMessage("[Arena] An error occurred in the match, neither side will be penalized for losing!");
                    arenaPlayers.remove(p);
                }
            }
            return;
        }
        for (Player p : winners){
            if (p!=null){
                p.sendMessage(ChatColor.GREEN + "[Arena] You have won this game!");
                if (winners.size()==1) QuestManager.onSoloDuelWon(p);
            }
        }
        for (Player p : losers){
            if (p!=null){
                p.sendMessage(ChatColor.RED + "[Arena] You have lost this game!");
            }
        }
        for (Player p : allPlayers){
            if (p!=null){
                p.sendMessage(ChatColor.GREEN + "You will be warped out in 5 seconds!");
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : allPlayers){
                    if (p!=null){
                        p.sendMessage(ChatColor.GREEN + "You have been warped!");
                        p.teleport(Bukkit.getWorld(mainWorld).getSpawnLocation());
                        if (p.isOp()) p.setGameMode(GameMode.CREATIVE);
                        else p.setGameMode(GameMode.SURVIVAL);
                        p.setHealth(p.getMaxHealth());
                        p.setInvulnerable(false);
                        p.setWalkSpeed(0.2f);
                    }
                }
                WorldManager.removeWorld(world);
            }
        }.runTaskLater(MagicArena.plugin, 100);
        games.remove(world);
    }

    public void finishDungeon(ArrayList<Player> allPlayers, World world, boolean won){
        if (startC!=null) startC.cancel();
        if (gameC!=null) gameC.cancel();
        if (gameCS!=null) gameCS.cancel();
        removeAllEntitiesFromGame();
        for (Player p : allPlayers){
            if (p!=null){
                arenaPlayers.remove(p);
                p.setFireTicks(0);
                p.setInvulnerable(true);
            }
        }
        for (Player p : allPlayers){
            if (p!=null){
                p.sendMessage(ChatColor.GREEN + "You will be warped out in 15 seconds!");
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : allPlayers){
                    if (p!=null){
                        p.sendMessage(ChatColor.GREEN + "You have been warped!");
                        p.teleport(Bukkit.getWorld(mainWorld).getSpawnLocation());
                        if (p.isOp()) p.setGameMode(GameMode.CREATIVE);
                        else p.setGameMode(GameMode.SURVIVAL);
                        p.setHealth(p.getMaxHealth());
                        p.setWalkSpeed(0.2f);
                        p.setInvulnerable(false);
                    }
                }
                WorldManager.removeWorld(world);
            }
        }.runTaskLater(MagicArena.plugin, 300);
        games.remove(world);
    }

    public void forceEndGame(){
        if (startC!=null) startC.cancel();
        gameC.cancel();
        gameCS.cancel();
        removeAllEntitiesFromGame();
        for (Player p : allPlayers){
            if (p!=null){
                arenaPlayers.remove(p);
                p.sendMessage(ChatColor.RED + "[Arena] This match was terminated, neither side will be penalized for losing!");
            }
        }
        for (Player p : allPlayers){
            if (p!=null){
                p.sendMessage(ChatColor.GREEN + "You have been warped!");
                p.teleport(Bukkit.getWorld(mainWorld).getSpawnLocation());
                p.setWalkSpeed(0.2f);
                if (p.isOp()) p.setGameMode(GameMode.CREATIVE);
                else p.setGameMode(GameMode.SURVIVAL);
                p.setHealth(p.getMaxHealth());
            }
        }
        if (world!=null){
            games.remove(world);
            WorldManager.removeWorld(world);
        }
    }

    public boolean isTeamAlive(ArrayList<Player> players){
        if (players==null) return true;
        for (Player p : players) {
            if (p!=null && arenaPlayers.containsKey(p)){
                return true;
            }
        }
        return false;
    }

    public abstract String pickRandomArena();

    public void playerDied(Player p){
        p.sendMessage(ChatColor.RED + "[Arena] You died!");
        arenaPlayers.remove(p);
        p.setGameMode(GameMode.SPECTATOR);
        p.teleport(new Location(p.getWorld(), 0, 65, 0));


        for (Player player : allPlayers){
            if (player!=null && !player.equals(p)){
                player.sendMessage(ChatColor.RED + "[Arena] Player " + p.getDisplayName() + " was slain!");
            }
        }

    }

    private boolean doesArenaExist(String arenaName){
        for (World w: Bukkit.getWorlds()){
            if (w.getName().equalsIgnoreCase("temp_" + arenaName)){
                return true;
            }
        }
        return false;
    }

    public void broadcast(String message){
        for (Player p : allPlayers){
            if (p!=null){
                p.sendMessage(message);
            }
        }
    }

    private void removeAllEntitiesFromGame(){
        ArrayList<Entity> toDel = new ArrayList<>();
        for (Entity e : allEntities){
            if (e != null && arenaEntities.containsKey(e)){
                if (arenaEntities.get(e).getGame().equals(this)){
                    toDel.add(e);
                }
            }
        }
        for (Entity e : toDel){
            allEntities.remove(e);
            arenaEntities.remove(e);
            e.remove();
        }
    }

    public ArrayList<Player> getAllPlayersInPlayersTeam(Player p){
        if (team1.contains(p)) return team1;
        else if (team2 !=null && team2.contains(p)) return team2;
        return new ArrayList<>();
    }

    public void updatePlayerItemStats(Player p, boolean forceUpdate){
        updatePlayerStatsItemHeld(p, forceUpdate);
        updatePlayerStatsArmorWorn(p);
    }

    private void updatePlayerStatsArmorWorn(Player p) {
        String key = "armor";
        double bonusDefence = 0;
        double bonusDamage = 0;
        double bonusAbilityDamage = 0;
        double bonusCritDamage = 0;
        double bonusCritChance = 0;
        double bonusMagicDefence = 0;
        double bonusIntelligence = 0;
        double bonusSpeed = 0;
        double bonusHealth = 0;
        double bonusManaRegen = 0;
        double bonusHealthRegen = 0;

        ItemStack helmItemStack = p.getEquipment().getHelmet();
        ItemStack chestItemStack = p.getEquipment().getChestplate();
        ItemStack legsItemStack = p.getEquipment().getLeggings();
        ItemStack bootsItemStack = p.getEquipment().getBoots();

        if (helmItemStack != null || chestItemStack != null || legsItemStack != null || bootsItemStack != null){
            if (helmItemStack!=null && helmItemStack.getItemMeta()!=null){
                Item item = new Item(helmItemStack);
                if (armorItemIds.contains(item.getItemId())){
                    bonusDefence += item.getDefence();
                    bonusDamage += item.getDamage();
                    bonusAbilityDamage += item.getAbilityPower();
                    bonusCritDamage += item.getCritDamage();
                    bonusCritChance += item.getCritChance();
                    bonusMagicDefence += item.getResistance();
                    bonusIntelligence += item.getIntelligence();
                    bonusSpeed += item.getSpeed();
                    bonusHealth += item.getHealth();
                    bonusManaRegen += item.getManaRegen();
                    bonusHealthRegen += item.getHealthRegen();
                }
            }
            if (chestItemStack!=null && chestItemStack.getItemMeta()!=null){
                Item item = new Item(chestItemStack);
                if (armorItemIds.contains(item.getItemId())){
                    bonusDefence += item.getDefence();
                    bonusDamage += item.getDamage();
                    bonusAbilityDamage += item.getAbilityPower();
                    bonusCritDamage += item.getCritDamage();
                    bonusCritChance += item.getCritChance();
                    bonusMagicDefence += item.getResistance();
                    bonusIntelligence += item.getIntelligence();
                    bonusSpeed += item.getSpeed();
                    bonusHealth += item.getHealth();
                    bonusManaRegen += item.getManaRegen();
                    bonusHealthRegen += item.getHealthRegen();
                }
            }
            if (legsItemStack!=null && legsItemStack.getItemMeta()!=null){
                Item item = new Item(legsItemStack);
                if (armorItemIds.contains(item.getItemId())){
                    bonusDefence += item.getDefence();
                    bonusDamage += item.getDamage();
                    bonusAbilityDamage += item.getAbilityPower();
                    bonusCritDamage += item.getCritDamage();
                    bonusCritChance += item.getCritChance();
                    bonusMagicDefence += item.getResistance();
                    bonusIntelligence += item.getIntelligence();
                    bonusSpeed += item.getSpeed();
                    bonusHealth += item.getHealth();
                    bonusManaRegen += item.getManaRegen();
                    bonusHealthRegen += item.getHealthRegen();
                }
            }
            if (bootsItemStack!=null && bootsItemStack.getItemMeta()!=null){
                Item item = new Item(bootsItemStack);
                if (armorItemIds.contains(item.getItemId())){
                    bonusDefence += item.getDefence();
                    bonusDamage += item.getDamage();
                    bonusAbilityDamage += item.getAbilityPower();
                    bonusCritDamage += item.getCritDamage();
                    bonusCritChance += item.getCritChance();
                    bonusMagicDefence += item.getResistance();
                    bonusIntelligence += item.getIntelligence();
                    bonusSpeed += item.getSpeed();
                    bonusHealth += item.getHealth();
                    bonusManaRegen += item.getManaRegen();
                    bonusHealthRegen += item.getHealthRegen();
                }
            }
        }
        if (doesHaveFullSetBonus(p, "superior_armor")){
            bonusDefence +=20;
            bonusDamage +=20;
            bonusAbilityDamage+=20;
            bonusCritDamage+=20;
            bonusMagicDefence+=20;
            bonusIntelligence+=20;
            bonusHealth+=20;
            bonusSpeed+=20;
        }

        if (arenaPlayers.get(p).getBonusDefence().containsKey(key)){
            arenaPlayers.get(p).getBonusDefence().replace(key, bonusDefence);
        } else {
            arenaPlayers.get(p).getBonusDefence().put(key, bonusDefence);
        }
        if (arenaPlayers.get(p).getBonusDamage().containsKey(key)){
            arenaPlayers.get(p).getBonusDamage().replace(key, bonusDamage);
        } else {
            arenaPlayers.get(p).getBonusDamage().put(key, bonusDamage);
        }
        if (arenaPlayers.get(p).getBonusCritDamage().containsKey(key)){
            arenaPlayers.get(p).getBonusCritDamage().replace(key, bonusCritDamage);
        } else {
            arenaPlayers.get(p).getBonusCritDamage().put(key, bonusCritDamage);
        }
        if (arenaPlayers.get(p).getBonusCritChance().containsKey(key)){
            arenaPlayers.get(p).getBonusCritChance().replace(key, bonusCritChance);
        } else {
            arenaPlayers.get(p).getBonusCritChance().put(key, bonusCritChance);
        }
        if (arenaPlayers.get(p).getBonusMagicDefence().containsKey(key)){
            arenaPlayers.get(p).getBonusMagicDefence().replace(key, bonusMagicDefence);
        } else {
            arenaPlayers.get(p).getBonusMagicDefence().put(key, bonusMagicDefence);
        }
        if (arenaPlayers.get(p).getBonusMaxMana().containsKey(key)){
            arenaPlayers.get(p).getBonusMaxMana().replace(key, bonusIntelligence);
        } else {
            arenaPlayers.get(p).getBonusMaxMana().put(key, bonusIntelligence);
        }
        if (arenaPlayers.get(p).getBonusSpeed().containsKey(key)){
            arenaPlayers.get(p).getBonusSpeed().replace(key, bonusSpeed);
        } else {
            arenaPlayers.get(p).getBonusSpeed().put(key, bonusSpeed);
        }
        if (arenaPlayers.get(p).getBonusMaxHealth().containsKey(key)){
            arenaPlayers.get(p).getBonusMaxHealth().replace(key, bonusHealth);
        } else {
            arenaPlayers.get(p).getBonusMaxHealth().put(key, bonusHealth);
        }
        if (arenaPlayers.get(p).getBonusMagicDamage().containsKey(key)){
            arenaPlayers.get(p).getBonusMagicDamage().replace(key, bonusAbilityDamage);
        } else {
            arenaPlayers.get(p).getBonusMagicDamage().put(key, bonusAbilityDamage);
        }
        if (arenaPlayers.get(p).getBonusManaRegen().containsKey(key)){
            arenaPlayers.get(p).getBonusManaRegen().replace(key, bonusManaRegen);
        } else {
            arenaPlayers.get(p).getBonusManaRegen().put(key, bonusManaRegen);
        }
        if (arenaPlayers.get(p).getBonusHpRegen().containsKey(key)){
            arenaPlayers.get(p).getBonusHpRegen().replace(key, bonusHealthRegen);
        } else {
            arenaPlayers.get(p).getBonusHpRegen().put(key, bonusHealthRegen);
        }
    }

    private void updatePlayerStatsItemHeld(Player p, boolean force){
        ArenaPlayer player = arenaPlayers.get(p);
        ItemStack heldItem = p.getInventory().getItemInMainHand();
        ItemStack lastHeldItemStack = player.getLastHeldItemStack();

        if (lastHeldItemStack!=null && !force){
            if (lastHeldItemStack.equals(heldItem)){
                return;
            }
        }

        double prevBonusHp, prevBonusMana, newBonusHp = 0, newBonusMana = 0, healthPerc, manaPerc;
        String key = "itemHeld";
        String cjKey = "cj-bonus";

        manaPerc = player.getMana()/player.getMaxMana();
        if (player.getBonusMaxMana().containsKey(key)) {
            prevBonusMana = player.getBonusMaxMana().get(key);
        } else prevBonusMana = 0;
        healthPerc = player.getHealth()/player.getMaxHealth();
        if (player.getBonusMaxHealth().containsKey(key)){
            prevBonusHp = player.getBonusMaxHealth().get(key);
        } else prevBonusHp = 0;

        if (heldItem.getItemMeta() != null && itemIds.contains(new Item(heldItem).getItemId())){
            //item calcs

            if (new Item(heldItem).getItemId().equalsIgnoreCase("considered_judgment")){ //bron navii
                if (player.getBonusDamage().containsKey(cjKey)){
                    player.getBonusDamage().remove(cjKey);
                    double val = player.getDamage()*0.2;
                    player.getBonusDamage().put(cjKey, val);
                } else {
                    double val = player.getDamage()*0.2;
                    player.getBonusDamage().put(cjKey, val);
                }
            } else player.getBonusDamage().remove(cjKey);
            //rest of the calcs
            if (player.getBonusDefence().containsKey(key)){
                player.getBonusDefence().replace(key, new Item(heldItem).getDefence());
            }else {
                player.getBonusDefence().put(key, new Item(heldItem).getDefence());
            }
            if (player.getBonusMaxMana().containsKey(key)){
                player.getBonusMaxMana().replace(key, new Item(heldItem).getIntelligence());
            }else {
                player.getBonusMaxMana().put(key, new Item(heldItem).getIntelligence());
            }
            newBonusMana = player.getBonusMaxMana().get(key);
            if (player.getBonusSpeed().containsKey(key)){
                player.getBonusSpeed().replace(key, new Item(heldItem).getSpeed());
            }else {
                player.getBonusSpeed().put(key, new Item(heldItem).getSpeed());
            }
            if (player.getBonusCritDamage().containsKey(key)){
                player.getBonusCritDamage().replace(key, new Item(heldItem).getCritDamage());
            }else {
                player.getBonusCritDamage().put(key, new Item(heldItem).getCritDamage());
            }
            if (player.getBonusCritChance().containsKey(key)){
                player.getBonusCritChance().replace(key, new Item(heldItem).getCritChance());
            }else {
                player.getBonusCritChance().put(key, new Item(heldItem).getCritChance());
            }
            if (player.getBonusMaxHealth().containsKey(key)){
                player.getBonusMaxHealth().replace(key, new Item(heldItem).getHealth());
            }else {
                player.getBonusMaxHealth().put(key, new Item(heldItem).getHealth());
            }
            newBonusHp = player.getBonusMaxHealth().get(key);
            if (player.getBonusMagicDamage().containsKey(key)){
                player.getBonusMagicDamage().replace(key, new Item(heldItem).getAbilityPower());
            }else {
                player.getBonusMagicDamage().put(key, new Item(heldItem).getAbilityPower());
            }
            if (player.getBonusMagicDefence().containsKey(key)){
                player.getBonusMagicDefence().replace(key, new Item(heldItem).getResistance());
            }else {
                player.getBonusMagicDefence().put(key, new Item(heldItem).getResistance());
            }
            if (player.getBonusManaRegen().containsKey(key)){
                player.getBonusManaRegen().replace(key, new Item(heldItem).getManaRegen());
            }else {
                player.getBonusManaRegen().put(key, new Item(heldItem).getManaRegen());
            }
            if (player.getBonusHpRegen().containsKey(key)){
                player.getBonusHpRegen().replace(key, new Item(heldItem).getHealthRegen());
            }else {
                player.getBonusHpRegen().put(key, new Item(heldItem).getHealthRegen());
            }
        } else {
            player.getBonusDamage().remove(cjKey);
            //rest
            if (player.getBonusDefence().containsKey(key)){
                player.getBonusDefence().replace(key, 0.0);
            }else {
                player.getBonusDefence().put(key, 0.0);
            }
            if (player.getBonusMaxMana().containsKey(key)){
                player.getBonusMaxMana().replace(key, 0.0);
            }else {
                player.getBonusMaxMana().put(key, 0.0);
            }
            if (player.getBonusSpeed().containsKey(key)){
                player.getBonusSpeed().replace(key, 0.0);
            }else {
                player.getBonusSpeed().put(key, 0.0);
            }
            if (player.getBonusCritDamage().containsKey(key)){
                player.getBonusCritDamage().replace(key, 0.0);
            }else {
                player.getBonusCritDamage().put(key, 0.0);
            }
            if (player.getBonusCritChance().containsKey(key)){
                player.getBonusCritChance().replace(key, 0.0);
            }else {
                player.getBonusCritChance().put(key, 0.0);
            }
            if (player.getBonusMaxHealth().containsKey(key)){
                player.getBonusMaxHealth().replace(key, 0.0);
            }else {
                player.getBonusMaxHealth().put(key, 0.0);
            }
            if (player.getBonusMagicDamage().containsKey(key)){
                player.getBonusMagicDamage().replace(key, 0.0);
            }else {
                player.getBonusMagicDamage().put(key, 0.0);
            }
            if (player.getBonusMagicDefence().containsKey(key)){
                player.getBonusMagicDefence().replace(key, 0.0);
            }else {
                player.getBonusMagicDefence().put(key, 0.0);
            }
            if (player.getBonusManaRegen().containsKey(key)){
                player.getBonusManaRegen().replace(key, 0.0);
            }else {
                player.getBonusManaRegen().put(key, 0.0);
            }
            if (player.getBonusHpRegen().containsKey(key)){
                player.getBonusHpRegen().replace(key, 0.0);
            }else {
                player.getBonusHpRegen().put(key, 0.0);
            }
        }
        player.updateStats();
        if (prevBonusHp!=newBonusHp) player.setHealth(player.getMaxHealth()*healthPerc);
        if (prevBonusMana!=newBonusMana) player.setMana(player.getMaxMana()*manaPerc);
        player.setLastHeldItemStack(heldItem);
    }

    public void addEntity(Entity e){
        allEntities.add(e);
    }
    public ArrayList<Entity> getAllEntities(){
        return allEntities;
    }
    public void removeEntity(Entity e){
        allEntities.remove(e);
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld(){
        return this.world;
    }

    public void setTeam1(ArrayList<Player> team1) {
        this.team1 = team1;
    }

    public void setTeam2(ArrayList<Player> team2) {
        this.team2 = team2;
    }

    public void setAllPlayers(ArrayList<Player> allPlayers) {
        this.allPlayers = allPlayers;
    }
    public ArrayList<Player> getAllPlayers(){
        return this.allPlayers;
    }

    public void setStartC(BukkitRunnable startC) {
        this.startC = startC;
    }

    public boolean hasStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
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
