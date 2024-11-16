package jp.dreamingpig.dollyMC.utils.execution.actionEvent;


import jp.dreamingpig.dollyMC.utils.execution.AbstractExecution;
import jp.dreamingpig.dollyMC.utils.execution.ExecutionScenario;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ActionEventInstance<Container> extends AbstractExecution<Container> {
    ActionEvent<Container> myEvent;

    ActionEventInstance(ActionEvent<Container> definition, Player player, Container container) {
        super(player, container);
        myEvent = definition;
        if(myEvent.onActivated != null) myEvent.onActivated.accept(this);
    }

    public boolean onChat(Component chat){
        if(myEvent.onChat != null) return myEvent.onChat.onChat(this, chat);
        return false;
    }
    public boolean onClickBlock(Block block, BlockFace face, ClickType clickType){
        if(myEvent.onClickBlock != null) return myEvent.onClickBlock.onClickBlock(this, block, face, clickType);
        return false;
    }
    public boolean onClickEntity(Entity entity, ClickType clickType){
        if(myEvent.onClickEntity != null) return myEvent.onClickEntity.onClickEntity(this, entity, clickType);
        return false;
    }
    public boolean onClickAir(ClickType clickType){
        if(myEvent.onClickAir != null) return myEvent.onClickAir.onClickAir(this, clickType);
        return false;
    }

    @Override
    public ExecutionScenario<?> getScenario() {
        return myEvent.scenario;
    }
}