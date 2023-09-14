package net.pullolo.magicarena.wish;

import net.pullolo.magicarena.guis.AnimationManager;
import net.pullolo.magicarena.guis.GuiManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WishSystem {

    public enum WishType{
        WEAPON_WISH,
        ARMOR_WISH
    }

    public enum WishRarity{
        UNCOMMON,
        RARE,
        EPIC,
        LEGENDARY,
        MYTHIC
    }

    private final AnimationManager anims;

    public WishSystem(AnimationManager anims){
        this.anims = anims;
    }

    public boolean wish(Player player, WishType wishType){

        //todo add a proper wishing system
        WishRarity wishRarity = WishRarity.MYTHIC;

        anims.playWishAnim(player, wishRarity, wishType, 5, new ItemStack(Material.NETHERITE_SWORD), 10);
        return true;
    }

    public static int getWishRarityAsInt(WishRarity wishRarity){
        int rarity = 0;
        switch (wishRarity){
            case UNCOMMON:
                rarity=1;
                break;
            case RARE:
                rarity=2;
                break;
            case EPIC:
                rarity=3;
                break;
            case LEGENDARY:
                rarity=4;
                break;
            case MYTHIC:
                rarity=5;
                break;
            default:
                break;
        }
        return rarity;
    }

    public static Material getWishRarityAsGlassPane(WishRarity wishRarity){
        Material rarity = null;
        switch (wishRarity){
            case UNCOMMON:
                rarity=Material.LIME_STAINED_GLASS_PANE;
                break;
            case RARE:
                rarity= Material.LIGHT_BLUE_STAINED_GLASS_PANE;
                break;
            case EPIC:
                rarity=Material.MAGENTA_STAINED_GLASS_PANE;
                break;
            case LEGENDARY:
                rarity=Material.YELLOW_STAINED_GLASS_PANE;
                break;
            case MYTHIC:
                rarity=Material.PINK_STAINED_GLASS_PANE;
                break;
            default:
                break;
        }
        return rarity;
    }
}
