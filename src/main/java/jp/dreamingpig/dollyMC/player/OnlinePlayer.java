package jp.dreamingpig.dollyMC.player;

import jp.dreamingpig.dollyMC.DollyMC;
import jp.dreamingpig.dollyMC.augmentedEquipment.AugmentedEquipment;
import jp.dreamingpig.dollyMC.events.PlayerEntryGeneratedEvent;
import jp.dreamingpig.dollyMC.utils.serialization.DStructureEntry;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * オンラインのプレイヤーを表します。
 * プレイヤーに紐づいたデータを格納できます。
 * プレイヤーがオンラインの間のみ操作できれば十分なデータを置いてください。
 */
public class OnlinePlayer {
    final static private Map<UUID, OnlinePlayer> fastPlayerMap = new HashMap<>();
    final private DStructureEntry myEntry;
    final private UUID uuid;
    @Getter
    final AugmentedEquipment augmentedEquipment;

    /**
     * プレイヤーのエントリを取得します。
     * @return
     */
    public DStructureEntry getRuntimeDataEntry(){
        return myEntry;
    }

    static OnlinePlayer generateOnlinePlayer(@NotNull UUID player){
        if(fastPlayerMap.containsKey(player)){
            return fastPlayerMap.get(player);
        }
        return new OnlinePlayer(player);
    }

    private OnlinePlayer(@NotNull UUID player){
        uuid = player;
        myEntry = DStructureEntry.openStructure(DollyMC.getPlugin(), "players/" + player.toString());

        fastPlayerMap.put(player, this);

        //各種モジュールの読み込み
        this.augmentedEquipment = new AugmentedEquipment(this);

        Bukkit.getPluginManager().callEvent(new PlayerEntryGeneratedEvent(this));
    }

    public UUID getUniqueId() {
        return uuid;
    }

    static public @Nullable OnlinePlayer getPlayer(@NotNull UUID uniqueId){
        return fastPlayerMap.get(uniqueId);
    }
}
