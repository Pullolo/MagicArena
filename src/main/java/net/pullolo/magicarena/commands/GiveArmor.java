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
        if (args[0].equalsIgnoreCase("test_armor")){
            if (args.length==2 && args[1].equalsIgnoreCase("maxed")){
                ((Player) sender).getInventory().addItem(new Item(testHelmet, 5, 100).getItem());
                ((Player) sender).getInventory().addItem(new Item(testChestplate, 5, 100).getItem());
                ((Player) sender).getInventory().addItem(new Item(testLeggings, 5, 100).getItem());
                ((Player) sender).getInventory().addItem(new Item(testBoots, 5, 100).getItem());
            } else if (args.length == 2 && args[1].equalsIgnoreCase("worst")) {
                ((Player) sender).getInventory().addItem(new Item(testHelmet, 0, 0).getItem());
                ((Player) sender).getInventory().addItem(new Item(testChestplate, 0, 0).getItem());
                ((Player) sender).getInventory().addItem(new Item(testLeggings, 0, 0).getItem());
                ((Player) sender).getInventory().addItem(new Item(testBoots, 0, 0).getItem());
            } else{
                ((Player) sender).getInventory().addItem(testHelmet.getItem());
                ((Player) sender).getInventory().addItem(testChestplate.getItem());
                ((Player) sender).getInventory().addItem(testLeggings.getItem());
                ((Player) sender).getInventory().addItem(testBoots.getItem());
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
            addToCompletion("test_armor", args[0], completion);
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
