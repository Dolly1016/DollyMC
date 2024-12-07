package jp.dreamingpig.dollyMC.augmentedEquipment;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class EquipmentInstance implements Listener {
    static private HashMap<String, BiFunction<Player, String, EquipmentInstance>> allConstructors = new HashMap<>();
    static public void registerDefinition(String classTag, BiFunction<Player, String, EquipmentInstance> constructor){
        allConstructors.put(classTag, constructor);
    }
    static EquipmentInstance instantiate(String classTag, Player player, String argument){
        if(allConstructors.containsKey(classTag)){
            var instance = allConstructors.get(classTag).apply(player, argument);
            instance.player = player;
            return instance;
        }
        return null;
    }
    @Getter
    private Player player;

    public void onActivated(){}
    public void onInactivated(){}
}
