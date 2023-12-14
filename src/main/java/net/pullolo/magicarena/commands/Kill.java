package net.pullolo.magicarena.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.pullolo.magicarena.players.ArenaEntity.arenaEntities;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;

public class Kill implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("kill")){
            return false;
        }
        if (args.length<1){
            if (sender instanceof Player){
                if (arenaPlayers.containsKey(sender)){
                    arenaPlayers.get(sender).setHealth(0);
                } else ((Player) sender).setHealth(0);
            }
        } else {
            if (args[0].equalsIgnoreCase("allmobs")){
                try {
                    sender.sendMessage(ChatColor.YELLOW + "Note that this command only deletes custom mobs!");
                    ArrayList<Entity> toDel = new ArrayList<>(arenaEntities.keySet());
                    int i = 0;
                    for (Entity e : toDel){
                        arenaEntities.get(e).getGame().removeEntity(e);
                        arenaEntities.remove(e);
                        e.remove();
                        i++;
                    }
                    if (i==1){
                        sender.sendMessage(ChatColor.GREEN + "Deleted " + i + " mob!");
                    } else sender.sendMessage(ChatColor.GREEN + "Deleted " + i + " mobs!");
                    return true;
                } catch (Exception e){
                    sender.sendMessage(ChatColor.RED + "Something went Wrong!");
                    return true;
                }
            }
            try {
                Player target = Bukkit.getPlayer(args[0]);
                if (arenaPlayers.containsKey(target)){
                    arenaPlayers.get(target).setHealth(0);
                } else target.setHealth(0);
                return true;
            } catch (Exception e){
                sender.sendMessage(ChatColor.RED + "This player is not online!");
                return true;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("kill")){
            return null;
        }
        if (args.length==1){
            List<String> completion = new ArrayList<>();
            addToCompletion("allmobs", args[0], completion);
            for (Player p : Bukkit.getOnlinePlayers()){
                addToCompletion(p.getDisplayName(), args[0], completion);
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
