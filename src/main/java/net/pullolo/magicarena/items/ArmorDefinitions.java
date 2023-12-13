package net.pullolo.magicarena.items;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ArmorDefinitions {

    public static Item testHelmet;
    public static Item testChestplate;
    public static Item testLeggings;
    public static Item testBoots;

    public static Item wandererHelmet;
    public static Item wandererChestplate;
    public static Item wandererLeggings;
    public static Item wandererBoots;
    //rare
    public static Item hardenedDiamondHelmet;
    public static Item hardenedDiamondChestplate;
    public static Item hardenedDiamondLeggings;
    public static Item hardenedDiamondBoots;
    //epic
    public static Item angelHelmet;
    public static Item angelChestplate;
    public static Item angelLeggings;
    public static Item angelBoots;
    //legendary
    public static Item superiorHelmet;
    public static Item superiorChestplate;
    public static Item superiorLeggings;
    public static Item superiorBoots;
    //mythic
    public static Item shadowweaveShroudHelmet;
    public static Item shadowweaveShroudChestplate;
    public static Item shadowweaveShroudLeggings;
    public static Item shadowweaveShroudBoots;

    public static final ArrayList<String> armorItemIds = new ArrayList<>();
    public static final HashMap<String, ArrayList<Item>> armorItems = new HashMap<>();

    public static void init(){
        createTestArmor();

        createWandererArmor();

        createHardenedDiamondArmor();

        createAngelArmor();

        createSuperiorArmor();

        createShadowweaveShroud();
    }

    private static void createShadowweaveShroud(){
        ArrayList<Item> pieces = new ArrayList<>();
        String name1 = "&dShadowweave Shroud Helmet";
        List<String> lore1 = new ArrayList<>();

        lore1.add("&7Health: &4+100");
        lore1.add("&7Defence: &a+235");
        lore1.add("&7Resistance: &3+230");
        lore1.add("&7Damage: &c+160");
        lore1.add("&7Critical Damage: &c+150");
        lore1.add("&7Critical Chance: &c+20");
        lore1.add("§7Intelligence: §b+200");
        lore1.add("&7Mana Regen: &b+2");
        lore1.add("&7Speed: &f+5");
        lore1.add("");
        lore1.add("&6Full Set Bonus: Teared Into The Void");
        lore1.add("&7If you die u are teleported to your ally.");
        lore1.add("&8Cooldown: &a120s");
        lore1.add("");
        lore1.add("&d&lMYTHIC HELMET");
        lore1.add("&8item_id:shadowweave_shroud_helmet");
        ItemStack item1 = createTrimItem(Material.NETHERITE_HELMET, TrimPattern.WARD, TrimMaterial.AMETHYST, name1, lore1);
        shadowweaveShroudHelmet = new Item(item1, 0);
        armorItemIds.add(shadowweaveShroudHelmet.getItemId());
        pieces.add(shadowweaveShroudHelmet);

        String name2 = "&dShadowweave Shroud Chestplate";
        List<String> lore2 = new ArrayList<>();

        lore2.add("&7Health: &4+100");
        lore2.add("&7Defence: &a+235");
        lore2.add("&7Resistance: &3+230");
        lore2.add("&7Damage: &c+350");
        lore2.add("&7Critical Damage: &c+200");
        lore2.add("&7Critical Chance: &c+20");
        lore2.add("§7Intelligence: §b+200");
        lore2.add("&7Mana Regen: &b+2");
        lore2.add("&7Speed: &f+5");
        lore2.add("");
        lore2.add("&6Full Set Bonus: Teared Into The Void");
        lore2.add("&7If you die u are teleported to your ally.");
        lore2.add("&8Cooldown: &a120s");
        lore2.add("");
        lore2.add("&d&lMYTHIC CHESTPLATE");
        lore2.add("&8item_id:shadowweave_shroud_chestplate");
        ItemStack item2 = createTrimItem(Material.NETHERITE_CHESTPLATE, TrimPattern.TIDE, TrimMaterial.AMETHYST, name2, lore2);
        shadowweaveShroudChestplate = new Item(item2, 0);
        armorItemIds.add(shadowweaveShroudChestplate.getItemId());
        pieces.add(shadowweaveShroudChestplate);

        String name3 = "&dShadowweave Shroud Leggings";
        List<String> lore3 = new ArrayList<>();

        lore3.add("&7Health: &4+100");
        lore3.add("&7Defence: &a+235");
        lore3.add("&7Resistance: &3+230");
        lore3.add("&7Damage: &c+190");
        lore3.add("&7Critical Damage: &c+150");
        lore3.add("&7Critical Chance: &c+20");
        lore3.add("§7Intelligence: §b+200");
        lore3.add("&7Mana Regen: &b+2");
        lore3.add("&7Speed: &f+5");
        lore3.add("");
        lore3.add("&6Full Set Bonus: Teared Into The Void");
        lore3.add("&7If you die u are teleported to your ally.");
        lore3.add("&8Cooldown: &a120s");
        lore3.add("");
        lore3.add("&d&lMYTHIC LEGGINGS");
        lore3.add("&8item_id:shadowweave_shroud_leggings");
        ItemStack item3 = createTrimItem(Material.NETHERITE_LEGGINGS, TrimPattern.SILENCE, TrimMaterial.AMETHYST, name3, lore3);
        shadowweaveShroudLeggings = new Item(item3, 0);
        armorItemIds.add(shadowweaveShroudLeggings.getItemId());
        pieces.add(shadowweaveShroudLeggings);

        String name4 = "&dShadowweave Shroud Boots";
        List<String> lore4 = new ArrayList<>();

        lore4.add("&7Health: &4+100");
        lore4.add("&7Defence: &a+235");
        lore4.add("&7Resistance: &3+230");
        lore4.add("&7Damage: &c+150");
        lore4.add("&7Critical Damage: &c+150");
        lore4.add("&7Critical Chance: &c+20");
        lore4.add("§7Intelligence: §b+200");
        lore4.add("&7Mana Regen: &b+2");
        lore4.add("&7Speed: &f+5");
        lore4.add("");
        lore4.add("&6Full Set Bonus: Teared Into The Void");
        lore4.add("&7If you die u are teleported to your ally.");
        lore4.add("&8Cooldown: &a120s");
        lore4.add("");
        lore4.add("&6Piece Bonus: Shadow Step &eSHIFT");
        lore4.add("&7Teleport a short distance ahead of you.");
        lore4.add("&8Mana Cost: &350");
        lore4.add("&8Cooldown: &a1s");
        lore4.add("");
        lore4.add("&d&lMYTHIC BOOTS");
        lore4.add("&8item_id:shadowweave_shroud_boots");
        ItemStack item4 = createTrimItem(Material.NETHERITE_BOOTS, TrimPattern.COAST, TrimMaterial.AMETHYST, name4, lore4);
        shadowweaveShroudBoots = new Item(item4, 0);
        armorItemIds.add(shadowweaveShroudBoots.getItemId());
        pieces.add(shadowweaveShroudBoots);

        armorItems.put("shadowweave_shroud", pieces);
    }

    private  static void createSuperiorArmor(){
        ArrayList<Item> pieces = new ArrayList<>();
        String name1 = "&6Superior Dragon Helmet";
        List<String> lore1 = new ArrayList<>();

        lore1.add("&7Health: &4+390");
        lore1.add("&7Defence: &a+335");
        lore1.add("&7Resistance: &3+330");
        lore1.add("&7Damage: &c+60");
        lore1.add("&7Critical Damage: &c+50");
        lore1.add("&7Critical Chance: &c+20");
        lore1.add("§7Intelligence: §b+200");
        lore1.add("&7Mana Regen: &b+2");
        lore1.add("&7Speed: &f+20");
        lore1.add("");
        lore1.add("&6Full Set Bonus: Superior Blood");
        lore1.add("&7Most of your stats are increased by &a20&7.");
        lore1.add("");
        lore1.add("&6&lLEGENDARY HELMET");
        lore1.add("&8item_id:superior_helmet");
        ItemStack item1 = createSkullItem(name1, lore1, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzU1OGVmYmU2Njk3NjA5OWNmZDYyNzYwZDllMDUxNzBkMmJiOGY1MWU2ODgyOWFiOGEwNTFjNDhjYmM0MTVjYiJ9fX0=");
        superiorHelmet = new Item(item1, 0);
        armorItemIds.add(superiorHelmet.getItemId());
        pieces.add(superiorHelmet);

        String name2 = "&6Superior Dragon Chestplate";
        List<String> lore2 = new ArrayList<>();

        lore2.add("&7Health: &4+490");
        lore2.add("&7Defence: &a+435");
        lore2.add("&7Resistance: &3+330");
        lore2.add("&7Damage: &c+100");
        lore2.add("&7Critical Damage: &c+50");
        lore2.add("&7Critical Chance: &c+5");
        lore2.add("§7Intelligence: §b+200");
        lore2.add("&7Mana Regen: &b+2");
        lore2.add("&7Speed: &f+20");
        lore2.add("");
        lore2.add("&6Full Set Bonus: Superior Blood");
        lore2.add("&7Most of your stats are increased by &a20&7.");
        lore2.add("");
        lore2.add("&6&lLEGENDARY CHESTPLATE");
        lore2.add("&8item_id:superior_chestplate");
        ItemStack item2 = createColoredItem(Material.LEATHER_CHESTPLATE, Color.YELLOW, name2, lore2);
        superiorChestplate = new Item(item2, 0);
        armorItemIds.add(superiorChestplate.getItemId());
        pieces.add(superiorChestplate);

        String name3 = "&6Superior Dragon Leggings";
        List<String> lore3 = new ArrayList<>();

        lore3.add("&7Health: &4+420");
        lore3.add("&7Defence: &a+335");
        lore3.add("&7Resistance: &3+330");
        lore3.add("&7Damage: &c+90");
        lore3.add("&7Critical Damage: &c+50");
        lore3.add("&7Critical Chance: &c+5");
        lore3.add("§7Intelligence: §b+200");
        lore3.add("&7Mana Regen: &b+2");
        lore3.add("&7Speed: &f+20");
        lore3.add("");
        lore3.add("&6Full Set Bonus: Superior Blood");
        lore3.add("&7Most of your stats are increased by &a20&7.");
        lore3.add("");
        lore3.add("&6&lLEGENDARY LEGGINGS");
        lore3.add("&8item_id:superior_leggings");
        ItemStack item3 = createColoredItem(Material.LEATHER_LEGGINGS, Color.YELLOW, name3, lore3);
        superiorLeggings = new Item(item3, 0);
        armorItemIds.add(superiorLeggings.getItemId());
        pieces.add(superiorLeggings);

        String name4 = "&6Superior Dragon Boots";
        List<String> lore4 = new ArrayList<>();

        lore4.add("&7Health: &4+390");
        lore4.add("&7Defence: &a+335");
        lore4.add("&7Resistance: &3+330");
        lore4.add("&7Damage: &c+50");
        lore4.add("&7Critical Damage: &c+50");
        lore4.add("&7Critical Chance: &c+5");
        lore4.add("§7Intelligence: §b+200");
        lore4.add("&7Mana Regen: &b+2");
        lore4.add("&7Speed: &f+20");
        lore4.add("");
        lore4.add("&6Full Set Bonus: Superior Blood");
        lore4.add("&7Most of your stats are increased by &a20&7.");
        lore4.add("");
        lore4.add("&6&lLEGENDARY BOOTS");
        lore4.add("&8item_id:superior_boots");
        ItemStack item4 = createColoredItem(Material.LEATHER_BOOTS, Color.ORANGE, name4, lore4);
        superiorBoots = new Item(item4, 0);
        armorItemIds.add(superiorBoots.getItemId());
        pieces.add(superiorBoots);

        armorItems.put("superior_armor", pieces);
    }

    private  static void createAngelArmor(){
        ArrayList<Item> pieces = new ArrayList<>();
        String name1 = "&5Angel Helmet";
        List<String> lore1 = new ArrayList<>();

        lore1.add("&7Health: &4+190");
        lore1.add("&7Defence: &a+135");
        lore1.add("&7Resistance: &3+130");
        lore1.add("&7Critical Damage: &c+20");
        lore1.add("&7Critical Chance: &c+5");
        lore1.add("");
        lore1.add("&6Full Set Bonus: Salvation");
        lore1.add("&7You negate the &a1st&7 hit.");
        lore1.add("&8Cooldown: &a20s");
        lore1.add("");
        lore1.add("&5&lEPIC HELMET");
        lore1.add("&8item_id:angel_helmet");
        ItemStack item1 = createSkullItem(name1, lore1, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2ZlNWRjNTEyZGQ1OTFhYjAyZWJkMzcyYzI4NWY0ODFjODc2YjQ5MWNiYWQyMDU2NjUzZGQxMTg5Yzg0ZGVmZiJ9fX0=");
        angelHelmet = new Item(item1, 0);
        armorItemIds.add(angelHelmet.getItemId());
        pieces.add(angelHelmet);

        String name2 = "&5Angel Chestplate";
        List<String> lore2 = new ArrayList<>();

        lore2.add("&7Health: &4+230");
        lore2.add("&7Defence: &a+150");
        lore2.add("&7Resistance: &3+180");
        lore2.add("&7Critical Damage: &c+20");
        lore2.add("&7Critical Chance: &c+5");
        lore2.add("");
        lore2.add("&6Full Set Bonus: Salvation");
        lore2.add("&7You negate the &a1st&7 hit.");
        lore2.add("&8Cooldown: &a20s");
        lore2.add("");
        lore2.add("&5&lEPIC CHESTPLATE");
        lore2.add("&8item_id:angel_chestplate");
        ItemStack item2 = createItem(Material.GOLDEN_CHESTPLATE, name2, lore2);
        angelChestplate = new Item(item2, 0);
        armorItemIds.add(angelChestplate.getItemId());
        pieces.add(angelChestplate);

        String name3 = "&5Angel Leggings";
        List<String> lore3 = new ArrayList<>();

        lore3.add("&7Health: &4+175");
        lore3.add("&7Defence: &a+100");
        lore3.add("&7Resistance: &3+20");
        lore3.add("&7Critical Damage: &c+20");
        lore3.add("&7Critical Chance: &c+5");
        lore3.add("");
        lore3.add("&6Full Set Bonus: Salvation");
        lore3.add("&7You negate the &a1st&7 hit.");
        lore3.add("&8Cooldown: &a20s");
        lore3.add("");
        lore3.add("&5&lEPIC LEGGINGS");
        lore3.add("&8item_id:angel_leggings");
        ItemStack item3 = createColoredItem(Material.LEATHER_LEGGINGS, Color.WHITE, name3, lore3);
        angelLeggings = new Item(item3, 0);
        armorItemIds.add(angelLeggings.getItemId());
        pieces.add(angelLeggings);

        String name4 = "&5Angel Boots";
        List<String> lore4 = new ArrayList<>();

        lore4.add("&7Health: &4+120");
        lore4.add("&7Defence: &a+70");
        lore4.add("&7Resistance: &3+20");
        lore4.add("&7Critical Damage: &c+20");
        lore4.add("&7Critical Chance: &c+5");
        lore4.add("");
        lore4.add("&6Full Set Bonus: Salvation");
        lore4.add("&7You negate the &a1st&7 hit.");
        lore4.add("&8Cooldown: &a20s");
        lore4.add("");
        lore4.add("&5&lEPIC BOOTS");
        lore4.add("&8item_id:angel_boots");
        ItemStack item4 = createColoredItem(Material.LEATHER_BOOTS, Color.WHITE, name4, lore4);
        angelBoots = new Item(item4, 0);
        armorItemIds.add(angelBoots.getItemId());
        pieces.add(angelBoots);

        armorItems.put("angel_armor", pieces);
    }

    private static void createHardenedDiamondArmor(){
        ArrayList<Item> pieces = new ArrayList<>();
        String name1 = "&9Hardened Diamond Helmet";
        List<String> lore1 = new ArrayList<>();

        lore1.add("&7Health: &4+50");
        lore1.add("&7Defence: &a+80");
        lore1.add("&7Resistance: &3+20");
        lore1.add("");
        lore1.add("&9&lRARE HELMET");
        lore1.add("&8item_id:hardened_diamond_helmet");
        ItemStack item1 = createItem(Material.DIAMOND_HELMET, name1, lore1);
        hardenedDiamondHelmet = new Item(item1, 0);
        armorItemIds.add(hardenedDiamondHelmet.getItemId());
        pieces.add(hardenedDiamondHelmet);

        String name2 = "&9Hardened Diamond Chestplate";
        List<String> lore2 = new ArrayList<>();

        lore2.add("&7Health: &4+100");
        lore2.add("&7Defence: &a+120");
        lore2.add("&7Resistance: &3+20");
        lore2.add("&7Speed: &f-5");
        lore2.add("");
        lore2.add("&9&lRARE CHESTPLATE");
        lore2.add("&8item_id:hardened_diamond_chestplate");
        ItemStack item2 = createItem(Material.DIAMOND_CHESTPLATE, name2, lore2);
        hardenedDiamondChestplate = new Item(item2, 0);
        armorItemIds.add(hardenedDiamondChestplate.getItemId());
        pieces.add(hardenedDiamondChestplate);

        String name3 = "&9Hardened Diamond Leggings";
        List<String> lore3 = new ArrayList<>();

        lore3.add("&7Health: &4+50");
        lore3.add("&7Defence: &a+90");
        lore3.add("&7Resistance: &3+20");
        lore3.add("&7Speed: &f-3");
        lore3.add("");
        lore3.add("&9&lRARE LEGGINGS");
        lore3.add("&8item_id:hardened_diamond_leggings");
        ItemStack item3 = createItem(Material.DIAMOND_LEGGINGS, name3, lore3);
        hardenedDiamondLeggings = new Item(item3, 0);
        armorItemIds.add(hardenedDiamondLeggings.getItemId());
        pieces.add(hardenedDiamondLeggings);

        String name4 = "&9Hardened Diamond Boots";
        List<String> lore4 = new ArrayList<>();

        lore4.add("&7Health: &4+50");
        lore4.add("&7Defence: &a+70");
        lore4.add("&7Resistance: &3+20");
        lore4.add("");
        lore4.add("&9&lRARE BOOTS");
        lore4.add("&8item_id:hardened_diamond_boots");
        ItemStack item4 = createItem(Material.DIAMOND_BOOTS, name4, lore4);
        hardenedDiamondBoots = new Item(item4, 0);
        armorItemIds.add(hardenedDiamondBoots.getItemId());
        pieces.add(hardenedDiamondBoots);

        armorItems.put("hardened_diamond_armor", pieces);
    }

    private static void createWandererArmor(){
        ArrayList<Item> pieces = new ArrayList<>();
        String name1 = "&aWanderer Helmet";
        List<String> lore1 = new ArrayList<>();

        lore1.add("&7Health: &4+20");
        lore1.add("&7Defence: &a+20");
        lore1.add("");
        lore1.add("&a&lUNCOMMON HELMET");
        lore1.add("&8item_id:wanderer_helmet");
        ItemStack item1 = createItem(Material.LEATHER_HELMET, name1, lore1);
        wandererHelmet = new Item(item1, 0);
        armorItemIds.add("wanderer_helmet");
        pieces.add(wandererHelmet);

        String name2 = "&aWanderer Chestplate";
        List<String> lore2 = new ArrayList<>();

        lore2.add("&7Health: &4+20");
        lore2.add("&7Defence: &a+30");
        lore2.add("&7Damage: &c+20");
        lore2.add("");
        lore2.add("&a&lUNCOMMON CHESTPLATE");
        lore2.add("&8item_id:wanderer_chestplate");
        ItemStack item2 = createItem(Material.LEATHER_CHESTPLATE, name2, lore2);
        wandererChestplate = new Item(item2, 0);
        armorItemIds.add("wanderer_chestplate");
        pieces.add(wandererChestplate);

        String name3 = "&aWanderer Leggings";
        List<String> lore3 = new ArrayList<>();

        lore3.add("&7Health: &4+20");
        lore3.add("&7Defence: &a+25");
        lore3.add("&7Speed: &f+5");
        lore3.add("");
        lore3.add("&a&lUNCOMMON LEGGINGS");
        lore3.add("&8item_id:wanderer_leggings");
        ItemStack item3 = createItem(Material.LEATHER_LEGGINGS, name3, lore3);
        wandererLeggings = new Item(item3, 0);
        armorItemIds.add("wanderer_leggings");
        pieces.add(wandererLeggings);

        String name4 = "&aWanderer Boots";
        List<String> lore4 = new ArrayList<>();

        lore4.add("&7Health: &4+20");
        lore4.add("&7Defence: &a+20");
        lore4.add("&7Speed: &f+2");
        lore4.add("");
        lore4.add("&a&lUNCOMMON BOOTS");
        lore4.add("&8item_id:wanderer_boots");
        ItemStack item4 = createItem(Material.LEATHER_BOOTS, name4, lore4);
        wandererBoots = new Item(item4, 0);
        armorItemIds.add("wanderer_boots");
        pieces.add(wandererBoots);

        armorItems.put("wanderer_armor", pieces);
    }

    private static void createTestArmor(){
        ArrayList<Item> pieces = new ArrayList<>();
        String name1 = "&aTest Helmet";
        List<String> lore1 = new ArrayList<>();

        lore1.add("&7Health: &4+20");
        //lore1.add("&7Health Regen: &4+20");
        lore1.add("&7Defence: &a+20");
        lore1.add("&7Resistance: &3+20");
        //lore1.add("&7Mana Regen: &b+20");
//        lore1.add("&7Damage: &c+20");
        lore1.add("&7Critical Chance: &c+20");
        lore1.add("&7Critical Damage: &c+20");
        lore1.add("&7Intelligence: &b+20");
        lore1.add("&7Ability Damage: &3+20");
        lore1.add("&7Speed: &f+20");
        lore1.add("");
        lore1.add("&a&lUNCOMMON HELMET");
        lore1.add("&8item_id:test_helmet");
        ItemStack item1 = createItem(Material.IRON_HELMET, name1, lore1);
        testHelmet = new Item(item1, 0);
        armorItemIds.add("test_helmet");
        pieces.add(testHelmet);

        String name2 = "&aTest Chestplate";
        List<String> lore2 = new ArrayList<>();

        lore2.add("&7Health: &4+20");
        //lore2.add("&7Health Regen: &4+20");
        lore2.add("&7Defence: &a+20");
        lore2.add("&7Resistance: &3+20");
        //lore2.add("&7Mana Regen: &b+20");
//        lore2.add("&7Damage: &c+20");
        lore2.add("&7Critical Chance: &c+20");
        lore2.add("&7Critical Damage: &c+20");
        lore2.add("&7Intelligence: &b+20");
        lore2.add("&7Ability Damage: &3+20");
        lore2.add("&7Speed: &f+20");
        lore2.add("");
        lore2.add("&a&lUNCOMMON CHESTPLATE");
        lore2.add("&8item_id:test_chestplate");
        ItemStack item2 = createItem(Material.IRON_CHESTPLATE, name2, lore2);
        testChestplate = new Item(item2, 0);
        armorItemIds.add("test_chestplate");
        pieces.add(testChestplate);

        String name3 = "&aTest Leggings";
        List<String> lore3 = new ArrayList<>();

        lore3.add("&7Health: &4+20");
        //lore3.add("&7Health Regen: &4+20");
        lore3.add("&7Defence: &a+20");
        lore3.add("&7Resistance: &3+20");
        //lore3.add("&7Mana Regen: &b+20");
//        lore3.add("&7Damage: &c+20");
        lore3.add("&7Critical Chance: &c+20");
        lore3.add("&7Critical Damage: &c+20");
        lore3.add("&7Intelligence: &b+20");
        lore3.add("&7Ability Damage: &3+20");
        lore3.add("&7Speed: &f+20");
        lore3.add("");
        lore3.add("&a&lUNCOMMON LEGGINGS");
        lore3.add("&8item_id:test_leggings");
        ItemStack item3 = createItem(Material.IRON_LEGGINGS, name3, lore3);
        testLeggings = new Item(item3, 0);
        armorItemIds.add("test_leggings");
        pieces.add(testLeggings);

        String name4 = "&aTest Boots";
        List<String> lore4 = new ArrayList<>();

        lore4.add("&7Health: &4+20");
        //lore4.add("&7Health Regen: &4+20");
        lore4.add("&7Defence: &a+20");
        lore4.add("&7Resistance: &3+20");
        //lore4.add("&7Mana Regen: &b+20");
//        lore4.add("&7Damage: &c+20");
        lore4.add("&7Critical Chance: &c+20");
        lore4.add("&7Critical Damage: &c+20");
        lore4.add("&7Intelligence: &b+20");
        lore4.add("&7Ability Damage: &3+20");
        lore4.add("&7Speed: &f+20");
        lore4.add("");
        lore4.add("&a&lUNCOMMON BOOTS");
        lore4.add("&8item_id:test_boots");
        ItemStack item4 = createItem(Material.IRON_BOOTS, name4, lore4);
        testBoots = new Item(item4, 0);
        armorItemIds.add("test_boots");
        pieces.add(testBoots);

        armorItems.put("test_armor", pieces);
    }

    public static Item getRandomUncommonArmorPiece(ItemClass itemClass){
        int r = new Random().nextInt(1)+1; //1-1 range
        int piece = new Random().nextInt(4); //0-4 range
        switch (r){
            case 1:
                return armorItems.get("wanderer_armor").get(piece);
            default:
                ItemStack nullItem = new ItemStack(Material.BARRIER);
                ItemMeta im = nullItem.getItemMeta();
                im.setDisplayName("NULL");
                im.setLore(new ArrayList<>());
                nullItem.setItemMeta(im);
                return new Item(nullItem, 0);
        }
    }

    public static Item getRandomRareArmorPiece(ItemClass itemClass){
        int r = new Random().nextInt(1)+1; //1-1 range
        int piece = new Random().nextInt(4); //0-4 range
        switch (r){
            case 1:
                return armorItems.get("hardened_diamond_armor").get(piece);
            default:
                ItemStack nullItem = new ItemStack(Material.BARRIER);
                ItemMeta im = nullItem.getItemMeta();
                im.setDisplayName("NULL");
                im.setLore(new ArrayList<>());
                nullItem.setItemMeta(im);
                return new Item(nullItem, 0);
        }
    }

    public static Item getRandomEpicArmorPiece(ItemClass itemClass){
        int r = new Random().nextInt(1)+1; //1-1 range
        int piece = new Random().nextInt(4); //0-4 range
        switch (r){
            case 1:
                return armorItems.get("angel_armor").get(piece);
            default:
                ItemStack nullItem = new ItemStack(Material.BARRIER);
                ItemMeta im = nullItem.getItemMeta();
                im.setDisplayName("NULL");
                im.setLore(new ArrayList<>());
                nullItem.setItemMeta(im);
                return new Item(nullItem, 0);
        }
    }

    public static Item getRandomLegendaryArmorPiece(ItemClass itemClass){
        int r = new Random().nextInt(1)+1; //1-1 range
        int piece = new Random().nextInt(4); //0-4 range
        switch (r){
            case 1:
                return armorItems.get("superior_armor").get(piece);
            default:
                ItemStack nullItem = new ItemStack(Material.BARRIER);
                ItemMeta im = nullItem.getItemMeta();
                im.setDisplayName("NULL");
                im.setLore(new ArrayList<>());
                nullItem.setItemMeta(im);
                return new Item(nullItem, 0);
        }
    }

    public static Item getRandomMythicArmorPiece(ItemClass itemClass){
        int r = new Random().nextInt(1)+1; //1-1 range
        int piece = new Random().nextInt(4); //0-4 range
        switch (r){
            case 1:
                return armorItems.get("shadowweave_shroud").get(piece);
            default:
                ItemStack nullItem = new ItemStack(Material.BARRIER);
                ItemMeta im = nullItem.getItemMeta();
                im.setDisplayName("NULL");
                im.setLore(new ArrayList<>());
                nullItem.setItemMeta(im);
                return new Item(nullItem, 0);
        }
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

    private static ItemStack createSkullItem(String displayName, List<String> lore, String base64){
        ItemStack item = SkullCreator.itemFromBase64(base64);
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

    private static ItemStack createColoredItem(Material material, Color color, String displayName, List<String> lore){
        ItemStack item = new ItemStack(material, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
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
    private static ItemStack createTrimItem(Material material, TrimPattern trimPattern, TrimMaterial trimMaterial, String displayName, List<String> lore){
        ItemStack item = new ItemStack(material, 1);
        ArmorMeta meta = (ArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setTrim(new ArmorTrim(trimMaterial, trimPattern));
        List<String> newLore = new ArrayList<>();
        for (String s : lore){
            newLore.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        meta.setLore(newLore);
        item.setItemMeta(meta);

        return item;
    }

}

