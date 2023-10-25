package net.pullolo.magicarena.commands;

import net.pullolo.magicarena.data.PlayerData;
import net.pullolo.magicarena.players.UpdateManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.pullolo.magicarena.MagicArena.dbManager;
import static net.pullolo.magicarena.MagicArena.debugLog;
import static net.pullolo.magicarena.data.PlayerData.getPlayerData;
import static net.pullolo.magicarena.players.UpdateManager.updatePlayer;

public class UpdateGame implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("updategame")){
            return false;
        }
        if (args.length!=1){
            return false;
        }
        if (args[0].equalsIgnoreCase("all")){
            dbManager.updateGame();
            for (Player p : Bukkit.getOnlinePlayers()){
                getPlayerData(p).setUpdated(false);
                updatePlayer(p);
                getPlayerData(p).setUpdated(true);
            }
            return true;
        }
        try {
            Player target = Bukkit.getPlayer(args[0]);
            updatePlayer(target);
        } catch (Exception e){
            sender.sendMessage(ChatColor.RED + "This player can not be updated!");
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("updategame")){
            return null;
        }
        if (args.length==1){
            ArrayList<String> completion = new ArrayList<>();
            addToCompletion("all", args[0], completion);
            for (Player p : Bukkit.getOnlinePlayers()){
                addToCompletion(p.getDisplayName(), args[0], completion);
            }
            return completion;
        }
        return new ArrayList<>();
    }

    private void addToCompletion(String arg, String userInput, List<String> completion){
        if (arg.regionMatches(true, 0, userInput, 0, userInput.length()) || userInput.length() == 0){
            completion.add(arg);
        }
    }
}
