package net.pullolo.magicarena;

import net.pullolo.magicarena.commands.CreateWorld;
import net.pullolo.magicarena.commands.DeleteWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Random;

import static net.pullolo.magicarena.worlds.WorldManager.copyWorld;
import static net.pullolo.magicarena.worlds.WorldManager.removeWorld;

public final class MagicArena extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("createworld").setExecutor(new CreateWorld());
        getCommand("deleteworld").setExecutor(new DeleteWorld());

        ArrayList<String> savedWorlds = new ArrayList<>();

        for (String s : savedWorlds){
            new WorldCreator(s).createWorld();
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
