package net.pullolo.magicarena.items;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.pullolo.magicarena.items.ItemClass;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.pullolo.magicarena.MagicArena.getLog;

public class ItemsDefinitions {

    public static Item testItem;
    public static Item basicSword;
    public static Item stoneStick;
    public static Item leechingStaff; //healer
    public static Item solidStoneAxe; //tank
    public static Item unstableTome; //dps
    public static Item longBow; //archer

    public static Item bladeOfTheUniverse;
    public static final ArrayList<String> itemIds = new ArrayList<>();

    public static void init(){
        createTestItem();
        createBasicSword();
        createStoneStick();
        createLeechingStaff();
        createSolidStoneAxe();
        createUnstableTome();
        createLongBow();

        createBladeOfTheUniverse();
    }

    private static void createLongBow(){
        String name = "&aLong Bow";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+15");
        lore.add("");
        lore.add("&a&lUNCOMMON BOW");
        lore.add("&8item_id:long_bow");
        ItemStack item = createItem(Material.BOW, name, lore);
        longBow = new Item(item);
        itemIds.add("long_bow");
    }

    private static void createUnstableTome(){
        String name = "&aUnstable Tome";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+1");
        lore.add("&7Ability Damage: &3+30");
        lore.add("&7Intelligence: &b+20");
        lore.add("");
        lore.add("&6Ability: Magic Missile &eRIGHT CLICK");
        lore.add("&7Fires a magic missile dealing &320 + 110%");
        lore.add("&7your ability damage.");
        lore.add("&8Mana Cost: &330");
        lore.add("&8Cooldown: &a1.5s");
        lore.add("");
        lore.add("&a&lUNCOMMON BOOK");
        lore.add("&8item_id:unstable_tome");
        ItemStack item = createItem(Material.BOOK, name, lore);
        unstableTome = new Item(item);
        itemIds.add("unstable_tome");
    }

    private static void createSolidStoneAxe(){
        String name = "&aSolid Stone Axe";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+12");
        lore.add("&7Health: &4+50");
        lore.add("&7Defence: &a+15");
        lore.add("");
        lore.add("&a&lUNCOMMON AXE");
        lore.add("&8item_id:solid_stone_axe");
        ItemStack item = createItem(Material.STONE_AXE, name, lore);
        solidStoneAxe = new Item(item);
        itemIds.add("solid_stone_axe");
    }

    private static void createLeechingStaff(){
        String name = "&aLeeching Staff";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+9");
        lore.add("");
        lore.add("&6Item Ability: Druid Leech &eON HIT");
        lore.add("&7Heals you for &a2 &7health on hit.");
        lore.add("");
        lore.add("&a&lUNCOMMON SWORD");
        lore.add("&8item_id:leeching_staff");
        ItemStack item = createItem(Material.TIPPED_ARROW, name, lore);
        PotionMeta im = (PotionMeta) item.getItemMeta();
        im.clearCustomEffects();
        im.setColor(Color.LIME);
        item.setItemMeta(im);
        leechingStaff = new Item(item);
        itemIds.add("leeching_staff");
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

    private static void createBladeOfTheUniverse(){
        String name = "&d&k1 &dBlade Of The Universe &k1";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+999999979");
        lore.add("&7Critical Chance: &c+100");
        lore.add("&7Critical Damage: &c+999999999");
        lore.add("");
        lore.add("&d&lMYTHIC SWORD");
        lore.add("&8item_id:blade_of_the_universe");

        ItemStack item = createItem(Material.NETHERITE_SWORD, name, lore);
        bladeOfTheUniverse = new Item(new Item(item), 5, 100);
        itemIds.add("blade_of_the_universe");
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
        int r = new Random().nextInt(3)+1; //1-1 range
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
