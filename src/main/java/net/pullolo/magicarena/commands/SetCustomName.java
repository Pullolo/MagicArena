package net.pullolo.magicarena.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import java.util.List;

public class SetCustomName implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("setcustomname")){
            return false;
        }
        if (!(sender instanceof Player)){
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        Player p = (Player) sender;
        if (args.length>=1){
            Entity target = null;
            for (Entity e : p.getWorld().getNearbyEntities(p.getLocation(), 2, 2, 2)){
                if (!(e instanceof Player || e instanceof ItemFrame)){
                    target=e;
                    break;
                }
            }
            try {
                StringBuilder cName = new StringBuilder();
                for (String arg : args) {
                    cName.append(arg).append(" ");
                }
                target.setCustomName(ChatColor.translateAlternateColorCodes('&', cName.toString()));
                p.sendMessage(ChatColor.GREEN + "Successful!");
            } catch (Exception e){
                p.sendMessage(ChatColor.RED + "No entities found!");
                return true;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
