package net.pullolo.magicarena.commands;

import net.pullolo.magicarena.quests.Quest;
import net.pullolo.magicarena.quests.QuestManager;
import net.pullolo.magicarena.quests.QuestType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Quests implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("quests")){
            return false;
        }
        if (!(sender instanceof Player)){
            sender.sendMessage("Only players can do this.");
            return true;
        }
        Player p = (Player) sender;
        if (args.length<1) return false;
        if (args.length==1) {
            if (args[0].equalsIgnoreCase("get")){
                for (int i = 0; i<QuestManager.getPlayerQuests(p).size(); i++){
                    p.sendMessage(ChatColor.DARK_AQUA + "" + i + ": " + QuestManager.getPlayerQuests(p).get(i).getQuestType().toString());
                }
            }
        };
        if (args.length==2){
            if (args[0].equalsIgnoreCase("get")){
                try {
                    p.sendMessage(ChatColor.DARK_AQUA + QuestManager.getPlayerQuests(p).get(Integer.parseInt(args[1])).toString());
                    return true;
                } catch (Exception e){
                    sender.sendMessage(ChatColor.RED + "Quest not found!");
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("remove")){
                try {
                    QuestManager.finishQuest(p, QuestManager.getPlayerQuests(p).get(Integer.parseInt(args[1])));
                    p.sendMessage(ChatColor.GREEN + "Removed!");
                    return true;
                } catch (Exception e){
                    sender.sendMessage(ChatColor.RED + "Quest not found!");
                    return true;
                }
            }
        }
        if (args.length==3){
            if (args[0].equalsIgnoreCase("add")){
                try {
                    QuestManager.addPlayerQuest(p, new Quest(p, QuestType.valueOf(args[1].toUpperCase()), Integer.parseInt(args[2]), 0));
                    p.sendMessage(ChatColor.GREEN + "Added!");
                    return true;
                } catch (Exception e){
                    sender.sendMessage(ChatColor.RED + "Invalid usage!");
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("quests")){
            return null;
        }
        if (!(sender instanceof Player)){
            return new ArrayList<>();
        }
        if (args.length==1){
            ArrayList<String> completion = new ArrayList<>();
            addToCompletion("add", args[0], completion);
            addToCompletion("get", args[0], completion);
            addToCompletion("remove", args[0], completion);
            return completion;
        }
        if (args.length==2){
            ArrayList<String> completion = new ArrayList<>();
            if (args[0].equalsIgnoreCase("add")){
                for (QuestType q : QuestType.values()){
                    addToCompletion(q.toString(), args[1], completion);
                }
            } else {
                for (int i = 0; i<QuestManager.getPlayerQuests((Player) sender).size(); i++){
                    addToCompletion(String.valueOf(i), args[1], completion);
                }
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
