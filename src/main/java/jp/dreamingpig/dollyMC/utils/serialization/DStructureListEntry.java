package jp.dreamingpig.dollyMC.utils.serialization;

import org.bukkit.Bukkit;
import org.bukkit.configuration.MemoryConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

public class DStructureListEntry implements Iterable<DStructureEntry>{
    List<MemoryConfiguration> rawList;
    List<DStructureEntry> entryList;
    ISerialization serialization;

    DStructureListEntry(ISerialization serialization, List<MemoryConfiguration> list){
        this.rawList = list;
        this.serialization = serialization;
        this.entryList = new ArrayList<>(rawList.stream().map((m) -> new DStructureEntry(new SectionWrapper(serialization, m))).toList());
    }

    public DStructureEntry add(){
        var mem = new MemoryConfiguration();
        var entry = new DStructureEntry(new SectionWrapper(serialization, mem));
        rawList.add(mem);
        entryList.add(entry);
        return entry;
    }

    public void remove(int index){
        rawList.remove(index);
        entryList.remove(index);
    }

    public boolean remove(DStructureEntry element){
        int index = entryList.indexOf(element);
        if(index == -1) return false;
        remove(index);
        return true;
    }

    public DStructureEntry get(int index){
        return entryList.get(index);
    }

    public void save(){
        serialization.saveConfig();
    }

    @Override
    public @NotNull Iterator<DStructureEntry> iterator() {
        return new StructureIterator();
    }

    class StructureIterator implements Iterator<DStructureEntry> {
        int index = 0;

        @Override
        public boolean hasNext() {
            return index < entryList.size();
        }

        @Override
        public DStructureEntry next() {
            return entryList.get(index++);
        }
    }
}
