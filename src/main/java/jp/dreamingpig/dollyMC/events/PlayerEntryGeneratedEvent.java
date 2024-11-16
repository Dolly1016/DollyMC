package jp.dreamingpig.dollyMC.events;

import jp.dreamingpig.dollyMC.player.OnlinePlayer;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import java.util.UUID;

public class PlayerEntryGeneratedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList ( ) ;

    @Getter
    private final OnlinePlayer player;

    public static HandlerList getHandlerList () {
        return HANDLERS;
    }

    public PlayerEntryGeneratedEvent (OnlinePlayer player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers(){
        return HANDLERS ;
    }

    public UUID getUniqueId(){
        return player.getUniqueId();
    }

}
