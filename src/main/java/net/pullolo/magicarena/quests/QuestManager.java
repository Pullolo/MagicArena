package net.pullolo.magicarena.quests;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static net.pullolo.magicarena.MagicArena.dbManager;

public class QuestManager {
    private static final HashMap<Player, ArrayList<Quest>> playerQuests = new HashMap<>();

    public static void getPlayerQuestsOnJoin(Player p){
        playerQuests.put(p, dbManager.getQuests(p));
        if (playerQuests.get(p).size()<4){
            while (playerQuests.get(p).size()<4){
                addPlayerQuest(p, getNewRandomQuest(p));
            }
        }
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
        addPlayerQuest(p, getNewRandomQuest(p));
    }

    public static void addPlayerQuest(Player p, Quest quest){
        if (playerQuests.get(p).size()>=4){
            return;
        }
        playerQuests.get(p).add(quest);
    }

    public static void onMobKill(Player p){
        for (Quest q : new ArrayList<>(getPlayerQuests(p))){
            if (q.getQuestType().equals(QuestType.KILL_MOBS)){
                q.increaseProgress(1);
            }
        }
    }

    public static void onCollectSecrets(Player p){
        for (Quest q : new ArrayList<>(getPlayerQuests(p))){
            if (q.getQuestType().equals(QuestType.COLLECT_SECRETS)){
                q.increaseProgress(1);
            }
        }
    }

    public static void onFinishDungeons(Player p){
        for (Quest q : new ArrayList<>(getPlayerQuests(p))){
            if (q.getQuestType().equals(QuestType.FINISH_DUNGEONS)){
                q.increaseProgress(1);
            }
        }
    }

    public static void onOpenWitherDoors(Player p){
        for (Quest q : new ArrayList<>(getPlayerQuests(p))){
            if (q.getQuestType().equals(QuestType.OPEN_WITHER_DOORS)){
                q.increaseProgress(1);
            }
        }
    }

    public static void onWish(Player p){
        for (Quest q : new ArrayList<>(getPlayerQuests(p))){
            if (q.getQuestType().equals(QuestType.WISH)){
                q.increaseProgress(1);
            }
        }
    }

    public static void onPlayerRevive(Player p){
        for (Quest q : new ArrayList<>(getPlayerQuests(p))){
            if (q.getQuestType().equals(QuestType.REVIVE_PLAYERS)){
                q.increaseProgress(1);
            }
        }
    }

    public static Quest getNewRandomQuest(Player p){
        Random r = new Random();
        QuestType qt = QuestType.values()[r.nextInt(QuestType.values().length)];
        switch (qt){
            case KILL_MOBS:
                return new Quest(p, qt, 100+r.nextInt(31), 0);
            case COLLECT_SECRETS:
                return new Quest(p, qt, 10+r.nextInt(21), 0);
            case FINISH_DUNGEONS:
                return new Quest(p, qt, 2+r.nextInt(3), 0);
            case OPEN_WITHER_DOORS:
                return new Quest(p, qt, 8+r.nextInt(8), 0);
            case WISH:
                return new Quest(p, qt, 8+r.nextInt(5), 0);
            case REVIVE_PLAYERS:
                return new Quest(p, qt, 1+r.nextInt(2), 0);
            default:
                return null;
        }
    }

    public static ItemStack getQuestItemstack(QuestType questType){
        Random r = new Random();
        switch (questType){
            case KILL_MOBS:
                switch (r.nextInt(2)){
                    case 0:
                        return new ItemStack(Material.SKELETON_SKULL);
                    case 1:
                        return new ItemStack(Material.ZOMBIE_HEAD);
                }
            case COLLECT_SECRETS:
                return SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmIwNTVjODEwYmRkZmQxNjI2NGVjOGQ0MzljNDMyODNlMzViY2E3MWE1MDk4M2UxNWUzNjRjZDhhYjdjNjY4ZiJ9fX0=");
            case FINISH_DUNGEONS:
                return SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM3YjZmNTAxNTRkMTkyZDhjM2E3MmQxM2ZhNDRjOTUzYjQxMTM4NThjOWQyZWRmMjE4ZjUxNzk5OGQ3MzM2YyJ9fX0=");
            case OPEN_WITHER_DOORS:
                return SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTY3YjQ3ZmJkMzdjNmQ5ZDhkMjJkOTczZTcyOTBlODA4NTJlOTI2NmEwNzZmYjdhYjIxNWFmZTkxYjgxZWQ2YyJ9fX0=");
            case WISH:
                return new ItemStack(Material.NETHER_STAR);
            case REVIVE_PLAYERS:
                return SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGI2OTc1YWY3MDcyNGQ2YTQ0ZmQ1OTQ2ZTYwYjI3MTc3MzdkZmRiNTQ1YjRkYWIxODkzMzUxYTljOWRkMTgzYyJ9fX0=");
            default:
                return new ItemStack(Material.BARRIER);
        }
    }
}
