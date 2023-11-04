package cn.foodtower.ui.gui.clikguis.utils;

import cn.foodtower.module.modules.render.HUD;
import cn.foodtower.util.math.MathUtil;
import cn.foodtower.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static cn.foodtower.module.Module.mc;

public class SimpleRender {

    public static double delta;

    public static int width() {
        return ScaledResolution.getScaledWidth();
    }

    public static int height() {
        return ScaledResolution.getScaledHeight();
    }

    public static double interpolate(final double current, final double old, final double scale) {
        return old + (current - old) * scale;
    }

    public static float processFPS(final float defV) {
        final float defF = 1000;
        int limitFPS = Math.abs(Minecraft.getDebugFPS());
        return defV / (limitFPS <= 0 ? 1 : limitFPS / defF);
    }


    public static void drawCircle(final float x, final float y, final float r, final float lineWidth, final boolean isFull, final int color) {
        drawCircle(x, y, r, 10, lineWidth, 360, isFull, color);
    }

    public static void drawCircle(float cx, float cy, double r, final int segments, final float lineWidth, final int part, final boolean isFull, final int c) {
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        r *= 2.0D;
        cx *= 2.0F;
        cy *= 2.0F;
        final float f2 = (c >> 24 & 0xFF) / 255.0F;
        final float f3 = (c >> 16 & 0xFF) / 255.0F;
        final float f4 = (c >> 8 & 0xFF) / 255.0F;
        final float f5 = (c & 0xFF) / 255.0F;
        GL11.glEnable(3042);
        GL11.glLineWidth(lineWidth);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f3, f4, f5, f2);
        GL11.glBegin(3);
        for (int i = segments - part; i <= segments; i++) {
            final double x = Math.sin(i * Math.PI / 180.0D) * r;
            final double y = Math.cos(i * Math.PI / 180.0D) * r;
            GL11.glVertex2d(cx + x, cy + y);
            if (isFull)
                GL11.glVertex2d(cx, cy);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glScalef(2.0F, 2.0F, 2.0F);
    }

    public static Color getBlendColor(final double current, final double max) {
        final long base = Math.round(max / 5.0);
        if (current >= base * 5L) {
            return new Color(15, 255, 15);
        }
        if (current >= base << 2) {
            return new Color(166, 255, 0);
        }
        if (current >= base * 3L) {
            return new Color(255, 191, 0);
        }
        if (current >= base << 1) {
            return new Color(255, 89, 0);
        }
        return new Color(255, 0, 0);
    }

    public static void enableRender2D() {
        GL11.glEnable(3042);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.0F);
    }

    public static void disableRender2D() {
        GL11.glDisable(3042);
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    public static void setColor(final int colorHex) {
        final float alpha = (colorHex >> 24 & 255) / 255.0F;
        final float red = (colorHex >> 16 & 255) / 255.0F;
        final float green = (colorHex >> 8 & 255) / 255.0F;
        final float blue = (colorHex & 255) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }


    public static void drawRect(final double left, final double top, final double right, final double bottom, final int color) {
        RenderUtil.drawRect(left, top, right, bottom, color);
    }

    public static void drawRectFloat(final float left, final float top, final float right, final float bottom, final int color) {
        RenderUtil.drawRect(left, top, right, bottom, color);
    }

    public static void drawBorderedRect(final double left, final double top, final double right, final double bottom, final double borderWidth, final int insideColor, final int borderColor, final boolean borderIncludedInBounds) {
        drawRect(left - (borderIncludedInBounds ? 0.0 : borderWidth), top - (borderIncludedInBounds ? 0.0 : borderWidth), right + (borderIncludedInBounds ? 0.0 : borderWidth), bottom + (borderIncludedInBounds ? 0.0 : borderWidth), borderColor);
        drawRect(left + (borderIncludedInBounds ? borderWidth : 0.0), top + (borderIncludedInBounds ? borderWidth : 0.0), right - (borderIncludedInBounds ? borderWidth : 0.0), bottom - (borderIncludedInBounds ? borderWidth : 0.0), insideColor);
    }

    public static int reAlpha(final int color, final float alpha) {
        final Color c = new Color(color);
        final float r = 0.003921569f * c.getRed();
        final float g = 0.003921569f * c.getGreen();
        final float b = 0.003921569f * c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

    public static int getArrayRainbow(final int counter, final int alpha) {

        int colorDelay = 11;
        int colorLength = 110;
        double rainbowState = Math.ceil(System.currentTimeMillis() - (long) counter * colorLength) / colorDelay;
        rainbowState %= 360;

        final Color color = Color.getHSBColor((float) (rainbowState / 360), HUD.g.get().intValue(), HUD.b.get().intValue());
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }

    public static String abcdefg() {
        String[] abc = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        String[] ABC = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        String[] aBc = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        try {
            int var0 = (int) MathUtil.randomDouble(0, aBc.length - 1);
            return aBc[var0] + var0 + abc[abc.length - 1] + ABC[ABC.length - 1] + aBc[abc.length] + abc[ABC.length - 1];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
