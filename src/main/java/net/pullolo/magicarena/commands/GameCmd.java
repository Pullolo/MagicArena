package net.pullolo.magicarena.commands;

import net.pullolo.magicarena.game.Game;
import net.pullolo.magicarena.game.QueueManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;
import static net.pullolo.magicarena.players.ArenaPlayer.isPlayerInGame;

public class GameCmd implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("game")){
            return false;
        }
        if (args.length<1){
            return false;
        }
        if (!(sender instanceof Player)){
            sender.sendMessage("This command can be executed only by players!");
            return true;
        }
        if (args.length==2){
            if (args[0].equalsIgnoreCase("start")){
                if (isPlayerInGame((Player) sender)){
                    sender.sendMessage( ChatColor.RED + "You are currently in game!");
                    return true;
                }
                ArrayList<Player> senderToArray = new ArrayList<>();
                senderToArray.add((Player) sender);
                try {
                    if (args[1].equalsIgnoreCase("solo")){
                        new Game(senderToArray, new ArrayList<>(), QueueManager.QueueType.SOLO, false, true);
                    }
                    if (args[1].equalsIgnoreCase("duo")){
                        new Game(senderToArray, new ArrayList<>(), QueueManager.QueueType.DUO, false, true);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    sender.sendMessage(ChatColor.RED + "An error occurred while creating a game!");
                }
            }
        }
        if (args.length==1){
            if (args[0].equalsIgnoreCase("end")){
                if (!isPlayerInGame((Player) sender)){
                    sender.sendMessage( ChatColor.RED + "You are currently not in game!");
                    return true;
                }
                arenaPlayers.get(sender).getGame().forceEndGame();
            } else return false;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("game")){
            return null;
        }
        if (args.length==1){
            ArrayList<String> completion = new ArrayList<>();
            addToCompletion("start", args[0], completion);
            addToCompletion("end", args[0], completion);
            return completion;
        } else if (args.length==2 && args[0].equalsIgnoreCase("start")){
            ArrayList<String> completion = new ArrayList<>();
            addToCompletion("solo", args[1], completion);
            addToCompletion("duo", args[1], completion);
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
