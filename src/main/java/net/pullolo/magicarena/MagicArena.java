package net.pullolo.magicarena;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static net.pullolo.magicarena.worlds.WorldManager.copyWorld;
import static net.pullolo.magicarena.worlds.WorldManager.removeWorld;

public final class MagicArena extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        copyWorld(Bukkit.getWorld("world"), "copy");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        removeWorld(Bukkit.getWorld("copy"));
    }
}
