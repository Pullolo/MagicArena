package net.pullolo.magicarena;

import net.pullolo.magicarena.commands.CopyWorld;
import net.pullolo.magicarena.commands.CreateWorld;
import net.pullolo.magicarena.commands.DeleteWorld;
import net.pullolo.magicarena.commands.Worlds;
import net.pullolo.magicarena.worlds.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import static net.pullolo.magicarena.worlds.WorldManager.*;

public final class MagicArena extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    public static FileConfiguration config;

    private static final String prefix = "[MagicArena] ";
    public static String mainWorld;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        WorldManager.init(this);
        config = getConfig();
        setMainWorld();
        registerCommand(new CreateWorld(), "createworld");
        registerCommand(new DeleteWorld(), "deleteworld");
        registerCommand(new CopyWorld(), "copyworld");
        registerCommand(new Worlds(), "worlds");
        loadSavedWorlds();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        deleteActiveTempWorlds();
    }

    private void loadSavedWorlds(){
        ArrayList<String> savedWorlds = getSavedWorlds();
        savedWorlds = deleteTempWorlds(savedWorlds);
        if (config.getBoolean("indev")){
            for (String s : savedWorlds){
                new WorldCreator(s).createWorld();
                log.info(prefix + "Loaded world " + s);
            }
        }
    }

    private void registerCommand(Object cmd, String cmdName){
        if ((cmd instanceof CommandExecutor) && (cmd instanceof TabCompleter)){
            getCommand(cmdName).setExecutor((CommandExecutor) cmd);
            getCommand(cmdName).setTabCompleter((TabCompleter) cmd);
        } else {
            throw new RuntimeException("Provided object is not a command executor and a tab completer at the same time!");
        }
    }

    private ArrayList<String> deleteTempWorlds(ArrayList<String> savedWorlds){
        ArrayList<String> remaingWorlds = new ArrayList<>();
        for (String s: savedWorlds){
            if (!s.contains("temp_")){
                remaingWorlds.add(s);
                continue;
            }
            log.info(prefix + "Deleting world " + s + "!");
            WorldManager.removeDisabledWorld(new File(getServer().getWorldContainer().getAbsolutePath().replace(".", "") + s), s);
        }

        return remaingWorlds;
    }

    private void setMainWorld(){
        mainWorld = config.getString("main-world");
        if (mainWorld == null || Bukkit.getWorld(mainWorld) == null){
            mainWorld = Bukkit.getWorlds().get(0).getName();
        }
    }

    private void deleteActiveTempWorlds(){
        List<World> toDel = Bukkit.getWorlds();
        for (World w: toDel){
            if (!w.getName().contains("temp_")){
                continue;
            }
            log.info(prefix + "Deleting world " + w.getName() + "!");
            WorldManager.removeWorld(w);
        }
    }

    public static Logger getLog(){
        return log;
    }
}
