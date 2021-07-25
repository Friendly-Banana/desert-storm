package org.gara.desertstorm;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public interface AttackAction {
    void onAttack(Player player, InteractionHand hand);
}