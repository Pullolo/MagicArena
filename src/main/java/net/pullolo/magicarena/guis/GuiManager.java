package net.pullolo.magicarena.guis;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.Date;

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
}
