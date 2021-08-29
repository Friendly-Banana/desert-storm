package org.gara.desertstorm;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public interface AttackAction {
    void OnLeftClick(PlayerEntity player, Hand hand);
}