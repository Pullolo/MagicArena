package net.pullolo.magicarena.players;

import net.pullolo.magicarena.items.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static net.pullolo.magicarena.MagicArena.debugLog;
import static net.pullolo.magicarena.data.PlayerData.getPlayerData;
import static net.pullolo.magicarena.items.ArmorDefinitions.armorItems;
import static net.pullolo.magicarena.items.ItemsDefinitions.items;

public class UpdateManager {

    public static void updatePlayer(Player p){
        for (int slot = 0; slot < p.getInventory().getSize(); slot++){
            ItemStack item = p.getInventory().getItem(slot);
            if (item==null) continue;
            if (item.getItemMeta()==null) continue;
            Item i = new Item(item);
            if (i.getItemId().equalsIgnoreCase("NULL")) continue;
            p.getInventory().setItem(slot, updatePlayerItem(i).getItem());
            debugLog("Updated " + i.getItemId(), false);
        }
        getPlayerData(p).setUpdated(true);
        debugLog("Finished updating Player " + p.getName(), false);
    }

    private static Item updatePlayerItem(Item item){
        if (!items.containsKey(item.getItemId())){
            for (ArrayList<Item> armorSet : armorItems.values()){
                int i = 0;
                for (Item piece : armorSet){
                    if (piece.getItemId().equals(item.getItemId())){
                        return new Item(armorSet.get(i), item.getStars(), (float) item.getQuality());
                    }
                    i++;
                }
            }
            debugLog("Couldn't update " + item.getItemId(), true);
            return item;
        }
        return new Item(items.get(item.getItemId()), item.getStars(), (float) item.getQuality());
    }
}
