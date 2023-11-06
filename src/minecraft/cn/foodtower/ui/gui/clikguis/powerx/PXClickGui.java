/*
 * Decompiled with CFR 0.136.
 */
package cn.foodtower.ui.gui.clikguis.powerx;

import cn.foodtower.Client;
import cn.foodtower.api.value.Mode;
import cn.foodtower.api.value.Numbers;
import cn.foodtower.api.value.Option;
import cn.foodtower.api.value.Value;
import cn.foodtower.fastuni.FastUniFontRenderer;
import cn.foodtower.manager.ModuleManager;
import cn.foodtower.module.Module;
import cn.foodtower.module.ModuleType;
import cn.foodtower.util.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class PXClickGui extends GuiScreen {
    public static ModuleType currentModuleType = ModuleType.Combat;
    public static Module currentModule = !ModuleManager.getModulesInType(currentModuleType).isEmpty() ? ModuleManager.getModulesInType(currentModuleType).get(0) : null;
    public static float startX = 100, startY = 100;
    final FastUniFontRenderer font = Client.FontLoaders.GoogleSans18;
    final FastUniFontRenderer typefont = Client.FontLoaders.GoogleSans24;
    public int moduleStart;
    public int valueStart;
    public float moveX, moveY;
    boolean previousmouse = true;
    boolean mouse;

    public static boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public static boolean isSettingsButtonHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    public static boolean isButtonHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public static boolean isCheckBoxHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public static boolean isCategoryHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (isHovered(startX, startY - 25, startX + 400, startY + 25, mouseX, mouseY) && Mouse.isButtonDown(0)) {
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

        RenderUtil.drawRoundedRect(startX - 1, startY - 1, startX + 370 + 1, startY + 230 + 1, 3F, new Color(11, 84, 150, 150).getRGB());
        RenderUtil.drawRoundedRect(startX, startY, startX + 370, startY + 230, 2F, new Color(27, 27, 27).getRGB());
        RenderUtil.drawRect(startX, startY + 20.5, startX + 370, startY + 20, new Color(21, 21, 21, 140).getRGB());
        RenderUtil.drawRect(startX, startY + 23, startX + 370, startY + 20, new Color(23, 23, 23, 130).getRGB());

        RenderUtil.drawRect(startX + 80, startY + 20.3, startX + 180, startY + 230, new Color(27, 27, 27).getRGB());

        RenderUtil.drawRect(startX + 80, startY + 20.3, startX + 80.8, startY + 230, new Color(12, 12, 12, 200).getRGB());
        RenderUtil.drawRect(startX + 80.8, startY + 20.3, startX + 81.6, startY + 230, new Color(13, 13, 13, 200).getRGB());
        RenderUtil.drawRect(startX + 81.6, startY + 20.3, startX + 82.4, startY + 230, new Color(14, 14, 14, 200).getRGB());
        RenderUtil.drawRect(startX + 82.4, startY + 20.3, startX + 83.2, startY + 230, new Color(15, 15, 15, 200).getRGB());
        RenderUtil.drawRect(startX + 83.2, startY + 20.3, startX + 84, startY + 230, new Color(16, 16, 16, 200).getRGB());
        RenderUtil.drawRect(startX + 84, startY + 20.3, startX + 84.8, startY + 230, new Color(17, 17, 17, 200).getRGB());
        RenderUtil.drawRect(startX + 84.8, startY + 20.3, startX + 85.8, startY + 230, new Color(18, 18, 18, 190).getRGB());
        RenderUtil.drawRect(startX + 85.8, startY + 20.3, startX + 90, startY + 230, new Color(20, 20, 20, 110).getRGB());
        RenderUtil.drawRect(startX + 185, startY + 20.3, startX + 182, startY + 230, new Color(21, 21, 21, 110).getRGB());
        RenderUtil.drawRect(startX + 182, startY + 20.3, startX + 180, startY + 230, new Color(21, 21, 21, 100).getRGB());
        Client.FontLoaders.GoogleSans22.drawString(Client.name, startX + 5, startY + 5, (new Color(152, 152, 152)).getRGB());


        for (int i = 0; i < ModuleType.values().length; i++) {
            if (ModuleType.values()[i] != currentModuleType) {

                RenderUtil.circle(startX + 30, startY + 50 + i * 20, 6, new Color(255, 255, 255, 0).getRGB());
            }
            try {
                if (isCategoryHovered(startX + 15, startY + 40 + i * 20, startX + 60, startY + 50 + i * 20, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    currentModuleType = ModuleType.values()[i];
                    currentModule = !ModuleManager.getModulesInType(currentModuleType).isEmpty() ? ModuleManager.getModulesInType(currentModuleType).get(0) : null;
                    moduleStart = 0;
                }
            } catch (Exception ignored) {
            }
        }
        int m = Mouse.getDWheel();

        if (isCategoryHovered(startX + 60, startY, startX + 200, startY + 320, mouseX, mouseY)) {
            if (m < 0 && moduleStart < ModuleManager.getModulesInType(currentModuleType).size() - 1) {
                moduleStart++;
            }
            if (m > 0 && moduleStart > 0) {
                moduleStart--;
            }
        }

        if (isCategoryHovered(startX + 200, startY, startX + 420, startY + 320, mouseX, mouseY)) {
            if (m < 0 && valueStart < currentModule.getValues().size() - 1) {
                valueStart++;
            }
            if (m > 0 && valueStart > 0) {
                valueStart--;
            }
        }
        Client.FontLoaders.GoogleSans20.drawString(currentModuleType.toString(), startX + 85, startY + 5, new Color(152, 152, 152).getRGB());
        Client.FontLoaders.GoogleSans20.drawString(currentModule == null ? currentModuleType.toString() : "Module:" + currentModule.getName(), startX + 185, startY + 5, new Color(152, 152, 152).getRGB());
        if (currentModule != null) {
            float mY = startY + 16;
            for (int i = 0; i < ModuleManager.getModulesInType(currentModuleType).size(); i++) {
                Module module = ModuleManager.getModulesInType(currentModuleType).get(i);
                if (mY > startY + 200) break;
                if (i < moduleStart) {
                    continue;
                }
                if (module.isEnabled()) {
                    RenderUtil.drawRoundedRect(startX + 88, mY + 8, startX + 175, mY + 25, 8F, new Color(58, 58, 58).getRGB());
                    RenderUtil.circle(startX + 95, mY + 16, 2, new Color(0, 100, 237).getRGB());
                } else {
                    RenderUtil.drawRoundedRect(startX + 88, mY + 8, startX + 175, mY + 25, 8F, new Color(39, 39, 39).getRGB());
                    RenderUtil.circle(startX + 95, mY + 16, 2, new Color(152, 152, 152).getRGB());
                }
                RenderUtil.startGlScissor((int) (startX), (int) (startY - 16), 165, 285);
                Client.FontLoaders.GoogleSans20.drawString(module.getName(), startX + 108 - 5, mY + 13, module.isEnabled() ? new Color(152, 152, 152).getRGB() : new Color(68, 68, 68).getRGB());
                RenderUtil.stopGlScissor();
                if (!module.getValues().isEmpty()) {
                    Client.FontLoaders.GoogleSans18.drawString("+", startX + 167, mY + 15, new Color(150, 150, 150).getRGB());
                }
                if (isSettingsButtonHovered(startX + 108, mY, startX + 100 + (typefont.getStringWidth(module.getName())), mY + 12 + typefont.getHeight(), mouseX, mouseY)) {
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
                if (isSettingsButtonHovered(startX + 90, mY, startX + 100 + (Client.FontLoaders.GoogleSans20.getStringWidth(module.getName())), mY + 12 + Client.FontLoaders.GoogleSans20.getHeight(), mouseX, mouseY) && Mouse.isButtonDown(1)) {
                    currentModule = module;
                    valueStart = 0;
                }
                mY += 18;
            }
            mY = startY + 25;
            for (int i = 0; i < currentModule.getValues().size(); i++) {
                if (mY > startY + 215) break;
                if (i < valueStart) {
                    continue;
                }
                Value value = currentModule.getValues().get(i);
                if (!value.isDisplayable()) continue;
                if (value instanceof Numbers) {
                    float x = startX + 290;
                    double render = 68.0F * (((Number) value.getValue()).floatValue() - ((Numbers<?>) value).getMinimum().floatValue()) / (((Numbers<?>) value).getMaximum().floatValue() - ((Numbers<?>) value).getMinimum().floatValue());
                    RenderUtil.drawRect(x - 6, mY + 2, (float) ((double) x + 74), mY + 3, (new Color(200, 200, 200)).getRGB());
                    RenderUtil.drawRect(x - 6, mY + 2, (float) (x + render + 6.5D), mY + 3, (new Color(0, 100, 237)).getRGB());
                    RenderUtil.circle((float) (x + render + 4), mY + 2, 2F, new Color(0, 100, 237).getRGB());
                    font.drawString(value.getName() + ": " + value.getValue(), startX + 190, mY, new Color(152, 152, 152).getRGB());
                    if (!Mouse.isButtonDown(0)) {
                        previousmouse = false;
                    }
                    if (isButtonHovered(x, mY - 2, x + 100, mY + 7, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                        if (!previousmouse && Mouse.isButtonDown(0)) {
                            render = ((Numbers<?>) value).getMinimum().doubleValue();
                            double max = ((Numbers<?>) value).getMaximum().doubleValue();
                            double inc = ((Numbers<?>) value).getIncrement().doubleValue();
                            double valAbs = mouseX - (x + 1.0D);
                            double perc = valAbs / 68.0D;
                            perc = Math.min(Math.max(0.0D, perc), 1.0D);
                            double valRel = (max - render) * perc;
                            double val = render + valRel;
                            val = Math.round(val * (1.0D / inc)) / (1.0D / inc);
                            value.setValue(val);
                        }
                        if (!Mouse.isButtonDown(0)) {
                            previousmouse = false;
                        }
                    }
                    mY += 20;
                }
                if (value instanceof Option) {
                    float x = startX + 230;
                    font.drawString(value.getName(), startX + 190, mY, new Color(152, 152, 152).getRGB());
                    if ((boolean) value.getValue()) {
                        RenderUtil.drawRoundedRect(x + 56, mY - 1, x + 76, mY + 9, 4F, new Color(58, 58, 58).getRGB());
                        RenderUtil.circle(x + 72, mY + 4, 4, new Color(0, 100, 237).getRGB());
                    } else {

                        RenderUtil.drawRoundedRect(x + 56, mY - 1, x + 76, mY + 9, 4F, new Color(58, 58, 58).getRGB());
                        RenderUtil.circle(x + 60, mY + 4, 4, new Color(152, 152, 152).getRGB());
                    }
                    if (isCheckBoxHovered(x + 56, mY, x + 76, mY + 9, mouseX, mouseY)) {
                        if (!previousmouse && Mouse.isButtonDown(0)) {
                            previousmouse = true;
                            mouse = true;
                        }

                        if (mouse) {
                            value.setValue(!(boolean) value.getValue());
                            mouse = false;
                        }
                    }
                    if (!Mouse.isButtonDown(0)) {
                        previousmouse = false;
                    }
                    mY += 20;
                }
                if (value instanceof Mode) {
                    Mode mo = (Mode) value;
                    float x = startX + 275;
                    font.drawStringWithShadow(value.getName(), startX + 190, mY, new Color(152, 152, 152).getRGB());
                    RenderUtil.drawRect(x + 20, mY, x + 65, mY + 10, new Color(120, 120, 120).getRGB());

                    RenderUtil.drawRect(x + 5, mY, x + 15, mY + 10, new Color(0, 100, 237).getRGB());
                    RenderUtil.drawRect(x + 70, mY, x + 80, mY + 10, new Color(0, 100, 237).getRGB());
                    font.drawStringWithShadow("<", x + 8 - 1, mY + 3, new Color(152, 152, 152).getRGB());
                    font.drawStringWithShadow(">", x + 72 + 1, mY + 3, new Color(152, 152, 152).getRGB());
                    RenderUtil.startGlScissor((int) (x + 20), (int) mY, (int) (x + 65), (int) (mY + 10));
                    font.drawStringWithShadow(((Mode) value).getModeAsString(), x + 40 - font.getStringWidth(((Mode) value).getModeAsString()) / 2, mY + 2.5f, -1);
                    RenderUtil.stopGlScissor();
                    if (isStringHovered(x, mY - 5, x + 100, mY + 15, mouseX, mouseY)) {
                        if (!previousmouse) {
                            if ((Mouse.isButtonDown(0) || Mouse.isButtonDown(1))) {
                                List<String> options = Collections.singletonList(Arrays.toString(mo.getModes()));
                                //noinspection SuspiciousMethodCalls
                                int index = options.indexOf(mo.getValue());
                                if (Mouse.isButtonDown(0)) {
                                    index++;
                                } else {
                                    index--;
                                }
                                if (index >= options.size()) {
                                    index = 0;
                                } else if (index < 0) {
                                    index = options.size() - 1;
                                }
                                mo.setValue(mo.getModes()[index]);
                            }
                            previousmouse = true;
                        }
                        if (!Mouse.isButtonDown(0)) {
                            previousmouse = false;
                        }

                    }
                    mY += 25;
                }
            }
            RenderUtil.startGlScissor((int) (startX), (int) (startY - 16), 165 - 87, 285);
            float typeY = 0;
            ModuleType category;
            for (int i = 0; i < ModuleType.values().length; i++) {
                category = ModuleType.values()[i];
                typefont.drawString(category.name(), startX + 25, startY + 40 + typeY, category == currentModuleType ? new Color(0, 100, 237).getRGB() : new Color(152, 152, 152).getRGB());
                typeY += 20;
            }
            Client.FontLoaders.NovIcon28.drawString("G", startX + 7, startY + 140, currentModuleType.name().equals("Misc") ? new Color(0, 100, 237).getRGB() : new Color(152, 152, 152).getRGB());
            Client.FontLoaders.NovIcon28.drawString("J", startX + 7, startY + 160, currentModuleType.name().equals("Globals") ? new Color(0, 100, 237).getRGB() : new Color(152, 152, 152).getRGB());
            Client.FontLoaders.NovoIcon25.drawString("G", startX + 7, startY + 180, currentModuleType.name().equals("Script") ? new Color(0, 100, 237).getRGB() : new Color(152, 152, 152).getRGB());
            final int typeWidth = -4;
            switch (currentModuleType.name()) {
                case "Combat": {
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/" + currentModuleType + 2 + ".png"), (int) startX + 5, (int) startY + 40 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Render.png"), (int) startX + 5, (int) startY + 80 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Movement.png"), (int) startX + 5, (int) startY + 60 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Player.png"), (int) startX + 5, (int) startY + 100 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/World.png"), (int) startX + 5, (int) startY + 120 + typeWidth, 14, 14);
                    break;
                }
                case "Render": {
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Combat.png"), (int) startX + 5, (int) startY + 40 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/" + currentModuleType + 2 + ".png"), (int) startX + 5, (int) startY + 80 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Movement.png"), (int) startX + 5, (int) startY + 60 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Player.png"), (int) startX + 5, (int) startY + 100 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/World.png"), (int) startX + 5, (int) startY + 120 + typeWidth, 14, 14);
                    break;
                }
                case "Movement": {
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Combat.png"), (int) startX + 5, (int) startY + 40 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Render.png"), (int) startX + 5, (int) startY + 80 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/" + currentModuleType + 2 + ".png"), (int) startX + 5, (int) startY + 60 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Player.png"), (int) startX + 5, (int) startY + 100 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/World.png"), (int) startX + 5, (int) startY + 120 + typeWidth, 14, 14);
                    break;
                }
                case "Player": {
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Combat.png"), (int) startX + 5, (int) startY + 40 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Render.png"), (int) startX + 5, (int) startY + 80 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Movement.png"), (int) startX + 5, (int) startY + 60 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/" + currentModuleType + 2 + ".png"), (int) startX + 5, (int) startY + 100 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/World.png"), (int) startX + 5, (int) startY + 120 + typeWidth, 14, 14);
                    break;
                }
                case "World": {
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Combat.png"), (int) startX + 5, (int) startY + 40 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Render.png"), (int) startX + 5, (int) startY + 80 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Movement.png"), (int) startX + 5, (int) startY + 60 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Player.png"), (int) startX + 5, (int) startY + 100 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/" + currentModuleType + 2 + ".png"), (int) startX + 5, (int) startY + 120 + typeWidth, 14, 14);
                    break;
                }
                case "Globals":
                case "Script":
                case "Misc": {
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Combat.png"), (int) startX + 5, (int) startY + 40 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Render.png"), (int) startX + 5, (int) startY + 80 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Movement.png"), (int) startX + 5, (int) startY + 60 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Player.png"), (int) startX + 5, (int) startY + 100 + typeWidth, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/World.png"), (int) startX + 5, (int) startY + 120 + typeWidth, 14, 14);
                    break;
                }
            }
            RenderUtil.stopGlScissor();
        }

    }

}
