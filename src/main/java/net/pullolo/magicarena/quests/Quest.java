package net.pullolo.magicarena.quests;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static net.pullolo.magicarena.MagicArena.dbManager;

public class Quest {
    private final QuestType questType;
    private int rewardXp;
    private int rewardStarEssence;
    private final int goal;
    private int progress;
    private final Player player;

    public Quest(Player player, QuestType questType, int goal, int progress) {
        this.questType = questType;
        this.goal = goal;
        this.progress = progress;
        this.player = player;
        calculateRewards();
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
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&4Magic&6Arena&7] Quest completed! &a+" + getRewardXp() + " ❊"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&4Magic&6Arena&7] Added star essence - &a" + getRewardStarEssence() + " ✷"));
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

    private void calculateRewards(){
        switch (questType){
            case KILL_MOBS:
                this.rewardXp=10*goal;
                this.rewardStarEssence = (int) (goal*1.2);
                return;
            default:
                this.rewardXp = 0;
                this.rewardStarEssence = 0;
                return;
        }
    }

    @Override
    public String toString() {
        return this.questType.toString() + ": " + Math.round(getPercentageProgress()*1000)/10 + "%";
    }

    public String getStyledString() {
        switch (questType){
            case KILL_MOBS:
                return "&f" + uppercaseFirstLetter(questType.toString().split("_")[0].toLowerCase()) + " &a" + goal + " &f" + questType.toString().split("_")[1].toLowerCase()+
                        " - &a" + Math.round(getPercentageProgress()*1000)/10 + "% &fdone.\n&r&c" + getProgress() + "&7/&c" + getGoal() + " &7killed.";
            case COLLECT_SECRETS:
                return "&f" + uppercaseFirstLetter(questType.toString().split("_")[0].toLowerCase()) + " &a" + goal + " &f" + questType.toString().split("_")[1].toLowerCase()+
                        " - &a" + Math.round(getPercentageProgress()*1000)/10 + "% &fdone.\n&r&6" + getProgress() + "&7/&6" + getGoal() + " &7collected.";
            case FINISH_DUNGEONS:
                return "&f" + uppercaseFirstLetter(questType.toString().split("_")[0].toLowerCase()) + " &a" + goal + " &f" + questType.toString().split("_")[1].toLowerCase()+
                        " - &a" + Math.round(getPercentageProgress()*1000)/10 + "% &fdone.\n&r&4" + getProgress() + "&7/&4" + getGoal() + " &7finished.";
            default:
                return toString();
        }
    }

    public String uppercaseFirstLetter(String s){
        String c1 = String.valueOf(s.charAt(0)).toUpperCase();
        String rest = s.substring(1);
        return c1+rest;
    }

    public int getRewardXp() {
        return rewardXp;
    }

    public int getRewardStarEssence() {
        return rewardStarEssence;
    }
}
