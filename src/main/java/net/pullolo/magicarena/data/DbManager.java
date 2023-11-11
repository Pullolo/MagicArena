package net.pullolo.magicarena.data;

import net.pullolo.magicarena.quests.Quest;
import net.pullolo.magicarena.quests.QuestType;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

import static net.pullolo.magicarena.MagicArena.getLog;
import static net.pullolo.magicarena.MagicArena.plugin;

public class DbManager {

    private Connection conn;

    public void init(){
        File file = new File(plugin.getDataFolder(), "data.db");
        if (!file.exists()){
            file.getParentFile().mkdirs();
            plugin.saveResource("data.db", false);
        }
    }

    public boolean isDbEnabled(){
        return conn!=null;
    }

    public boolean connect(){
        try{
            Class.forName("org.sqlite.JDBC");
            this.conn = DriverManager.getConnection("jdbc:sqlite:plugins/"+plugin.getDataFolder().getName()+"/data.db");
            Statement stmt = conn.createStatement();
            String sql = "create table if not exists plugin_data (name TEXT PRIMARY KEY NOT NULL, level INT NOT NULL, xp TEXT NOT NULL," +
                    " star_essence INT NOT NULL, wishes INT NOT NULL, dungeon_essence INT NOT NULL, updated BOOLEAN NOT NULL);";
            stmt.execute(sql);
            sql = "create table if not exists plugin_quests (name TEXT NOT NULL, goal INT NOT NULL, progress INT NOT NULL, type TEXT NOT NULL);";
            stmt.execute(sql);
            stmt.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPlayerInDb(String name){
        boolean is = false;
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:plugins/"+plugin.getDataFolder().getName()+"/data.db");
            PreparedStatement stmt = conn.prepareStatement("select * from plugin_data where name=?;");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.isClosed()){
                return false;
            }
            if(rs.getString("name") != null){
                is = true;
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }

    public void addPlayer(String name, int level, double xp, int star_essence, int wishes, int dungeon_essence, boolean updated){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:plugins/"+plugin.getDataFolder().getName()+"/data.db");
            String insert = "insert into plugin_data (name, level, xp, star_essence, wishes, dungeon_essence, updated) values" +
                    " (?, " + level + ", \"" + xp +"\", " + star_essence + ", " + wishes + ", " + dungeon_essence + ", " + updated + ");";

            PreparedStatement stmt = conn.prepareStatement(insert);
            stmt.setString(1, name);
            stmt.execute();

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PlayerData getPlayerData(String playerName) {
        PlayerData pd = null;
        if (!isPlayerInDb(playerName)){
            addPlayer(playerName, 1, 0, 0, 10, 0, true);
        }
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:plugins/"+plugin.getDataFolder().getName()+"/data.db");
            PreparedStatement stmt = conn.prepareStatement("select * from plugin_data where name=?;");
            stmt.setString(1, playerName);

            ResultSet rs = stmt.executeQuery();
            pd = new PlayerData(playerName, rs.getInt("level"), Double.parseDouble(rs.getString("xp")), rs.getInt("star_essence"), rs.getInt("wishes"), rs.getInt("dungeon_essence"), rs.getBoolean("updated"));

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pd;
    }

    public void updatePlayer(String name, int level, double xp, int star_essence, int wishes, int dungeon_essence, boolean updated){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:plugins/"+plugin.getDataFolder().getName()+"/data.db");


            String update = "update plugin_data set level=" + level + ", xp=\"" + xp + "\", star_essence=" + star_essence + ", wishes=" + wishes + ", dungeon_essence=" + dungeon_essence + ", updated=" + updated + " where name=?;";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setString(1, name);
            stmt.execute();

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateGame(){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:plugins/"+plugin.getDataFolder().getName()+"/data.db");
            Statement stmt = conn.createStatement();

            String update = "update plugin_data set updated=false";
            stmt.execute(update);

            stmt.close();
            conn.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try {
            conn.close();
            conn=null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Quest> getQuests(Player p){
        ArrayList<Quest> quests = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:plugins/"+plugin.getDataFolder().getName()+"/data.db");
            PreparedStatement stmt = conn.prepareStatement("select * from plugin_quests where name=?;");
            stmt.setString(1, p.getName());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                quests.add(new Quest(p, QuestType.valueOf(rs.getString("type")), rs.getInt("goal"), rs.getInt("progress")));
            }
            stmt.close();
            conn.close();
            removeQueriedQuests(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quests;
    }

    private void removeQueriedQuests(Player p){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:plugins/"+plugin.getDataFolder().getName()+"/data.db");
            String del = "delete from plugin_quests where name=?";
            PreparedStatement stmt = conn.prepareStatement(del);
            stmt.setString(1, p.getName());
            stmt.execute();

            stmt.close();
            conn.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveQuest(Quest quest) {
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:plugins/"+plugin.getDataFolder().getName()+"/data.db");
            String insert = "insert into plugin_quests (name, goal, progress, type) values" +
                    " (?, " + quest.getGoal() + ", " + quest.getProgress() + ", ?);";

            PreparedStatement stmt = conn.prepareStatement(insert);
            stmt.setString(1, quest.getPlayer().getName());
            stmt.setString(2, quest.getQuestType().toString());
            stmt.execute();

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
