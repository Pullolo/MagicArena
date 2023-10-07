package net.pullolo.magicarena.items;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Item {

    private final ItemStack item;
    private String itemId = "NULL";
    private int stars;
    private double quality;

    private Double damage, intelligence, health, defence, abilityPower, critDamage, critChance, resistance, speed;

    public Item(ItemStack item) {
        this.item = item.clone();
        this.damage = 0.0;
        this.intelligence = 0.0;
        this.health = 0.0;
        this.defence = 0.0;
        this.abilityPower = 0.0;
        this.critDamage = 0.0;
        this.critChance = 0.0;
        this.resistance = 0.0;
        this.speed = 0.0;
        this.stars = 0;
        this.quality = 0.0;
        if (item.getItemMeta() == null || item.getItemMeta().getLore() == null){
            return;
        }
        List<String> lore = item.getItemMeta().getLore();
        for (String s : lore){
            try{
                if (s.contains("§7Damage: ")){
                    String[] ss = s.split("§c");
                    damage = Double.parseDouble(ss[1]);
                }
                if (s.contains("§7Intelligence: ")){
                    String[] ss = s.split("§b");
                    intelligence = Double.parseDouble(ss[1]);
                }
                if (s.contains("§7Health: ")){
                    String[] ss = s.split("§4");
                    health = Double.parseDouble(ss[1]);
                }
                if (s.contains("§7Defence: ")){
                    String[] ss = s.split("§a");
                    defence = Double.parseDouble(ss[1]);
                }
                if (s.contains("§7Ability Damage: ")){
                    String[] ss = s.split("§3");
                    abilityPower = Double.parseDouble(ss[1]);
                }
                if (s.contains("§7Critical Chance: ")){
                    String[] ss = s.split("§c");
                    critChance = Double.parseDouble(ss[1]);
                }
                if (s.contains("§7Critical Damage: ")){
                    String[] ss = s.split("§c");
                    critDamage = Double.parseDouble(ss[1]);
                }
                if (s.contains("§7Resistance: ")){
                    String[] ss = s.split("§3");
                    resistance = Double.parseDouble(ss[1]);
                }
                if (s.contains("§7Speed: ")){
                    String[] ss = s.split("§f");
                    speed = Double.parseDouble(ss[1]);
                }
                if (s.contains("§7Quality: ")){
                    String[] ss = s.split("- ");
                    quality = Double.parseDouble(ss[1]);
                }
                if (s.contains("§8item_id:")){
                    itemId = s.split(":")[1];
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            try{
                if (item.getItemMeta().getDisplayName().contains("§f")){
                    stars=item.getItemMeta().getDisplayName().split("§f")[1].length();
                }
            } catch (Exception e){
                stars=0;
            }

            setStars(stars);
            setQuality((float) quality);

        }
    }

    public Item(Item item, int stars, float quality){
        this.item = item.getItem().clone();
        this.damage = item.getDamage();
        this.intelligence = item.getIntelligence();
        this.health = item.getHealth();
        this.defence = item.getDefence();
        this.abilityPower = item.getAbilityPower();
        this.critDamage = item.getCritDamage();
        this.critChance = item.getCritChance();
        this.resistance = item.getResistance();
        this.speed = item.getSpeed();
        setStars(stars);
        setQuality(quality);
        updateStats();
    }

    public Double getDamage() {
        return damage;
    }

    public Double getIntelligence() {
        return intelligence;
    }

    public Double getHealth() {
        return health;
    }

    public Double getDefence() {
        return defence;
    }

    public Double getAbilityPower() {
        return abilityPower;
    }

    public Double getCritDamage() {
        return critDamage;
    }

    public Double getCritChance() {
        return critChance;
    }

    public Double getResistance() {
        return resistance;
    }

    public Double getSpeed() {
        return speed;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public String getItemId() {
        return itemId;
    }


    public int getStars() {
        return stars;
    }

    public double getQuality() {
        return quality;
    }

    public void setStars(int stars) {
        this.stars = stars;
        ItemMeta meta = item.getItemMeta();
        String name = meta.getDisplayName();
        if (name.contains(ChatColor.translateAlternateColorCodes('&', " &f"))){
            name = name.split(ChatColor.translateAlternateColorCodes('&', " &f"))[0];
        }
        name+=ChatColor.translateAlternateColorCodes('&', " &f");
        for (int i = 0; i<stars; i++){
            name+="✪";
        }
        meta.setDisplayName(name);
        item.setItemMeta(meta);
    }

    public void setQuality(float q) {
        this.quality = q;
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        lore.removeIf(s -> s.contains(ChatColor.translateAlternateColorCodes('&', "&7Quality: ")));
        String quality = "&7Quality: ";
        if (q>90){
            quality += "&dPerfect - " + q;
        } else if (q>70) {
            quality += "&6Undamaged - " + q;
        } else if (q>40) {
            quality += "&cDamaged - " + q;
        } else {
            quality += "&4Highly Damaged - " + q;
        }
        List<String> newLore = new ArrayList<>();
        newLore.add(ChatColor.translateAlternateColorCodes('&', quality));
        newLore.addAll(lore);
        meta.setLore(newLore);
        item.setItemMeta(meta);
    }

    public void updateStats(){
        int bonusDamage;
        double q = this.quality;
        if (q>90){
            bonusDamage=10;
        } else if (q>70) {
            bonusDamage=6;
        } else if (q>40) {    //amounts per quality todo balance
            bonusDamage=-4;
        } else {
            bonusDamage=-8;
        }
        for (int i = 0; i<this.stars; i++){
            bonusDamage+=2;  //amount per star todo balance
        }

        if (this.damage == 1){
            ItemMeta meta = item.getItemMeta();
            List<String> newLore = new ArrayList<>();
            List<String> lore = meta.getLore();
            for (String s: lore){
                if (s.contains("§7Ability Damage: ")){
                    String f;
                    if ((this.abilityPower+bonusDamage)>=0){
                        f = s.split(":")[0]+": §3+"+((int) (this.abilityPower+bonusDamage));
                    } else {
                        f = s.split(":")[0]+": §3-"+((int) (this.abilityPower+bonusDamage));
                    }
                    newLore.add(f);
                    continue;
                }
                newLore.add(s);
            }
            meta.setLore(newLore);
            item.setItemMeta(meta);
            return;
        }

        if (this.damage+bonusDamage<1){
            bonusDamage= (int) (-(this.damage)+1);
        }

        ItemMeta meta = item.getItemMeta();
        List<String> newLore = new ArrayList<>();
        List<String> lore = meta.getLore();
        for (String s: lore){
            if (s.contains("§7Damage: ")){
                String f;
                if ((this.damage+bonusDamage)>=0){
                    f = s.split(" ")[0]+" §c+"+((int) (this.damage+bonusDamage));
                } else {
                    f = s.split(" ")[0]+" §c-"+((int) (this.damage+bonusDamage));
                }
                newLore.add(f);
                continue;
            }
            newLore.add(s);
        }
        meta.setLore(newLore);
        item.setItemMeta(meta);
    }
}
