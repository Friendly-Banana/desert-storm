package org.gara.desertstorm.mixin;

import net.minecraft.client.texture.SpriteAtlasHolder;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpriteAtlasHolder.class)
public abstract class SpriteAtlasHolderMixin extends SinglePreparationResourceReloader<SpriteAtlasTexture.Data> implements AutoCloseable {
	@Inject(method = "toSpriteId", at = @At("HEAD"), cancellable = true)
	protected void accessibleToSpriteId(Identifier objectId, CallbackInfoReturnable<Identifier> cir) {
	}
}
