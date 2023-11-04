package cn.foodtower.ui.gui.clikguis.exhibition;

import cn.foodtower.Client;
import cn.foodtower.api.value.Mode;
import cn.foodtower.api.value.Numbers;
import cn.foodtower.api.value.Option;
import cn.foodtower.api.value.Value;
import cn.foodtower.fastuni.FastUniFontRenderer;
import cn.foodtower.manager.ModuleManager;
import cn.foodtower.module.Module;
import cn.foodtower.module.ModuleType;
import cn.foodtower.module.modules.render.HUD;
import cn.foodtower.ui.gui.clikguis.utils.SimpleRender;
import cn.foodtower.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.List;

public class Clickgui extends GuiScreen {
    public static ModuleType currentModuleType;
    public static Module currentModule;
    public static float startX;
    public static float startY;

    static {
        currentModuleType = ModuleType.Combat;
        currentModule = ((!ModuleManager.getModulesInType(currentModuleType).isEmpty()) ? ModuleManager.getModulesInType(currentModuleType).get(0) : null);
        startX = 100;
        startY = 100;
    }

    private final FastUniFontRenderer titlefont = Client.FontLoaders.GoogleSans20;
    private final FastUniFontRenderer efont = Client.FontLoaders.GoogleSans16;
    public int moduleStart;
    public int valueStart;
    public float moveX;
    public float moveY;
    boolean previousmouse;
    float x;
    float mY;
    boolean a;
    String inmode;
    String nowinmode;
    String inmodule;
    boolean mouse;

    public Clickgui() {
        moduleStart = 0;
        valueStart = 0;
        previousmouse = true;
        a = false;
        inmode = null;
        nowinmode = null;
        inmodule = null;
        moveX = 0;
        moveY = 0;
    }

