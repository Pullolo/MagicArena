package net.pullolo.magicarena.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

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
            try {
                Player target = Bukkit.getPlayer(args[0]);
                if (arenaPlayers.containsKey(target)){
                    arenaPlayers.get(target).setHealth(0);
                } else target.setHealth(0);
            } catch (Exception e){
                sender.sendMessage(ChatColor.RED + "This player is not online!");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
