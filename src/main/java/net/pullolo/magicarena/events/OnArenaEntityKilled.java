package net.pullolo.magicarena.events;

import net.pullolo.magicarena.game.Dungeon;
import net.pullolo.magicarena.players.DungeonEntity;
import net.pullolo.magicarena.quests.QuestManager;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Random;

import static net.pullolo.magicarena.MagicArena.getLog;
import static net.pullolo.magicarena.players.ArenaEntity.arenaEntities;
import static net.pullolo.magicarena.players.ArenaPlayer.isPlayerInGame;

public class OnArenaEntityKilled {

    public OnArenaEntityKilled(Player killer, Entity e){
        QuestManager.onMobKill(killer);
        if (e instanceof Spider) QuestManager.onSpiderKill(killer);
        if (e instanceof Witch) QuestManager.onWitchKill(killer);
        if (e instanceof Evoker) QuestManager.onEvokerKilled(killer);
        if (arenaEntities.get(e) instanceof DungeonEntity){
            Dungeon d = (Dungeon) arenaEntities.get(e).getGame();
            if (((DungeonEntity) arenaEntities.get(e)).hasWitherKey()){
                d.findWitherKey(killer);
            }
            if (((DungeonEntity) arenaEntities.get(e)).hasBossKey()){
                d.findBossKey(killer);
            }
            d.addScore(5);
        }
        //works
    }

    public OnArenaEntityKilled(Entity e){
        if (arenaEntities.get(e) instanceof DungeonEntity){
            Dungeon d = (Dungeon) arenaEntities.get(e).getGame();
            //Random Player
            Player p = d.getAllPlayers().get(new Random().nextInt(d.getAllPlayers().size()));
            if (((DungeonEntity) arenaEntities.get(e)).hasWitherKey()){
                d.findWitherKey(p);
            }
            if (((DungeonEntity) arenaEntities.get(e)).hasBossKey()){
                d.findBossKey(p);
            }
            QuestManager.onMobKill(p);
            return;
        }
    }
}
