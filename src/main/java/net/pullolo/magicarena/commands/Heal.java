package net.pullolo.magicarena.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;
import static net.pullolo.magicarena.players.ArenaPlayer.isPlayerInGame;

public class Heal implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("Only players can do this.");
            return true;
        }
        Player p = (Player) sender;
        if (!isPlayerInGame(p)){
            sender.sendMessage(ChatColor.RED + "You are not in game.");
            return true;
        }
        arenaPlayers.get(p).setHealth(arenaPlayers.get(p).getMaxHealth());
        arenaPlayers.get(p).setMana(arenaPlayers.get(p).getMaxMana());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        return null;
    }
}
