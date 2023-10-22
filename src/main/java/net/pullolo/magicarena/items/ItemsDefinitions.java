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
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static net.pullolo.magicarena.MagicArena.getLog;

public class ItemsDefinitions {

    //uncommon
    public static Item testItem;
    public static Item basicSword;
    public static Item stoneStick;
    public static Item leechingStaff; //healer
    public static Item solidStoneAxe; //tank
    public static Item unstableTome; //dps
    public static Item longBow; //archer
    public static Item undeadSword; //dungeonItem
    //rare
    public static Item stormRuler;
    public static Item healingStaff; //healer
    public static Item golemSword; //tank
    public static Item aspectOfTheEnd; //dps
    public static Item flamingBow; //archer
    public static Item brutalityBlade; //dungeonItem

    public static Item bladeOfTheUniverse;
    public static final ArrayList<String> itemIds = new ArrayList<>();
    public static final HashMap<String, Item> items = new HashMap<>();

    public static void init(){
        createTestItem();
        createBasicSword();
        createStoneStick();
        createLeechingStaff();
        createSolidStoneAxe();
        createUnstableTome();
        createLongBow();
        createUndeadSword();

        createStormRuler();
        createHealingStaff();
        createGolemSword();
        createAspectOfTheEnd();
        createFlamingBow();
        createBrutalityBlade();

        createBladeOfTheUniverse();
    }

    private static void createBrutalityBlade(){
        String name = "&9Brutality Blade";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+55");
        lore.add("&7Critical Chance: &c+10");
        lore.add("&7Critical Damage: &c+40");
        lore.add("");
        lore.add("&6Item Ability: Brutality");
        lore.add("&7If the target is below &a50% &7health");
        lore.add("&7deal &c200% &7damage.");
        lore.add("");
        lore.add("&9&lRARE AXE");
        lore.add("&8item_id:brutality_blade");
        ItemStack item = createItem(Material.NETHERITE_AXE, name, lore);
        brutalityBlade = new Item(item);
        itemIds.add(brutalityBlade.getItemId());
        items.put(brutalityBlade.getItemId(), brutalityBlade);
    }

    private static void createFlamingBow(){
        String name = "&9Flaming Bow";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+40");
        lore.add("&7Critical Chance: &c+30");
        lore.add("&7Critical Damage: &c+110");
        lore.add("");
        lore.add("&6Item Ability: Ignition");
        lore.add("&7Set anyone on fire for 3 seconds.");
        lore.add("");
        lore.add("&9&lRARE BOW");
        lore.add("&8item_id:flaming_bow");
        ItemStack item = createItem(Material.BOW, name, lore);
        flamingBow = new Item(item);
        itemIds.add(flamingBow.getItemId());
        items.put(flamingBow.getItemId(), flamingBow);
    }

    private static void createAspectOfTheEnd(){
        String name = "&9Aspect Of The End";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+60");
        lore.add("&7Intelligence: &b+50");
        lore.add("&7Critical Chance: &c+20");
        lore.add("&7Critical Damage: &c+80");
        lore.add("");
        lore.add("&6Item Ability: Instant Transmission &eRIGHT CLICK");
        lore.add("&7Teleport &a8 blocks&7 ahead of you.");
        lore.add("&8Mana Cost: &350");
        lore.add("");
        lore.add("&9&lRARE SWORD");
        lore.add("&8item_id:aspect_of_the_end");
        ItemStack item = createItem(Material.DIAMOND_SWORD, name, lore);
        aspectOfTheEnd = new Item(item);
        itemIds.add(aspectOfTheEnd.getItemId());
        items.put(aspectOfTheEnd.getItemId(), aspectOfTheEnd);
    }

