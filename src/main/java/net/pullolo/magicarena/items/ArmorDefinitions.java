package net.pullolo.magicarena.items;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

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

    public static final ArrayList<String> armorItemIds = new ArrayList<>();
    public static final HashMap<String, ArrayList<Item>> armorItems = new HashMap<>();

    public static void init(){
        createTestArmor();

        createWandererArmor();

        createHardenedDiamondArmor();

        createAngelArmor();
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
        hardenedDiamondHelmet = new Item(item1);
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
        hardenedDiamondChestplate = new Item(item2);
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
        hardenedDiamondLeggings = new Item(item3);
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
        hardenedDiamondBoots = new Item(item4);
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
        wandererHelmet = new Item(item1);
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
        wandererChestplate = new Item(item2);
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
        wandererLeggings = new Item(item3);
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
        wandererBoots = new Item(item4);
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
        testHelmet = new Item(item1);
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
        testChestplate = new Item(item2);
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
        testLeggings = new Item(item3);
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
        testBoots = new Item(item4);
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
                nullItem.setItemMeta(im);
                return new Item(nullItem);
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
                nullItem.setItemMeta(im);
                return new Item(nullItem);
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
                nullItem.setItemMeta(im);
                return new Item(nullItem);
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
}

