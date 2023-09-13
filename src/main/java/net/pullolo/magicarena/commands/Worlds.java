package net.pullolo.magicarena.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Worlds implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("worlds")){
            return false;
        }
        if (args.length<1){
            return false;
        }
        if (args[0].equalsIgnoreCase("list")){
            sender.sendMessage(ChatColor.GRAY + "-----------------------------------------");
            for (World w : Bukkit.getWorlds()){
                sender.sendMessage(ChatColor.GREEN + w.getName());
            }
            sender.sendMessage(ChatColor.GRAY + "-----------------------------------------");
        } else if (args.length==2 && args[0].equalsIgnoreCase("tp") && sender instanceof Player){
            try{
                ((Player) sender).teleport(Bukkit.getWorld(args[1]).getSpawnLocation());
            } catch (Exception e){
                sender.sendMessage(ChatColor.RED + "You can't be teleported there!");
            }
        } else return false;
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("worlds")){
            return null;
        }
        if (args.length==1){
            List<String> completion = new ArrayList<>();
            addToCompletion("tp", args[0], completion);
            addToCompletion("list", args[0], completion);
            return completion;
        } else if (args.length==2 && !args[0].equalsIgnoreCase("list")){
            List<String> completion = new ArrayList<>();
            for (World w : Bukkit.getWorlds()){
                addToCompletion(w.getName(), args[1], completion);
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
