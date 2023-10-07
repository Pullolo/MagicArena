package net.pullolo.magicarena.items;

import net.pullolo.magicarena.guis.GuiManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static net.pullolo.magicarena.MagicArena.config;
import static net.pullolo.magicarena.MagicArena.getLog;

public class MainMenuItemManager implements Listener {
    private final JavaPlugin plugin;
    private final GuiManager guiManager;

    public MainMenuItemManager(JavaPlugin plugin, GuiManager guiManager){
        this.plugin = plugin;
        this.guiManager = guiManager;
    }

    @EventHandler
    public void onMainMenuItemClick(InventoryClickEvent event){
        if (event.getCurrentItem()!=null && event.getCurrentItem().equals(getMainMenuItem()) && event.getSlot() == 8){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMainMenuItemDrop(PlayerDropItemEvent event){
        if (event.getItemDrop().getItemStack().equals(getMainMenuItem())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerSwitchWorlds(PlayerChangedWorldEvent event){
        if (config.getList("main-menu-item-disabled-worlds").contains(event.getPlayer().getWorld().getName()) || event.getPlayer().getWorld().getName().contains("temp")){
            if (event.getPlayer().getInventory().contains(getMainMenuItem())) {
                event.getPlayer().getInventory().remove(getMainMenuItem());
            }
        } else {
            setItem(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        setItem(event.getPlayer());
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event){
        if ((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        && event.getItem()!=null && event.getItem().equals(getMainMenuItem())){
            guiManager.createMainMenuGui(event.getPlayer()).show(event.getPlayer());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        event.getDrops().remove(getMainMenuItem());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        if (!event.getPlayer().getInventory().contains(getMainMenuItem())){
            setItem(event.getPlayer());
        }
    }

    private void setItem(Player player){
        if (player.getInventory().getItem(8)==null) {
            player.getInventory().setItem(8, getMainMenuItem());
            return;
        }
        if (!player.getInventory().getItem(8).equals(getMainMenuItem())){
            ItemStack prevItem = player.getInventory().getItem(8);
            player.getInventory().setItem(8, getMainMenuItem());
            player.getInventory().addItem(prevItem);
            return;
        }
    }

    private ItemStack getMainMenuItem(){
        ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&r&3Main Menu"));
        item.setItemMeta(im);
        return item;
    }
}
