package jp.dreamingpig.dollyMC.utils.execution.actionEvent;

import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import jp.dreamingpig.dollyMC.DollyMC;
import jp.dreamingpig.dollyMC.utils.execution.AbstractExecution;
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
    public ActionEventListener(){
        Bukkit.getPluginManager().registerEvents(this, DollyMC.getPlugin());
    }

    @Nullable ActionEventInstance<?> tryGetEventInstance(UUID player){
        if(AbstractExecution.getExecution(player) instanceof ActionEventInstance<?> casted) return casted;
        return null;
    }

    @EventHandler
    void onInteractEntity(PlayerInteractEntityEvent ev){
        var instance = tryGetEventInstance(ev.getPlayer().getUniqueId());
        if(instance == null) return;

        boolean cancelVanillaAction = instance.onClickEntity(ev.getRightClicked(), ev.getPlayer().isSneaking() ? ClickType.SHIFT_RIGHT : ClickType.RIGHT);
        ev.setCancelled(cancelVanillaAction);
    }

    @EventHandler
    void onPreAttackEntity(PrePlayerAttackEntityEvent ev){
        var instance = tryGetEventInstance(ev.getPlayer().getUniqueId());
        if(instance == null) return;

        boolean cancelVanillaAction = instance.onClickEntity(ev.getAttacked(), ev.getPlayer().isSneaking() ? ClickType.SHIFT_LEFT : ClickType.LEFT);
        ev.setCancelled(cancelVanillaAction);
    }

    @EventHandler
    void onInteractBlock(PlayerInteractEvent ev){
        var instance = tryGetEventInstance(ev.getPlayer().getUniqueId());
        if(instance == null) return;

        if(ev.getMaterial() == Material.AIR) {
            if(ev.getHand() == EquipmentSlot.OFF_HAND){
                ev.setCancelled(true);
                return;
            }

            boolean cancelVanillaAction = instance.onClickAir(ev.getPlayer().isSneaking() ? ClickType.SHIFT_RIGHT : ClickType.RIGHT);
            ev.setCancelled(cancelVanillaAction);
        }else{
            if(ev.getHand() == EquipmentSlot.OFF_HAND){
                ev.setCancelled(true);
                return;
            }

            boolean cancelVanillaAction = instance.onClickBlock(ev.getClickedBlock(), ev.getBlockFace(), ev.getPlayer().isSneaking() ? ClickType.SHIFT_RIGHT : ClickType.RIGHT);
            ev.setCancelled(cancelVanillaAction);
        }
    }

    @EventHandler
    void onBlockDamageBlock(BlockDamageEvent ev) {
        var instance = tryGetEventInstance(ev.getPlayer().getUniqueId());
        if (instance == null) return;

        boolean cancelVanillaAction = instance.onClickBlock(ev.getBlock(), ev.getBlockFace(), ev.getPlayer().isSneaking() ? ClickType.SHIFT_RIGHT : ClickType.RIGHT);
        ev.setCancelled(cancelVanillaAction);
    }

    @EventHandler
    void onAsyncChat(AsyncChatEvent ev) {
        var instance = tryGetEventInstance(ev.getPlayer().getUniqueId());
        if (instance == null) return;

        ev.setCancelled(instance.onChat(ev.message()));
    }
}
