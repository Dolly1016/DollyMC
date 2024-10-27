package jp.dreamingpig.dollyMC.utils.execution;

import org.bukkit.entity.Player;

abstract public class AbstractExecution implements IExecution {
    final private Player myPlayer;
    final private IExecutionHandler myHandler;
    private boolean isActiveFlag = true;
    protected void inactivate(){
        isActiveFlag = false;
    }

    @Override
    public boolean isActive(){
        return isActiveFlag;
    }

    protected AbstractExecution(Player player, IExecutionHandler handler){
        this.myPlayer = player;
        this.myHandler = handler;
    }



    @Override
    public Player getPlayer(){
        return myPlayer;
    }

    @Override
    public IExecutionHandler getHandler(){
        return myHandler;
    }
}
