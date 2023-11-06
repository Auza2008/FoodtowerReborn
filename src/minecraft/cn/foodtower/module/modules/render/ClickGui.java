package cn.foodtower.module.modules.render;

import cn.foodtower.api.value.Mode;
import cn.foodtower.api.value.Option;
import cn.foodtower.module.Module;
import cn.foodtower.module.ModuleType;
import cn.foodtower.ui.gui.clikguis.ClickUi.ClickUi;
import cn.foodtower.ui.gui.clikguis.clickgui3.ClientClickGui;
import cn.foodtower.ui.gui.clikguis.exhibition.Clickgui;
import cn.foodtower.ui.gui.clikguis.powerx.PXClickGui;
import cn.foodtower.util.sound.SoundFxPlayer;

public class ClickGui extends Module {
    public static final Option CustomColor = new Option("CustomColor", false);
    private static final Mode mode = new Mode("Mode", "mode", ClickGui.modes.values(), modes.Exhibition);

    public ClickGui() {
        super("ClickGui", new String[]{"clickui"}, ModuleType.Render);
        this.addValues(mode, CustomColor);
    }

    @Override
    public void onEnable() {

        new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.ClickGuiOpen, -4);
        switch ((modes) mode.getValue()) {
            case Exhibition: {
                mc.displayGuiScreen(new Clickgui());
                break;
            }
            case PowerX: {
                mc.displayGuiScreen(new PXClickGui());
                break;
            }
            case Nov: {
                mc.displayGuiScreen(new ClickUi());
                break;
            }
            case Azlips: {
                mc.displayGuiScreen(new cn.foodtower.ui.gui.clikguis.clickgui4.ClickGui());
                break;
            }
            case Distance: {
                mc.displayGuiScreen(new ClientClickGui());
                break;
            }
        }
        this.setEnabled(false);
    }

    enum modes {
        Exhibition,
        PowerX,
        Nov,
        Distance,
        Azlips
    }
}
