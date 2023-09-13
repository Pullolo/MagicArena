package net.pullolo.magicarena.commands;

import net.pullolo.magicarena.worlds.WorldManager;
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

import static net.pullolo.magicarena.MagicArena.mainWorld;
import static net.pullolo.magicarena.worlds.WorldManager.removeWorld;

public class DeleteWorld implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("deleteworld")){
            return false;
        }
        if (args.length<1){
            return false;
        }
        try {
            if ((sender instanceof Player) && ((Player) sender).getWorld().equals(Bukkit.getWorld(args[0]))){
                ((Player) sender).teleport(Bukkit.getWorld(mainWorld).getSpawnLocation());
            }
            removeWorld(Bukkit.getWorld(args[0]));
            sender.sendMessage(ChatColor.GREEN + "Successfully deleted world " + args[0] + "!");
        } catch (Exception e){
            sender.sendMessage(ChatColor.RED + "Could not remove world " + args[0] + "!");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command cmd, String s, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("deleteworld")){
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
