package net.pullolo.magicarena.quests;

import net.pullolo.magicarena.data.XpManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static net.pullolo.magicarena.MagicArena.dbManager;
import static net.pullolo.magicarena.data.PlayerData.getPlayerData;

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
        XpManager.addXp(player, getRewardXp());
        getPlayerData(player).setStarEssence(getPlayerData(player).getStarEssence()+getRewardStarEssence());
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
                this.rewardStarEssence = (int) (goal*1.4);
                return;
            case COLLECT_SECRETS:
                this.rewardXp=100*goal;
                this.rewardStarEssence =10*goal;
                return;
            case FINISH_DUNGEONS:
                this.rewardXp=400*goal;
                this.rewardStarEssence =40*goal;
                return;
            case OPEN_WITHER_DOORS:
                this.rewardXp=100*goal;
                this.rewardStarEssence=40*goal;
                return;
            case WISH:
                this.rewardXp=150*goal;
                this.rewardStarEssence=60*goal;
                return;
            case REVIVE_PLAYERS:
                this.rewardXp=600*goal;
                this.rewardStarEssence=110*goal;
                return;
            case USE_TELEPORT_ABILITY:
                this.rewardXp=90*goal;
                this.rewardStarEssence= (int) 6.2*goal;
                return;
            case WIN_SOLO_DUELS:
                this.rewardXp=1000*goal;
                this.rewardStarEssence=90*goal;
                return;
            case KILL_SPIDERS:
                this.rewardXp=20*goal;
                this.rewardStarEssence = (int) (goal*5.4);
                return;
            case KILL_WITCHES:
                this.rewardXp=100*goal;
                this.rewardStarEssence = goal*10;
                return;
            case KILL_EVOKERS:
                this.rewardXp=200*goal;
                this.rewardStarEssence = goal*65;
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
            case OPEN_WITHER_DOORS:
                return "&f" + uppercaseFirstLetter(questType.toString().split("_")[0].toLowerCase()) + " &a" + goal + " &f" + questType.toString().split("_")[1].toLowerCase()+ " " + questType.toString().split("_")[2].toLowerCase()+
                        " - &a" + Math.round(getPercentageProgress()*1000)/10 + "% &fdone.\n&r&4" + getProgress() + "&7/&4" + getGoal() + " &7opened.";
            case WISH:
                return "&f" + uppercaseFirstLetter(questType.toString().split("_")[0].toLowerCase()) + " &a" + goal + " &ftimes" +
                        " - &a" + Math.round(getPercentageProgress()*1000)/10 + "% &fdone.\n&r&3" + getProgress() + "&7/&3" + getGoal() + " &7wished.";
            case REVIVE_PLAYERS:
                return "&f" + uppercaseFirstLetter(questType.toString().split("_")[0].toLowerCase()) + " &a" + goal + " &f" + questType.toString().split("_")[1].toLowerCase()+
                        " - &a" + Math.round(getPercentageProgress()*1000)/10 + "% &fdone.\n&r&d" + getProgress() + "&7/&d" + getGoal() + " &7revived.";
            case USE_TELEPORT_ABILITY:
                return "&f" + getStyledType() + " &a" + goal + " &ftimes" +
                        " - &a" + Math.round(getPercentageProgress()*1000)/10 + "% &fdone.\n&r&3" + getProgress() + "&7/&3" + getGoal() + " &7times.";
            case WIN_SOLO_DUELS:
                return "&f" + uppercaseFirstLetter(questType.toString().split("_")[0].toLowerCase()) + " &a" + goal + " &f" + questType.toString().split("_")[1].toLowerCase()+ " " + questType.toString().split("_")[2].toLowerCase()+
                        " - &a" + Math.round(getPercentageProgress()*1000)/10 + "% &fdone.\n&r&a" + getProgress() + "&7/&a" + getGoal() + " &7won.";
            case KILL_EVOKERS:
                return "&f" + uppercaseFirstLetter(questType.toString().split("_")[0].toLowerCase()) + " &a" + goal + " &f" + questType.toString().split("_")[1].toLowerCase()+
                        " - &a" + Math.round(getPercentageProgress()*1000)/10 + "% &fdone.\n&r&c" + getProgress() + "&7/&c" + getGoal() + " &7evokers killed.";
            case KILL_SPIDERS:
                return "&f" + uppercaseFirstLetter(questType.toString().split("_")[0].toLowerCase()) + " &a" + goal + " &f" + questType.toString().split("_")[1].toLowerCase()+
                        " - &a" + Math.round(getPercentageProgress()*1000)/10 + "% &fdone.\n&r&c" + getProgress() + "&7/&c" + getGoal() + " &7spiders killed.";
            case KILL_WITCHES:
                return "&f" + uppercaseFirstLetter(questType.toString().split("_")[0].toLowerCase()) + " &a" + goal + " &f" + questType.toString().split("_")[1].toLowerCase()+
                        " - &a" + Math.round(getPercentageProgress()*1000)/10 + "% &fdone.\n&r&c" + getProgress() + "&7/&c" + getGoal() + " &7witches killed.";
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

    public String getStyledType() {
        String s = uppercaseFirstLetter(questType.toString().split("_")[0].toLowerCase());
        for (int i = 1; i<questType.toString().split("_").length; i++){
            s+= " " + questType.toString().split("_")[i].toLowerCase();
        }
        return s;
    }
}
