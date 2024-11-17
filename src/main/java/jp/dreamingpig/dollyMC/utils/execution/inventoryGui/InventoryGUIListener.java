package jp.dreamingpig.dollyMC.utils.execution.inventoryGui;

import io.papermc.paper.event.player.PlayerArmSwingEvent;
import jp.dreamingpig.dollyMC.DollyMC;
import jp.dreamingpig.dollyMC.utils.Boxed;
import jp.dreamingpig.dollyMC.utils.execution.AbstractExecution;
import jp.dreamingpig.dollyMC.utils.execution.ExecutionScenario;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class InventoryGUIListener implements Listener {
    public InventoryGUIListener(){
        Bukkit.getPluginManager().registerEvents(this, DollyMC.getPlugin());
    }

    @Nullable InventoryGUIInstance<?> tryGetEventInstance(UUID player, InventoryView view){
        if(AbstractExecution.getExecution(player) instanceof InventoryGUIInstance<?> casted && casted.getInventory() == view.getTopInventory()) return casted;
        return null;
    }

    @EventHandler
    void onInventoryClick(InventoryClickEvent ev){
        var instance = tryGetEventInstance(ev.getView().getPlayer().getUniqueId(),ev.getView());
        if(instance == null)return;

        var type = ev.getClick();
        if(
        type == ClickType.LEFT ||
        type == ClickType.SHIFT_LEFT ||
        type == ClickType.RIGHT ||
        type == ClickType.SHIFT_RIGHT ||
        type == ClickType.MIDDLE
        ) {
            var result = instance.onClick(ev.getClickedInventory(), ev.getSlot(), ev.getCursor());

            ev.setCancelled(!result.allowUpdateCursor());
            if(!result.allowUpdateCursor() && result.newCursor() != null) ev.getView().setCursor(result.newCursor());
        }else {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    void onInventoryDrag(InventoryDragEvent ev){
        var instance = tryGetEventInstance(ev.getView().getPlayer().getUniqueId(), ev.getView());
        if(instance == null)return;

        ev.setCancelled(true);
    }

    @EventHandler
    void onInventoryClose(InventoryCloseEvent ev){
        //閉じようとしているインベントリが現在有効なGUIである場合にのみ実行される。
        var instance = tryGetEventInstance(ev.getPlayer().getUniqueId(), ev.getView());
        if(instance == null)return;

        //現在のGUIを終了させる。
        AbstractExecution.unregisterExecution(ev.getPlayer().getUniqueId(), null);
    }
}
