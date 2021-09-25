package org.gara.desertstorm.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.gara.desertstorm.Utils;

public class MixerScreen extends HandledScreen<ScreenHandler> {
    // path to the gui texture
    private static final Identifier TEXTURE = Utils.NewIdentifier("textures/gui/mixer.png");

    public MixerScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        // top left edge of the menu
        int leftX = (width - backgroundWidth) / 2;
        int topY = (height - backgroundHeight) / 2;
        drawTexture(matrices, leftX, topY, 0, 0, backgroundWidth, backgroundHeight);

        int progress = ((MixerScreenHandler) this.handler).getProgress();
        if (progress > 0) {
            int n = (int) (28.0F * (1.0F - (float) progress / 300.0F));
            if (n > 0) {
                this.drawTexture(matrices, width / 2 + 39, topY + 30, 177, 0, 7, n);
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }
}
