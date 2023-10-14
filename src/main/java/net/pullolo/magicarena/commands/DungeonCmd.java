package net.pullolo.magicarena.commands;

import net.pullolo.magicarena.game.Dungeon;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.pullolo.magicarena.MagicArena.partyManager;
import static net.pullolo.magicarena.players.ArenaPlayer.isPlayerInGame;

public class DungeonCmd implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("dungeon")){
            return false;
        }
        if (!(sender instanceof Player)){
            sender.sendMessage("Only players can execute this command!");
            return true;
        }
        Player p = (Player) sender;
        if (partyManager.isPlayerInParty(p) && partyManager.isPartyOwner(p)){
            for (Player member : partyManager.getPlayersParty(p)){
                if (isPlayerInGame(member)){
                    sender.sendMessage("No players can be in game!");
                    return true;
                }
            }
            sender.sendMessage(ChatColor.GREEN + "Creating Dungeon...");
            new Dungeon(partyManager.getPlayersParty(p), 1, Dungeon.Difficulty.NORMAL, false);
            return true;
        } else sender.sendMessage(ChatColor.RED + "You need to be in a party and be it's owner to execute this command!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
//        if (!cmd.getName().equalsIgnoreCase("game")){
//            return null;
//        }
//        if (args.length==1){
//            ArrayList<String> completion = new ArrayList<>();
//            addToCompletion("start", args[0], completion);
//            addToCompletion("dungeon", args[0], completion);
//            addToCompletion("end", args[0], completion);
//            return completion;
//        }
        return null;
    }

    private void addToCompletion(String arg, String userInput, List<String> completion){
        if (arg.regionMatches(true, 0, userInput, 0, userInput.length()) || userInput.length() == 0){
            completion.add(arg);
        }
    }
}
