package net.pullolo.magicarena.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.pullolo.magicarena.MagicArena.guiManager;
import static net.pullolo.magicarena.players.ArenaPlayer.isPlayerInGame;

public class Open implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("open")){
            return false;
        }
        if (!(sender instanceof Player)){
            return false;
        }
        Player p = (Player) sender;
        if (isPlayerInGame(p)){
            p.sendMessage(ChatColor.RED + "You can't be in game to use this command!");
            return true;
        }
        if (args.length>0){
            if (args[0].equalsIgnoreCase("dungeon_chest")){
                try {
                    guiManager.createDungeonRewardMenu(p, Integer.parseInt(args[1]), Integer.parseInt(args[2]), true).show(p);
                } catch (Exception e){
                    p.sendMessage(ChatColor.RED + "Usage: /open dungeon_chest <score> <level>");
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("open")){
            return null;
        }
        if (args.length==1){
            ArrayList<String> completion = new ArrayList<>();
            addToCompletion("dungeon_chest", args[0], completion);
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
