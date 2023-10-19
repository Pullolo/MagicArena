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
        }
        if (args[0].equalsIgnoreCase("test_item")){
            if (args.length==2 && args[1].equalsIgnoreCase("maxed")){
                ((Player) sender).getInventory().addItem(new Item(testItem, 5, 100).getItem());
            } else if (args.length == 2 && args[1].equalsIgnoreCase("worst")) {
                ((Player) sender).getInventory().addItem(new Item(testItem, 0, 0).getItem());
            } else ((Player) sender).getInventory().addItem(testItem.getItem());
        }
        if (args[0].equalsIgnoreCase("basic_sword")){
            if (args.length==2 && args[1].equalsIgnoreCase("maxed")){
                ((Player) sender).getInventory().addItem(new Item(basicSword, 5, 100).getItem());
            } else if (args.length == 2 && args[1].equalsIgnoreCase("worst")) {
                ((Player) sender).getInventory().addItem(new Item(basicSword, 0, 0).getItem());
            } else ((Player) sender).getInventory().addItem(basicSword.getItem());
        }
        if (args[0].equalsIgnoreCase("stone_stick")){
            if (args.length==2 && args[1].equalsIgnoreCase("maxed")){
                ((Player) sender).getInventory().addItem(new Item(stoneStick, 5, 100).getItem());
            } else if (args.length == 2 && args[1].equalsIgnoreCase("worst")) {
                ((Player) sender).getInventory().addItem(new Item(stoneStick, 0, 0).getItem());
            } else ((Player) sender).getInventory().addItem(stoneStick.getItem());
        }
        if (args[0].equalsIgnoreCase("leeching_staff")){
            if (args.length==2 && args[1].equalsIgnoreCase("maxed")){
                ((Player) sender).getInventory().addItem(new Item(leechingStaff, 5, 100).getItem());
            } else if (args.length == 2 && args[1].equalsIgnoreCase("worst")) {
                ((Player) sender).getInventory().addItem(new Item(leechingStaff, 0, 0).getItem());
            } else ((Player) sender).getInventory().addItem(leechingStaff.getItem());
        }
        if (args[0].equalsIgnoreCase("unstable_tome")){
            if (args.length==2 && args[1].equalsIgnoreCase("maxed")){
                ((Player) sender).getInventory().addItem(new Item(unstableTome, 5, 100).getItem());
            } else if (args.length == 2 && args[1].equalsIgnoreCase("worst")) {
                ((Player) sender).getInventory().addItem(new Item(unstableTome, 0, 0).getItem());
            } else ((Player) sender).getInventory().addItem(unstableTome.getItem());
        }
        if (args[0].equalsIgnoreCase("long_bow")){
            if (args.length==2 && args[1].equalsIgnoreCase("maxed")){
                ((Player) sender).getInventory().addItem(new Item(longBow, 5, 100).getItem());
            } else if (args.length == 2 && args[1].equalsIgnoreCase("worst")) {
                ((Player) sender).getInventory().addItem(new Item(longBow, 0, 0).getItem());
            } else ((Player) sender).getInventory().addItem(longBow.getItem());
        }
        if (args[0].equalsIgnoreCase("solid_stone_axe")){
            if (args.length==2 && args[1].equalsIgnoreCase("maxed")){
                ((Player) sender).getInventory().addItem(new Item(solidStoneAxe, 5, 100).getItem());
            } else if (args.length == 2 && args[1].equalsIgnoreCase("worst")) {
                ((Player) sender).getInventory().addItem(new Item(solidStoneAxe, 0, 0).getItem());
            } else ((Player) sender).getInventory().addItem(solidStoneAxe.getItem());
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
            addToCompletion("blade_of_the_universe", args[0], completion);
            addToCompletion("test_item", args[0], completion);
            addToCompletion("basic_sword", args[0], completion);
            addToCompletion("stone_stick", args[0], completion);
            addToCompletion("leeching_staff", args[0], completion);
            addToCompletion("long_bow", args[0], completion);
            addToCompletion("unstable_tome", args[0], completion);
            addToCompletion("solid_stone_axe", args[0], completion);
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
