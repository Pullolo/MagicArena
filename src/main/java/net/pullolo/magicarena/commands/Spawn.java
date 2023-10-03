package net.pullolo.magicarena.commands;

import net.pullolo.magicarena.players.ArenaEntity;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.pullolo.magicarena.players.ArenaEntity.arenaEntities;
import static net.pullolo.magicarena.players.ArenaPlayer.arenaPlayers;
import static net.pullolo.magicarena.players.ArenaPlayer.isPlayerInGame;

public class Spawn implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("spawn")){
            return false;
        }
        if (!(sender instanceof Player)){
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length>0){
            try {
                boolean dummy = false;
                int level = 1;
                Entity e = player.getWorld().spawnEntity(player.getLocation(), EntityType.valueOf(args[0].toUpperCase()));
                if (args.length>1){
                    level = Integer.parseInt(args[1]);
                }
                if (args.length>2){
                    if (args[2].equalsIgnoreCase("dummy")){
                        dummy=true;
                        if (e instanceof LivingEntity){
                            ((LivingEntity) e).setAI(false);
                        }
                    }
                }
                if (isPlayerInGame(player) && (e instanceof LivingEntity)) {
                    arenaEntities.put(e, new ArenaEntity(e, level, arenaPlayers.get(player).getGame(), dummy));
                }
            } catch (Exception e){
                sender.sendMessage("This mob doesn't exist!");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("spawn")){
            return null;
        }
        if (args.length==1){
            List<String> completion = new ArrayList<>();
            for (EntityType e : EntityType.values()){
                addToCompletion(e.toString().toLowerCase(), args[0], completion);
            }
            return completion;
        }
        if (args.length==2){
            List<String> completion = new ArrayList<>();
            addToCompletion("1", args[1], completion);
            return completion;
        }
        if (args.length==3){
            List<String> completion = new ArrayList<>();
            addToCompletion("dummy", args[2], completion);
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
