package jp.dreamingpig.dollyMC.utils.execution.actionEvent;

import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import jp.dreamingpig.dollyMC.DollyMC;
import jp.dreamingpig.dollyMC.utils.execution.ExecutionListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public class ActionEventListener implements Listener {
    final private HashMap<UUID, ActionEventInstance> fastMap;

    static private ActionEventListener listener;
    public ActionEventListener(){
        listener = this;
        fastMap = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, DollyMC.getPlugin());
    }

    static public void registerInventoryGUIInstance(UUID owner, ActionEventInstance instance){
        ExecutionListener.ResetAllExecution(owner);
        listener.fastMap.put(owner, instance);
    }

    static public void unregister(UUID player){
        if(listener.fastMap.containsKey(player)) {
            var lastInstance = listener.fastMap.remove(player);
            lastInstance.onUnregistered();
        }
    }

    @Nullable ActionEventInstance tryGetEvenetInstance(UUID player){
        if(fastMap.containsKey(player)){
            return fastMap.get(player);
        }
        return null;
    }

    @EventHandler
    void onInteractEntity(PlayerInteractEntityEvent ev){
        var instance = tryGetEvenetInstance(ev.getPlayer().getUniqueId());
        if(instance == null) return;

        var onEntity = instance.myEvent.onClickEntity;
        if(onEntity == null) return;

        boolean cancelVanillaAction = onEntity.onClickEntity(instance, ev.getRightClicked(), ev.getPlayer().isSneaking() ? ClickType.SHIFT_RIGHT : ClickType.RIGHT);
        ev.setCancelled(cancelVanillaAction);
    }

    @EventHandler
    void onPreAttackEntity(PrePlayerAttackEntityEvent ev){
        var instance = tryGetEvenetInstance(ev.getPlayer().getUniqueId());
        if(instance == null) return;

        var onEntity = instance.myEvent.onClickEntity;
        if(onEntity == null) return;

        boolean cancelVanillaAction = onEntity.onClickEntity(instance, ev.getAttacked(), ev.getPlayer().isSneaking() ? ClickType.SHIFT_LEFT : ClickType.LEFT);
        ev.setCancelled(cancelVanillaAction);
    }

    @EventHandler
    void onInteractBlock(PlayerInteractEvent ev){
        var instance = tryGetEvenetInstance(ev.getPlayer().getUniqueId());
        if(instance == null) return;

        if(ev.getMaterial() == Material.AIR) {
            var onAir = instance.myEvent.onClickAir;
            if (onAir == null) return;

            if(ev.getHand() == EquipmentSlot.OFF_HAND){
                ev.setCancelled(true);
                return;
            }

            boolean cancelVanillaAction = onAir.onClickAir(instance, ev.getPlayer().isSneaking() ? ClickType.SHIFT_RIGHT : ClickType.RIGHT);
            ev.setCancelled(cancelVanillaAction);
        }else{
            var onBlock = instance.myEvent.onClickBlock;
            if(onBlock == null) return;

            if(ev.getHand() == EquipmentSlot.OFF_HAND){
                ev.setCancelled(true);
                return;
            }

            boolean cancelVanillaAction = onBlock.onClickBlock(instance, ev.getClickedBlock(), ev.getBlockFace(), ev.getPlayer().isSneaking() ? ClickType.SHIFT_RIGHT : ClickType.RIGHT);
            ev.setCancelled(cancelVanillaAction);
        }
    }

    @EventHandler
    void onBlockDamageBlock(BlockDamageEvent ev) {
        var instance = tryGetEvenetInstance(ev.getPlayer().getUniqueId());
        if (instance == null) return;

        var onBlock = instance.myEvent.onClickBlock;
        if (onBlock == null) return;

        boolean cancelVanillaAction = onBlock.onClickBlock(instance, ev.getBlock(), ev.getBlockFace(), ev.getPlayer().isSneaking() ? ClickType.SHIFT_RIGHT : ClickType.RIGHT);
        ev.setCancelled(cancelVanillaAction);
    }

    @EventHandler
    void onAsyncChat(AsyncChatEvent ev) {
        var instance = tryGetEvenetInstance(ev.getPlayer().getUniqueId());
        if (instance == null) return;

        var onBlock = instance.myEvent.onChat;
        if (onBlock == null) return;

        boolean cancelVanillaAction = onBlock.onChat(instance, ev.message());
        ev.setCancelled(cancelVanillaAction);
    }
}
