package net.pullolo.magicarena.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.pullolo.magicarena.MagicArena.partyManager;

public class Party implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("party")){
            return false;
        }
        if (!(sender instanceof Player)){
            sender.sendMessage("This command can be executed only by players!");
            return true;
        }
        Player p = (Player) sender;
        if (args.length<1){
            if (partyManager.isPlayerInParty(p)){
                sender.sendMessage(ChatColor.GRAY + "-----------------------------------------");
                for (Player player : partyManager.getPlayersParty(p)){
                    p.sendMessage(player.getDisplayName());
                }
                sender.sendMessage(ChatColor.GRAY + "-----------------------------------------");
                return true;
            }
            sender.sendMessage(ChatColor.RED + "[Party] You are not in a party!");
            return true;
        }
        if (args.length==1){
            if (args[0].equalsIgnoreCase("list")){
                if (partyManager.isPlayerInParty(p)){
                    sender.sendMessage(ChatColor.GRAY + "-----------------------------------------");
                    for (Player player : partyManager.getPlayersParty(p)){
                        p.sendMessage(player.getDisplayName());
                    }
                    sender.sendMessage(ChatColor.GRAY + "-----------------------------------------");
                    return true;
                }
                sender.sendMessage(ChatColor.RED + "[Party] You are not in a party!");
                return true;
            }
            if (args[0].equalsIgnoreCase("create")){
                if (partyManager.isPlayerInParty(p)) {
                    sender.sendMessage(ChatColor.RED + "[Party] You are already in a party!");
                    return true;
                }
                partyManager.createParty(p);
                return true;
            }
            if (args[0].equalsIgnoreCase("leave")){
                partyManager.leaveParty(p);
                return true;
            }
            if (args[0].equalsIgnoreCase("join")){
                partyManager.joinParty(p);
                return true;
            }
            if (args[0].equalsIgnoreCase("decline")){
                partyManager.declineInvite(p);
                return true;
            }
            if (args[0].equalsIgnoreCase("disband")){
                partyManager.disband(p);
                return true;
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("invite")){
                try {
                    partyManager.inviteToParty(p, Bukkit.getPlayer(args[1]));
                } catch (Exception e){
                    sender.sendMessage(ChatColor.RED + "[Party] This player does not exist!");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("kick")){
                try {
                    partyManager.kickPlayer(p, Bukkit.getPlayer(args[1]));
                } catch (Exception e){
                    sender.sendMessage(ChatColor.RED + "[Party] This player does not exist!");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("transfer")){
                try {
                    partyManager.transferOwnerShip(p, Bukkit.getPlayer(args[1]));
                } catch (Exception e){
                    sender.sendMessage(ChatColor.RED + "[Party] This player does not exist!");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("chat")){
                partyManager.message(p, args[1]);
                return true;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("party")){
            return null;
        }
        if (!(sender instanceof Player)){
            return null;
        }
        if (args.length==1){
            List<String> completion = new ArrayList<>();
            addToCompletion("create", args[0], completion);
            addToCompletion("invite", args[0], completion);
            addToCompletion("join", args[0], completion);
            addToCompletion("decline", args[0], completion);
            addToCompletion("leave", args[0], completion);
            addToCompletion("kick", args[0], completion);
            addToCompletion("list", args[0], completion);
            addToCompletion("transfer", args[0], completion);
            addToCompletion("chat", args[0], completion);
            addToCompletion("disband", args[0], completion);
            return completion;
        }
        if (args.length==2 && (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("transfer"))){
            boolean playersInParty = args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("transfer");
            List<String> completion = new ArrayList<>();

            if (playersInParty && partyManager.isPlayerInParty((Player) sender)){
                for (Player p : partyManager.getPlayersParty((Player) sender)){
                    addToCompletion(p.getDisplayName(), args[1], completion);
                }
            } else {
                for (Player p : Bukkit.getOnlinePlayers()){
                    addToCompletion(p.getDisplayName(), args[1], completion);
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
