package net.pullolo.magicarena.commands;

import net.pullolo.magicarena.items.Item;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.pullolo.magicarena.items.ArmorDefinitions.*;
import static net.pullolo.magicarena.items.ItemsDefinitions.*;
import static net.pullolo.magicarena.items.ItemsDefinitions.stormRuler;

public class GiveArmor implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("givearmor")){
            return false;
        }
        if (!(sender instanceof Player)){
            sender.sendMessage("Only players can execute this command!");
            return true;
        }
        if (args.length<1){
            return false;
        }
        if (armorItems.containsKey(args[0])){
            if (args.length==2 && args[1].equalsIgnoreCase("maxed")){
                for (Item item : armorItems.get(args[0])){
                    ((Player) sender).getInventory().addItem(new Item(item, 5, 100).getItem());
                }
            } else if (args.length == 2 && args[1].equalsIgnoreCase("worst")) {
                for (Item item : armorItems.get(args[0])){
                    ((Player) sender).getInventory().addItem(new Item(item, 0, 0).getItem());
                }
            } else{
                for (Item item : armorItems.get(args[0])){
                    ((Player) sender).getInventory().addItem(item.getItem());
                }
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("givearmor")){
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
            for (String s : armorItems.keySet()){
                addToCompletion(s, args[0], completion);
            }
            return completion;
        }
        if (args.length==2){
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
