package net.pullolo.magicarena.players;

import net.pullolo.magicarena.game.ArenaGame;
import net.pullolo.magicarena.game.Game;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;

public class ArenaEntity extends ArenaEntityBlueprint{

    private final Game game;
    private final Entity entity;
    private final boolean isTester;
    private boolean loaded = true;

    public static HashMap<Entity, ArenaEntity> arenaEntities = new HashMap<>();

    public ArenaEntity(Entity entity, int level, Game game, boolean isTester){
        if (entity instanceof LivingEntity){
            ((LivingEntity) entity).setRemoveWhenFarAway(false);
        }
        if (arenaEntities.containsKey(entity)){
            throw new RuntimeException("Cannot convert entity that is already converted!");
        } else arenaEntities.put(entity, this);
        this.game = game;
        this.entity = entity;
        this.isTester = isTester;
        game.addEntity(entity);
        setLevel(level);
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
        setBaseCritChance(10);
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
