package org.gara.desertstorm.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.gara.desertstorm.DesertStorm;

public class SandWither extends WitherEntity {

    public SandWither(EntityType<? extends WitherEntity> entityType, World level) {
        super(entityType, level);
    }

    @Override
    protected void dropEquipment(DamageSource damageSource, int i, boolean bl) {
        // drop picked up stuff
        // copied from Mob
        EquipmentSlot[] equipment = EquipmentSlot.values();
        for (EquipmentSlot equipmentSlot : equipment) {
            ItemStack itemStack = this.getEquippedStack(equipmentSlot);
            float f = this.getDropChance(equipmentSlot);
            boolean bl2 = f > 1.0F;
            if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack) && (bl || bl2)
                    && Math.max(this.random.nextFloat() - (float) i * 0.01F, 0.0F) < f) {
                if (!bl2 && itemStack.isDamageable()) {
                    itemStack.setDamage(itemStack.getMaxDamage()
                            - this.random.nextInt(1 + this.random.nextInt(Math.max(itemStack.getMaxDamage() - 3, 1))));
                }

                this.dropStack(itemStack);
                this.equipStack(equipmentSlot, ItemStack.EMPTY);
            }
        }
        ItemEntity itemEntity = this.dropItem(DesertStorm.SANDSTAR_ITEM);
        if (itemEntity != null) {
            itemEntity.setCovetedItem();
        }
    }
}