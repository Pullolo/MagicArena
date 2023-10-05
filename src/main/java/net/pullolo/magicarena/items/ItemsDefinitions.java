package net.pullolo.magicarena.items;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.pullolo.magicarena.items.ItemClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.pullolo.magicarena.MagicArena.getLog;

public class ItemsDefinitions {

    public static Item testItem;
    public static Item basicSword;
    public static Item stoneStick;
    public static final ArrayList<String> itemIds = new ArrayList<>();

    public static void init(){
        createTestItem();
        createBasicSword();
        createStoneStick();
    }

    private static void createStoneStick(){
        String name = "&aStone Stick";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+16");
        lore.add("");
        lore.add("&a&lUNCOMMON SWORD");
        lore.add("&8item_id:stone_stick");
        ItemStack item = createItem(Material.STONE_SWORD, name, lore);
        stoneStick = new Item(item);
        itemIds.add("stone_stick");
    }

    private static void createBasicSword(){
        String name = "&aBasic Sword";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+14");
        lore.add("");
        lore.add("&a&lUNCOMMON SWORD");
        lore.add("&8item_id:basic_sword");
        ItemStack item = createItem(Material.WOODEN_SWORD, name, lore);
        basicSword = new Item(item);
        itemIds.add("basic_sword");
    }

    private static void createTestItem(){
        String name = "&aTest Item";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+20");
        lore.add("&7Critical Chance: &c+20");
        lore.add("&7Critical Damage: &c+20");
        lore.add("&7Intelligence: &b+20");
        lore.add("&7Ability Damage: &3+20");
        lore.add("&7Health: &4+20");
        lore.add("&7Defence: &a+20");
        lore.add("&7Resistance: &3+20");
        lore.add("&7Speed: &f+20");
        lore.add("");
        lore.add("&a&lUNCOMMON SWORD");
        lore.add("&8item_id:test_item");

        ItemStack item = createItem(Material.STONE_SWORD, name, lore);
        testItem = new Item(item);
        itemIds.add("test_item");
    }

    private static ItemStack createItem(Material material, String displayName, List<String> lore){
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        List<String> newLore = new ArrayList<>();
        for (String s : lore){
            newLore.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        meta.setLore(newLore);
        item.setItemMeta(meta);

        return item;
    }

    public static Item getRandomUncommonWeapon(ItemClass itemClass){
        int r = new Random().nextInt(2)+1; //1-1 range
        switch (r){
            case 1:
                return basicSword;
            case 2:
                return stoneStick;
            default:
                ItemStack nullItem = new ItemStack(Material.BARRIER);
                ItemMeta im = nullItem.getItemMeta();
                im.setDisplayName("NULL");
                nullItem.setItemMeta(im);
                return new Item(nullItem);
        }
    }

    private static ItemStack getSkull(String base64) {
        return SkullCreator.itemFromBase64(base64);
    }

    public static Item getItemFromPlayer(ItemStack item){
        if (!isCustom(item)){
            getLog().warning("This item isn't on custom items list!");
        }
        return new Item(item);
    }

    private static boolean isCustom(ItemStack item){
        if (item.getItemMeta().getLore()!=null){
            for (String s : item.getItemMeta().getLore()){
                if (s.contains("§8item_id:")){
                    if (itemIds.contains(s.split(":")[1])){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
