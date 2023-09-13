package net.pullolo.magicarena.commands;

import net.pullolo.magicarena.worlds.WorldManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class CopyWorld implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("copyworld")){
            return false;
        }
        if (args.length<2){
            return false;
        }
        try {
            sender.sendMessage(ChatColor.GREEN + "Generating world...");
            WorldManager.copyActiveWorld(Bukkit.getWorld(args[0]), args[1]);
            sender.sendMessage(ChatColor.GREEN + "Successfully copied world " + args[0] + " with a new name of " + args[1] + "!");
            World world = Bukkit.getWorld(args[1]);
            WorldManager.saveWorld(world, false);
            if (sender instanceof Player){
                ((Player) sender).teleport(world.getSpawnLocation());
            }
        } catch (Exception e){
            sender.sendMessage(ChatColor.RED + "Could not copy world " + args[0] + "!");
        }
        return true;
    }
}
