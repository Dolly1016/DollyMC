package jp.dreamingpig.dollyMC.utils.region;

import jp.dreamingpig.dollyMC.utils.serialization.DPrimitiveEntry;
import jp.dreamingpig.dollyMC.utils.serialization.DStructureEntry;
import org.bukkit.World;

public class RegionContent {
    final private DPrimitiveEntry<String> worldName;
    final private DPrimitiveEntry<Integer> xMin, xMax, yMin, yMax, zMin, zMax;

    /**
     * 保存済みのデータから読み込むためのコンストラクタ
     * @param entry データの格納先
     */
    public RegionContent(DStructureEntry entry){
        worldName = entry.getString("world", "INVALID");
        xMin = entry.getInt("xMin", 0);
        xMax = entry.getInt("xMax", 0);
        yMin = entry.getInt("yMin", 0);
        yMax = entry.getInt("yMax", 0);
        zMin = entry.getInt("zMin", 0);
        zMax = entry.getInt("zMax", 0);
    }

    /**
     * エリアから新たなコンテンツを生成するためのコンストラクタ
     * @param entry データの格納先
     * @param area エリア
     */
    public RegionContent(DStructureEntry entry, Area area){
        this(entry);
        worldName.set(area.getWorldName());
        xMin.set(area.getXMin());
        xMax.set(area.getXMax());
        yMin.set(area.getYMin());
        yMax.set(area.getYMax());
        zMin.set(area.getZMin());
        zMax.set(area.getZMax());
    }

    public String getWorldName(){
        return worldName.get();
    }

    public int getXMin(){
        return xMin.get();
    }

    public int getXMax(){
        return xMax.get();
    }

    public int getYMin(){
        return yMin.get();
    }

    public int getYMax(){
        return yMax.get();
    }

    public int getZMin(){
        return zMin.get();
    }

    public int getZMax(){
        return zMax.get();
    }

    final boolean overlap(int x,int y, int z){
        return getXMin() <= x && x <= getXMax() && getYMin() <= y && y <= getYMax() && getZMin() <= z && z <= getZMax();
    }
}
