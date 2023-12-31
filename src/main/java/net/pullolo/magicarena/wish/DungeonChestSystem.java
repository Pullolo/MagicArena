package net.pullolo.magicarena.wish;

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

import static net.pullolo.magicarena.MagicArena.guiManager;
import static net.pullolo.magicarena.items.ItemsDefinitions.*;

public class DungeonChestSystem {

    public enum ChestType{
        BASIC,
        EPIC,
        MYTHIC
    }

    public void createDungeonChest(Player p, int score, int level, ChestType chestType, boolean won){
        Random rand = new Random();
        boolean skipUncommons = false;
        int mul = level/20;
        if (mul<1) mul=1;
        int wepVal = 0;
        int add = 1;
        int val1 = rand.nextInt(10);
        int val2 = rand.nextInt(10);
        if (score>=500){
            mul+=2;
        }
        if (score>=1000){
            mul+=3;
        }
        switch (chestType){
            case EPIC:
                if (level>=100){
                    wepVal=100;
                } else if (level >= 50) {
                    wepVal=24;
                } else wepVal=2;
                mul+=1;
                break;
            case MYTHIC:
                if (level>=100){
                    wepVal=100;
                    skipUncommons=true;
                } else if (level >= 50) {
                    wepVal=90;
                } else wepVal=12;
                mul+=3;
                add=(int) Math.floor((double) score/300);
                break;
        }

        if (!won) mul=0;
        if (rand.nextInt(100)+1>96-mul-wepVal){
            int stars = 1;
            int rarityChance = rand.nextInt(100)+1;
            WishSystem.WishRarity wishRarity;
            if (rarityChance<=80 && skipUncommons){
                if (rand.nextInt(4)==0){
                    rarityChance = 80 + rand.nextInt(20)+1;
                } else rarityChance=81;
            }
            if (rarityChance>99){
                wishRarity = WishSystem.WishRarity.MYTHIC;
            } else if (rarityChance>97) {
                wishRarity = WishSystem.WishRarity.LEGENDARY;
            } else if (rarityChance>90) {
                wishRarity = WishSystem.WishRarity.EPIC;
            } else if (rarityChance>80) {
                wishRarity = WishSystem.WishRarity.RARE;
            } else {
                wishRarity = WishSystem.WishRarity.UNCOMMON;
            }
            for (int i= 0; i<4; i++){
                if (rand.nextBoolean()){
                    stars++;
                }
            }
            float q = ((float)((int) (rand.nextFloat()*1000)))/10;
            Item finalItem;

            switch (wishRarity){
                case UNCOMMON:
                    finalItem=getRandomUncommonDungeonWeapon();
                    break;
                case RARE:
                    finalItem=getRandomRareDungeonWeapon();
                    break;
                case EPIC:
                    finalItem=getRandomEpicDungeonWeapon();
                    break;
                case LEGENDARY:
                    finalItem=getRandomLegendaryDungeonWeapon();
                    break;
                case MYTHIC:
                    finalItem=getRandomMythicDungeonWeapon();
                    break;
                default:
                    ItemStack item = new ItemStack(Material.WOODEN_SWORD);
                    ItemMeta im = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    lore.add("null");
                    im.setLore(lore);
                    im.setDisplayName(ChatColor.RED + wishRarity.toString() + " not implemented yet");
                    item.setItemMeta(im);
                    finalItem=getItemFromPlayer(item);
            }
            guiManager.createDungeonChestReward(p, 100*mul+val1+add, new Item(finalItem, stars, q).getItem(), chestType, wishRarity).show(p);
        } else {
            guiManager.createDungeonChestReward(p, 100*mul+val1+add, 30*mul+val2+add, chestType).show(p);
        }
    }

    public static Item getRandomUncommonDungeonWeapon(){
        int r = new Random().nextInt(1)+1; //1-1 range
        switch (r){
            case 1:
                return undeadSword;
            default:
                ItemStack nullItem = new ItemStack(Material.BARRIER);
                ItemMeta im = nullItem.getItemMeta();
                im.setDisplayName("NULL");
                im.setLore(new ArrayList<>());
                nullItem.setItemMeta(im);
                return new Item(nullItem, 0);
        }
    }

    public static Item getRandomRareDungeonWeapon(){
        int r = new Random().nextInt(1)+1; //1-1 range
        switch (r){
            case 1:
                return brutalityBlade;
            default:
                ItemStack nullItem = new ItemStack(Material.BARRIER);
                ItemMeta im = nullItem.getItemMeta();
                im.setDisplayName("NULL");
                im.setLore(new ArrayList<>());
                nullItem.setItemMeta(im);
                return new Item(nullItem, 0);
        }
    }

    public static Item getRandomEpicDungeonWeapon(){
        int r = new Random().nextInt(1)+1; //1-1 range
        switch (r){
            case 1:
                return rodOfChaos;
            default:
                ItemStack nullItem = new ItemStack(Material.BARRIER);
                ItemMeta im = nullItem.getItemMeta();
                im.setDisplayName("NULL");
                im.setLore(new ArrayList<>());
                nullItem.setItemMeta(im);
                return new Item(nullItem, 0);
        }
    }

    public static Item getRandomLegendaryDungeonWeapon(){
        int r = new Random().nextInt(1)+1; //1-1 range
        switch (r){
            case 1:
                return priscillasDagger;
            default:
                ItemStack nullItem = new ItemStack(Material.BARRIER);
                ItemMeta im = nullItem.getItemMeta();
                im.setDisplayName("NULL");
                im.setLore(new ArrayList<>());
                nullItem.setItemMeta(im);
                return new Item(nullItem, 0);
        }
    }

    public static Item getRandomMythicDungeonWeapon(){
        int r = new Random().nextInt(1)+1; //1-1 range
        switch (r){
            case 1:
                return scorpionChainDart;
            default:
                ItemStack nullItem = new ItemStack(Material.BARRIER);
                ItemMeta im = nullItem.getItemMeta();
                im.setDisplayName("NULL");
                im.setLore(new ArrayList<>());
                nullItem.setItemMeta(im);
                return new Item(nullItem, 0);
        }
    }
}