    public int getAstolfoRainbow(final int v1) {
        final double length = 1.6;
        double delay = Math.ceil((System.currentTimeMillis() + (long) (v1 * length)) / 5);
        final float rainbow = ((float) ((delay %= 360) / 360) < 0.5) ? (-(float) (delay / 360)) : ((float) (delay / 360));
        return Color.getHSBColor(rainbow, 0.6f, 1).getRGB();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (isHovered(startX, startY - 10, startX + 400, startY + 10, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            if (moveX == 0 && moveY == 0) {
                moveX = mouseX - startX;
                moveY = mouseY - startY;
            } else {
                startX = mouseX - moveX;
                startY = mouseY - moveY;
            }
            previousmouse = true;
        } else if (moveX != 0 || moveY != 0) {
            moveX = 0;
            moveY = 0;
        }
//        if (ClickGui.Streamer.getValue()) {
//            drawGradientRect(0, 0, width, height, new Color(0, 0, 0, 0).getRGB(), ColorCreator.createRainbowFromOffset(-6000, 5));
//        }
        SimpleRender.drawBorderedRect(startX + 0.5, startY + 0.5, (startX + 450) - 0.5, (startY + 300) - 0.5, 0.5, new Color(40, 40, 40).getRGB(), new Color(60, 60, 60).getRGB(), true);
        SimpleRender.drawBorderedRect(startX + 2, startY + 2, startX + 450 - 2, startY + 300 - 2, 0.5, new Color(22, 22, 22).getRGB(), new Color(60, 60, 60).getRGB(), true);
        SimpleRender.drawBorderedRect(startX + 2, startY + 2, startX + 50 - 2, startY + 300 - 2, 0.5, new Color(15, 15, 15).getRGB(), new Color(60, 60, 60).getRGB(), true);
        //Rainbow
        RenderUtil.drawGradientSideways(startX + 3, startY + 3, startX + 450 - 2.5, startY + 4, getAstolfoRainbow((int) (startX * 20)), getAstolfoRainbow((int) (startX * 60)));
        //Value
        SimpleRender.drawBorderedRect(startX + 190, startY + 20, startX + 440, startY + 280 - 2 + 11, 0.5, new Color(15, 15, 15).getRGB(), new Color(60, 60, 60).getRGB(), true);
        RenderUtil.drawRect(startX + 195, startY + 20, startX + 222, startY + 25, new Color(15, 15, 15).getRGB());
        efont.drawString("Values", startX + 197, startY + 20, -1);

        titlefont.drawString("F", startX + 6, startY + 7, HUDColor());
        titlefont.drawString("oodtower", startX + 6 + titlefont.getStringWidth("S"), startY + 7, -1);

        for (int i = 0; i < ModuleType.values().length; ++i) {
            if (ModuleType.values()[i] == currentModuleType) {
                if (ModuleType.values()[i].name().equals("Combat")) {
                    SimpleRender.drawBorderedRect(startX + 2, startY + 20, startX + 50 - 2, startY + 65 - 2, 0.5, new Color(22, 22, 22).getRGB(), new Color(50, 50, 50).getRGB(), true);
                    Gui.drawRect(startX + 47, startY + 20 + 1 - 0.5, startX + 50, (startY + 65) - 2.5, new Color(22, 22, 22).getRGB());
                }
                if (ModuleType.values()[i].name().equals("Render")) {
                    SimpleRender.drawBorderedRect(startX + 2, startY + 62, startX + 50 - 2, startY + 103, 0.5, new Color(22, 22, 22).getRGB(), new Color(50, 50, 50).getRGB(), true);
                    Gui.drawRect(startX + 47, startY + 62 + 0.5, startX + 50, (startY + 103) - 0.5, new Color(22, 22, 22).getRGB());
                }
                if (ModuleType.values()[i].name().equals("Movement")) {
                    SimpleRender.drawBorderedRect(startX + 2, startY + 103, startX + 50 - 2, startY + 143, 0.5, new Color(22, 22, 22).getRGB(), new Color(50, 50, 50).getRGB(), true);
                    Gui.drawRect(startX + 47, startY + 103 + 0.5, startX + 50, (startY + 143) - 0.5, new Color(22, 22, 22).getRGB());
                }
                if (ModuleType.values()[i].name().equals("Player")) {
                    SimpleRender.drawBorderedRect(startX + 2, startY + 143, startX + 50 - 2, startY + 183 - 2, 0.5, new Color(22, 22, 22).getRGB(), new Color(50, 50, 50).getRGB(), true);
                    Gui.drawRect(startX + 47, startY + 143 + 0.5, startX + 50, (startY + 181) - 0.5, new Color(22, 22, 22).getRGB());
                }
                if (ModuleType.values()[i].name().equals("World")) {
                    SimpleRender.drawBorderedRect(startX + 2, startY + 183, startX + 50 - 2, startY + 223 - 2, 0.5, new Color(22, 22, 22).getRGB(), new Color(50, 50, 50).getRGB(), true);
                    Gui.drawRect(startX + 47, startY + 183 + 0.5, startX + 50, (startY + 221) - 0.5, new Color(22, 22, 22).getRGB());
                }
            }
            if (ModuleType.values()[i].name().equals("Combat")) {
                Client.FontLoaders.CSGO46.drawString("J", startX + 15, startY + 49, (ModuleType.values()[i] == currentModuleType) ? -1 : new Color(200, 200, 200).getRGB());
            }
            if (ModuleType.values()[i].name().equals("Render")) {
                Client.FontLoaders.CSGO40.drawString("C", startX + 15, startY + 88, (ModuleType.values()[i] == currentModuleType) ? -1 : new Color(200, 200, 200).getRGB());
            }
            if (ModuleType.values()[i].name().equals("Movement")) {
                Client.FontLoaders.CSGO40.drawString("E", startX + 15, startY + 127, (ModuleType.values()[i] == currentModuleType) ? -1 : new Color(200, 200, 200).getRGB());
            }
            if (ModuleType.values()[i].name().equals("Player")) {
                Client.FontLoaders.CSGO36.drawString("F", startX + 15, startY + 166, (ModuleType.values()[i] == currentModuleType) ? -1 : new Color(200, 200, 200).getRGB());
            }
            if (ModuleType.values()[i].name().equals("World")) {
                Client.FontLoaders.CSGO36.drawString("I", startX + 15, startY + 207, (ModuleType.values()[i] == currentModuleType) ? -1 : new Color(200, 200, 200).getRGB());
            }
            try {
                if (isCategoryHovered(startX, startY + 15 + (titlefont.getHeight() + 3) + (i * 40), startX + 45, startY + 45 + (titlefont.getHeight() + 3) + (i * 40), mouseX, mouseY)) {
                    if (Mouse.isButtonDown(0)) {
                        currentModuleType = ModuleType.values()[i];
                        currentModule = ((!ModuleManager.getModulesInType(currentModuleType).isEmpty()) ? ModuleManager.getModulesInType(currentModuleType).get(0) : null);
                        moduleStart = 0;
                    }
                }
            } catch (Exception ignored) {
            }
        }
        final int m = Mouse.getDWheel();
        if (isCategoryHovered(startX + 60, startY + (titlefont.getHeight() + 3), startX + 200, startY + (titlefont.getHeight() + 3) + 320, mouseX, mouseY)) {
            if (m < 0 && moduleStart < ModuleManager.getModulesInType(currentModuleType).size() - 1) {
                ++moduleStart;
            }
            if (m > 0 && moduleStart > 0) {
                --moduleStart;
            }
        }
        final List<Value<?>> values1 = currentModule.getValues();

        if (isCategoryHovered(startX + 200, startY + (titlefont.getHeight() + 3), startX + 420, startY + (titlefont.getHeight() + 3) + 320, mouseX, mouseY)) {
            if (m < 0 && valueStart < currentModule.getValues().size() - 1) {
                ++valueStart;
            }
            if (m > 0 && valueStart > 0) {
                --valueStart;
            }
        }
        efont.drawString((currentModule == null) ? currentModuleType.toString() : (currentModuleType.toString() + "-" + currentModule.getName()), startX + 420 - Minecraft.getMinecraft().fontRendererObj.getStringWidth((currentModule == null) ? currentModuleType.toString() : (currentModuleType.toString() + "-" + currentModule.getName())), startY + 10, new Color(248, 248, 248).getRGB());
        if (currentModule != null) {
            //  final UnicodeFontRenderer efont = Client.instance.FontManager.arial17;
            mY = startY + 5;
            for (int j = 0; j < ModuleManager.getModulesInType(currentModuleType).size(); ++j) {
                final Module module = ModuleManager.getModulesInType(currentModuleType).get(j);
                if (mY > startY + 290) {
                    break;
                }
                if (j >= moduleStart) {
                    SimpleRender.drawBorderedRect(startX + 65, mY + 2, startX + 175, mY + 20, 0.5, new Color(22, 22, 22).getRGB(), new Color(60, 60, 60).getRGB(), true);
                    if (!module.isEnabled()) {
                        SimpleRender.drawBorderedRect(startX + 161, mY + 4 + 5, startX + 165, mY + 13, 0.5, new Color(22, 22, 22).getRGB(), new Color(60, 60, 60).getRGB(), true);
                    } else {
                        SimpleRender.drawBorderedRect(startX + 161, mY + 4 + 5, startX + 165, mY + 13, 0.5, HUDColor(), new Color(60, 60, 60).getRGB(), true);
                    }
                    efont.drawString(module.getName(), startX + 75, mY + 9, module.isEnabled() ? HUDColor() : new Color(200, 200, 200).getRGB());
                    inmodule = module.getName();
                    if (isSettingsButtonHovered(startX + 90, mY, startX + 100 + efont.getStringWidth(module.getName()), mY + 8 + efont.getHeight(), mouseX, mouseY)) {
                        if (!previousmouse && Mouse.isButtonDown(0)) {
                            module.setEnabled(!module.isEnabled());
                            previousmouse = true;
                        }
                        if (!previousmouse && Mouse.isButtonDown(1)) {
                            previousmouse = true;
                        }
                    }
                    if (!Mouse.isButtonDown(0)) {
                        previousmouse = false;
                    }
                    if (isSettingsButtonHovered(startX + 90, mY, startX + 100 + efont.getStringWidth(module.getName()), mY + 8 + efont.getHeight(), mouseX, mouseY) && Mouse.isButtonDown(1)) {
                        currentModule = module;
                        valueStart = 0;
                    }
                    mY += 22;
                }
            }
            mY = startY + 30;
            for (int j = 0; j < currentModule.getValues().size() && mY <= startY + 277; ++j) {
                if (j >= valueStart) {
                    if (mY > startY + 277) {
                        break;
                    }
                    final Value value = values1.get(j);
                    if (!value.isDisplayable()) continue;
                    if (value instanceof Numbers) {
                        x = startX + 205;
                        double render = 68 * (((Number) value.getValue()).floatValue() - ((Numbers) value).getMinimum().floatValue()) / (((Numbers) value).getMaximum().floatValue() - ((Numbers) value).getMinimum().floatValue());
                        SimpleRender.drawBorderedRect(x - 6 - 1, mY + 2 + 5 - 1, x + 75, mY + 5 + 5 + 1, 0.5, new Color(22, 22, 22).getRGB(), new Color(15, 15, 15).getRGB(), true);
                        RenderUtil.drawRect(x - 6, mY + 2 + 5, x + render + 6.5, mY + 5 + 5, HUDColor());
                        efont.drawString(value.getName(), startX + 200, mY, -1);
                        efont.drawString(String.valueOf(value.getValue()), startX + 290, mY + 6, -1);
                        if (!Mouse.isButtonDown(0)) {
                            previousmouse = false;
                        }
                        if (isButtonHovered(x, mY + 3, x + 100, mY + 12, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                            if (!previousmouse && Mouse.isButtonDown(0)) {
                                render = ((Numbers) value).getMinimum().doubleValue();
                                final double max = ((Numbers) value).getMaximum().doubleValue();
                                final double inc = ((Numbers) value).getIncrement().doubleValue();
                                final double valAbs = mouseX - (x + 1);
                                double perc = valAbs / 68;
                                perc = Math.min(Math.max(0, perc), 1);
                                final double valRel = (max - render) * perc;
                                double val = render + valRel;
                                val = Math.round(val * (1 / inc)) / (1 / inc);
                                value.setValue(val);
                            }
                            if (!Mouse.isButtonDown(0)) {
                                previousmouse = false;
                            }
                        }
                        mY += 17;
                    }
                    if (value instanceof Option) {
                        x = startX + 120;
                        efont.drawString(value.getName(), startX + 200, mY + 3, -1);
                        if ((Boolean) value.getValue()) {
                            SimpleRender.drawBorderedRect(x + 182, (int) mY + 2, x + 188, mY + 8, 0.5, HUDColor(), new Color(60, 60, 60).getRGB(), true);
                        } else {
                            SimpleRender.drawBorderedRect(x + 182, (int) mY + 2, x + 188, mY + 8, 0.5, new Color(22, 22, 22).getRGB(), new Color(60, 60, 60).getRGB(), true);
                        }
                        if (isCheckBoxHovered(x + 80, mY, x + 216, mY + 9, mouseX, mouseY)) {
                            if (!previousmouse && Mouse.isButtonDown(0)) {
                                previousmouse = true;
                                mouse = true;
                            }
                            if (mouse) {
                                value.setValue(!(Boolean) value.getValue());
                                mouse = false;
                            }
                        }
                        if (!Mouse.isButtonDown(0)) {
                            previousmouse = false;
                        }
                        mY += 18;
                    }
                    if (value instanceof Mode) {
                        x = startX + 300;
                        efont.drawString(value.getName(), startX + 200, mY + 1, -1);
                        if (isStringHovered(x - 15, mY, x + 100 - 25, mY + 15, mouseX, mouseY)) {
                            SimpleRender.drawBorderedRect(x - 5, mY - 3, x + 80, mY + 10, 0.5, new Color(40, 40, 40).getRGB(), new Color(60, 60, 60).getRGB(), true);
                        } else {
                            SimpleRender.drawBorderedRect(x - 5, mY - 3, x + 80, mY + 10, 0.5, new Color(40, 40, 40).getRGB(), new Color(0, 0, 0).getRGB(), true);
                        }
                        efont.drawString(((Mode) value).getModeAsString(), x, mY + 1, -1);
                        nowinmode = inmodule + value.getName();
                        if (isStringHovered(x - 15, mY, x + 75, mY + 15, mouseX, mouseY)) {
                            if (Mouse.isButtonDown(0) && !previousmouse) {
                                if (inmode.equals(inmodule + value.getName())) {
                                    inmode = "heshang";
                                } else {
                                    a = true;
                                    inmode = nowinmode;
                                }
                                previousmouse = true;
                            }
                            if (Mouse.isButtonDown(0) && !previousmouse) {
                                previousmouse = true;
                            }
                            if (!Mouse.isButtonDown(0)) {
                                previousmouse = false;
                            }
                        }
                        if (a) {
                            if (inmode.equals(inmodule + value.getName())) {
                                mc.fontRendererObj.drawString(">", ((int) x + 70), ((int) mY - 1), -1);
                                SimpleRender.drawBorderedRect((int) x + 81, (int) mY - 3, (int) x + 70 + 60, (int) mY - 1 + 10 * ((Mode) value).getModes().length, 0.5, new Color(22, 22, 22).getRGB(), new Color(60, 60, 60).getRGB(), true);
                                final int next = ((Mode) value).getModes().length - 1;
                                if (next == 0) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                }
                                if (next == 1) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                }
                                if (next == 2) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                }
                                if (next == 3) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                }
                                if (next == 4) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[4]), startX + 384, mY + 1 + 40, ModeColor());
                                }
                                if (next == 5) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[4]), startX + 384, mY + 1 + 40, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[5]), startX + 384, mY + 1 + 50, ModeColor());
                                }
                                if (next == 6) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[4]), startX + 384, mY + 1 + 40, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[5]), startX + 384, mY + 1 + 50, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[6]), startX + 384, mY + 1 + 60, ModeColor());
                                }
                                if (next == 7) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[4]), startX + 384, mY + 1 + 40, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[5]), startX + 384, mY + 1 + 50, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[6]), startX + 384, mY + 1 + 60, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[7]), startX + 384, mY + 1 + 70, ModeColor());
                                }
                                if (next == 8) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[4]), startX + 384, mY + 1 + 40, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[5]), startX + 384, mY + 1 + 50, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[6]), startX + 384, mY + 1 + 60, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[7]), startX + 384, mY + 1 + 70, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[8]), startX + 384, mY + 1 + 80, ModeColor());
                                }
                                if (next == 9) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[4]), startX + 384, mY + 1 + 40, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[5]), startX + 384, mY + 1 + 50, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[6]), startX + 384, mY + 1 + 60, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[7]), startX + 384, mY + 1 + 70, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[8]), startX + 384, mY + 1 + 80, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[9]), startX + 384, mY + 1 + 90, ModeColor());
                                }
                                if (next == 10) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[4]), startX + 384, mY + 1 + 40, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[5]), startX + 384, mY + 1 + 50, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[6]), startX + 384, mY + 1 + 60, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[7]), startX + 384, mY + 1 + 70, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[8]), startX + 384, mY + 1 + 80, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[9]), startX + 384, mY + 1 + 90, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[10]), startX + 384, mY + 1 + 100, ModeColor());
                                }
                                if (next == 11) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[4]), startX + 384, mY + 1 + 40, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[5]), startX + 384, mY + 1 + 50, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[6]), startX + 384, mY + 1 + 60, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[7]), startX + 384, mY + 1 + 70, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[8]), startX + 384, mY + 1 + 80, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[9]), startX + 384, mY + 1 + 90, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[10]), startX + 384, mY + 1 + 100, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[11]), startX + 384, mY + 1 + 110, ModeColor());
                                }
                                if (next == 12) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[4]), startX + 384, mY + 1 + 40, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[5]), startX + 384, mY + 1 + 50, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[6]), startX + 384, mY + 1 + 60, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[7]), startX + 384, mY + 1 + 70, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[8]), startX + 384, mY + 1 + 80, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[9]), startX + 384, mY + 1 + 90, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[10]), startX + 384, mY + 1 + 100, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[11]), startX + 384, mY + 1 + 110, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[12]), startX + 384, mY + 1 + 120, ModeColor());
                                }
                                if (next == 13) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[4]), startX + 384, mY + 1 + 40, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[5]), startX + 384, mY + 1 + 50, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[6]), startX + 384, mY + 1 + 60, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[7]), startX + 384, mY + 1 + 70, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[8]), startX + 384, mY + 1 + 80, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[9]), startX + 384, mY + 1 + 90, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[10]), startX + 384, mY + 1 + 100, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[11]), startX + 384, mY + 1 + 110, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[10]), startX + 384, mY + 1 + 100, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[11]), startX + 384, mY + 1 + 110, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[12]), startX + 384, mY + 1 + 120, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[13]), startX + 384, mY + 1 + 130, ModeColor());
                                }
                                if (next == 14) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[4]), startX + 384, mY + 1 + 40, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[5]), startX + 384, mY + 1 + 50, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[6]), startX + 384, mY + 1 + 60, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[7]), startX + 384, mY + 1 + 70, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[8]), startX + 384, mY + 1 + 80, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[9]), startX + 384, mY + 1 + 90, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[10]), startX + 384, mY + 1 + 100, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[11]), startX + 384, mY + 1 + 110, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[12]), startX + 384, mY + 1 + 120, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[13]), startX + 384, mY + 1 + 130, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[14]), startX + 384, mY + 1 + 140, ModeColor());
                                }
                                if (next == 15) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[4]), startX + 384, mY + 1 + 40, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[5]), startX + 384, mY + 1 + 50, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[6]), startX + 384, mY + 1 + 60, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[7]), startX + 384, mY + 1 + 70, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[8]), startX + 384, mY + 1 + 80, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[9]), startX + 384, mY + 1 + 90, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[10]), startX + 384, mY + 1 + 100, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[11]), startX + 384, mY + 1 + 110, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[12]), startX + 384, mY + 1 + 120, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[13]), startX + 384, mY + 1 + 130, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[14]), startX + 384, mY + 1 + 140, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[15]), startX + 384, mY + 1 + 150, ModeColor());
                                }
                                if (next == 16) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[4]), startX + 384, mY + 1 + 40, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[5]), startX + 384, mY + 1 + 50, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[6]), startX + 384, mY + 1 + 60, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[7]), startX + 384, mY + 1 + 70, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[8]), startX + 384, mY + 1 + 80, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[9]), startX + 384, mY + 1 + 90, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[10]), startX + 384, mY + 1 + 100, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[11]), startX + 384, mY + 1 + 110, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[12]), startX + 384, mY + 1 + 120, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[13]), startX + 384, mY + 1 + 130, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[14]), startX + 384, mY + 1 + 140, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[15]), startX + 384, mY + 1 + 150, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[16]), startX + 384, mY + 1 + 160, ModeColor());
                                }
                                if (next == 17) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[4]), startX + 384, mY + 1 + 40, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[5]), startX + 384, mY + 1 + 50, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[6]), startX + 384, mY + 1 + 60, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[7]), startX + 384, mY + 1 + 70, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[8]), startX + 384, mY + 1 + 80, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[9]), startX + 384, mY + 1 + 90, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[10]), startX + 384, mY + 1 + 100, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[11]), startX + 384, mY + 1 + 110, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[12]), startX + 384, mY + 1 + 120, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[13]), startX + 384, mY + 1 + 130, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[14]), startX + 384, mY + 1 + 140, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[15]), startX + 384, mY + 1 + 150, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[16]), startX + 384, mY + 1 + 160, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[17]), startX + 384, mY + 1 + 170, ModeColor());
                                }
                                if (next == 18) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[4]), startX + 384, mY + 1 + 40, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[5]), startX + 384, mY + 1 + 50, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[6]), startX + 384, mY + 1 + 60, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[7]), startX + 384, mY + 1 + 70, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[8]), startX + 384, mY + 1 + 80, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[9]), startX + 384, mY + 1 + 90, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[10]), startX + 384, mY + 1 + 100, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[11]), startX + 384, mY + 1 + 110, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[12]), startX + 384, mY + 1 + 120, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[13]), startX + 384, mY + 1 + 130, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[14]), startX + 384, mY + 1 + 140, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[15]), startX + 384, mY + 1 + 150, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[16]), startX + 384, mY + 1 + 160, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[17]), startX + 384, mY + 1 + 170, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[18]), startX + 384, mY + 1 + 180, ModeColor());
                                }
                                if (next == 19) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[4]), startX + 384, mY + 1 + 40, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[5]), startX + 384, mY + 1 + 50, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[6]), startX + 384, mY + 1 + 60, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[7]), startX + 384, mY + 1 + 70, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[8]), startX + 384, mY + 1 + 80, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[9]), startX + 384, mY + 1 + 90, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[10]), startX + 384, mY + 1 + 100, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[11]), startX + 384, mY + 1 + 110, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[12]), startX + 384, mY + 1 + 120, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[13]), startX + 384, mY + 1 + 130, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[14]), startX + 384, mY + 1 + 140, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[15]), startX + 384, mY + 1 + 150, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[16]), startX + 384, mY + 1 + 160, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[17]), startX + 384, mY + 1 + 170, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[18]), startX + 384, mY + 1 + 180, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[19]), startX + 384, mY + 1 + 190, ModeColor());
                                }
                                if (next == 20) {
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[0]), startX + 384, mY + 1, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[1]), startX + 384, mY + 1 + 10, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[2]), startX + 384, mY + 1 + 20, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[3]), startX + 384, mY + 1 + 30, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[4]), startX + 384, mY + 1 + 40, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[5]), startX + 384, mY + 1 + 50, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[6]), startX + 384, mY + 1 + 60, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[7]), startX + 384, mY + 1 + 70, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[8]), startX + 384, mY + 1 + 80, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[9]), startX + 384, mY + 1 + 90, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[10]), startX + 384, mY + 1 + 100, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[11]), startX + 384, mY + 1 + 110, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[12]), startX + 384, mY + 1 + 120, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[13]), startX + 384, mY + 1 + 130, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[14]), startX + 384, mY + 1 + 140, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[15]), startX + 384, mY + 1 + 150, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[16]), startX + 384, mY + 1 + 160, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[17]), startX + 384, mY + 1 + 170, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[18]), startX + 384, mY + 1 + 180, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[19]), startX + 384, mY + 1 + 190, ModeColor());
                                    efont.drawStringWithShadow(String.valueOf(((Mode) value).getModes()[20]), startX + 384, mY + 1 + 200, ModeColor());
                                }
                                if (Mouse.isButtonDown(0)) {
                                    if (next >= 0 && isStringHovered(((int) x + 81), (int) mY, ((int) x + 70 + 60), ((int) mY + 5), mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[0]);
                                        a = false;
                                    }
                                    if (next >= 1 && isStringHovered(((int) x + 81), ((int) mY + 10), ((int) x + 70 + 60), ((int) mY + 5 + 10), mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[1]);
                                        a = false;
                                    }
                                    if (next >= 2 && isStringHovered(((int) x + 81), ((int) mY + 20), ((int) x + 70 + 60), ((int) mY + 5 + 20), mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[2]);
                                        a = false;
                                    }
                                    if (next >= 3 && isStringHovered(((int) x + 81), ((int) mY + 30), ((int) x + 70 + 60), ((int) mY + 5 + 30), mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[3]);
                                        a = false;
                                    }
                                    if (next >= 4 && isStringHovered(((int) x + 81), ((int) mY + 40), ((int) x + 70 + 60), ((int) mY + 5 + 40), mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[4]);
                                        a = false;
                                    }
                                    if (next >= 5 && isStringHovered(((int) x + 81), ((int) mY + 50), ((int) x + 70 + 60), ((int) mY + 5 + 50), mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[5]);
                                        a = false;
                                    }
                                    if (next >= 6 && isStringHovered(((int) x + 81), ((int) mY + 60), ((int) x + 70 + 60), ((int) mY + 5 + 60), mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[6]);
                                        a = false;
                                    }
                                    if (next >= 7 && isStringHovered(((int) x + 81), ((int) mY + 70), ((int) x + 70 + 60), ((int) mY + 5 + 70), mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[7]);
                                        a = false;
                                    }
                                    if (next >= 8 && isStringHovered(((int) x + 81), ((int) mY + 80), ((int) x + 70 + 60), ((int) mY + 5 + 80), mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[8]);
                                        a = false;
                                    }
                                    if (next >= 9 && isStringHovered(((int) x + 81), ((int) mY + 90), ((int) x + 70 + 60), ((int) mY + 5 + 90), mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[9]);
                                        a = false;
                                    }
                                    if (next >= 10 && isStringHovered(((int) x + 81), ((int) mY + 100), ((int) x + 70 + 60), ((int) mY + 5 + 100), mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[10]);
                                        a = false;
                                    }
                                    if (next >= 11 && isStringHovered(((int) x + 81), ((int) mY + 110), ((int) x + 70 + 60), ((int) mY + 5 + 110), mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[11]);
                                        a = false;
                                    }
                                    if (next >= 12 && isStringHovered(((int) x + 81), ((int) mY + 120), ((int) x + 70 + 60), ((int) mY + 5 + 120), mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[12]);
                                        a = false;
                                    }
                                    if (next >= 13 && isStringHovered((int) x + 81, (int) mY + 130, (int) x + 70 + 60, (int) mY + 5 + 130, mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[13]);
                                        a = false;
                                    }
                                    if (next >= 14 && isStringHovered((int) x + 81, (int) mY + 140, (int) x + 70 + 60, (int) mY + 5 + 140, mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[14]);
                                        a = false;
                                    }
                                    if (next >= 15 && isStringHovered((int) x + 81, (int) mY + 150, (int) x + 70 + 60, (int) mY + 5 + 150, mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[15]);
                                        a = false;
                                    }
                                    if (next >= 16 && isStringHovered((int) x + 81, (int) mY + 160, (int) x + 70 + 60, (int) mY + 5 + 160, mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[16]);
                                        a = false;
                                    }
                                    if (next >= 17 && isStringHovered((int) x + 81, (int) mY + 170, (int) x + 70 + 60, (int) mY + 5 + 170, mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[17]);
                                        a = false;
                                    }
                                    if (next >= 18 && isStringHovered((int) x + 81, (int) mY + 180, (int) x + 70 + 60, (int) mY + 5 + 180, mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[18]);
                                        a = false;
                                    }
                                    if (next >= 19 && isStringHovered((int) x + 81, (int) mY + 190, (int) x + 70 + 60, (int) mY + 5 + 190, mouseX, mouseY)) {
                                        value.setValue(((Mode) value).getModes()[19]);
                                        a = false;
                                    }
                                    if (next >= 20) {
                                        if (isStringHovered((int) x + 81, (int) mY + 200, (int) x + 70 + 60, (int) mY + 5 + 200, mouseX, mouseY)) {
                                            value.setValue(((Mode) value).getModes()[20]);
                                            a = false;
                                        }
                                        previousmouse = true;
                                    }
                                }
                            } else {
                                mc.fontRendererObj.drawString("V", ((int) x + 70), ((int) mY - 1), -1);
                            }
                        } else {
                            inmode = "START";
                        }
                        mY += 17;
                    }
                }
            }
        }
    }

    private int ModeColor() {
        return new Color(225, 225, 225).getRGB();
    }

    private int HUDColor() {
        return new Color(HUD.r.getValue().intValue(), HUD.g.getValue().intValue(), HUD.b.getValue().intValue()).getRGB();
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
    }

    public boolean isStringHovered(final float f, final float y, final float g, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public boolean isSettingsButtonHovered(final float x, final float y, final float x2, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    public boolean isButtonHovered(final float f, final float y, final float g, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public boolean isCheckBoxHovered(final float f, final float y, final float g, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public boolean isCategoryHovered(final float x, final float y, final float x2, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    public boolean isHovered(final float x, final float y, final float x2, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
