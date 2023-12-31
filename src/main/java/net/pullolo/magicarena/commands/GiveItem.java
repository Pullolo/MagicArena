package net.pullolo.magicarena.commands;

import net.pullolo.magicarena.items.Item;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.pullolo.magicarena.items.ItemsDefinitions.*;

public class GiveItem implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("giveitem")){
            return false;
        }
        if (!(sender instanceof Player)){
            sender.sendMessage("Only players can execute this command!");
            return true;
        }
        if (args.length<1){
            return false;
        }
        if (args[0].equalsIgnoreCase("copy")){
            if (((Player) sender).getInventory().getItemInMainHand().getItemMeta()!=null){
                ((Player) sender).getInventory().addItem(getItemFromPlayer(((Player) sender).getInventory().getItemInMainHand()).getItem());
            }
        }
        if (args[0].equalsIgnoreCase("blade_of_the_universe")){
            ((Player) sender).getInventory().addItem(bladeOfTheUniverse.getItem());
        } else if (items.containsKey(args[0])){
            if (args.length==2 && args[1].equalsIgnoreCase("maxed")){
                ((Player) sender).getInventory().addItem(new Item(items.get(args[0]), 5, 100).getItem());
            } else if (args.length == 2 && args[1].equalsIgnoreCase("worst")) {
                ((Player) sender).getInventory().addItem(new Item(items.get(args[0]), 0, 0).getItem());
            } else ((Player) sender).getInventory().addItem(items.get(args[0]).getItem());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("giveitem")){
            return null;
        }
        if (!(sender instanceof Player)){
            sender.sendMessage("Only players can execute this command!");
            return null;
        }
        if (args.length<1){
            return null;
        }
        if (args.length==1){
            ArrayList<String> completion = new ArrayList<>();
            addToCompletion("copy", args[0], completion);
            for (String s : items.keySet()){
                addToCompletion(s, args[0], completion);
            }
            return completion;
        }
        if (args.length==2 && !args[0].equalsIgnoreCase("blade_of_the_universe")){
            ArrayList<String> completion = new ArrayList<>();
            addToCompletion("maxed", args[1], completion);
            addToCompletion("worst", args[1], completion);
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
