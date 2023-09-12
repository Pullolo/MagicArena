package net.pullolo.magicarena;

import net.pullolo.magicarena.commands.CreateWorld;
import net.pullolo.magicarena.commands.DeleteWorld;
import net.pullolo.magicarena.worlds.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import static net.pullolo.magicarena.worlds.WorldManager.*;

public final class MagicArena extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        WorldManager.init(this);
        config = getConfig();
        getCommand("createworld").setExecutor(new CreateWorld());
        getCommand("deleteworld").setExecutor(new DeleteWorld());

        loadSavedWorlds();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    private void loadSavedWorlds(){
        if (config.getBoolean("indev")){
            ArrayList<String> savedWorlds = getSavedWorlds();

            for (String s : savedWorlds){
                new WorldCreator(s).createWorld();
                log.info("Loaded world " + s);
            }
        }
    }

    public static Logger getLog(){
        return log;
    }
}
