package jp.dreamingpig.dollyMC.utils.execution.inventoryGui;

import jp.dreamingpig.dollyMC.DollyMC;
import jp.dreamingpig.dollyMC.utils.execution.CloseRule;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public class InventoryGUIListener implements Listener {
    final private HashMap<UUID, InventoryGUIInstance> fastMap;
    static private InventoryGUIListener listener;
    public InventoryGUIListener(){
        listener = this;
        fastMap = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, DollyMC.getPlugin());
    }

    static public void registerInventoryGUIInstance(UUID owner, InventoryGUIInstance instance){
        var last = listener.fastMap.put(owner, instance);
        if(last != null) listener.treatClosedInventory(last);
    }

    @Nullable InventoryGUIInstance tryGetInventoryGUIInstance(UUID player, InventoryView view){
        if(fastMap.containsKey(player)){
            var gui = fastMap.get(player);
            if(gui.getGUIInventory() == view.getTopInventory())return gui;
        }
        return null;
    }

    @EventHandler
    void onInventoryClick(InventoryClickEvent ev){
        var instance = tryGetInventoryGUIInstance(ev.getView().getPlayer().getUniqueId(),ev.getView());
        if(instance == null)return;

        var type = ev.getClick();
        if(
        type == ClickType.LEFT ||
        type == ClickType.SHIFT_LEFT ||
        type == ClickType.RIGHT ||
        type == ClickType.SHIFT_RIGHT ||
        type == ClickType.MIDDLE
        ) {
            instance.onClick(ev.getSlot());
        }

        ev.setCancelled(true);
    }

    @EventHandler
    void onInventoryDrag(InventoryDragEvent ev){
        var instance = tryGetInventoryGUIInstance(ev.getView().getPlayer().getUniqueId(), ev.getView());
        if(instance == null)return;

        ev.setCancelled(true);
    }

    @EventHandler
    void onInventoryClose(InventoryCloseEvent ev){
        var instance = tryGetInventoryGUIInstance(ev.getPlayer().getUniqueId(), ev.getView());
        if(instance == null)return;

        treatClosedInventory(instance);
        fastMap.remove(ev.getPlayer().getUniqueId());
    }

    private void treatClosedInventory(InventoryGUIInstance instance){
        //終了していないプロセスでかつ、プレイヤーによるインベントリ終了であれば、ルールに沿って適宜実行可能要素を終了させる。
        if(instance.getGUI().closeRule != CloseRule.TREAT_INTERRUPT && !instance.getProcess().isClosed()){
            instance.terminate(instance.getGUI().closeRule == CloseRule.TREAT_COMPLETE);
        }
    }

    /*
    @EventHandler
    void onArmSwing(PlayerArmSwingEvent ev){
        var gui = new InventoryGUI("テストGUI ページ1", 2)
                .pushContent(new InventoryGUI.GUIFixedItem((slot) -> slot.line() == 0, new ItemStack(Material.GRAY_STAINED_GLASS_PANE)))
                .pushContent(new InventoryGUI.GUIStaticItem(17, new ItemStack(Material.NETHER_STAR), (instance)->{
                    var nextPage =new InventoryGUI("テストGUI ページ2", 2)
                            .pushContent(new InventoryGUI.GUIFixedItem((slot) -> slot.line() == 0, new ItemStack(Material.RED_STAINED_GLASS_PANE)))
                            .pushContent(new InventoryGUI.GUIStaticItem(9, new ItemStack(Material.NETHER_STAR), (instance2)->{
                                instance2.terminate(true);
                            }));
                    instance.getHandler().push(nextPage);
                }));
        new StackExecutionHandler(null, ev.getPlayer(), gui).resume();
    }
    */
}
