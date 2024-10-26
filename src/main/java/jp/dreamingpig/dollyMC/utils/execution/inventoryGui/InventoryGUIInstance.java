package jp.dreamingpig.dollyMC.utils.execution.inventoryGui;

import jp.dreamingpig.dollyMC.DollyMC;
import jp.dreamingpig.dollyMC.utils.execution.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class InventoryGUIInstance extends AbstractExecution {
    final private InventoryGUI myGUI;
    private Inventory myInventory;
    private InventoryView myView;

    InventoryGUIInstance(Player player, InventoryGUI definition, IExecutionHandler handler){
        super(player, handler);

        myGUI = definition;

        InventoryGUIListener.registerInventoryGUIInstance(player.getUniqueId(), this);

        Bukkit.getScheduler().scheduleSyncDelayedTask(DollyMC.getPlugin(), ()->{
            myInventory = Bukkit.createInventory(getPlayer(), myGUI.lines * 9, myGUI.title);
            myView = getPlayer().openInventory(myInventory);
            onOpen();
        });
    }

    InventoryGUI getGUI(){
        return myGUI;
    }

    public Inventory getGUIInventory(){
        return myInventory;
    }
    public Inventory getPlayerInventory(){
        return myView.getBottomInventory();
    }

    void onOpen(){
        updateStaticItems();
        updateDynamicItems();
    }

    private void updateStaticItems(){
        int size = myInventory.getSize();

        for(int i = 0;i<size;i++){
            var slotInfo = new InventoryGUI.SlotInfo(i);

            for(var item : myGUI.fixedItems){
                if(item.predicate().predicate(slotInfo)){
                    myInventory.setItem(i, item.item());
                }
            }

            for(var item : myGUI.staticItems){
                if(item.slot() == i){
                    myInventory.setItem(i, item.item());
                }
            }
        }
    }

    private void updateDynamicItems(){
        int size = myInventory.getSize();

        for(int i = 0;i<size;i++){
            for(var item : myGUI.dynamicItems){
                if(item.slot() == i){
                    myInventory.setItem(i, item.item().apply(this));
                }
            }
        }

        for(var item : myGUI.areaItems) {
            int index = 0;
            for (int i = 0; i < size; i++) {
                if (item.predicate().predicate(new InventoryGUI.SlotInfo(i))) {
                    myInventory.setItem(i, item.item().apply(this, index++));
                }
            }
        }
    }

    private boolean onClickInternal(int slot){
        if(myInventory.getSize() <= slot){
            if(myGUI.playerAreaEditor != null){
                return myGUI.playerAreaEditor.onEdit(this, slot - myInventory.getSize());
            }
            return false;
        }else {
            for (var item : myGUI.staticItems) {
                if (item.slot() == slot) {
                    item.onClick().onClick(this);

                    return true;
                }
            }

            for (var item : myGUI.dynamicItems) {
                if (item.slot() == slot) {
                    item.onClick().onClick(this);

                    return true;
                }
            }

            for (var item : myGUI.areaItems) {
                var slotInfo = new InventoryGUI.SlotInfo(slot);
                if (item.predicate().predicate(slotInfo)) {
                    item.onClick().onClick(this, item.getIndex(slot));

                    return true;
                }
            }
        }
        return false;
    }

    void onClick(int slot){
        if(onClickInternal(slot)){
            updateDynamicItems();
        }
    }

    @Override
    public IExecutable getProcess(){
        return myGUI;
    }

    @Override
    public void terminate(boolean complete) {
        super.terminate(complete);

        //終了後もGUIが開かれていたら閉じる。
        Bukkit.getScheduler().scheduleSyncDelayedTask(DollyMC.getPlugin(),()->{
           if(getPlayer().getOpenInventory() == myView) getPlayer().closeInventory();
        });
    }
}
