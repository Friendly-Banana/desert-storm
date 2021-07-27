package org.gara.desertstorm.entities;

import org.gara.desertstorm.DesertStorm;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class SandWither extends WitherBoss {

    public SandWither(EntityType<? extends WitherBoss> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int i, boolean bl) {
        // drop picked up stuff
        // copied from Mob
        EquipmentSlot[] equipment = EquipmentSlot.values();
        for (int itemSlot = 0; itemSlot < equipment.length; ++itemSlot) {
            EquipmentSlot equipmentSlot = equipment[itemSlot];
            ItemStack itemStack = this.getItemBySlot(equipmentSlot);
            float f = this.getEquipmentDropChance(equipmentSlot);
            boolean bl2 = f > 1.0F;
            if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack) && (bl || bl2)
                    && Math.max(this.random.nextFloat() - (float) i * 0.01F, 0.0F) < f) {
                if (!bl2 && itemStack.isDamageableItem()) {
                    itemStack.setDamageValue(itemStack.getMaxDamage()
                            - this.random.nextInt(1 + this.random.nextInt(Math.max(itemStack.getMaxDamage() - 3, 1))));
                }

                this.spawnAtLocation(itemStack);
                this.setItemSlot(equipmentSlot, ItemStack.EMPTY);
            }
        }
        ItemEntity itemEntity = this.spawnAtLocation(DesertStorm.SANDSTAR_ITEM);
        if (itemEntity != null) {
            itemEntity.setExtendedLifetime();
        }
    }
}