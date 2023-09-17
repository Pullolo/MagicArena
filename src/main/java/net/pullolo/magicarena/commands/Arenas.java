package net.pullolo.magicarena.commands;

import net.pullolo.magicarena.worlds.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class Arenas implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("arenas")){
            return false;
        }
        if (args.length<1){
            return false;
        }
        if (args.length==1){
            if (args[0].equalsIgnoreCase("list")){
                sender.sendMessage(ChatColor.GRAY + "-----------------------------------------");
                for (String s : WorldManager.getArenas()){
                    sender.sendMessage(ChatColor.GREEN + s.split("_")[1]);
                }
                sender.sendMessage(ChatColor.GRAY + "-----------------------------------------");
            }
        }
        if (args.length==2){
            try{
                if (args[0].equalsIgnoreCase("save")){
                    World w = Bukkit.getWorld(args[1]);
                    WorldManager.saveWorld(w, false, true);
                    WorldManager.deleteWorld(w.getName());
                    WorldManager.unloadWorld(w, true);
                }
                if (args[0].equalsIgnoreCase("edit")) {
                    if (!WorldManager.getArenas().contains("arena_" + args[1])){
                        sender.sendMessage(ChatColor.RED + "This arena does not exist!");
                        return true;
                    }
                    WorldManager.deleteWorld("arena_" + args[1]);
                    WorldManager.saveWorld(new WorldCreator(args[1]).createWorld(), false, false);
                }
                if (args[0].equalsIgnoreCase("remove")){
                    if (!WorldManager.getArenas().contains("arena_" + args[1])){
                        sender.sendMessage(ChatColor.RED + "This arena does not exist!");
                        return true;
                    }
                    WorldManager.removeDisabledWorld(new File(getServer().getWorldContainer().getAbsolutePath().replace(".", "") + args[1]), "arena_" + args[1]);
                }
            } catch (Exception e){
                sender.sendMessage(ChatColor.RED + "An error occurred!");
            }

        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("arenas")){
            return null;
        }
        if (args.length == 1){
            List<String> completion = new ArrayList<>();
            addToCompletion("save", args[0], completion);
            addToCompletion("edit", args[0], completion);
            addToCompletion("remove", args[0], completion);
            addToCompletion("list", args[0], completion);
            return completion;
        }
        if (args.length == 2){
            List<String> completion = new ArrayList<>();
            if (args[0].equalsIgnoreCase("save")){
                for (World w : Bukkit.getWorlds()){
                    addToCompletion(w.getName(), args[1], completion);
                }
            }
            if (args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("remove")){
                for (String s : WorldManager.getArenas()){
                    addToCompletion(s.split("_")[1], args[1], completion);
                }
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
