package jp.dreamingpig.dollyMC.utils.execution.inventoryGui;

import io.papermc.paper.event.player.PlayerArmSwingEvent;
import jp.dreamingpig.dollyMC.DollyMC;
import jp.dreamingpig.dollyMC.utils.execution.CloseRule;
import jp.dreamingpig.dollyMC.utils.execution.ExecutionListener;
import jp.dreamingpig.dollyMC.utils.execution.StackExecutionHandler;
import jp.dreamingpig.dollyMC.utils.execution.actionEvent.ActionEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
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
        ExecutionListener.ResetAllExecution(owner);
        listener.fastMap.put(owner, instance);
    }

    static public void unregister(UUID player){
        if(listener.fastMap.containsKey(player)) {
            var lastGUI = listener.fastMap.remove(player);
            Bukkit.getScheduler().scheduleSyncDelayedTask(DollyMC.getPlugin(), () -> {
                var onlinePlayer = Bukkit.getPlayer(player);
                if (onlinePlayer != null && onlinePlayer.getOpenInventory().getTopInventory() == lastGUI.getGUIInventory()) {
                    onlinePlayer.closeInventory();
                }
            });
            lastGUI.onGUIClosed();
        }
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
        //閉じようとしているインベントリが現在有効なGUIである場合にのみ実行される。
        var instance = tryGetInventoryGUIInstance(ev.getPlayer().getUniqueId(), ev.getView());
        if(instance == null)return;

        var removed = fastMap.remove(ev.getPlayer().getUniqueId());
        removed.onGUIClosed();
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
                                instance2.suspend();
                            }))
                            .pushContent(new InventoryGUI.GUIStaticItem(10, new ItemStack(Material.MELON_SEEDS), (instance2)->{
                                instance2.deepen(new ActionEvent().activatedAction((aei)->{
                                    aei.getPlayer().sendMessage("チャットで好きな食べ物を教えてください。");
                                })
                                .withChat((aei, chat)->{
                                    aei.getPlayer().sendMessage(PlainTextComponentSerializer.plainText().serialize(chat) +"が好きなんですね！");
                                    aei.step(true);
                                    return true;
                                })
                                );
                            }))
                            .closeRule(CloseRule.TREAT_COMPLETE);
                    instance.deepen(nextPage);
                }));
        new StackExecutionHandler(null, ev.getPlayer(), gui).resume();
    }
    //*/

}
