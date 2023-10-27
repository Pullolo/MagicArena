package net.pullolo.magicarena.data;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

import static net.pullolo.magicarena.MagicArena.*;
import static net.pullolo.magicarena.data.PlayerData.getPlayerData;

public class XpManager {

    private static final int levelCap = 50;

    public static void addXp(Player p, double xp){
        PlayerData playerData = getPlayerData(p);
        playerData.setXp(playerData.getXp()+xp);
        checkForLevelUp(p, playerData);
    }

    public static void checkForLevelUp(Player p, PlayerData playerData){
        int i = 0;
        while (playerData.getXp() >= getAmountToLevelUp(playerData) && playerData.getLevel()<levelCap){
            levelUp(p, playerData, i);
            i++;
        }
    }

    private static void levelUp(Player p, PlayerData playerData, int i){
        final int wishes = new Random().nextInt(5)+1; //1-5
        playerData.setLevel(playerData.getLevel()+1);
        playerData.setWishes(playerData.getWishes()+wishes);
        final int newLevel = playerData.getLevel();
        new BukkitRunnable() {
            @Override
            public void run() {
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&4Magic&6Arena&7] Level Up, your level is now: &a" + newLevel));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&4Magic&6Arena&7] Added wishes - &a" + wishes + " ✧"));
            }
        }.runTaskLater(plugin, 20L *i);
    }

    public static double getAmountToLevelUp(PlayerData playerData){
        double amount = 0;

        int level = playerData.getLevel();
        amount = Math.pow(level, 2.4)*423;

        return amount;
    }
}
