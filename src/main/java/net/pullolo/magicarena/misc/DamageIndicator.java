package net.pullolo.magicarena.misc;

import net.pullolo.magicarena.MagicArena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.Random;

import static org.bukkit.Bukkit.getServer;

public class DamageIndicator {
    public DamageIndicator(Entity e, double amou, boolean isCrit){
        Random rand = new Random();
        long amount = Math.round(amou);
        Location loc = e.getLocation();
        loc.add(e.getLocation().clone().getDirection().multiply(1).add(e.getLocation().clone().getDirection().multiply(rand.nextFloat()*0.1)));
        loc.add(0, 1+rand.nextFloat()*0.1, 0);

        ArmorStand armorStand = loc.getWorld().spawn(loc, ArmorStand.class, en -> {
            en.setVisible(false);
            en.setInvisible(true);
            en.setMarker(true);
        });
        armorStand.setGravity(false);

        if (isCrit){

            ChatColor[] colors = new ChatColor[4];
            colors[0] = ChatColor.WHITE;
            colors[1] = ChatColor.YELLOW;
            colors[2] = ChatColor.GOLD;
            colors[3] = ChatColor.RED;

            String am = "✧" + amount + "✧";
            String changed = "";
            for (char c:am.toCharArray()) {
                changed += "" + colors[rand.nextInt(4)] + c;
            }
            armorStand.setCustomName(changed);
        } else {
            armorStand.setCustomName("§7" + amount);
        }
        armorStand.setCustomNameVisible(true);

        Bukkit.getScheduler().runTaskLater(MagicArena.plugin, () -> {
            armorStand.remove();
        }, 20);
    }
}
