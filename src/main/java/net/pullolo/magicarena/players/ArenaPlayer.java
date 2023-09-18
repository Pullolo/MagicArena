package net.pullolo.magicarena.players;

import net.pullolo.magicarena.MagicArena;
import net.pullolo.magicarena.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ArenaPlayer extends ArenaEntity {

    private final Game game;
    private boolean inGame = true;

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
        setBaseDamage((getLevel()/2+1)*5+getLevel());
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

    private void performChecksAndCalc(){
        calcStats();
        runChecks();
    }

    public void runChecks(){
        if (getLevel()<1){
            setLevel(1);
        }
        if (getHealth()>getMaxHealth()){
            setHealth(getMaxHealth());
        }
        if (getCritChance() > (double) 100){
            setCritChance(100);
        }
        if (getCritChance() < (double) 0){
            setCritChance(0);
        }
        if (getMana()>getMaxMana()){
            setMana(getMaxMana());
        }
        if (getMana() < 0){
            setMana(0);
        }
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
}
