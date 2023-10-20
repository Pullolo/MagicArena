package net.pullolo.magicarena.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ArmorDefinitions {

    public static Item testHelmet;
    public static Item testChestplate;
    public static Item testLeggings;
    public static Item testBoots;

    public static final ArrayList<String> armorItemIds = new ArrayList<>();

    public static void init(){
        createTestArmor();

    }

    private static void createTestArmor(){
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
}

