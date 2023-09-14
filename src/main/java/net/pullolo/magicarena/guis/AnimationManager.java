package net.pullolo.magicarena.guis;

import de.themoep.inventorygui.InventoryGui;
import net.pullolo.magicarena.wish.WishSystem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class AnimationManager {

    public static HashMap<Player, Boolean> skipAnim = new HashMap<>();

    private final GuiManager guis;
    private final JavaPlugin plugin;
    public AnimationManager(JavaPlugin plugin, GuiManager guis){
        this.plugin = plugin;
        this.guis = guis;
    }

    public void playWishAnim(Player player, WishSystem.WishRarity wishRarity, WishSystem.WishType wishType, int stars, ItemStack finalItem, int animPeriod){
        addPlayerToAnimation(player);
        ArrayList<InventoryGui> animGuis = guis.createWishAnim(player, wishRarity, wishType, stars, finalItem);
        //todo add animation
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (!(player!=null && skipAnim.get(player)!=null && !skipAnim.get(player))){
                    removePlayerFromAnimation(player);
                    this.cancel();
                    return;
                }
                if (i!=0) animGuis.get(i-1).close(player);
                animGuis.get(i).show(player);

                i++;
                if (i > animGuis.size()-1) {
                    removePlayerFromAnimation(player);
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, animPeriod);
    }

    private void addPlayerToAnimation(Player p){
        skipAnim.put(p, false);
    }

    private void removePlayerFromAnimation(Player p){
        skipAnim.remove(p);
    }

    public static void skipAnimation(Player p){
        if (skipAnim.containsKey(p)){
            skipAnim.replace(p, true);
        }
    }
}
