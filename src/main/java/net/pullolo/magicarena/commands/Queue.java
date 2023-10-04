package net.pullolo.magicarena.commands;

import net.pullolo.magicarena.game.GameManager;
import net.pullolo.magicarena.game.QueueManager;
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

import static net.pullolo.magicarena.MagicArena.gameManager;
import static net.pullolo.magicarena.MagicArena.partyManager;

public class Queue implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("queue")){
            return false;
        }
        if (args.length<1){
            return false;
        }
        if (args.length==1){
            if (args[0].equalsIgnoreCase("list")){
                sender.sendMessage(ChatColor.GRAY + "-----------------------------------------");
                for (Player p : gameManager.getQueueManager().getPlayersInQueue()){
                    sender.sendMessage(ChatColor.GREEN + p.getDisplayName());
                }
                sender.sendMessage(ChatColor.GRAY + "-----------------------------------------");
            }
            if (!(sender instanceof Player)){
                sender.sendMessage( ChatColor.RED + "Only players can join queue!");
                return true;
            }
            if (args[0].equalsIgnoreCase("solo")){
                sender.sendMessage( ChatColor.GREEN + "Joining queue");
                gameManager.getQueueManager().addPlayerToQueue((Player) sender, QueueManager.QueueType.SOLO);
            }
            if (args[0].equalsIgnoreCase("duo")){
                sender.sendMessage( ChatColor.GREEN + "Joining queue");
                if (partyManager.isPlayerInParty((Player) sender)){
                    gameManager.getQueueManager().addPartyToQueue(partyManager.getPlayersParty((Player) sender), QueueManager.QueueType.DUO);
                } else gameManager.getQueueManager().addPlayerToQueue((Player) sender, QueueManager.QueueType.DUO);
            }
            if (args[0].equalsIgnoreCase("leave")){
                sender.sendMessage( ChatColor.GREEN + "Leaving queue");
                gameManager.getQueueManager().removePlayerFromQueue((Player) sender);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("queue")){
            return null;
        }
        if (args.length == 1){
            List<String> completion = new ArrayList<>();
            addToCompletion("list", args[0], completion);
            if (sender instanceof Player){
                addToCompletion("solo", args[0], completion);
                addToCompletion("duo", args[0], completion);
                addToCompletion("leave", args[0], completion);
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
