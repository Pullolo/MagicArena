package net.pullolo.magicarena.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;


public class Stats implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("stats")){
            if (!(sender instanceof Player)){
                return false;
            }
            if (args.length > 0){
                try {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (arenaPlayers.containsKey(target)){
                        sender.sendMessage("§7Displaying stats for " + target.getName());
                        sender.sendMessage("§7-------------------------------");
                        sender.sendMessage("§7Level: §f" + Math.round(arenaPlayers.get(target).getLevel()));
                        sender.sendMessage("§7Max Health: §4" + Math.round(arenaPlayers.get(target).getMaxHealth()));
                        sender.sendMessage("§7Health Regen: §4" + Math.round(arenaPlayers.get(target).getHpRegen()));
                        sender.sendMessage("§7Mana Regen: §b" + Math.round(arenaPlayers.get(target).getManaRegen()));
                        sender.sendMessage("§7Max Mana: §b" + Math.round(arenaPlayers.get(target).getMaxMana()));
                        sender.sendMessage("§7Defence: §a" + Math.round(arenaPlayers.get(target).getDefence()));
                        sender.sendMessage("§7Resistance: §3" + Math.round(arenaPlayers.get(target).getMagicDefence()));
                        sender.sendMessage("§7Ability Power: §3" + Math.round(arenaPlayers.get(target).getMagicDamage()));
                        sender.sendMessage("§7Speed: §f" + Math.round(arenaPlayers.get(target).getSpeed()));
                        sender.sendMessage("§7Damage: §c" + Math.round(arenaPlayers.get(target).getDamage()));
                        sender.sendMessage("§7Crit Chance: §c" + Math.round(arenaPlayers.get(target).getCritChance()) + " %");
                        sender.sendMessage("§7Crit Damage: §c" + Math.round(arenaPlayers.get(target).getCritDamage()));
                        sender.sendMessage("§7-------------------------------");
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                if (arenaPlayers.containsKey(sender)){
                    sender.sendMessage("§7Displaying stats for " + sender.getName());
                    sender.sendMessage("§7-------------------------------");
                    sender.sendMessage("§7Level: §f" + Math.round(arenaPlayers.get(sender).getLevel()));
                    sender.sendMessage("§7Max Health: §4" + Math.round(arenaPlayers.get(sender).getMaxHealth()));
                    sender.sendMessage("§7Health Regen: §4" + Math.round(arenaPlayers.get(sender).getHpRegen()));
                    sender.sendMessage("§7Mana Regen: §b" + Math.round(arenaPlayers.get(sender).getManaRegen()));
                    sender.sendMessage("§7Max Mana: §b" + Math.round(arenaPlayers.get(sender).getMaxMana()));
                    sender.sendMessage("§7Defence: §a" + Math.round(arenaPlayers.get(sender).getDefence()));
                    sender.sendMessage("§7Resistance: §3" + Math.round(arenaPlayers.get(sender).getMagicDefence()));
                    sender.sendMessage("§7Ability Power: §3" + Math.round(arenaPlayers.get(sender).getMagicDamage()));
                    sender.sendMessage("§7Speed: §f" + Math.round(arenaPlayers.get(sender).getSpeed()));
                    sender.sendMessage("§7Damage: §c" + Math.round(arenaPlayers.get(sender).getDamage()));
                    sender.sendMessage("§7Crit Chance: §c" + Math.round(arenaPlayers.get(sender).getCritChance()) + " %");
                    sender.sendMessage("§7Crit Damage: §c" + Math.round(arenaPlayers.get(sender).getCritDamage()));
                    sender.sendMessage("§7-------------------------------");
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
