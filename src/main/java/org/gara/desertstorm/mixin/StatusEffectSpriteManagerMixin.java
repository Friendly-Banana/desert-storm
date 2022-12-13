package org.gara.desertstorm.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.data.client.ModelIds;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.gara.desertstorm.Cocktails;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

/**
 * Some status effects should reuse Minecraft's textures. Copying would probably violate ToS. So, we inject into the sprite atlas for status effects and change the sprite ids. <p>
 * As {@link net.minecraft.client.texture.SpriteAtlasHolder#toSpriteId(Identifier) toSpriteId} is private in a parent class we need a {@link SpriteAtlasHolderMixin helper mixin} injecting there. Then we can overwrite that method.
 */
@Mixin(StatusEffectSpriteManager.class)
public abstract class StatusEffectSpriteManagerMixin extends SpriteAtlasHolderMixin {
	private final static Map<Identifier, Identifier> remapEffects = Map.of(id(Cocktails.FREEZING), ModelIds.getItemModelId(Items.POWDER_SNOW_BUCKET), id(Cocktails.TELEPORTER), ModelIds.getItemModelId(Items.CHORUS_FRUIT), id(Cocktails.MIDAS_TOUCH), ModelIds.getBlockModelId(Blocks.GOLD_BLOCK));

	private static Identifier id(StatusEffect s) {
		return Registry.STATUS_EFFECT.getId(s);
	}

	@Override
	protected void accessibleToSpriteId(Identifier objectId, CallbackInfoReturnable<Identifier> cir) {
		if (remapEffects.containsKey(objectId)) {
			cir.setReturnValue(remapEffects.get(objectId));
		}
	}
}
