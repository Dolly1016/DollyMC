package jp.dreamingpig.dollyMC.utils.region;

import org.bukkit.Location;

public class Area {
    private int xMin, xMax, yMin, yMax, zMin, zMax;
    private String worldName;

    @Deprecated
    public Area(String worldName, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax){
        this.worldName = worldName;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.zMin = zMin;
        this.zMax = zMax;
    }
    public Area(Location loc1, Location loc2){
        if(loc1.getWorld() != loc2.getWorld()) throw new IllegalArgumentException("Area has received different world location.");

        worldName = loc1.getWorld().getName();

        int temp;

        xMin = loc1.getBlockX();
        xMax = loc2.getBlockX();
        if(xMin > xMax){
            temp = xMax;
            xMax = xMin;
            xMin = temp;
        }

        yMin = loc1.getBlockY();
        yMax = loc2.getBlockY();
        if(yMin > yMax){
            temp = yMax;
            yMax = yMin;
            yMin = temp;
        }

        zMin = loc1.getBlockZ();
        zMax = loc2.getBlockZ();
        if(zMin > zMax){
            temp = xMax;
            zMax = zMin;
            zMin = temp;
        }
    }

    public int getXMin(){
        return xMin;
    }

    public int getXMax(){
        return xMax;
    }

    public int getYMin(){
        return yMin;
    }

    public int getYMax(){
        return yMax;
    }

    public int getZMin(){
        return zMin;
    }

    public int getZMax(){
        return zMax;
    }

    public String getWorldName(){
        return worldName;
    }

}
