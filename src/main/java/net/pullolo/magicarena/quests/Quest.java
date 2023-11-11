package net.pullolo.magicarena.quests;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static net.pullolo.magicarena.MagicArena.dbManager;

public class Quest {
    private final QuestType questType;
    private final int goal;
    private int progress;
    private final Player player;

    public Quest(Player player, QuestType questType, int goal, int progress) {
        this.questType = questType;
        this.goal = goal;
        this.progress = progress;
        this.player = player;
    }

    public int getProgress() {
        return progress;
    }

    public double getPercentageProgress(){
        return (double) progress/goal;
    }

    public void increaseProgress(int progress){
        this.progress+=progress;
        if (this.progress>=goal){
            finishQuest();
        }
    }

    public void finishQuest(){
        QuestManager.finishQuest(player, this);
        player.sendMessage(ChatColor.LIGHT_PURPLE + "You have completed a Quest!" + ChatColor.AQUA + " +100 xp");
    }

    public QuestType getQuestType() {
        return questType;
    }

    public int getGoal(){
        return goal;
    }

    public void saveToDb(){
        dbManager.saveQuest(this);
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return this.questType.toString() + ": " + Math.round(getPercentageProgress()*1000)/10 + "%";
    }
}
