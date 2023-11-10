package net.pullolo.magicarena.players;

import net.pullolo.magicarena.game.ArenaGame;
import net.pullolo.magicarena.game.Game;
import net.pullolo.magicarena.game.GameWorld;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attributable;
import org.bukkit.entity.*;

import java.util.HashMap;
import java.util.Random;

import static net.pullolo.magicarena.MagicArena.getLog;
import static org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH;

public class ArenaEntity extends ArenaEntityBlueprint{

    private final Game game;
    private final Entity entity;
    private final boolean isTester;
    private boolean loaded = true;

    private double originalMobMaxHealth;

    public static HashMap<Entity, ArenaEntity> arenaEntities = new HashMap<>();

    public ArenaEntity(Entity entity, int level, Game game, boolean isTester){
        if (entity instanceof LivingEntity && !(game instanceof GameWorld)){
            ((LivingEntity) entity).setRemoveWhenFarAway(false);
        }
        if (arenaEntities.containsKey(entity)){
            throw new RuntimeException("Cannot convert entity that is already converted!");
        } else arenaEntities.put(entity, this);
        this.game = game;
        this.entity = entity;
        this.isTester = isTester;
        try {
            originalMobMaxHealth = ((Attributable) entity).getAttribute(GENERIC_MAX_HEALTH).getDefaultValue();
        } catch (Exception e){
            originalMobMaxHealth = 20;
        }
        game.addEntity(entity);
        int levelOffset = 0;
        //handle vanilla mobs levels
        if (entity instanceof Boss || entity instanceof Warden){
            levelOffset+=50+level*10;
            level=0;
        } else {
            if (game instanceof GameWorld){
                levelOffset+=new Random().nextInt(5);
            }
        }
        setLevel(level+levelOffset);
        updateStats();
        respawn();
    }

    @Override
    public void regen() {
        if (getHealth()>getMaxHealth()){
            setHealth(getMaxHealth());
        } else {
            setHealth(getHealth() + getHpRegen());
        }
    }

    @Override
    public void regenMana() {
        if (getMana()>getMaxMana()){
            setMana(getMaxMana());
        } else {
            setMana(getMana() + getManaRegen());
        }
    }

    @Override
    public void respawn() {
        setMana(getMaxMana());
        setHealth(getMaxHealth());
    }

    public void fixedUpdateStats(){
        setBaseMaxHealth(90+Math.pow(getLevel(), 2));
        setBaseMaxMana(80+getLevel()*20);
        setBaseManaRegen(getLevel());
        if (isTester) setBaseHpRegen(getMaxHealth());
        else setBaseHpRegen(getLevel()/20+1);
        setBaseDefence(getLevel()*2);
        setBaseMagicDefence(getLevel()*1.5);
        setBaseDamage((getLevel()/2+1)*5+getLevel());
        setBaseCritDamage(getLevel()*5+20);
        setBaseCritChance(7+(getLevel()/3));
        setBaseMagicDamage(getLevel()*2);
        setBaseSpeed(100);
    }

    @Override
    public void updateStats() {
        if (!loaded){
            return;
        }
        fixedUpdateStats();
        performChecksAndCalc();
        updateName();
    }

    public void save(){
        try {
            ((Attributable) entity).getAttribute(GENERIC_MAX_HEALTH).setBaseValue(originalMobMaxHealth);
        } catch (Exception e){
            getLog().warning("Couldn't restore health to " + entity.getType() + "!");
        }
    }

    public void updateName(){
        entity.setCustomNameVisible(true);
        entity.setCustomName(ChatColor.translateAlternateColorCodes('&', "&7[Lv" + getLevel() + "] &f" + entity.getType().toString().toLowerCase().replace("_", " ") + " &c" + (int) getHealth() + "&7/&c" + (int) getMaxHealth()));
    }

    public Game getGame() {
        return game;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
