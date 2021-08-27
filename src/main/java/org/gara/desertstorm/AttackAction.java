package org.gara.desertstorm;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public interface AttackAction {
    void OnLeftClick(Player player, InteractionHand hand);
}