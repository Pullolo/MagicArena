package net.pullolo.magicarena.commands;

import net.pullolo.magicarena.guis.AnimationManager;
import net.pullolo.magicarena.guis.GuiManager;
import net.pullolo.magicarena.items.ItemClass;
import net.pullolo.magicarena.wish.WishSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Gui implements CommandExecutor, TabCompleter {

    private final GuiManager guiManager;
    private final AnimationManager guiAnimations;

    public Gui(GuiManager guiManager, AnimationManager guiAnimations){
        this.guiManager = guiManager;
        this.guiAnimations = guiAnimations;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("gui")){
            return false;
        }
        if (!(sender instanceof Player)){
            return true;
        }
        if (args.length<1){
            return false;
        }
        if (args.length==2){
            if (args[0].equalsIgnoreCase("open")){
                if (args[1].equalsIgnoreCase("gameSelect")){
                    guiManager.createGameSelectGui((Player) sender).show((HumanEntity) sender);
                } else if (args[1].equalsIgnoreCase("wish")) {
                    guiManager.createWishGui((Player) sender).show((HumanEntity) sender);
                } else if (args[1].equalsIgnoreCase("menu")) {
                    guiManager.createMainMenuGui((Player) sender).show((HumanEntity) sender);
                } else sender.sendMessage(ChatColor.RED + "Invalid gui!");
            }
            if (args[0].equalsIgnoreCase("anim")){
                if (args[1].equalsIgnoreCase("wish")){
                    guiAnimations.playWishAnim((Player) sender, WishSystem.WishRarity.MYTHIC, WishSystem.WishType.WEAPON_WISH, 5, ItemClass.DPS, new ItemStack(Material.NETHERITE_SWORD), 10);
                } else sender.sendMessage(ChatColor.RED + "Invalid gui!");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("gui")){
            return null;
        }
        if (!(sender instanceof Player)){
            return null;
        }
        if (args.length == 1){
            List<String> completion = new ArrayList<>();
            addToCompletion("open", args[0], completion);
            addToCompletion("anim", args[0], completion);
            return completion;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("open")){
            List<String> completion = new ArrayList<>();
            addToCompletion("gameSelect", args[1], completion);
            addToCompletion("wish", args[1], completion);
            addToCompletion("menu", args[1], completion);
            return completion;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("anim")){
            List<String> completion = new ArrayList<>();
            addToCompletion("wish", args[1], completion);
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
