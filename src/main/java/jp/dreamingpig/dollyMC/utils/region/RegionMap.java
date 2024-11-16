package jp.dreamingpig.dollyMC.utils.region;

import com.google.common.primitives.UnsignedLong;
import jp.dreamingpig.dollyMC.utils.MathHelper;
import jp.dreamingpig.dollyMC.utils.serialization.DStructureEntry;
import jp.dreamingpig.dollyMC.utils.serialization.DStructureListEntry;
import org.bukkit.Location;
import org.bukkit.block.data.type.Bed;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 領域を管理するマップです。
 * @param <T> 領域コンテンツ。{@link RegionContent}を継承する必要があります。
 */
public class RegionMap<T extends RegionContent> {
    final private DStructureEntry myEntry;
    final private DStructureListEntry contentsEntry;
    final private List<T> allEntries;

    //部分チャンクの最大サイズ
    static final private int MAX_PARTIAL_CHUNK_SIZE = 1 << 9;
    //部分チャンクの最小サイズ
    static final private int MIN_PARTIAL_CHUNK_SIZE = 1 << 6;
    //部分チャンクごとの最大コンテンツ数 (最小サイズのチャンク除く)
    static final private int MAX_PARTIAL_REGION_CONTENTS = 20;

    record IntPair(int x, int z){}

    private class WorldRegionMap {
        Map<IntPair, PartialRegionMap> myMap;

        public WorldRegionMap(){
            myMap = new HashMap<>();
        }

        public void register(T content){
            int xMin = MathHelper.intDivide(content.getXMin(), MAX_PARTIAL_CHUNK_SIZE);
            int xMax = MathHelper.intDivide(content.getXMax(), MAX_PARTIAL_CHUNK_SIZE);
            int zMin = MathHelper.intDivide(content.getZMin(), MAX_PARTIAL_CHUNK_SIZE);
            int zMax = MathHelper.intDivide(content.getZMax(), MAX_PARTIAL_CHUNK_SIZE);
            for(int x = xMin; x <= xMax;x++){
                for(int z = zMin; z <= zMax;z++){
                    var pair = new IntPair(x,z);
                    if(!myMap.containsKey(pair)) myMap.put(pair, new PartialRegionMap(MAX_PARTIAL_CHUNK_SIZE, new IntPair(x * MAX_PARTIAL_CHUNK_SIZE,z * MAX_PARTIAL_CHUNK_SIZE)));
                    myMap.get(pair).register(content);
                }
            }
        }

        public @Nullable T get(int blockX, int blockY, int blockZ){
            int chunkX = MathHelper.intDivide(blockX, MAX_PARTIAL_CHUNK_SIZE);
            int chunkZ = MathHelper.intDivide(blockZ, MAX_PARTIAL_CHUNK_SIZE);
            int inChunkX = MathHelper.intSurplus(blockX, MAX_PARTIAL_CHUNK_SIZE);
            int inChunkZ = MathHelper.intSurplus(blockZ, MAX_PARTIAL_CHUNK_SIZE);
            var pair = new IntPair(chunkX, chunkZ);
            if(myMap.containsKey(pair)){
                return myMap.get(pair).get(inChunkX, inChunkZ, blockX, blockY, blockZ);
            }
            return null;
        }
    }

    private class PartialRegionMap{
        final private int chunkSize;
        final private IntPair minPoint;

        //コンテンツ数が少ない間はここで管理する。
        @Nullable List<T> contents;
        //コンテンツ数が増えてきたらこちらを使用する。
        @Nullable List<PartialRegionMap> children;
        public boolean hasPartialMap(){
            return contents == null;
        }

        public PartialRegionMap(int chunkSize, IntPair minPoint){
            this.minPoint = minPoint;
            this.chunkSize = chunkSize;
            this.contents = new ArrayList<>();
            this.children = null;
        }

        public void register(T content){
            if(!hasPartialMap()){
                this.contents.add(content);
                if(this.contents.size() >= MAX_PARTIAL_REGION_CONTENTS && chunkSize > MIN_PARTIAL_CHUNK_SIZE){
                    this.children = new ArrayList<>();
                    children.add(new PartialRegionMap(chunkSize / 2, minPoint)); //0b00
                    children.add(new PartialRegionMap(chunkSize / 2, new IntPair(minPoint.x() + chunkSize / 2, minPoint.z()))); //0b01
                    children.add(new PartialRegionMap(chunkSize / 2, new IntPair(minPoint.x() , minPoint.z() + chunkSize / 2))); //0b10
                    children.add(new PartialRegionMap(chunkSize / 2, new IntPair(minPoint.x() + chunkSize / 2, minPoint.z() + chunkSize / 2))); //0b11
                    for(var c : contents) register(content);
                    this.contents = null;
                }
            }else{
                for(var child : this.children) checkAndRegister(content);
            }
        }

        void checkAndRegister(T content){
            if(content.getXMax() < minPoint.x()) return;
            if(content.getXMin() >= minPoint.x() + chunkSize) return;
            if(content.getZMax() < minPoint.z()) return;
            if(content.getZMin() >= minPoint.x() + chunkSize) return;

            register(content);
        }

        public @Nullable T get(int inChunkX, int inChunkZ, int blockX, int blockY, int blockZ){
            if(hasPartialMap()) {
                int bit = 0;
                int halfSize = chunkSize / 2;
                if (inChunkX / halfSize == 1) bit |= 0b01;
                if (inChunkZ / halfSize == 1) bit |= 0b10;
                children.get(bit).get(inChunkX % halfSize, inChunkZ % halfSize, blockX, blockY, blockZ);
            }else {
                for (var c : contents) if (c.overlap(blockX, blockY, blockZ)) return c;
            }
            return null;
        }
    }

    final private Map<String,WorldRegionMap> worldFastMap;
    final private BiFunction<DStructureEntry, Area, T> constructor;
    /**
     *
     * @param entry データを格納するエントリ
     * @param deserializer コンテンツのデシリアライザ
     * @param constructor コンテンツのコンストラクタ
     */
    public RegionMap(DStructureEntry entry, Function<DStructureEntry, T> deserializer, BiFunction<DStructureEntry, Area, T> constructor){
        this.myEntry = entry;
        this.contentsEntry = myEntry.getStructureList("contents");
        this.allEntries = new ArrayList<>();
        this.worldFastMap = new HashMap<>();
        this.constructor = constructor;

        for(var e : contentsEntry){
            var content = deserializer.apply(e);

            register(content);
        }
    }

    /**
     * 指定の範囲に領域コンテンツを追加します。
     * 他領域との重複は考慮されません。
     * @param area
     * @return
     */
    public T register(Area area){
        var entry = contentsEntry.add();
        var content = constructor.apply(entry, area);
        register(content);
        return content;
    }

    private void register(@NotNull T content){
        if(!worldFastMap.containsKey(content.getWorldName())){
            worldFastMap.put(content.getWorldName(), new WorldRegionMap());
        }
        worldFastMap.get(content.getWorldName()).register(content);

        allEntries.add(content);
    }

    public @Nullable T get(Location location){
        return get(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public @Nullable T get(String worldName, int blockX, int blockY, int blockZ){
        if(!worldFastMap.containsKey(worldName)) return null;
        return worldFastMap.get(worldName).get(blockX, blockY, blockZ);
    }
}
