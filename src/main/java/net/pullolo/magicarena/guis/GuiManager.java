package net.pullolo.magicarena.guis;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import net.pullolo.magicarena.wish.WishSystem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static net.pullolo.magicarena.MagicArena.getLog;
import static net.pullolo.magicarena.wish.WishSystem.getWishRarityAsInt;

public class GuiManager {

    private final JavaPlugin plugin;


    public GuiManager(JavaPlugin plugin){
        this.plugin = plugin;
    }

    public InventoryGui createGameSelectGui(Player player){
        String[] guiSetup = {
                "         ",
                " a b b b ",
                "         "
        };
        InventoryGui gui = new InventoryGui(this.plugin, player, "Select Game", guiSetup);
        gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1)); // fill the empty slots with this
        gui.addElement(new StaticGuiElement('a', new ItemStack (Material.COMPASS),
                click -> {
                    click.getGui().close();
                    click.getWhoClicked().teleport(click.getWhoClicked().getLocation().add(0, 1, 0));
                    return true;
                }, ChatColor.translateAlternateColorCodes('&', "&r&aPlay 1v1!")));
        gui.addElement(new StaticGuiElement('b', new ItemStack (Material.BARRIER),
                ChatColor.translateAlternateColorCodes('&', "&r&cComing Soon!")));
        return gui;
    }

    public InventoryGui createWishGui(Player player){
        String[] guiSetup = {
                "         ",
                "  a   w  ",
                "    n    "
        };
        InventoryGui gui = new InventoryGui(this.plugin, player, "Select Game", guiSetup);
        gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        gui.addElement(new StaticGuiElement('a', new ItemStack(Material.NETHERITE_INGOT),
                click -> {
                    click.getGui().close();
                    return true;
                },
                ChatColor.translateAlternateColorCodes('&', "&r&3Wish for Armor! &a1 ✧")));
        gui.addElement(new StaticGuiElement('w', new ItemStack(Material.BLAZE_ROD),
                click -> {
                    click.getGui().close();
                    return true;
                },
                ChatColor.translateAlternateColorCodes('&', "&r&cWish for Weapons! &a1 ✧")));
        gui.addElement(new DynamicGuiElement('n', (viewer)->{
            return new StaticGuiElement('n', new ItemStack(Material.NETHER_STAR),
                    ChatColor.translateAlternateColorCodes('&', "&r&7Your wishes: &a" + "10" + " ✧"));
        }));

        return gui;
    }

    public ArrayList<InventoryGui> createWishAnim(Player player, WishSystem.WishRarity wishRarity, WishSystem.WishType wishType, int stars, ItemStack finalItem){
        ArrayList<InventoryGui> finishedList = new ArrayList<>();
        int rarity = getWishRarityAsInt(wishRarity);

        ArrayList<String[]> rarities = new ArrayList<>();

        String[] gui1 = {
                "   bbb   ",
                "   bbb   ",
                "   bbb   "
        };
        rarities.add(gui1);
        String[] guiUncommon = {
                "  buuub  ",
                "  buuub  ",
                "  buuub  "
        };
        rarities.add(guiUncommon);
        String[] guiRare = {
                " burrrub ",
                " burrrub ",
                " burrrub "
        };
        rarities.add(guiRare);
        String[] guiEpic = {
                "bureeerub",
                "bureeerub",
                "bureeerub"
        };
        rarities.add(guiEpic);
        String[] guiLegendary = {
                "urellleru",
                "urellleru",
                "urellleru"
        };
        rarities.add(guiLegendary);
        String[] guiMythic = {
                "relmmmler",
                "relmmmler",
                "relmmmler"
        };
        rarities.add(guiMythic);

        for (int i = 0; i<=rarity; i++){
            InventoryGui gui = new InventoryGui(this.plugin, player, ChatColor.translateAlternateColorCodes('&', "&r&5Wishing for " + wishType.toString().toLowerCase().split("_")[0]), rarities.get(i));
            gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
            gui.addElement(new DynamicGuiElement('b', (viewer)->{
                return new StaticGuiElement('b', new ItemStack(Material.BLACK_STAINED_GLASS_PANE), "");
            }));
            gui.addElement(new DynamicGuiElement('u', (viewer)->{
                return new StaticGuiElement('u', new ItemStack(Material.LIME_STAINED_GLASS_PANE), "");
            }));
            gui.addElement(new DynamicGuiElement('r', (viewer)->{
                return new StaticGuiElement('r', new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), "");
            }));
            gui.addElement(new DynamicGuiElement('e', (viewer)->{
                return new StaticGuiElement('e', new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE), "");
            }));
            gui.addElement(new DynamicGuiElement('l', (viewer)->{
                return new StaticGuiElement('l', new ItemStack(Material.YELLOW_STAINED_GLASS_PANE), "");
            }));
            gui.addElement(new DynamicGuiElement('m', (viewer)->{
                return new StaticGuiElement('m', new ItemStack(Material.PINK_STAINED_GLASS_PANE), "");
            }));

            finishedList.add(gui);
        }
        finishedList.addAll(createPulloutAnim(player, wishRarity, wishType, stars, finalItem));
        return finishedList;
    }

    //todo add item class
    private ArrayList<InventoryGui> createPulloutAnim(Player player, WishSystem.WishRarity wishRarity, WishSystem.WishType wishType, int stars, ItemStack finalItem){
        ArrayList<InventoryGui> finishedList = new ArrayList<>();
        String[] gui1 = {
                "         ",
                "c        ",
                "         "
        };
        String[] gui2 = {
                "         ",
                "c aaaaa  ",
                "         "
        };
        ArrayList<String[]> starList = new ArrayList<>();
        String[] guiStars0 = {
                "         ",
                "c saaaa  ",
                "         "
        };
        starList.add(guiStars0);
        String[] guiStars1 = {
                "         ",
                "c ssaaa  ",
                "         "
        };
        starList.add(guiStars1);
        String[] guiStars2 = {
                "         ",
                "c sssaa  ",
                "         "
        };
        starList.add(guiStars2);
        String[] guiStars3 = {
                "         ",
                "c ssssa  ",
                "         "
        };
        starList.add(guiStars3);
        String[] guiStars4 = {
                "         ",
                "c sssss  ",
                "         "
        };
        starList.add(guiStars4);
        String[] guiFinal = {
                "         ",
                "c   i    ",
                "         "
        };
        String s = wishType.toString().toLowerCase().split("_")[0];
        InventoryGui gui = new InventoryGui(this.plugin, player, ChatColor.translateAlternateColorCodes('&', "&r&5Wishing for " + s), gui1);
        gui.setFiller(new ItemStack(WishSystem.getWishRarityAsGlassPane(wishRarity), 1));
        gui.addElement(new DynamicGuiElement('c', (viewer)->{
            return new StaticGuiElement('c', new ItemStack(Material.IRON_SWORD), ChatColor.translateAlternateColorCodes('&', "&7Class: &aDPS"));
        }));
        finishedList.add(gui);

        InventoryGui guiSecond = new InventoryGui(this.plugin, player, ChatColor.translateAlternateColorCodes('&', "&r&5Wishing for " + s), gui2);
        guiSecond.setFiller(new ItemStack(WishSystem.getWishRarityAsGlassPane(wishRarity), 1));
        guiSecond.addElement(new DynamicGuiElement('c', (viewer)->{
            return new StaticGuiElement('c', new ItemStack(Material.IRON_SWORD), ChatColor.translateAlternateColorCodes('&', "&7Class: &aDPS"));
        }));
        guiSecond.addElement(new DynamicGuiElement('a', (viewer)->{
            return new StaticGuiElement('a', new ItemStack(Material.AIR));
        }));
        finishedList.add(guiSecond);

        for (int i = 0; i<stars; i++){
            final int iteration = i+1;
            InventoryGui g = new InventoryGui(this.plugin, player, ChatColor.translateAlternateColorCodes('&', "&r&5Wishing for " + s), starList.get(i));
            g.setFiller(new ItemStack(WishSystem.getWishRarityAsGlassPane(wishRarity), 1));
            g.addElement(new DynamicGuiElement('c', (viewer)->{
                return new StaticGuiElement('c', new ItemStack(Material.IRON_SWORD), ChatColor.translateAlternateColorCodes('&', "&7Class: &aDPS"));
            }));
            g.addElement(new DynamicGuiElement('a', (viewer)->{
                return new StaticGuiElement('a', new ItemStack(Material.AIR));
            }));
            g.addElement(new DynamicGuiElement('s', (viewer)->{
                return new StaticGuiElement('s', new ItemStack(Material.NETHER_STAR), ChatColor.translateAlternateColorCodes('&', "&r&6Star " + iteration));
            }));
            finishedList.add(g);
        }

        InventoryGui finalgui = new InventoryGui(this.plugin, player, ChatColor.translateAlternateColorCodes('&', "&r&5Wishing for " + s), guiFinal);
        finalgui.setCloseAction(close -> {
            close.getGui().show(close.getPlayer());
            return false;
        });
        finalgui.setFiller(new ItemStack(WishSystem.getWishRarityAsGlassPane(wishRarity), 1));
        finalgui.addElement(new DynamicGuiElement('c', (viewer)->{
            return new StaticGuiElement('c', new ItemStack(Material.IRON_SWORD), ChatColor.translateAlternateColorCodes('&', "&7Class: &aDPS"));
        }));
        finalgui.addElement(new DynamicGuiElement('i', (viewer)->{
            return new StaticGuiElement('i', finalItem, click -> {
                click.getWhoClicked().sendMessage("wohoooo");
                click.getGui().close();
                return true;
            });
        }));
        addClosePrevention(finalgui);

        finishedList.add(finalgui);

        return finishedList;
    }

    private void addClosePrevention(InventoryGui gui){
        gui.setCloseAction(close -> {
            new BukkitRunnable() {
                @Override
                public void run() {
                    close.getGui().show(close.getPlayer());
                }
            }.runTaskLater(plugin, 1);
            return false;
        });
    }
}
