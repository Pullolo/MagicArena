package net.pullolo.magicarena.commands;

import net.pullolo.magicarena.worlds.WorldManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class CreateWorld implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("createworld")){
            return false;
        }
        if (args.length<1){
            return false;
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
        WorldManager.saveWorld(world, false);
        sender.sendMessage(ChatColor.GREEN + "Successfully created world " + args[0] + "!");
        new Location(world, 0, 60, 0).getBlock().setType(Material.BEDROCK);
        if (sender instanceof Player){
            ((Player) sender).teleport(new Location(world, 0, 61, 0));
        }
        return true;
    }
}
