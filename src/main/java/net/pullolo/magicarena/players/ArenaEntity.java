package net.pullolo.magicarena.players;

import net.pullolo.magicarena.misc.DamageIndicator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Random;

import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;

public abstract class ArenaEntity {

    private String entityType;
    private boolean isUndead;
    private String name;
    private String customName;

    private double health, mana;

    private int level;

    private double baseMaxHealth, maxHealth;
    private HashMap<String, Double> bonusMaxHealth = new HashMap<>();

    private double baseHpRegen, hpRegen;
    private HashMap<String, Double> bonusHpRegen = new HashMap<>();

    private double baseMaxMana, maxMana;
    private HashMap<String, Double> bonusMaxMana = new HashMap<>();
    private double baseManaRegen, manaRegen;
    private HashMap<String, Double> bonusManaRegen = new HashMap<>();

    private double baseDefence, defence;
    private HashMap<String, Double> bonusDefence = new HashMap<>();

    private double baseMagicDefence, magicDefence;
    private HashMap<String, Double> bonusMagicDefence = new HashMap<>();

    private double baseDamage, damage;
    private HashMap<String,Double> bonusDamage = new HashMap<>();

    private double baseCritDamage, critDamage;
    private HashMap<String,Double> bonusCritDamage = new HashMap<>();

    private double baseCritChance, critChance;
    private HashMap<String,Double> bonusCritChance = new HashMap<>();

    private double baseMagicDamage, magicDamage;
    private HashMap<String,Double> bonusMagicDamage = new HashMap<>();

    private double baseSpeed, speed;
    private HashMap<String,Double> bonusSpeed = new HashMap<>();

    public abstract void regen();
    public abstract void regenMana();
    public void damage(Player damager, Player damaged, double amou, boolean isMagic){
        boolean isCrit = rollForCrit(arenaPlayers.get(damager).getCritChance());

        double amount;
        double effectiveHpDef = getHealth() * (1 + getDefence()/100);
        double effectiveHpRes = getHealth() * (1 + getMagicDefence()/100);
        if (isCrit){
            amount = amou * ((100+arenaPlayers.get(damager).getCritDamage())/100);
        } else amount = amou;

        if (isMagic){
            amount = (amount*(getHealth()/effectiveHpRes));
        } else {
            amount = (amount*(getHealth()/effectiveHpDef));
        }
        setHealth(getHealth() - amount);

        new DamageIndicator(damaged, amount, isCrit);
    }
    public void damage(Player damaged, double amount, boolean isMagic){
        double effectiveHpDef = getHealth() * (1 + getDefence()/100);
        double effectiveHpRes = getHealth() * (1 + getMagicDefence()/100);

        if (isMagic){
            amount = (amount*(getHealth()/effectiveHpRes));
        } else {
            amount = (amount*(getHealth()/effectiveHpDef));
        }
        setHealth(getHealth() - amount);
        new DamageIndicator(damaged, amount, false);
    }
    public void trueDamage(Player damaged, double amount){
        setHealth(getHealth() - amount);
        new DamageIndicator(damaged, amount, false);
    }
    public abstract void respawn();
    public abstract void updateStats();

    public static boolean rollForCrit(double critChance){
        if (critChance<=0){
            return false;
        } else return (new Random().nextInt(100) <= critChance);
    }

    public void calcStats(){
        calcMaxHealth();
        calcMaxMana();
        calcHealthRegen();
        calcManaRegen();
        calcDefence();
        calcMagicDefence();
        calcDamage();
        calcCritDamage();
        calcCritChance();
        calcMagicDamage();
        calcSpeed();
    }
    private void calcMaxHealth(){
        double h = 0;
        for (Double i:bonusMaxHealth.values()){
            if (i != null){
                h+=i;
            }
        }
        maxHealth = baseMaxHealth + h;
    }
    private void calcMaxMana(){
        double h = 0;
        for (Double i:bonusMaxMana.values()){
            if (i != null){
                h+=i;
            }
        }
        maxMana = baseMaxMana + h;
    }
    private void calcHealthRegen(){
        double h = 0;
        for (Double i:bonusHpRegen.values()){
            if (i != null){
                h+=i;
            }
        }
        hpRegen = baseHpRegen + h;
    }
    private void calcManaRegen(){
        double h = 0;
        for (Double i:bonusManaRegen.values()){
            if (i != null){
                h+=i;
            }
        }
        manaRegen = baseManaRegen + h;
    }
    private void calcDefence(){
        double h = 0;
        for (Double i:bonusDefence.values()){
            if (i != null){
                h+=i;
            }
        }
        defence = baseDefence + h;
    }
    private void calcMagicDefence(){
        double h = 0;
        for (Double i:bonusMagicDefence.values()){
            if (i != null){
                h+=i;
            }
        }
        magicDefence = baseMagicDefence + h;
    }

