package net.pullolo.magicarena.players;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

import static net.pullolo.magicarena.MagicArena.plugin;

public class PartyManager {
    HashMap<Player, ArrayList<Player>> playersParties = new HashMap<>();
    ArrayList<ArrayList<Player>> parties = new ArrayList<>();
    HashMap<Player, ArrayList<Player>> partyInvites = new HashMap<>();

    public void createParty(Player p){
        ArrayList<Player> newParty = new ArrayList<>();
        newParty.add(p);
        parties.add(newParty);
        playersParties.put(p, newParty);
        announceMessage(newParty, ChatColor.GREEN + "[Party] Party successfully created!");
    }

    public void addPlayerToParty(Player newPlayer, ArrayList<Player> party){
        if (!parties.contains(party)){
            throw new IllegalArgumentException("This party does not exist!");
        }
        party.add(newPlayer);
        playersParties.put(newPlayer, party);
    }

    public void addPlayerToParty(Player newPlayer, Player partyOwner){
        if (!isPlayerInParty(partyOwner)){
            throw new IllegalArgumentException("This party does not exist!");
        }
        getPlayersParty(partyOwner).add(newPlayer);
        playersParties.put(newPlayer, getPlayersParty(partyOwner));
    }

    public void inviteToParty(Player partyOwner, Player invited){
        if (!isPlayerInParty(partyOwner)){
            partyOwner.sendMessage(ChatColor.RED + "[Party] You are not in a party!");
            return;
        }
        ArrayList<Player> party = getPlayersParty(partyOwner);
        //check if the owner is actually an owner
        if (!party.get(0).equals(partyOwner)){
            partyOwner.sendMessage(ChatColor.RED + "[Party] You don't own the party!");
            return;
        }
        if (partyInvites.containsKey(invited)){
            partyOwner.sendMessage(ChatColor.RED + "[Party] This player already has an outgoing invite! Try again later!");
            return;
        }
        partyInvites.put(invited, party);
        announceMessage(party, ChatColor.GREEN + "[Party] Player " + invited + " has been invited to join this party!");
        invited.sendMessage(ChatColor.GREEN + "[Party] You have been invited to join " + partyOwner.getDisplayName() + "'s party! (You have 60s to accept)");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (invited!=null){
                    partyInvites.remove(invited);
                }
            }
        }.runTaskLater(plugin, 1200);
    }

    public void joinParty(Player p){
        if (!partyInvites.containsKey(p)){
            p.sendMessage(ChatColor.RED + "[Party] You have not been invited to any party!");
            return;
        }
        if (isPlayerInParty(p)){
            p.sendMessage(ChatColor.RED + "[Party] You are already in a party!");
            return;
        }
        ArrayList<Player> party = partyInvites.get(p);
        addPlayerToParty(p, party);
        announceMessage(party, ChatColor.GREEN + "[Party] Player " + p.getDisplayName() + " has joined the party!");
    }

    public void declineInvite(Player p){
        if (!partyInvites.containsKey(p)){
            p.sendMessage(ChatColor.RED + "[Party] You have not been invited to any party!");
            return;
        }
        announceMessage(partyInvites.get(p), ChatColor.RED + "[Party] " + p.getDisplayName() + " has declined the invite!");
        partyInvites.remove(p);
    }

    public void leaveParty(Player p){
        if (!isPlayerInParty(p)){
            p.sendMessage(ChatColor.RED + "[Party] You are not in a party!");
            return;
        }
        ArrayList<Player> party = getPlayersParty(p);
        Player prevOwner = party.get(0);
        party.remove(p);
        playersParties.remove(p);
        announceMessage(party, ChatColor.RED + "[Party] " + p.getDisplayName() + " has left the party!");
        if (party.size()<1){
            parties.remove(party);
        } else if (!party.get(0).equals(prevOwner)){
            party.get(0).sendMessage(ChatColor.GREEN + "[Party] You are now the party owner!");
        }
        p.sendMessage(ChatColor.GREEN + "[Party] You have left the party!");
    }

    public void kickPlayer(Player partyOwner, Player kicked){
        if (!isPlayerInParty(partyOwner)){
            partyOwner.sendMessage(ChatColor.RED + "[Party] You are not in a party!");
            return;
        }
        ArrayList<Player> party = getPlayersParty(partyOwner);
        //check if the owner is actually an owner
        if (!party.get(0).equals(partyOwner)){
            partyOwner.sendMessage(ChatColor.RED + "[Party] You don't own the party!");
            return;
        }
        if (!party.contains(kicked)){
            partyOwner.sendMessage(ChatColor.RED + "[Party] The player you are trying to kick does not belong to this party!");
            return;
        }
        leaveParty(kicked);
    }

    public ArrayList<Player> getPlayersParty(Player p){
        if (isPlayerInParty(p)){
            return playersParties.get(p);
        }
        return null;
    }

    public boolean isPlayerInParty(Player p){
        if (playersParties.containsKey(p)){
            for (ArrayList<Player> players: parties){
                if (players.contains(p)){
                    return true;
                }
            }
        }
        return false;
    }

    public void announceMessage(ArrayList<Player> players, String message){
        for (Player p : players){
            p.sendMessage(message);
        }
    }
}