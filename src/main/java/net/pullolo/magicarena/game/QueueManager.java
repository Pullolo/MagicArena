package net.pullolo.magicarena.game;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class QueueManager {

    private final ArrayList<Player> playersInSoloQue = new ArrayList<>();
    private final ArrayList<ArrayList<Player>> playersInDuoQue = new ArrayList<>();
    private final ArrayList<ArrayList<Player>> playersInTrioQue = new ArrayList<>();
    private final ArrayList<ArrayList<Player>> playersInSquadQue = new ArrayList<>();
    private final ArrayList<ArrayList<Player>> playersInTeamQue = new ArrayList<>();

    private final GameManager gameManager;

    public QueueManager(GameManager gameManager){
        this.gameManager = gameManager;
    }

    public enum QueueType{
        SOLO,
        DUO,
        TRIO,
        SQUAD,
        TEAM
    }

    public void addPlayerToQueue(Player p, QueueType queueType){
        if (getPlayersInQueue().contains(p)){
            p.sendMessage(ChatColor.RED + "You are already in queue");
            return;
        }
        if (queueType.equals(QueueType.SOLO)){
            playersInSoloQue.add(p);
            p.sendMessage(ChatColor.GREEN + "You have benn added to queue!");
        } else {
            p.sendMessage(ChatColor.RED + "This queue type is not available!");
            return;
        }
        tryStartMatch(queueType);
    }

    public void addPartyToQueue(ArrayList<Player> party, QueueType queueType, boolean fill){
        for (Player p: party){
            if (getPlayersInQueue().contains(p)){
                for (Player pp : party){
                    pp.sendMessage(ChatColor.RED + "Player " + p + " is already in queue");
                }
                return;
            }
        }
        //todo add fill option
        if (!(party.size()==getQueueTypeAsInt(queueType))){
            for (Player p : party){
                p.sendMessage(ChatColor.RED + "Could not enter Matchmaking due to invalid party size!");
            }
            return;
        }
        if (queueType.equals(QueueType.DUO)){
            for (Player p : party){
                p.sendMessage(ChatColor.GREEN + "Your party was added to " + queueType.toString().toLowerCase() + " queue!");
            }
            playersInDuoQue.add(party);
        }
        tryStartMatch(queueType);
    }

    public void removePlayerFromQueue(Player p){
        playersInSoloQue.remove(p);
        playersInDuoQue.removeIf(players -> players.contains(p));
        playersInTrioQue.removeIf(players -> players.contains(p));
        playersInSquadQue.removeIf(players -> players.contains(p));
        playersInTeamQue.removeIf(players -> players.contains(p));
        p.sendMessage(ChatColor.GREEN + "You have left the queue!");
    }

    public void removePartyFromQueue(ArrayList<Player> party){
        for (Player p: party){
            playersInSoloQue.remove(p);
            for (ArrayList<Player> players: playersInDuoQue){
                players.remove(p);
            }
            for (ArrayList<Player> players: playersInTrioQue){
                players.remove(p);
            }
            for (ArrayList<Player> players: playersInSquadQue){
                players.remove(p);
            }
            for (ArrayList<Player> players: playersInTeamQue){
                players.remove(p);
            }
            p.sendMessage(ChatColor.GREEN + "Your party was removed from queue!");
        }
    }

    public boolean isPlayerInQueue(Player p){
        if (playersInSoloQue.contains(p)){
            return true;
        }
        for (ArrayList<Player> players: playersInDuoQue){
            if (players.contains(p)) return true;
        }
        for (ArrayList<Player> players: playersInTrioQue){
            if (players.contains(p)) return true;
        }
        for (ArrayList<Player> players: playersInSquadQue){
            if (players.contains(p)) return true;
        }
        for (ArrayList<Player> players: playersInTeamQue){
            if (players.contains(p)) return true;
        }

        return false;
    }

    private void tryStartMatch(QueueType queueType){
        if (queueType.equals(QueueType.SOLO)){
            if (playersInSoloQue.size()>1){
                //start game
                Player player1 = playersInSoloQue.get(0);
                Player player2 = playersInSoloQue.get(1);
                ArrayList<Player> players = new ArrayList<>();
                players.add(player1);
                players.add(player2);
                playersInSoloQue.remove(player1);
                playersInSoloQue.remove(player2);
                for (Player p : players){
                    p.sendMessage( ChatColor.GREEN + "Starting match...");
                }
                //todo start game
                gameManager.prepGame(player1, player2, queueType);

                tryStartMatch(queueType);
            }
        }
        if (queueType.equals(QueueType.DUO)){
            if (playersInDuoQue.size()>1){
                ArrayList<Player> team1 = playersInDuoQue.get(0);
                ArrayList<Player> team2 = playersInDuoQue.get(1);
                ArrayList<Player> allPlayers = new ArrayList<>();
                allPlayers.addAll(team1);
                allPlayers.addAll(team2);
                for(Player p : allPlayers){
                    p.sendMessage( ChatColor.GREEN + "Starting match...");
                }

                gameManager.prepGame(team1, team2, queueType);

                tryStartMatch(queueType);
            }
        }
    }

    public ArrayList<Player> getPlayersInQueue(){
        ArrayList<Player> inQueue = new ArrayList<>();
        inQueue.addAll(playersInSoloQue);
        for (ArrayList<Player> players: playersInDuoQue){
            inQueue.addAll(players);
        }
        for (ArrayList<Player> players: playersInTrioQue){
            inQueue.addAll(players);
        }
        for (ArrayList<Player> players: playersInSquadQue){
            inQueue.addAll(players);
        }
        for (ArrayList<Player> players: playersInTeamQue){
            inQueue.addAll(players);
        }
        return inQueue;
    }

    public int getQueueTypeAsInt(QueueType queueType){
        int a = 0;
        switch (queueType){
            case SOLO:
                a=1;
                break;
            case DUO:
                a=2;
                break;
            case TRIO:
                a=3;
                break;
            case SQUAD:
                a=4;
                break;
            case TEAM:
                a=5;
                break;
        }
        return a;
    }
}
