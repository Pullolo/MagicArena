package net.pullolo.magicarena.data;

import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;

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
            this.conn = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement stmt = conn.createStatement();
            String sql = "create table if not exists plugin_data (name TEXT PRIMARY KEY NOT NULL, level INT NOT NULL, xp TEXT NOT NULL," +
                    " star_essence INT NOT NULL, wishes INT NOT NULL, dungeon_essence INT NOT NULL, updated BOOLEAN NOT NULL);";
            stmt.execute(sql);
            stmt.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean recreateDatabase(){
        try{
            Class.forName("org.sqlite.JDBC");
            this.conn = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement stmt2 = conn.createStatement();
            String sql2 = "drop table plugin_data;";
            stmt2.execute(sql2);
            stmt2.close();
            Statement stmt = conn.createStatement();
            String sql = "create table plugin_data (name TEXT PRIMARY KEY NOT NULL, level INT NOT NULL, xp TEXT NOT NULL," +
                    " star_essence INT NOT NULL, wishes INT NOT NULL, dungeon_essence INT NOT NULL, updated BOOLEAN NOT NULL);";
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
            Connection conn = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("select * from plugin_data where name=\"" + name + "\";");
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
            Connection conn = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement stmt = conn.createStatement();

            String insert = "insert into plugin_data (name, level, xp, star_essence, wishes, dungeon_essence, updated) values" +
                    " (\"" + name + "\", " + level + ", \"" + xp +"\", " + star_essence + ", " + wishes + ", " + dungeon_essence + ", " + updated + ");";
            stmt.execute(insert);

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
            Connection conn = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("select * from plugin_data where name=\"" + playerName + "\";");
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
            Connection conn = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement stmt = conn.createStatement();

            String update = "update plugin_data set level=" + level + ", xp=\"" + xp + "\", star_essence=" + star_essence + ", wishes=" + wishes + ", dungeon_essence=" + dungeon_essence + ", updated=" + updated + " where name=\"" + name + "\";";
            stmt.execute(update);

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateGame(){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:data.db");
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


}
