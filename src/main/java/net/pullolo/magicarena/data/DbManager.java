package net.pullolo.magicarena.data;

import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
//            Statement stmt = conn.createStatement();
//            String sql = "create table if not exists plugin_data (name TEXT PRIMARY KEY NOT NULL, level INT NOT NULL, hp INT NOT NULL, mana INT NOT NULL, xp TEXT NOT NULL);";
//            stmt.execute(sql);
//            stmt.close();
//            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
