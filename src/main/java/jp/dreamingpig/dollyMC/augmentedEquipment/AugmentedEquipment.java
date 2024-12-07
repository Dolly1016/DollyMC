package jp.dreamingpig.dollyMC.augmentedEquipment;

import jp.dreamingpig.dollyMC.DollyMC;
import jp.dreamingpig.dollyMC.player.OnlinePlayer;
import jp.dreamingpig.dollyMC.utils.Cache;
import jp.dreamingpig.dollyMC.utils.execution.inventoryGui.InventoryGUI;
import jp.dreamingpig.dollyMC.utils.serialization.DPrimitiveEntry;
import jp.dreamingpig.dollyMC.utils.serialization.DStructureEntry;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * プレイヤーごとの拡張インベントリです。
 */
public class AugmentedEquipment {
    final private OnlinePlayer myPlayer;
    final private DStructureEntry myEntry;
    final private Map<String, DPrimitiveEntry<ItemStack>> entryMapCache = new HashMap<>();
    final private List<EquipmentInstance> allInstances = new ArrayList<>();

    static private Cache<List<String>> allSlots = new Cache<>(()->DollyMC.getOption().getStringList("augmentedEquipment"));

    static public NamespacedKey getNamespacedKey(String slot){
        return new NamespacedKey(DollyMC.getPlugin(), "augmentedEquipment." + slot);
    }
    static public void setAEType(PersistentDataContainer container, String slot, String classTag){
        container.set(getNamespacedKey(slot), PersistentDataType.STRING, classTag);
    }
    static public boolean hasAEType(ItemStack item, String slot){
        return item.getItemMeta().getPersistentDataContainer().has(getNamespacedKey(slot), PersistentDataType.STRING);
    }
    static public @Nullable String getAEClassTag(ItemStack item, String slot){
        return item.getItemMeta().getPersistentDataContainer().get(getNamespacedKey(slot), PersistentDataType.STRING);
    }

    public AugmentedEquipment(OnlinePlayer player){
        this.myPlayer = player;
        myEntry = player.getRuntimeDataEntry().getStructure("augmentedEquipment");
    }

    public DPrimitiveEntry<ItemStack> getEquipment(String slot){
        if(!entryMapCache.containsKey(slot)) entryMapCache.put(slot, myEntry.getItemStack(slot, null));
        return entryMapCache.get(slot);
    }

    public void onUpdateEquipments(){
        for(var listener : allInstances) {
            listener.onInactivated();
            HandlerList.unregisterAll(listener);
        }
        allInstances.clear();
        for(var slot : allSlots.get()){
            var equipment = getEquipment(slot).get();
            if(equipment != null) {
                var classTag = getAEClassTag(equipment, slot);
                if (classTag != null) {
                    var splitted = classTag.split("\\$", 2);
                    var instance = EquipmentInstance.instantiate(splitted[0], Bukkit.getPlayer(myPlayer.getUniqueId()), splitted.length == 2 ? splitted[1] : null);
                    if (instance != null) {
                        allInstances.add(instance);
                    } else {
                        Bukkit.getLogger().info("[Augmented Equipment] Unknown class tag \"" + splitted[0] + "\"");
                    }
                }
            }
        }
        for(var listener : allInstances) {
            Bukkit.getPluginManager().registerEvents(listener, DollyMC.getPlugin());
            listener.onActivated();
        }
    }

    public void openGUI(){
        var player = Bukkit.getPlayer(myPlayer.getUniqueId());

        int lines = allSlots.get().size() / 9 + 1;
        var gui = new InventoryGUI<Boolean>("Augmented Equipments", lines).playerAreaEditor((instance, slot) -> true);

        int num = 0;
        for(var slot : allSlots.get()){
            final String finalSlot = slot;
            final int finalNum = num;
            final var entry = getEquipment(finalSlot);
            gui.pushContent(new InventoryGUI.GUIInteractiveAreaItem<>(
                    s -> s.slot() == finalNum,
                    (instance, i) -> entry.get(),
                    ((instance, cursor, i) -> {
                        if(cursor.isEmpty()){
                            var returned = entry.get();
                            entry.set(null);
                            entry.save();
                            onUpdateEquipments();
                            return returned;
                        }
                        if(hasAEType(cursor, finalSlot)){
                            var returned = entry.get();
                            if(returned == null) returned = ItemStack.empty();
                            entry.set(cursor);
                            entry.save();
                            onUpdateEquipments();
                            return returned;
                        }else{
                            return null;
                        }
                    })
            ));

            num++;
        }

        gui.execute(player, false);
    }

}
