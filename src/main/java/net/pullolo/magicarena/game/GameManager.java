package net.pullolo.magicarena.game;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GameManager {

    private final QueueManager queueManager;

    public GameManager(){
        this.queueManager = new QueueManager(this);
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public void prepGame(ArrayList<Player> team1, ArrayList<Player> team2, QueueManager.QueueType gameType){
        startGame(team1, team2, gameType);
    }

    public void prepGame(Player p1, Player p2, QueueManager.QueueType gameType){
        ArrayList<Player> t1 = new ArrayList<>();
        t1.add(p1);
        ArrayList<Player> t2 = new ArrayList<>();
        t2.add(p2);
        startGame(t1, t2, gameType);
    }

    private void startGame(ArrayList<Player> team1, ArrayList<Player> team2, QueueManager.QueueType gameType){
        for (Player p : team1){
            p.sendMessage(ChatColor.GREEN + "Match Started Successfully!");
        }
        for (Player p : team2){
            p.sendMessage(ChatColor.GREEN + "Match Started Successfully!");
        }
    }
}
