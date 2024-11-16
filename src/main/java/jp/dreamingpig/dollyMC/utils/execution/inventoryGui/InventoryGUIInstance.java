package jp.dreamingpig.dollyMC.utils.execution.inventoryGui;

import jp.dreamingpig.dollyMC.DollyMC;
import jp.dreamingpig.dollyMC.utils.execution.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InventoryGUIInstance<Container> extends AbstractExecution<Container> {
    @Getter
    final private InventoryGUI<Container> gui;
    @Getter
    private Inventory inventory;
    private InventoryView inventoryView;

    InventoryGUIInstance(InventoryGUI<Container> definition, Player player, Container container){
        super(player, container);
        gui = definition;

        Bukkit.getScheduler().scheduleSyncDelayedTask(DollyMC.getPlugin(), ()->{
            inventory = Bukkit.createInventory(getPlayer(), gui.lines * 9, gui.title);
            inventoryView = getPlayer().openInventory(inventory);
            onOpen();
        });
    }

    public Inventory getPlayerInventory(){
        return inventoryView.getBottomInventory();
    }

    void onOpen(){
        updateStaticItems();
        updateDynamicItems();
    }

    private void updateStaticItems(){
        int size = inventory.getSize();

        for(int i = 0;i<size;i++){
            var slotInfo = new InventoryGUI.SlotInfo(i);

            for(var item : gui.fixedItems){
                if(item.predicate().predicate(slotInfo)){
                    inventory.setItem(i, item.item());
                }
            }

            for(var item : gui.staticItems){
                if(item.slot() == i){
                    inventory.setItem(i, item.item());
                }
            }
        }
    }

    private void updateDynamicItems(){
        int size = inventory.getSize();

        for(int i = 0;i<size;i++){
            for(var item : gui.dynamicItems){
                if(item.slot() == i){
                    inventory.setItem(i, item.item().apply(this));
                }
            }
        }

        for(var item : gui.areaItems) {
            int index = 0;
            for (int i = 0; i < size; i++) {
                if (item.predicate().predicate(new InventoryGUI.SlotInfo(i))) {
                    inventory.setItem(i, item.item().apply(this, index++));
                }
            }
        }

        for(var item : gui.interactiveItems) {
            int index = 0;
            for (int i = 0; i < size; i++) {
                if (item.predicate().predicate(new InventoryGUI.SlotInfo(i))) {
                    inventory.setItem(i, item.item().apply(this, index++));
                }
            }
        }
    }

    /**
     * クリック結果
     * @param requireUpdate GUIコンテンツの更新を要求する場合true。
     * @param allowUpdateCursor カーソルの自然な更新を要求する場合true。
     * @param newCursor 上書きするカーソルのアイテム。上書きしない場合はnull。
     */
    record ClickResult(boolean requireUpdate, boolean allowUpdateCursor, @Nullable ItemStack newCursor){}
    private ClickResult onClickInternal(Inventory clicked, int slotInInventory, @NotNull ItemStack cursor){
        if(inventory != clicked){
            if(gui.playerAreaEditor != null){
                boolean allowUpdateCursor = gui.playerAreaEditor.onEdit(this, slotInInventory);
                return new ClickResult(true, allowUpdateCursor,  null);
            }
            return new ClickResult(false, false,  null);
        }else {
            for (var item : gui.staticItems) {
                if (item.slot() == slotInInventory) {
                    item.onClick().onClick(this);
                    return new ClickResult(true, false,  null);
                }
            }

            for (var item : gui.dynamicItems) {
                if (item.slot() == slotInInventory) {
                    item.onClick().onClick(this);
                    return new ClickResult(true, false,  null);
                }
            }

            var slotInfo =new InventoryGUI.SlotInfo(slotInInventory);

            for (var item : gui.areaItems) {
                if (item.predicate().predicate(slotInfo)) {
                    item.onClick().onClick(this, item.getIndex(slotInInventory));
                    return new ClickResult(true, false,  null);
                }
            }

            for(var item : gui.interactiveItems){
                if(item.predicate().predicate(slotInfo)){
                    var newCursor = item.onClick().onClick(this, cursor, item.getIndex(slotInInventory));
                    return new ClickResult(true, false,  newCursor);
                }
            }
        }
        return new ClickResult(false, false,  null);
    }

    ClickResult onClick(Inventory clicked, int slotInInventory, @NotNull ItemStack cursor){
        var result = onClickInternal(clicked, slotInInventory, cursor);
        if(result.requireUpdate) updateDynamicItems();
        return result;
    }

    public void changeTitle(String title){
        inventoryView.setTitle(title);
    }

    @Override
    public ExecutionScenario<?> getScenario() {
        return gui.scenario;
    }

    @Override
    protected void onSuspend(@Nullable Runnable callback){
        Bukkit.getScheduler().scheduleSyncDelayedTask(DollyMC.getPlugin(), ()->{
            if(getPlayer().getOpenInventory() == inventoryView){
                getPlayer().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            }
            super.onSuspend(callback);
        });
    }
}
