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
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static net.pullolo.magicarena.MagicArena.debugLog;
import static net.pullolo.magicarena.MagicArena.getLog;
import static net.pullolo.magicarena.data.PlayerData.getPlayerData;
import static net.pullolo.magicarena.items.ArmorDefinitions.*;
import static net.pullolo.magicarena.items.ItemsDefinitions.*;

public class WishSystem {

    public static final HashMap<Player, PreviousWishData> lastArmorSet = new HashMap<>();

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

        if (getPlayerData(player).getWishes()<1){
            return false;
        } else getPlayerData(player).setWishes(getPlayerData(player).getWishes()-1);

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
            finalItem=null;
            if (wishRarity.equals(WishRarity.UNCOMMON)){
                finalItem = new Item(getRandomUncommonWeapon(itemClass), stars, q).getItem();
            } else if (wishRarity.equals(WishRarity.RARE)) {
                finalItem = new Item(getRandomRareWeapon(itemClass), stars, q).getItem();
            } else if (wishRarity.equals(WishRarity.EPIC)) {
                finalItem = new Item(getRandomEpicWeapon(itemClass), stars, q).getItem();
            } else if (wishRarity.equals(WishRarity.LEGENDARY)) {
                finalItem = new Item(getRandomLegendaryWeapon(itemClass), stars, q).getItem();
            } else if (wishRarity.equals(WishRarity.MYTHIC)){
                finalItem = new Item(getRandomMythicWeapon(itemClass), stars, q).getItem();
            }

        } else {
            finalItem=null;
            if (wishRarity.equals(WishRarity.UNCOMMON)){
                finalItem = new Item(getRandomUncommonArmorPiece(itemClass), stars, q).getItem();
            } else if (wishRarity.equals(WishRarity.RARE)){
                finalItem = new Item(getRandomRareArmorPiece(itemClass), stars, q).getItem();
            } else if (wishRarity.equals(WishRarity.EPIC)){
                finalItem = new Item(getRandomEpicArmorPiece(itemClass), stars, q).getItem();
            } else if (wishRarity.equals(WishRarity.LEGENDARY)){
                finalItem = new Item(getRandomLegendaryArmorPiece(itemClass), stars, q).getItem();
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

            //add a 50% chance for the next armor piece to be the same set
            if (lastArmorSet.containsKey(player)){
                if (armorItems.containsKey(lastArmorSet.get(player).getLastArmorSet())){
                    if (!lastArmorSet.get(player).getLastWishRarity().equals(WishRarity.UNCOMMON)){
                        if (r.nextBoolean()){
                            ArrayList<Item> pieces = armorItems.get(lastArmorSet.get(player).getLastArmorSet());
                            finalItem = new Item(pieces.get(r.nextInt(pieces.size())), stars, q).getItem();
                            wishRarity=lastArmorSet.get(player).getLastWishRarity();
                            itemClass=lastArmorSet.get(player).getLastItemClass();
                        } else lastArmorSet.replace(player, new PreviousWishData(wishRarity, itemClass, getArmorId(finalItem)));
                    } else lastArmorSet.remove(player);
                } else lastArmorSet.remove(player);
            } else if (!wishRarity.equals(WishRarity.UNCOMMON)) lastArmorSet.put(player, new PreviousWishData(wishRarity, itemClass, getArmorId(finalItem)));
        }


        anims.playWishAnim(player, wishRarity, wishType, stars, itemClass, finalItem, 10);
        return true;
    }

    private String getArmorId(ItemStack itemStack){
        if (itemStack.getItemMeta()==null){
            return null;
        }
        Item item = new Item(itemStack);
        if (item.getItemId().equalsIgnoreCase("NULL")){
            return null;
        }
        for (String s : armorItems.keySet()){
            for (Item i : armorItems.get(s)){
                if (i.getItemId().equalsIgnoreCase(item.getItemId())){
                    return s;
                }
            }
        }
        return null;
    }

    public static Item getRandomUncommonWeapon(ItemClass itemClass){
        int r = new Random().nextInt(3)+1; //1-3 range
        switch (r){
            case 1:
                return basicSword;
            case 2:
                return stoneStick;
            case 3:
                switch (itemClass){
                    case DPS:
                        return unstableTome;
                    case HEALER:
                        return leechingStaff;
                    case ARCHER:
                        return longBow;
                    case TANK:
                        return solidStoneAxe;
                }
            default:
                ItemStack nullItem = new ItemStack(Material.BARRIER);
                ItemMeta im = nullItem.getItemMeta();
                im.setDisplayName("NULL");
                nullItem.setItemMeta(im);
                return new Item(nullItem);
        }
    }

    public static Item getRandomRareWeapon(ItemClass itemClass){
        int r = new Random().nextInt(2)+1; //1-2 range
        switch (r){
            case 1:
                return stormRuler;
            case 2:
                switch (itemClass){
                    case DPS:
                        return aspectOfTheEnd;
                    case HEALER:
                        return healingStaff;
                    case ARCHER:
                        return flamingBow;
                    case TANK:
                        return golemSword;
                }
            default:
                ItemStack nullItem = new ItemStack(Material.BARRIER);
                ItemMeta im = nullItem.getItemMeta();
                im.setDisplayName("NULL");
                nullItem.setItemMeta(im);
                return new Item(nullItem);
        }
    }

    public static Item getRandomEpicWeapon(ItemClass itemClass){
        int r = new Random().nextInt(1)+1; //1-2 range
        switch (r){
            case 1:
                return leapingSword;
            default:
                ItemStack nullItem = new ItemStack(Material.BARRIER);
                ItemMeta im = nullItem.getItemMeta();
                im.setDisplayName("NULL");
                nullItem.setItemMeta(im);
                return new Item(nullItem);
        }
    }

    public static Item getRandomLegendaryWeapon(ItemClass itemClass){
        int r = new Random().nextInt(1)+1; //1-2 range
        switch (r){
            case 1:
                return auroraStaff;
            default:
                ItemStack nullItem = new ItemStack(Material.BARRIER);
                ItemMeta im = nullItem.getItemMeta();
                im.setDisplayName("NULL");
                nullItem.setItemMeta(im);
                return new Item(nullItem);
        }
    }

    public static Item getRandomMythicWeapon(ItemClass itemClass){
        int r = new Random().nextInt(1)+1; //1-2 range
        switch (r){
            case 1:
                return kusumibaru;
            default:
                ItemStack nullItem = new ItemStack(Material.BARRIER);
                ItemMeta im = nullItem.getItemMeta();
                im.setDisplayName("NULL");
                nullItem.setItemMeta(im);
                return new Item(nullItem);
        }
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
