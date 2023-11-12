package net.pullolo.magicarena.players;

import net.pullolo.magicarena.game.ArenaGame;
import net.pullolo.magicarena.game.Game;
import net.pullolo.magicarena.game.GameWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

import static net.pullolo.magicarena.MagicArena.debugLog;
import static net.pullolo.magicarena.data.PlayerData.getPlayerData;

public class ArenaPlayer extends ArenaEntityBlueprint {

    private final Game game;
    private ItemStack lastHeldItemStack;
    private boolean inGame = true;
    private boolean dead = false;

    public static HashMap<Player, ArenaPlayer> arenaPlayers = new HashMap<>();

    public ArenaPlayer(Player player, int level, Game game){
        if (arenaPlayers.containsKey(player)){
            throw new RuntimeException("Cannot convert player that is already converted!");
        } else arenaPlayers.put(player, this);
        this.game = game;
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
        setBaseMaxHealth(90+getLevel()*10);
        setBaseMaxMana(80+getLevel()*20);
        setBaseManaRegen(getLevel());
        setBaseHpRegen(getLevel()/20+1);
        setBaseDefence(getLevel()*5);
        setBaseMagicDefence(getLevel()*5);
        setBaseDamage(getLevel()*10+5);
        setBaseCritDamage(getLevel()*5+20);
        setBaseCritChance(getLevel()+2);
        setBaseMagicDamage(getLevel()*2);
        setBaseSpeed(100);
    }

    @Override
    public void updateStats() {
        fixedUpdateStats();
        performChecksAndCalc();
    }

    public Game getGame() {
        return game;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public static boolean isPlayerInGame(Player p){
        if (arenaPlayers.containsKey(p)){
            return arenaPlayers.get(p).isInGame();
        }
        return false;
    }

    public static boolean isPlayerInMatch(Player p){
        if (arenaPlayers.containsKey(p)){
            return arenaPlayers.get(p).isInGame() && !(arenaPlayers.get(p).getGame() instanceof GameWorld);
        }
        return false;
    }

    public ItemStack getLastHeldItemStack() {
        return lastHeldItemStack;
    }

    public void setLastHeldItemStack(ItemStack lastHeldItemStack) {
        this.lastHeldItemStack = lastHeldItemStack;
    }

    public void preRemove(Player p) {
        getPlayerData(p).setHp((int) getHealth());
        getPlayerData(p).setMana((int) getMana());
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
