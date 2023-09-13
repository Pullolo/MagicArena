package net.pullolo.magicarena.worlds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static net.pullolo.magicarena.MagicArena.mainWorld;

public class WorldManager {

    private static File file;

    public static void init(Plugin plugin){
        file = new File(plugin.getDataFolder(), "worlds.txt");
        if (!file.exists()){
            file.getParentFile().mkdirs();
            plugin.saveResource("worlds.txt", false);
        }
    }
    public static void copyActiveWorld(World originalWorld, String newWorldName) {
        copyFileStructure(originalWorld.getWorldFolder(), new File(Bukkit.getWorldContainer(), newWorldName));
        new WorldCreator(newWorldName).createWorld();
    }
    public static void copyWorld(File worldFile, String newWorldName) {
        copyFileStructure(worldFile, new File(Bukkit.getWorldContainer(), newWorldName));
        new WorldCreator(newWorldName).createWorld();
    }
    public static boolean unloadWorld(World world) {
        for (Player p : world.getPlayers()){
            p.teleport(Bukkit.getWorld(mainWorld).getSpawnLocation());
        }
        return world!=null && Bukkit.getServer().unloadWorld(world, false);
    }
    public static void removeWorld(World world){
        unloadWorld(world);
        try {
            FileUtils.deleteDirectory(world.getWorldFolder());
            deleteWorld(world.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeDisabledWorld(File file, String name){
        try {
            FileUtils.deleteDirectory(file);
            deleteWorld(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void copyFileStructure(File source, File target){
        Thread copy = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
                    if(!ignore.contains(source.getName())) {
                        if(source.isDirectory()) {
                            if(!target.exists())
                                if (!target.mkdirs())
                                    throw new IOException("Couldn't create world directory!");
                            String files[] = source.list();
                            for (String file : files) {
                                File srcFile = new File(source, file);
                                File destFile = new File(target, file);
                                copyFileStructure(srcFile, destFile);
                            }
                        } else {
                            InputStream in = new FileInputStream(source);
                            OutputStream out = new FileOutputStream(target);
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = in.read(buffer)) > 0)
                                out.write(buffer, 0, length);
                            in.close();
                            out.close();
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        copy.start();
    }

    public static ArrayList<String> getSavedWorlds(){
        ArrayList<String> worlds = new ArrayList<>();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                worlds.add(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return worlds;
    }

    public static void saveWorld(World w, boolean temp){
        StringBuilder s = new StringBuilder();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                s.append(sc.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (temp) s.append("temp_" + w.getName());
        else s.append(w.getName());
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(s.toString());
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteWorld(String name){
        StringBuilder s = new StringBuilder();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                String nextLine = sc.nextLine();
                if (nextLine.equalsIgnoreCase(name)){
                    continue;
                }
                s.append(nextLine + "\n");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(s.toString());
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
