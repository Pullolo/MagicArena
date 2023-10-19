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

import static net.pullolo.magicarena.MagicArena.gameManager;
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
        if (args.length<1){
            return false;
        }
        int lvl;
        try{
            lvl = Integer.parseInt(args[1]);
        } catch (Exception e){
            return false;
        }
        if (!(lvl==10 || lvl==50 || lvl==90)){
            sender.sendMessage(ChatColor.RED + "Invalid dungeon level!");
            return true;
        }
        if (partyManager.isPlayerInParty(p) && partyManager.isPartyOwner(p) && !gameManager.getQueueManager().isPlayerInQueue(p)){
            for (Player member : partyManager.getPlayersParty(p)){
                if (isPlayerInGame(member)){
                    sender.sendMessage(ChatColor.RED + "No players can be in game!");
                    return true;
                }
            }
            sender.sendMessage(ChatColor.GREEN + "Creating Dungeon...");
            new Dungeon(partyManager.getPlayersParty(p), lvl, Dungeon.getRandomDifficulty(), false);
            return true;
        } else sender.sendMessage(ChatColor.RED + "You need to be in a party and be it's owner to execute this command!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("dungeon")){
            return null;
        }
        if (args.length==1){
            ArrayList<String> completion = new ArrayList<>();
            addToCompletion("10", args[0], completion);
            addToCompletion("50", args[0], completion);
            addToCompletion("90", args[0], completion);
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
