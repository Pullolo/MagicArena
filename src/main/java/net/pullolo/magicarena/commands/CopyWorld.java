package net.pullolo.magicarena.commands;

import net.pullolo.magicarena.worlds.WorldManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CopyWorld implements CommandExecutor, TabCompleter {

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

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command cmd, String s, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("copyworld")){
            return null;
        }
        if (args.length == 1){
            List<String> completion = new ArrayList<>();
            for (World w : Bukkit.getWorlds()){
                addToCompletion(w.getName(), args[0], completion);
            }
            return completion;
        }
        return null;
    }

    private void addToCompletion(String arg, String userInput, List<String> completion){
        if (arg.regionMatches(true, 0, userInput, 0, userInput.length()) || userInput.length() == 0){
            completion.add(arg);
        }
    }
}
