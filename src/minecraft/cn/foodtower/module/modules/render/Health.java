package cn.foodtower.module.modules.render;

import cn.foodtower.api.EventHandler;
import cn.foodtower.api.events.Render.EventRender2D;
import cn.foodtower.module.Module;
import cn.foodtower.module.ModuleType;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

import java.awt.*;

public class Health extends Module {
    private int width;

    public Health() {
        super("Health", new String[]{"health"}, ModuleType.Render);
    }

    @EventHandler
    private void renderHud(EventRender2D event) {
        if (mc.thePlayer.getHealth() >= 0.0f && mc.thePlayer.getHealth() < 10.0f) {
            this.width = 3;
        }
        if (mc.thePlayer.getHealth() >= 10.0f && mc.thePlayer.getHealth() < 100.0f) {
            this.width = 5;
        }

        mc.fontRendererObj.drawStringWithShadow("" + MathHelper.ceiling_float_int(mc.thePlayer.getHealth()), (float) (ScaledResolution.getScaledWidth() / 2 - this.width), (float) (ScaledResolution.getScaledHeight() / 2 - 5) - (float) Crosshair.SIZE.getValue().doubleValue() - (float) Crosshair.GAP.getValue().doubleValue(), mc.thePlayer.getHealth() <= 10.0f ? new Color(255, 0, 0).getRGB() : new Color(0, 255, 0).getRGB());
    }
}
