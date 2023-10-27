package net.pullolo.magicarena.commands;

import net.pullolo.magicarena.data.XpManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.pullolo.magicarena.data.PlayerData.getPlayerData;

public class Data implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("data")){
            return false;
        }
        Player p;
        if (args.length==3){
            if (!(sender instanceof Player)){
                sender.sendMessage("No target!");
                return true;
            }
            p = (Player) sender;
        } else if (args.length == 4) {
            try {
                p = Bukkit.getPlayer(args[3]);
            } catch (Exception e){
                sender.sendMessage(ChatColor.RED + "This player is not online!");
                return true;
            }
        } else return false;
        String dataType = args[0];
        String operation = args[1];
        String value = args[2];
        try {
            switch (dataType){
                case "wishes":
                    if (operation.equalsIgnoreCase("set")) getPlayerData(p).setWishes(Integer.parseInt(value));
                    else if (operation.equalsIgnoreCase("add")) getPlayerData(p).setWishes(getPlayerData(p).getWishes() + Integer.parseInt(value));
                    else if (operation.equalsIgnoreCase("remove")) getPlayerData(p).setWishes(getPlayerData(p).getWishes() - Integer.parseInt(value));
                    if (getPlayerData(p).getWishes()<0) getPlayerData(p).setWishes(0);
                    break;
                case "star_essence":
                    if (operation.equalsIgnoreCase("set")) getPlayerData(p).setStarEssence(Integer.parseInt(value));
                    else if (operation.equalsIgnoreCase("add")) getPlayerData(p).setStarEssence(getPlayerData(p).getStarEssence() + Integer.parseInt(value));
                    else if (operation.equalsIgnoreCase("remove")) getPlayerData(p).setStarEssence(getPlayerData(p).getStarEssence() - Integer.parseInt(value));
                    if (getPlayerData(p).getStarEssence()<0) getPlayerData(p).setStarEssence(0);
                    break;
                case "dungeon_essence":
                    if (operation.equalsIgnoreCase("set")) getPlayerData(p).setDungeonEssence(Integer.parseInt(value));
                    else if (operation.equalsIgnoreCase("add")) getPlayerData(p).setDungeonEssence(getPlayerData(p).getDungeonEssence() + Integer.parseInt(value));
                    else if (operation.equalsIgnoreCase("remove")) getPlayerData(p).setDungeonEssence(getPlayerData(p).getDungeonEssence() - Integer.parseInt(value));
                    if (getPlayerData(p).getDungeonEssence()<0) getPlayerData(p).setDungeonEssence(0);
                    break;
                case "xp":
                    if (operation.equalsIgnoreCase("set")) getPlayerData(p).setXp(Double.parseDouble(value));
                    else if (operation.equalsIgnoreCase("add")) getPlayerData(p).setXp(getPlayerData(p).getXp() + Double.parseDouble(value));
                    else if (operation.equalsIgnoreCase("remove")) getPlayerData(p).setXp(getPlayerData(p).getXp() - Double.parseDouble(value));
                    if (getPlayerData(p).getXp()<0) getPlayerData(p).setXp(0);
                    XpManager.checkForLevelUp(p, getPlayerData(p));
                    break;
                case "level":
                    if (operation.equalsIgnoreCase("set")) getPlayerData(p).setLevel(Integer.parseInt(value));
                    else if (operation.equalsIgnoreCase("add")) getPlayerData(p).setLevel(getPlayerData(p).getLevel() + Integer.parseInt(value));
                    else if (operation.equalsIgnoreCase("remove")) getPlayerData(p).setLevel(getPlayerData(p).getLevel() - Integer.parseInt(value));
                    if (getPlayerData(p).getLevel()<1) getPlayerData(p).setLevel(1);
                    break;
            }
            sender.sendMessage(ChatColor.GREEN+ "Successful!");
        } catch (Exception e){
            sender.sendMessage(ChatColor.RED + "Invalid usage!");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("data")){
            return null;
        }
        if (args.length==1){
            ArrayList<String> completion = new ArrayList<>();
            addToCompletion("wishes", args[0], completion);
            addToCompletion("level", args[0], completion);
            addToCompletion("xp", args[0], completion);
            addToCompletion("dungeon_essence", args[0], completion);
            addToCompletion("star_essence", args[0], completion);
            return completion;
        }
        if (args.length==2){
            ArrayList<String> completion = new ArrayList<>();
            addToCompletion("set", args[1], completion);
            addToCompletion("add", args[1], completion);
            addToCompletion("remove", args[1], completion);
            return completion;
        }
        if (args.length==3){
            return new ArrayList<>();
        }
        if (args.length==4){
            ArrayList<String> completion = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()){
                addToCompletion(p.getDisplayName(), args[3], completion);
            }
            return completion;
        }
        return new ArrayList<>();
    }

    private void addToCompletion(String arg, String userInput, List<String> completion){
        if (arg.regionMatches(true, 0, userInput, 0, userInput.length()) || userInput.length() == 0){
            completion.add(arg);
        }
    }
}
