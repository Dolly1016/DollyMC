package jp.dreamingpig.dollyMC;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import io.papermc.paper.event.player.PlayerPickItemEvent;
import jp.dreamingpig.dollyMC.augmentedEquipment.AugmentedEquipment;
import jp.dreamingpig.dollyMC.augmentedEquipment.EquipmentInstance;
import jp.dreamingpig.dollyMC.player.OnlinePlayer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DebugListener implements Listener {
    public DebugListener(){
        Bukkit.getPluginManager().registerEvents(this, DollyMC.getPlugin());
        EquipmentInstance.registerDefinition("instance", (p, arg)->{
            return new EquipmentInstance(){
                @EventHandler
                void onPlayerArmSwing(PlayerArmSwingEvent ev){
                    ev.getPlayer().sendMessage(arg);
                }
            };
        });

        Bukkit.getCommandMap().register("ae", new Command("ae") {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
                if(commandSender instanceof Player player){
                    OnlinePlayer.getPlayer(player.getUniqueId()).getAugmentedEquipment().openGUI();
                }else{
                    commandSender.sendMessage("only player can execute this command!");
                }
                return true;
            }
        });

        Bukkit.getCommandMap().register("aeitem", new Command("aeitem") {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
                if(commandSender instanceof Player player){
                    ItemStack is = ItemStack.of(Material.AZALEA);
                    is.editMeta(meta -> {
                        AugmentedEquipment.setAEType(meta.getPersistentDataContainer(), "test", "instance$" + (strings.length >= 1 ? strings[0] : "blank" ));
                        meta.displayName(PlainTextComponentSerializer.plainText().deserialize("テスト装備"));
                    });
                    player.getInventory().addItem(is);
                }else{
                    commandSender.sendMessage("only player can execute this command!");
                }
                return true;
            }
        });
    }
}
