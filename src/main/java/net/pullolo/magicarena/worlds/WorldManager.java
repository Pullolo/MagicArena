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
import java.util.logging.Logger;

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
        unloadWorld(originalWorld, true);
        copyFileStructure(originalWorld.getWorldFolder(), new File(Bukkit.getWorldContainer(), newWorldName));
        new WorldCreator(originalWorld.getName()).createWorld();
        new WorldCreator(newWorldName).createWorld();
    }
    public static void copyWorld(File worldFile, String newWorldName) {
        copyFileStructure(worldFile, new File(Bukkit.getWorldContainer(), newWorldName));
        new WorldCreator(newWorldName).createWorld();
    }
    public static boolean unloadWorld(World world, boolean save) {
        if (world.getName().equals(mainWorld)){
            return false;
        }
        for (Player p : world.getPlayers()){
            p.teleport(Bukkit.getWorld(mainWorld).getSpawnLocation());
        }
        return world!=null && Bukkit.getServer().unloadWorld(world, save);
    }
    public static void removeWorld(World world){
        unloadWorld(world, false);
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
    private static void copyFileStructure(File oldWorldFile, File newWorldFile){
        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        FileUtils.copyDirectory(oldWorldFile, newWorldFile);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                // do nothing
            }
            File uidFile = new File(newWorldFile, "uid.dat");
            uidFile.delete();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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

    public static ArrayList<String> getArenas(){
        ArrayList<String> worlds = new ArrayList<>();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                String line = sc.nextLine();
                if (line.regionMatches(0, "arena_", 0, "arena_".length())){
                    worlds.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return worlds;
    }

    public static ArrayList<String> getDungeons(){
        ArrayList<String> worlds = new ArrayList<>();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                String line = sc.nextLine();
                if (line.regionMatches(0, "dungeon_", 0, "dungeon_".length())){
                    worlds.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return worlds;
    }

    public static void saveWorld(World w, boolean temp, boolean arena, boolean dungeon){
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
        else if (arena) s.append("arena_" + w.getName());
        else if (dungeon) s.append("dungeon_" + w.getName());
        else s.append(w.getName());
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(s.toString());
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteWorld(String name){
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
