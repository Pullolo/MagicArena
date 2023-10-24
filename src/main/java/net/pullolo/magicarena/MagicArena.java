package net.pullolo.magicarena;

import net.pullolo.magicarena.commands.*;
import net.pullolo.magicarena.data.PlayerData;
import net.pullolo.magicarena.events.GameAbilitiesHandler;
import net.pullolo.magicarena.events.GameDamageHandler;
import net.pullolo.magicarena.data.DbManager;
import net.pullolo.magicarena.events.GameEventsHandler;
import net.pullolo.magicarena.game.GameManager;
import net.pullolo.magicarena.guis.AnimationManager;
import net.pullolo.magicarena.guis.GuiManager;
import net.pullolo.magicarena.items.ArmorDefinitions;
import net.pullolo.magicarena.items.ItemsDefinitions;
import net.pullolo.magicarena.items.MainMenuItemManager;
import net.pullolo.magicarena.misc.CooldownApi;
import net.pullolo.magicarena.players.PartyManager;
import net.pullolo.magicarena.wish.DungeonChestSystem;
import net.pullolo.magicarena.wish.WishSystem;
import net.pullolo.magicarena.worlds.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static net.pullolo.magicarena.worlds.WorldManager.*;

public final class MagicArena extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static WishSystem wishSystem;
    public static FileConfiguration config;

    private static final String prefix = "[MagicArena] ";
    public static String mainWorld;
    public static GameManager gameManager;
    public static PartyManager partyManager;
    public static GuiManager guiManager;
    public static DungeonChestSystem dungeonChestSystem;
    public static DbManager dbManager = new DbManager();
    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        dbManager.init();
        ItemsDefinitions.init();
        ArmorDefinitions.init();
        checkDb();
        setPlayerData();
        partyManager = new PartyManager();
        gameManager = new GameManager();
        guiManager = new GuiManager(this);
        AnimationManager animationManager = new AnimationManager(this, guiManager);
        wishSystem = new WishSystem(animationManager);
        dungeonChestSystem = new DungeonChestSystem();
        saveDefaultConfig();
        WorldManager.init(this);
        config = getConfig();
        setMainWorld();
        createCooldowns();
        registerCommand(new CreateWorld(), "createworld");
        registerCommand(new DeleteWorld(), "deleteworld");
        registerCommand(new CopyWorld(), "copyworld");
        registerCommand(new Worlds(), "worlds");
        registerCommand(new Gui(guiManager, animationManager), "gui");
        registerCommand(new Arenas(), "arenas");
        registerCommand(new Dungeons(), "dungeons");
        registerCommand(new Queue(), "queue");
        registerCommand(new Stats(), "stats");
        registerCommand(new Kill(), "kill");
        registerCommand(new GameCmd(), "game");
        registerCommand(new GiveItem(), "giveitem");
        registerCommand(new GiveArmor(), "givearmor");
        registerCommand(new Spawn(), "spawn");
        registerCommand(new Party(), "party");
        registerCommand(new DungeonCmd(), "dungeon");
        registerCommand(new GetSkull(), "getskull");
        registerCommand(new SetCustomName(), "setcustomname");
        registerCommand(new GetCustomName(), "getcustomname");
        registerCommand(new Data(), "data");
        registerCommand(new Open(), "open");
        getServer().getPluginManager().registerEvents(new MainMenuItemManager(this, guiManager), this);
        getServer().getPluginManager().registerEvents(new GameEventsHandler(), this);
        getServer().getPluginManager().registerEvents(new GameDamageHandler(), this);
        getServer().getPluginManager().registerEvents(new GameAbilitiesHandler(), this);

        loadSavedWorlds();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        savePlayers();
        dbManager.disconnect();
        deleteActiveTempWorlds();
    }

    private void loadSavedWorlds(){
        ArrayList<String> savedWorlds = getSavedWorlds();
        savedWorlds = deleteTempWorlds(savedWorlds);
        if (config.getBoolean("indev")){
            for (String s : savedWorlds){
                if (s.regionMatches(0, "arena_", 0, "arena_".length()) || s.regionMatches(0, "dungeon_", 0, "dungeon_".length())){
                    continue;
                }
                new WorldCreator(s).createWorld();
                log.info(prefix + "Loaded world " + s);
            }
        }
    }

    private void createCooldowns(){
        CooldownApi.createCooldown("UT", 1.5);
        CooldownApi.createCooldown("SR", 10);
        CooldownApi.createCooldown("HS", 15);
        CooldownApi.createCooldown("LS", 10);
        CooldownApi.createCooldown("AS", 1);
        CooldownApi.createCooldown("KU", 6);
        CooldownApi.createCooldown("ROC", 0.5);
    }

    private void registerCommand(CommandExecutor cmd, String cmdName){
        if (cmd instanceof TabCompleter){
            getCommand(cmdName).setExecutor(cmd);
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
    public static void debugLog(String msg, boolean warning){
        if (!config.getBoolean("debug")){
            return;
        }
        if (warning) log.warning("[MagicArena:Debug] " + msg);
        else log.info("[MagicArena:Debug] " + msg);
    }
    public static WishSystem getWishSystem() {
        return wishSystem;
    }
    public static void checkDb(){
        dbManager.connect();
        if (dbManager.isDbEnabled()){
            log.info("Database is operational");
        } else log.warning("Database is offline!");
    }
    private void setPlayerData() {
        for (Player p : getServer().getOnlinePlayers()){
            PlayerData.setPlayerDataFromDb(p, dbManager);
        }
    }

    private void savePlayers(){
        for (Player p : getServer().getOnlinePlayers()){
            PlayerData.savePlayerDataToDb(p, dbManager);
            PlayerData.removePlayerData(p);
        }
    }
}
