package net.pullolo.magicarena.wish;

import net.pullolo.magicarena.items.ItemClass;

public class PreviousWishData {
    private WishSystem.WishRarity lastWishRarity;
    private ItemClass lastItemClass;
    private String lastArmorSet;

    public PreviousWishData(WishSystem.WishRarity lastWishRarity, ItemClass lastItemClass, String lastArmorSet) {
        this.lastWishRarity = lastWishRarity;
        this.lastItemClass = lastItemClass;
        this.lastArmorSet = lastArmorSet;
    }

    public WishSystem.WishRarity getLastWishRarity() {
        return lastWishRarity;
    }

    public void setLastWishRarity(WishSystem.WishRarity lastWishRarity) {
        this.lastWishRarity = lastWishRarity;
    }

    public ItemClass getLastItemClass() {
        return lastItemClass;
    }

    public void setLastItemClass(ItemClass lastItemClass) {
        this.lastItemClass = lastItemClass;
    }

    public String getLastArmorSet() {
        return lastArmorSet;
    }

    public void setLastArmorSet(String lastArmorSet) {
        this.lastArmorSet = lastArmorSet;
    }
}
