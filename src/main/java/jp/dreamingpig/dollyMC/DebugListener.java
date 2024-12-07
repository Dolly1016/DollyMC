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
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
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
        EquipmentInstance.registerDefinition("heart", (p, arg)->{
            var heartNum = Integer.parseInt(arg);
            return new EquipmentInstance(){
                @Override
                public void onActivated(){
                    getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(new AttributeModifier(NamespacedKey.fromString("debug.heart"), heartNum, AttributeModifier.Operation.ADD_SCALAR));
                }
                @Override
                public void onInactivated(){
                    getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(NamespacedKey.fromString("debug.heart"));
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
                    ItemStack is = ItemStack.of(Material.POPPY);
                    is.editMeta(meta -> {
                        AugmentedEquipment.setAEType(meta.getPersistentDataContainer(), "test", "heart$" + (strings.length >= 1 ? strings[0] : "4" ));
                        meta.displayName(PlainTextComponentSerializer.plainText().deserialize("体力増強"));
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
