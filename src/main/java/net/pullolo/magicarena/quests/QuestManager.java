package net.pullolo.magicarena.quests;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

import static net.pullolo.magicarena.MagicArena.dbManager;

public class QuestManager {
    private static final HashMap<Player, ArrayList<Quest>> playerQuests = new HashMap<>();

    public static void getPlayerQuestsOnJoin(Player p){
        playerQuests.put(p, dbManager.getQuests(p));
    }

    public static void savePlayerQuestsOnLeave(Player p){
        for (Quest q : getPlayerQuests(p)){
            q.saveToDb();
        }
        playerQuests.remove(p);
    }

    public static ArrayList<Quest> getPlayerQuests(Player player){
        return playerQuests.get(player);
    }

    public static void finishQuest(Player p, Quest q){
        getPlayerQuests(p).remove(q);
    }

    public static void addPlayerQuest(Player p, Quest quest){
        playerQuests.get(p).add(quest);
    }

    public static void onMobKill(Player p){
        for (Quest q : new ArrayList<>(getPlayerQuests(p))){
            if (q.getQuestType().equals(QuestType.KILL_MOBS)){
                q.increaseProgress(1);
            }
        }
    }
}