    private static void createGolemSword(){
        String name = "&9Golem Sword";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+8");
        lore.add("&7Intelligence: &b+40");
        lore.add("&7Defence: &a+80");
        lore.add("&7Resistance: &3+60");
        lore.add("");
        lore.add("&6Item Ability: Iron Punch &eRIGHT CLICK");
        lore.add("&7A giant pile of iron appears above you");
        lore.add("&7and powerfully slams the ground dealing");
        lore.add("&c300% &7weapon's damage.");
        lore.add("&8Mana Cost: &3140");
        lore.add("");
        lore.add("&9&lRARE SWORD");
        lore.add("&8item_id:golem_sword");
        ItemStack item = createItem(Material.IRON_SWORD, name, lore);
        golemSword = new Item(item);
        itemIds.add(golemSword.getItemId());
        items.put(golemSword.getItemId(), golemSword);
    }

    private static void createHealingStaff(){
        String name = "&9Healing Staff";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+9");
        lore.add("&7Health Regen: &4+1");
        lore.add("");
        lore.add("&6Item Ability: Medium Heal &eRIGHT CLICK");
        lore.add("&7Gives you regeneration for &a5s.");
        lore.add("&8Mana Cost: &340");
        lore.add("&8Cooldown: &a15s");
        lore.add("");
        lore.add("&9&lRARE STAFF");
        lore.add("&8item_id:healing_staff");
        ItemStack item = createItem(Material.STICK, name, lore);
        healingStaff = new Item(item);
        itemIds.add(healingStaff.getItemId());
        items.put(healingStaff.getItemId(), healingStaff);
    }

    private static void createUndeadSword(){
        String name = "&aUndead Sword";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+15");
        lore.add("&7Health Regen: &4+1");
        lore.add("");
        lore.add("&6Ability: Smite");
        lore.add("&7Deal &c2x &7damage to skeletons and zombies.");
        lore.add("");
        lore.add("&a&lUNCOMMON SWORD");
        lore.add("&8item_id:undead_sword");
        ItemStack item = createItem(Material.STONE_SWORD, name, lore);
        undeadSword = new Item(item);
        itemIds.add(undeadSword.getItemId());
        items.put(undeadSword.getItemId(), undeadSword);
    }

    private static void createStormRuler(){
        String name = "&9Storm Ruler";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+30");
        lore.add("&7Critical Chance: &c+10");
        lore.add("");
        lore.add("&6Ability: Storm King &eRIGHT CLICK");
        lore.add("&7Fires a wave at the enemies dealing");
        lore.add("&c110 + 112% &7your strength magic damage.");
        lore.add("&8Cooldown: &a10s");
        lore.add("");
        lore.add("&9&lRARE GREAT SWORD");
        lore.add("&8item_id:storm_ruler");
        ItemStack item = createItem(Material.NETHERITE_SWORD, name, lore);
        stormRuler = new Item(item);
        itemIds.add("storm_ruler");
        items.put("storm_ruler", stormRuler);
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
        items.put("long_bow", longBow);
    }

    private static void createUnstableTome(){
        String name = "&aUnstable Tome";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+1");
        lore.add("&7Ability Damage: &3+30");
        lore.add("&7Intelligence: &b+20");
        lore.add("");
        lore.add("&6Ability: Magic Missile &eRIGHT CLICK");
        lore.add("&7Fires a magic missile dealing");
        lore.add("&320 + 110% &7your ability damage.");
        lore.add("&8Mana Cost: &330");
        lore.add("&8Cooldown: &a1.5s");
        lore.add("");
        lore.add("&a&lUNCOMMON BOOK");
        lore.add("&8item_id:unstable_tome");
        ItemStack item = createItem(Material.BOOK, name, lore);
        unstableTome = new Item(item);
        itemIds.add("unstable_tome");
        items.put("unstable_tome", unstableTome);
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
        items.put("solid_stone_axe", solidStoneAxe);
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
        items.put("leeching_staff", leechingStaff);
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
        items.put("stone_stick", stoneStick);
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
        items.put("basic_sword", basicSword);
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
        items.put("test_item", testItem);
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
        items.put("blade_of_the_universe", bladeOfTheUniverse);
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

    private static ItemStack getSkull(String base64) {
        return SkullCreator.itemFromBase64(base64);
    }

    public static Item getItemFromPlayer(ItemStack item){
        return new Item(item);
    }
}
