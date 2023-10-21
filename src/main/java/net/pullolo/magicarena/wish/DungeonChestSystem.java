package net.pullolo.magicarena.wish;

import org.bukkit.entity.Player;

import java.util.Random;

import static net.pullolo.magicarena.MagicArena.guiManager;

public class DungeonChestSystem {

    public enum ChestType{
        BASIC,
        EPIC,
        MYTHIC
    }

    public void createDungeonChest(Player p, int score, ChestType chestType){
        Random rand = new Random();
        int mul = 1;
        if (score>500){
            mul=2;
        }
        if (score>1000){
            mul=3;
        }
        switch (chestType){
            case EPIC:
                mul+=1;
                break;
            case MYTHIC:
                mul+=3;
                break;
        }
        if (rand.nextInt(100)+1>99-mul){
//            int stars = 1;
//            int rarityChance = rand.nextInt(100)+1;
//            WishSystem.WishRarity wishRarity;
//            if (rarityChance>99){
//                wishRarity = WishSystem.WishRarity.MYTHIC;
//            } else if (rarityChance>97) {
//                wishRarity = WishSystem.WishRarity.LEGENDARY;
//            } else if (rarityChance>90) {
//                wishRarity = WishSystem.WishRarity.EPIC;
//            } else if (rarityChance>80) {
//                wishRarity = WishSystem.WishRarity.RARE;
//            } else {
//                wishRarity = WishSystem.WishRarity.UNCOMMON;
//            }
//            for (int i= 0; i<4; i++){
//                if (rand.nextBoolean()){
//                    stars++;
//                }
//            }
//            float q = ((float)((int) (rand.nextFloat()*1000)))/10;
            //todo add dungeon Item
            //temp
            guiManager.createDungeonChestReward(p, 100*mul+rand.nextInt(10), 30*mul+rand.nextInt(10), chestType).show(p);
        } else {
            guiManager.createDungeonChestReward(p, 100*mul+rand.nextInt(10), 30*mul+rand.nextInt(10), chestType).show(p);
        }
    }
}
