package jp.dreamingpig.dollyMC.utils.execution;

import org.bukkit.entity.Player;

public interface ContainerConstructor<Container>{
    Container instantiate(Player player);
}