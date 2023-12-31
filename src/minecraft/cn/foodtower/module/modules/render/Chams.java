package cn.foodtower.module.modules.render;


import cn.foodtower.api.EventHandler;
import cn.foodtower.api.events.Render.EventPostRenderPlayer;
import cn.foodtower.api.events.Render.EventPreRenderPlayer;
import cn.foodtower.api.value.Numbers;
import cn.foodtower.api.value.Option;
import cn.foodtower.module.Module;
import cn.foodtower.module.ModuleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Chams extends Module {


    public Numbers<Double> visiblered = new Numbers<>("VisibleRed", "VisibleRed", 0.0, 0.001, 1.0, 0.001);
    public Numbers<Double> visiblegreen = new Numbers<>("VisibleGreen", "VisibleGreen", 1.0, 0.001, 1.0, 0.001);
    public Numbers<Double> visibleblue = new Numbers<>("VisibleBlue", "VisibleBlue", 0.0, 0.001, 1.0, 0.001);
    public Numbers<Double> hiddenred = new Numbers<>("HiddenRed", "HiddenRed", 0.0, 0.001, 1.0, 0.001);
    public Numbers<Double> hiddengreen = new Numbers<>("HiddenGreen", "HiddenGreen", 1.0, 0.001, 1.0, 0.001);
    public Numbers<Double> hiddenblue = new Numbers<>("HiddenBlue", "HiddenBlue", 0.0, 0.001, 1.0, 0.001);
    public Numbers<Double> alpha = new Numbers<>("Alpha", "Alpha", 1.0, 0.001, 1.0, 0.001);
    public Option invisibles = new Option("Invisibles", "Invisibles", false);
    public Option colored = new Option("Colored", "Colored", true);
    //public Option hands = new Option("Hands","Hands", false);
    public Option rainbow = new Option("Raindow", "Raindow", false);
    private final Option players = new Option("Players", "Players", true);
    private final Option animals = new Option("Animals", "Animals", true);
    private final Option mobs = new Option("Mobs", "Mobs", false);
    private final Option passives = new Option("Passives", "Passives", true);

    public Chams() {
        super("Chams", new String[]{"Chams"}, ModuleType.Render);
        addValues(visiblered, visiblegreen, visibleblue, hiddenred, hiddengreen, hiddenblue, alpha, players, animals, mobs, invisibles, passives, colored, rainbow);
    }

    @EventHandler
    private void preRenderPlayer(EventPreRenderPlayer e) {
        if (colored.getValue()) return;
        GL11.glEnable(32823);
        GL11.glPolygonOffset(1.0f, -1100000.0f);
    }

    @EventHandler
    private void postRenderPlayer(EventPostRenderPlayer e) {
        if (colored.getValue()) return;
        GL11.glDisable(32823);
        GL11.glPolygonOffset(1.0f, 1100000.0f);
    }

    public boolean isValid(EntityLivingBase entity) {
        return isValidType(entity) && entity.isEntityAlive() && (!entity.isInvisible() || invisibles.getValue());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return (players.getValue() && entity instanceof EntityPlayer) || (mobs.getValue() && (entity instanceof EntityMob || entity instanceof EntitySlime) || (passives.getValue() && (entity instanceof EntityVillager || entity instanceof EntityGolem)) || (animals.getValue() && entity instanceof EntityAnimal));
    }


}
