package net.pullolo.magicarena.misc;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class ParticleApi {

    private final JavaPlugin plugin;

    public ParticleApi(JavaPlugin plugin){
        this.plugin=plugin;
    }

    public void drawColoredHalfCircle(Location l1, double precision, double length, Color color, float size){
        Vector v1 = l1.getDirection().clone().setY(0).normalize().multiply(length);
        rotateVector(v1, -90);
        for (int i = 0; i<180*precision; i++) {
            drawColoredLine(l1, l1.clone().add(v1), precision, color, size, 0);
            rotateVector(v1, (double) 360/(360*precision));
        }
    }
    public void drawColoredCircle(Location l1, double precision, double length, Color color, float size){
        Vector v1 = l1.getDirection().clone().setY(0).normalize().multiply(length);
        for (int i = 0; i<360*precision; i++) {
            drawColoredLine(l1, l1.clone().add(v1), Math.max(precision, 0.5), color, size, 0);
            rotateVector(v1, (double) 360/(360*precision));
        }
    }
    public void drawColoredLine(Location l1, Location l2, double precision, Color color, float size, double lineOffset){
        Location startPos = l1.clone();
        Location loc = l1.clone();
        double step = precision/0.1;
        Vector dir = l2.toVector().subtract(l1.toVector()).normalize().multiply((double) 1/step);
        loc.add(dir.clone().normalize().multiply(lineOffset));
        for (int i = 0; i<startPos.distance(l2)*step; i++){
            loc.add(dir);
            spawnColoredParticles(loc, color, size, 1, 0, 0, 0);
        }
    }

    public void drawMultiParticleLine(Location l1, Location l2, double precision, HashMap<Particle, Double> proportions, double lineOffset){
        Location startPos = l1.clone();
        Location loc = l1.clone();
        double step = precision/0.1;
        Vector dir = l2.toVector().subtract(l1.toVector()).normalize().multiply((double) 1/step);
        double fullDist = startPos.distance(l2)*step;
        loc.add(dir.clone().normalize().multiply(lineOffset));
        for (Particle p : proportions.keySet()){
            for (int i = 0; i<fullDist*proportions.get(p); i++){
                spawnParticles(loc, p, 1, 0, 0, 0, 0.01);
                loc.add(dir);
            }
        }
    }

    public void spawnParticles(Location loc, Particle particle, int amount, double offsetX, double offsetY, double offsetZ, double speed){
        loc.getWorld().spawnParticle(particle, loc, amount, offsetX, offsetY, offsetZ, speed);
    }

    public void spawnColoredParticles(Location loc, Color color, float size, int amount, double offsetX, double offsetY, double offsetZ){
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, size);
        loc.getWorld().spawnParticle(Particle.REDSTONE, loc, amount, offsetX, offsetY, offsetZ, 1, dustOptions);
    }

    private Vector rotateVector(Vector vector, double whatAngle) {
        whatAngle = Math.toRadians(whatAngle);
        double sin = Math.sin(whatAngle);
        double cos = Math.cos(whatAngle);
        double x = vector.getX() * cos + vector.getZ() * sin;
        double z = vector.getX() * -sin + vector.getZ() * cos;

        return vector.setX(x).setZ(z);
    }
}