    private void calcDamage(){
        double h = 0;
        for (Double i:bonusDamage.values()){
            if (i != null){
                h+=i;
            }
        }
        damage = baseDamage + h;
    }
    private void calcCritDamage(){
        double h = 0;
        for (Double i:bonusCritDamage.values()){
            if (i != null){
                h+=i;
            }
        }
        critDamage = baseCritDamage + h;
    }
    private void calcCritChance(){
        double h = 0;
        for (Double i:bonusCritChance.values()){
            if (i != null){
                h+=i;
            }
        }
        critChance = baseCritChance + h;
    }
    private void calcMagicDamage(){
        double h = 0;
        for (Double i:bonusMagicDamage.values()){
            if (i != null){
                h+=i;
            }
        }
        magicDamage = baseMagicDamage + h;
    }
    private void calcSpeed(){
        double h = 0;
        for (Double i:bonusSpeed.values()){
            if (i != null){
                h+=i;
            }
        }
        speed = baseSpeed + h;
    }

    public HashMap<String, Double> getBonusMaxHealth() {
        return bonusMaxHealth;
    }

    public HashMap<String, Double> getBonusHpRegen() {
        return bonusHpRegen;
    }

    public HashMap<String, Double> getBonusDefence() {
        return bonusDefence;
    }

    public HashMap<String, Double> getBonusMagicDefence() {
        return bonusMagicDefence;
    }

    public HashMap<String, Double> getBonusDamage() {
        return bonusDamage;
    }

    public HashMap<String, Double> getBonusCritDamage() {
        return bonusCritDamage;
    }

    public HashMap<String, Double> getBonusCritChance() {
        return bonusCritChance;
    }

    public HashMap<String, Double> getBonusMagicDamage() {
        return bonusMagicDamage;
    }

    public HashMap<String, Double> getBonusSpeed() {
        return bonusSpeed;
    }

    public String getEntityType() {
        return entityType;
    }

    public boolean isUndead() {
        return isUndead;
    }

    public String getName() {
        return name;
    }

    public String getCustomName() {
        return customName;
    }

    public double getHealth() {
        return health;
    }

    public double getBaseMaxHealth() {
        return baseMaxHealth;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getBaseHpRegen() {
        return baseHpRegen;
    }

    public double getHpRegen() {
        return hpRegen;
    }

    public double getBaseDefence() {
        return baseDefence;
    }

    public double getDefence() {
        return defence;
    }

    public double getBaseMagicDefence() {
        return baseMagicDefence;
    }

    public double getMagicDefence() {
        return magicDefence;
    }

    public double getBaseDamage() {
        return baseDamage;
    }

    public double getDamage() {
        return damage;
    }

    public double getBaseCritDamage() {
        return baseCritDamage;
    }

    public double getCritDamage() {
        return critDamage;
    }

    public double getBaseCritChance() {
        return baseCritChance;
    }

    public double getCritChance() {
        return critChance;
    }

    public double getBaseMagicDamage() {
        return baseMagicDamage;
    }

    public double getMagicDamage() {
        return magicDamage;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public double getSpeed() {
        return speed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setBaseMaxHealth(double baseMaxHealth) {
        this.baseMaxHealth = baseMaxHealth;
    }

    public void setBaseHpRegen(double baseHpRegen) {
        this.baseHpRegen = baseHpRegen;
    }

    public void setBaseDefence(double baseDefence) {
        this.baseDefence = baseDefence;
    }

    public void setBaseMagicDefence(double baseMagicDefence) {
        this.baseMagicDefence = baseMagicDefence;
    }

    public void setBaseDamage(double baseDamage) {
        this.baseDamage = baseDamage;
    }

    public void setBaseCritDamage(double baseCritDamage) {
        this.baseCritDamage = baseCritDamage;
    }

    public void setBaseCritChance(double baseCritChance) {
        this.baseCritChance = baseCritChance;
    }

    public void setBaseMagicDamage(double baseMagicDamage) {
        this.baseMagicDamage = baseMagicDamage;
    }

    public void setBaseSpeed(double baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public void setUndead(boolean undead) {
        isUndead = undead;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getMana() {
        return mana;
    }

    public void setMana(double mana) {
        this.mana = mana;
    }

    public double getBaseMaxMana() {
        return baseMaxMana;
    }

    public void setBaseMaxMana(double baseMaxMana) {
        this.baseMaxMana = baseMaxMana;
    }

    public double getMaxMana() {
        return maxMana;
    }

    public HashMap<String, Double> getBonusMaxMana() {
        return bonusMaxMana;
    }

    public double getBaseManaRegen() {
        return baseManaRegen;
    }

    public void setBaseManaRegen(double baseManaRegen) {
        this.baseManaRegen = baseManaRegen;
    }

    public double getManaRegen() {
        return manaRegen;
    }

    public HashMap<String, Double> getBonusManaRegen() {
        return bonusManaRegen;
    }

    public void setCritChance(double critChance) {
        this.critChance = critChance;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }
}
