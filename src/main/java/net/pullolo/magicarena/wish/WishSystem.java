package net.pullolo.magicarena.wish;

import net.pullolo.magicarena.guis.AnimationManager;
import net.pullolo.magicarena.guis.GuiManager;
import net.pullolo.magicarena.items.Item;
import net.pullolo.magicarena.items.ItemClass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.pullolo.magicarena.MagicArena.getLog;
import static net.pullolo.magicarena.items.ArmorDefinitions.getRandomUncommonArmorPiece;
import static net.pullolo.magicarena.items.ItemsDefinitions.getRandomRareWeapon;
import static net.pullolo.magicarena.items.ItemsDefinitions.getRandomUncommonWeapon;

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

        //todo temp code to make wishes cost smth
        if (player.getLevel()<1){
            return false;
        } else player.setLevel(player.getLevel()-1);
        //todo temp end

        ItemStack finalItem;
        Random r = new Random();
        String starsStr = ChatColor.translateAlternateColorCodes('&', " &f✪");
        int stars = 1;
        int rarityChance = r.nextInt(100)+1;
        WishRarity wishRarity;
        if (rarityChance>99){
            wishRarity = WishRarity.MYTHIC;
        } else if (rarityChance>97) {
            wishRarity = WishRarity.LEGENDARY;
        } else if (rarityChance>90) {
            wishRarity = WishRarity.EPIC;
        } else if (rarityChance>80) {
            wishRarity = WishRarity.RARE;
        } else {
            wishRarity = WishRarity.UNCOMMON;
        }

        for (int i= 0; i<4; i++){
            if (r.nextBoolean()){
                starsStr+="✪";
                stars++;
            }
        }

        String quality = "&7Quality: ";
        float q = ((float)((int) (r.nextFloat()*1000)))/10;
        if (q>90){
            quality += "&dPerfect - " + q;
        } else if (q>70) {
            quality += "&6Undamaged - " + q;
        } else if (q>40) {
            quality += "&cDamaged - " + q;
        } else {
            quality += "&4Highly Damaged - " + q;
        }

        ItemClass itemClass;
        int randItemClass = r.nextInt(4)+1;
        if (randItemClass == 1){
            itemClass = ItemClass.DPS;
        } else if (randItemClass == 2) {
            itemClass = ItemClass.ARCHER;
        } else if (randItemClass == 3) {
            itemClass = ItemClass.TANK;
        } else {
            itemClass = ItemClass.HEALER;
        }

        //todo add a proper wishing system (getItem(itemClass, rarity))

        if (wishType == WishType.WEAPON_WISH){
            if (wishRarity.equals(WishRarity.UNCOMMON)){
                finalItem = new Item(getRandomUncommonWeapon(itemClass), stars, q).getItem();
            } else if (wishRarity.equals(WishRarity.RARE)) {
                finalItem = new Item(getRandomRareWeapon(itemClass), stars, q).getItem();
            } else {
                finalItem = new ItemStack(Material.NETHERITE_SWORD);
                ItemMeta im = finalItem.getItemMeta();

                //todo temp
                if (finalItem.getItemMeta().getDisplayName().equalsIgnoreCase("")){
                    im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&r&" + getRarityColorChar(wishRarity) + finalItem.getType().toString().replace('_', ' ').toLowerCase()));
                }

                im.setDisplayName(im.getDisplayName() + starsStr);

                List<String> lore;
                if (finalItem.getItemMeta().getLore()==null){
                    lore = new ArrayList<>();
                } else {
                    //todo possibly move it 2nd to last
                    lore = finalItem.getItemMeta().getLore();
                }
                lore.add(ChatColor.translateAlternateColorCodes('&', quality));
                im.setLore(lore);
                finalItem.setItemMeta(im);
            }

        } else {
            if (wishRarity.equals(WishRarity.UNCOMMON)){
                finalItem = new Item(getRandomUncommonArmorPiece(itemClass), stars, q).getItem();
            } else {
                finalItem = new ItemStack(Material.NETHERITE_CHESTPLATE);
                ItemMeta im = finalItem.getItemMeta();

                //todo temp
                if (finalItem.getItemMeta().getDisplayName().equalsIgnoreCase("")){
                    im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&r&" + getRarityColorChar(wishRarity) + finalItem.getType().toString().replace('_', ' ').toLowerCase()));
                }

                im.setDisplayName(im.getDisplayName() + starsStr);

                List<String> lore;
                if (finalItem.getItemMeta().getLore()==null){
                    lore = new ArrayList<>();
                } else {
                    //todo possibly move it 2nd to last
                    lore = finalItem.getItemMeta().getLore();
                }
                lore.add(ChatColor.translateAlternateColorCodes('&', quality));
                im.setLore(lore);
                finalItem.setItemMeta(im);
            }
        }


        anims.playWishAnim(player, wishRarity, wishType, stars, itemClass, finalItem, 10);
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

    public static char getRarityColorChar(WishRarity wishRarity){
        char rarity = ' ';
        switch (wishRarity){
            case UNCOMMON:
                rarity='a';
                break;
            case RARE:
                rarity='9';
                break;
            case EPIC:
                rarity='5';
                break;
            case LEGENDARY:
                rarity='6';
                break;
            case MYTHIC:
                rarity='d';
                break;
            default:
                break;
        }
        return rarity;
    }
}
