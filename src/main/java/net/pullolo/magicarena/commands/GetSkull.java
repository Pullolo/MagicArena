package net.pullolo.magicarena.commands;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class GetSkull implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("getskull")){
            return false;
        }
        if (!(sender instanceof Player)){
            return true;
        }
        Player p = (Player) sender;
        if (args.length==1){
            try {
                p.getInventory().addItem(getPlayerSkull(args[0]));
            } catch (Exception e){
                p.sendMessage(ChatColor.RED + "Error!");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }

    private ItemStack getPlayerSkull(String base64){
        return SkullCreator.itemFromBase64(base64);
    }
}


