package net.pullolo.magicarena.items;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    //epic
    public static Item leapingSword;
    public static Item rodOfChaos; //dungeonItem

    //legendary
    public static Item auroraStaff;
    public static Item priscillasDagger;
    //mythic
    public static Item kusumibaru;
    public static Item hyperion; //dps
    public static Item atomSplitKatana; //dps
    public static Item kunai; //dps
    public static Item bactaNade; //healer
    public static Item quenAxe; //tank
    public static Item terminator; //archer
    public static Item scorpionChainDart;

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

        createLeapingSword();
        createRodOfChaos();

        createAuroraStaff();
        createPriscillasDagger();

        createKusumibaru();
        createAtomSplitKatana();
        createHyperion();
        createKunai();
        createQuenAxe();
        createTerminator();
        createBactaNade();
        createScorpionChainDart();

        createBladeOfTheUniverse();
    }
    private static void createBactaNade(){
        String name = "&dBactaNade";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+1");
        lore.add("");
        lore.add("&6Item Ability: Healing Grenade &eRIGHT CLICK");
        lore.add("&7The grenade will heal players in 4 block radius");
        lore.add("&7for &540% &7player's health.");
        lore.add("&8Cooldown: &a30s");
        lore.add("");
        lore.add("&d&lMYTHIC HEAL GRENADE");
        lore.add("&8item_id:bacta_nade");
        ItemStack item = createItem(Material.SNOWBALL, name, lore);
        bactaNade = new Item(item);
        itemIds.add(bactaNade.getItemId());
        items.put(bactaNade.getItemId(), bactaNade);
    }

    private static void createTerminator(){
        String name = "&dTerminator";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+695");
        lore.add("&7Critical Chance: &c-60");
        lore.add("&7Critical Damage: &c+2200");
        lore.add("");
        lore.add("&6Shortbow: Instantly shoots!");
        lore.add("&7Shoot &b3 &7arrows at once.");
        lore.add("");
        lore.add("&d&lMYTHIC SHORTBOW");
        lore.add("&8item_id:terminator");
        ItemStack item = createItem(Material.BOW, name, lore);
        terminator = new Item(item);
        itemIds.add(terminator.getItemId());
        items.put(terminator.getItemId(), terminator);
    }
    private static void createQuenAxe(){
        String name = "&dQuen Axe";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+315");
        lore.add("&7Intelligence: &b+100");
        lore.add("&7Critical Chance: &c+5");
        lore.add("&7Critical Damage: &c+90");
        lore.add("");
        lore.add("&6Item Ability: Quen Shield &eRIGHT CLICK");
        lore.add("&7You get a shield which negates the first hit");
        lore.add("&7and explodes on impact damaging nearby enemies for");
        lore.add("&3200% &7base damage as magic damage.");
        lore.add("&8Mana Cost: &3100");
        lore.add("&8Cooldown: &a30s");
        lore.add("");
        lore.add("&d&lMYTHIC AXE");
        lore.add("&8item_id:quen_axe");
        ItemStack item = createItem(Material.GOLDEN_AXE, name, lore);
        quenAxe = new Item(item);
        itemIds.add(quenAxe.getItemId());
        items.put(quenAxe.getItemId(), quenAxe);
    }

    private static void createKunai(){
        String name = "&dKunai";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+700");
        lore.add("&7Ability Damage: &3+2390");
        lore.add("&7Critical Chance: &c+70");
        lore.add("&7Critical Damage: &c+550");
        lore.add("");
        lore.add("&6Item Ability: Throw kunai &eRIGHT CLICK");
        lore.add("&7You throw a kunai where your character is looking.");
        lore.add("&8Cooldown: &a1.5s");
        lore.add("");
        lore.add("&d&lMYTHIC DAGGER");
        lore.add("&8item_id:kunai");
        ItemStack item = createItem(Material.IRON_SWORD, name, lore);
        kunai = new Item(item);
        itemIds.add(kunai.getItemId());
        items.put(kunai.getItemId(), kunai);
    }

    private static void createHyperion(){
        String name = "&dHyperion";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+395");
        lore.add("&7Intelligence: &b+500");
        lore.add("&7Ability Damage: &3+2390");
        lore.add("&7Critical Chance: &c+50");
        lore.add("&7Critical Damage: &c+300");
        lore.add("");
        lore.add("&6Item Ability: Wither Impact &eRIGHT CLICK");
        lore.add("&7Teleport &a10 blocks &7forward and ");
        lore.add("&7deal &3(base/10)*(100% + 200%)&7 your ability");
        lore.add("&7damage and intelligence to nearby enemies.");
        lore.add("&8Mana Cost: &3300");
        lore.add("");
        lore.add("&d&lMYTHIC SWORD");
        lore.add("&8item_id:hyperion");
        ItemStack item = createItem(Material.IRON_SWORD, name, lore);
        hyperion = new Item(item);
        itemIds.add(hyperion.getItemId());
        items.put(hyperion.getItemId(), hyperion);
    }

    private static void createAtomSplitKatana(){
        String name = "&dAtom Split Katana";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+900");
        lore.add("&7Critical Chance: &c+100");
        lore.add("&7Critical Damage: &c+950");
        lore.add("");
        lore.add("&6Item Ability: Atom Split &eRIGHT CLICK");
        lore.add("&7You have a 50% chance to double strike the target.");
        lore.add("");
        lore.add("&d&lMYTHIC KATANA");
        lore.add("&8item_id:atom_split_katana");
        ItemStack item = createItem(Material.DIAMOND_SWORD, name, lore);
        atomSplitKatana = new Item(item);
        itemIds.add(atomSplitKatana.getItemId());
        items.put(atomSplitKatana.getItemId(), atomSplitKatana);
    }

    private static void createScorpionChainDart(){
        String name = "&dScorpion Chain Dart";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+1");
        lore.add("&7Ability Damage: &3+390");
        lore.add("&7Critical Chance: &c+50");
        lore.add("&7Critical Damage: &c+150");
        lore.add("");
        lore.add("&6Item Ability: Pull &eRIGHT CLICK");
        lore.add("&7Throws out a whip, which if you pull");
        lore.add("&7back deals &3400 + 300% &7ability damage ");
        lore.add("&7to the target and set it on hellfire.");
        lore.add("");
        lore.add("&d&lMYTHIC DUNGEON WHIP");
        lore.add("&8item_id:scorpion_chain_dart");
        ItemStack item = createItem(Material.FISHING_ROD, name, lore);
        scorpionChainDart = new Item(item);
        itemIds.add(scorpionChainDart.getItemId());
        items.put(scorpionChainDart.getItemId(), scorpionChainDart);
    }

    private static void createPriscillasDagger(){
        String name = "&6Priscilla's Dagger";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+310");
        lore.add("&7Critical Chance: &c+100");
        lore.add("&7Critical Damage: &c+110");
        lore.add("");
        lore.add("&6Item Ability: Backstab");
        lore.add("&7Your Crit deals &c2x &7damage from");
        lore.add("&7behind the target.");
        lore.add("");
        lore.add("&6&lLEGENDARY DUNGEON DAGGER");
        lore.add("&8item_id:priscillas_dagger");
        ItemStack item = createItem(Material.IRON_SWORD, name, lore);
        priscillasDagger = new Item(item);
        itemIds.add(priscillasDagger.getItemId());
        items.put(priscillasDagger.getItemId(), priscillasDagger);
    }

    private static void createRodOfChaos(){
        String name = "&5Rod of Chaos";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+1");
        lore.add("&7Ability Damage: &3+135");
        lore.add("&7Intelligence: &b+50");
        lore.add("");
        lore.add("&6Ability: Life Steal &eRIGHT CLICK");
        lore.add("&7Steals &410% &7health from enemies.");
        lore.add("&8Mana Cost: &350%");
        lore.add("&8Cooldown: &a0.5s");
        lore.add("");
        lore.add("&5&lEPIC DUNGEON ROD");
        lore.add("&8item_id:rod_of_chaos");
        ItemStack item = createItem(Material.AMETHYST_SHARD, name, lore);
        rodOfChaos = new Item(item);
        itemIds.add(rodOfChaos.getItemId());
        items.put(rodOfChaos.getItemId(), rodOfChaos);
    }

    private static void createKusumibaru(){
        String name = "&dKusumibaru";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+440");
        lore.add("&7Critical Chance: &c+50");
        lore.add("&7Critical Damage: &c+290");
        lore.add("");
        lore.add("&6Item Ability: Dash & Slice &eRIGHT CLICK");
        lore.add("&7Dash through the enemies to deal");
        lore.add("&c500% &7damage.");
        lore.add("&8Cooldown: &a6s");
        lore.add("");
        lore.add("&d&lMYTHIC SWORD");
        lore.add("&8item_id:kusumibaru");
        ItemStack item = createItem(Material.NETHERITE_SWORD, name, lore);
        kusumibaru = new Item(item);
        itemIds.add(kusumibaru.getItemId());
        items.put(kusumibaru.getItemId(), kusumibaru);
    }

    private static void createAuroraStaff(){
        String name = "&6Aurora Staff";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+1");
        lore.add("&7Intelligence: &b+300");
        lore.add("&7Ability Damage: &3+290");
        lore.add("&7Critical Chance: &c+50");
        lore.add("&7Critical Damage: &c+80");
        lore.add("");
        lore.add("&6Item Ability: Arcane Zap &eRIGHT CLICK");
        lore.add("&7Fires a beam of runic energy dealing");
        lore.add("&3300 + 300% &7ability damage. The more distance");
        lore.add("&7travelled the less damage it deals.");
        lore.add("&8Mana Cost: &310");
        lore.add("&8Cooldown: &a1s");
        lore.add("");
        lore.add("&6&lLEGENDARY STAFF");
        lore.add("&8item_id:aurora_staff");
        ItemStack item = createItem(Material.BLAZE_ROD, name, lore);
        auroraStaff = new Item(item);
        itemIds.add(auroraStaff.getItemId());
        items.put(auroraStaff.getItemId(), auroraStaff);
    }

    private static void createLeapingSword(){
        String name = "&5Leaping Sword";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+165");
        lore.add("&7Critical Chance: &c+20");
        lore.add("&7Critical Damage: &c+120");
        lore.add("");
        lore.add("&6Item Ability: Leap &eRIGHT CLICK");
        lore.add("&7Leap into the air and deal &a180%");
        lore.add("&7base damage to nearby enemies.");
        lore.add("&8Mana Cost: &3100");
        lore.add("&8Cooldown: &a10s");
        lore.add("");
        lore.add("&5&lEPIC SWORD");
        lore.add("&8item_id:leaping_sword");
        ItemStack item = createItem(Material.IRON_SWORD, name, lore);
        leapingSword = new Item(item);
        itemIds.add(leapingSword.getItemId());
        items.put(leapingSword.getItemId(), leapingSword);
    }

    private static void createBrutalityBlade(){
        String name = "&9Brutality Blade";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+85");
        lore.add("&7Critical Chance: &c+10");
        lore.add("&7Critical Damage: &c+40");
        lore.add("");
        lore.add("&6Item Ability: Brutality");
        lore.add("&7If the target is below &a50% &7health");
        lore.add("&7deal &c200% &7damage.");
        lore.add("");
        lore.add("&9&lRARE DUNGEON AXE");
        lore.add("&8item_id:brutality_blade");
        ItemStack item = createItem(Material.NETHERITE_AXE, name, lore);
        brutalityBlade = new Item(item);
        itemIds.add(brutalityBlade.getItemId());
        items.put(brutalityBlade.getItemId(), brutalityBlade);
    }

    private static void createFlamingBow(){
        String name = "&9Flaming Bow";
        List<String> lore = new ArrayList<>();

        lore.add("&7Damage: &c+80");
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

        lore.add("&7Damage: &c+110");
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

        lore.add("&7Damage: &c+30");
        lore.add("&7Intelligence: &b+50");
        lore.add("&7Defence: &a+180");
        lore.add("&7Resistance: &3+90");
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

        lore.add("&7Damage: &c+25");
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

        lore.add("&7Damage: &c+70");
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
        lore.add("&8Mana Cost: &310");
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

    private static ItemStack getSkull(String base64) {
        return SkullCreator.itemFromBase64(base64);
    }

    public static Item getItemFromPlayer(ItemStack item){
        return new Item(item);
    }
}
