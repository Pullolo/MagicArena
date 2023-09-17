package net.pullolo.magicarena.commands;

import net.pullolo.magicarena.worlds.WorldManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import java.util.List;
import java.util.Random;

public class CreateWorld implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("createworld")){
            return false;
        }
        if (args.length<1){
            return false;
        }
        if (WorldManager.getArenas().contains("arena_" + args[0])){
            sender.sendMessage(ChatColor.RED + "This world already exists!");
            return true;
        }
        for (World w : Bukkit.getWorlds()){
            if (args[0].equalsIgnoreCase(w.getName())){
                sender.sendMessage(ChatColor.RED + "This world already exists!");
                return true;
            }
        }
        sender.sendMessage(ChatColor.GREEN + "Generating world...");
        WorldCreator creator = new WorldCreator(args[0]);
        creator.generateStructures(false);
        creator.generator(new ChunkGenerator() {
            public byte[] generate(World world, Random random, int x, int z) {
                return new byte[32768];
            }
        });

        World world = creator.createWorld();
        WorldManager.saveWorld(world, false, false);
        sender.sendMessage(ChatColor.GREEN + "Successfully created world " + args[0] + "!");
        world.setSpawnLocation(new Location(world, 0.5, 61, 0.5));
        new Location(world, 0, 60, 0).getBlock().setType(Material.BEDROCK);
        if (sender instanceof Player){
            ((Player) sender).teleport(world.getSpawnLocation());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command cmd, String s, String[] strings) {
        return null;
    }
}
