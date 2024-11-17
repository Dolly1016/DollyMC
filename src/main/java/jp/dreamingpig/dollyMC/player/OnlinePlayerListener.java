package jp.dreamingpig.dollyMC.player;

import jp.dreamingpig.dollyMC.DollyMC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class OnlinePlayerListener implements Listener {
    public OnlinePlayerListener(){
        Bukkit.getPluginManager().registerEvents(this, DollyMC.getPlugin());
    }
    @EventHandler
    void onPlayerLogin(PlayerLoginEvent ev){
        OnlinePlayer.generateOnlinePlayer(ev.getPlayer().getUniqueId());
    }
}
