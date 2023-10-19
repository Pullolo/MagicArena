package net.pullolo.magicarena.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class GetCustomName implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("getcustomname")){
            return false;
        }
        if (!(sender instanceof Player)){
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        Player p = (Player) sender;
        Entity target = null;
        for (Entity e : p.getWorld().getNearbyEntities(p.getLocation(), 2, 2, 2)){
            if (!(e instanceof Player)){
                target=e;
                break;
            }
        }
        try {
            p.sendMessage(target.getCustomName());
        } catch (Exception e){
            p.sendMessage(ChatColor.RED + "No entities found!");
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
