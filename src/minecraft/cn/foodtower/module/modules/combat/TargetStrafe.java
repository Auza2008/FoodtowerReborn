package cn.foodtower.module.modules.combat;

import cn.foodtower.api.EventHandler;
import cn.foodtower.api.events.Render.EventRender3D;
import cn.foodtower.api.events.World.EventMove;
import cn.foodtower.api.events.World.EventPreUpdate;
import cn.foodtower.api.value.Mode;
import cn.foodtower.api.value.Numbers;
import cn.foodtower.api.value.Option;
import cn.foodtower.manager.ModuleManager;
import cn.foodtower.module.Module;
import cn.foodtower.module.ModuleType;
import cn.foodtower.module.modules.move.Fly;
import cn.foodtower.util.entity.PlayerUtil;
import cn.foodtower.util.math.RotationUtil;
import cn.foodtower.util.render.RenderUtil;
import cn.foodtower.util.render.gl.GLUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class TargetStrafe extends Module {
    public static Mode Esp = new Mode("ESP", EspMode.values(), EspMode.Point);
    public static Numbers<Double> Radius = new Numbers<>("Radius", 1.0D, 0.0D, 6.0D, 0.1D);
    public static Option onJump = new Option("onJump", false);
    public static Option WallCheck = new Option("Check", true);
    private int direction = -1;

    public TargetStrafe() {
        super("TargetStrafe", new String[]{"TargetStrafe"}, ModuleType.Combat);
        this.addValues(Radius, Esp, onJump, WallCheck);
    }

    public static double getSpeedByXZ(double motionX, double motionZ) {
        final double vel = Math.sqrt(motionX * motionX + motionZ * motionZ);
        return vel;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void switchDirection() {
        if (this.direction == 1) {
            this.direction = -1;
        } else {
            this.direction = 1;
        }

    }

    public final boolean doStrafeAtSpeed(EventMove event, double moveSpeed) {
        boolean strafe = this.canStrafe();
        if (strafe) {
            float[] rotations = RotationUtil.getRotations(KillAura.currentTarget);
            if ((double) mc.thePlayer.getDistanceToEntity(KillAura.currentTarget) <= Radius.getValue()) {
                PlayerUtil.setSpeed(event, moveSpeed, rotations[0], this.direction, 0.0D, false);
            } else {
                PlayerUtil.setSpeed(event, moveSpeed, rotations[0], this.direction, 1.0D, !(mc.thePlayer.getDistanceToEntity(KillAura.currentTarget) <= Radius.getValue() + 0.2));
            }
        }

        return strafe;
    }

    public final boolean doStrafeAtSpeedWithoutEvent(double moveSpeed) {
        boolean strafe = this.canStrafe();
        if (strafe) {
            float[] rotations = RotationUtil.getRotations(KillAura.currentTarget);
            if ((double) mc.thePlayer.getDistanceToEntity(KillAura.currentTarget) <= Radius.getValue()) {
                PlayerUtil.setSpeedWithoutEvent(moveSpeed, rotations[0], this.direction, 0.0D, false);
            } else {
                PlayerUtil.setSpeedWithoutEvent(moveSpeed, rotations[0], this.direction, 1.0D, !(mc.thePlayer.getDistanceToEntity(KillAura.currentTarget) <= Radius.getValue() + 0.2));
            }
        }

        return strafe;
    }

    @EventHandler
    public final void onUpdate(EventPreUpdate event) {
        if (canStrafe()) {
            float[] rotations = RotationUtil.getRotations(KillAura.currentTarget);
            double cos = Math.cos(Math.toRadians(rotations[0] + 90.0F));
            double sin = Math.sin(Math.toRadians(rotations[0] + 90.0F));
            double x = KillAura.currentTarget.posX + Radius.getValue() * cos;
            double z = KillAura.currentTarget.posZ + Radius.getValue() * sin;
            if (WallCheck.getValue() && needToChange(x, z)) {
                this.switchDirection();
            }
        }
    }

    public boolean needToChange(double x, double z) {
        if (mc.thePlayer.isCollidedHorizontally) return true;
        for (int i = (int) (mc.thePlayer.posY + 4.0D); i >= 0; i--) {
            BlockPos playerPos = new BlockPos(x, i, z);
            if (mc.theWorld.getBlockState(playerPos).getBlock().equals(Blocks.lava) || mc.theWorld.getBlockState(playerPos).getBlock().equals(Blocks.fire))
                return true;
            if (!ModuleManager.getModuleByClass(Fly.class).isEnabled()) {
                if (!mc.theWorld.isAirBlock(playerPos)) return false;
            } else {
                return false;
            }
        }
        return true;
    }

    @EventHandler
    public final void onRender3D(EventRender3D event) {
        if (!ModuleManager.getModuleByClass(KillAura.class).isEnabled() || KillAura.currentTarget == null) return;

        switch ((EspMode) Esp.getValue()) {
            case Circle:
                if (KillAura.currentTarget != null) {
                    drawExhibitionCircle(KillAura.currentTarget, event.getPartialTicks(), Radius.get().floatValue(), -1);
                }
                break;
            case Point:
                if (KillAura.currentTarget != null) {
                    renderStrafeCircle(KillAura.currentTarget, event.getPartialTicks(), Radius.get().floatValue(), -1);
                }
                break;
            case Polygon: {
                if (KillAura.currentTarget != null) {
                    drawPolygon(KillAura.currentTarget, event.getPartialTicks(), Radius.getValue(), 2.0f, new Color(255, 255, 255));
                    //drawPolygon(KillAura.currentTarget, event.getPartialTicks(), Radius.getValue(),2.5f,new Color(0,0,0));
                }
                break;
            }
        }
    }

    private boolean Check(Entity e2) {
        if (!e2.isEntityAlive()) {
            return false;
        } else if (KillAura.currentTarget != e2) {
            return false;
        } else {
            return e2 != mc.thePlayer && e2 instanceof EntityPlayer;
        }
    }

    private void drawPolygon(Entity entity, float partialTicks, double rad, float Line, Color color) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        RenderUtil.startDrawing();
        GLUtils.startSmooth();
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(Line);
        GL11.glBegin(3);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks - mc.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks - mc.getRenderManager().viewerPosY;
        //Color color = Color.WHITE;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks - mc.getRenderManager().viewerPosZ;

        float r = 0.004921569F * (float) color.getRed();
        float g = 0.003921569F * (float) color.getGreen();
        float b = 0.003921569F * (float) color.getBlue();

        for (int i = 0; i <= 90; ++i) {
            GL11.glColor3f(r, g, b);
            GL11.glVertex3d(x + rad * Math.cos((double) i * 35.283185307179586D / 90.0D), y, z + rad * Math.sin((double) i * 35.283185307179586D / 90.0D));
        }

        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        RenderUtil.stopDrawing();
        GLUtils.endSmooth();
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    public boolean canStrafe() {
        if (this.isEnabled()) {
            if (ModuleManager.getModuleByClass(KillAura.class).isEnabled() && KillAura.currentTarget != null) {
                return !((Boolean) onJump.getValue()) || mc.gameSettings.keyBindJump.isKeyDown();
            }
        }
        return false;
    }

    public void renderStrafeCircle(EntityLivingBase entity, float partialTicks, float range, int color) {
        if (entity != mc.thePlayer && !entity.isInvisible() && mc.thePlayer.canEntityBeSeen(entity) && entity.isEntityAlive()) {
            GL11.glPushMatrix();
            mc.entityRenderer.disableLightmap();
            GL11.glDisable(3553);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glDepthMask(false);

            final double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
            final double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
            final double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

            GL11.glPushMatrix();
            GL11.glLineWidth(2.0f);
            GL11.glEnable(GL11.GL_POINT_SMOOTH);
            GL11.glPointSize(7.0f);
            GL11.glBegin(GL11.GL_POINTS);
            for (int i = 0; i <= 90; ++i) {
                RenderUtil.color(color);
                GL11.glVertex3d(posX + range * Math.cos(i * Math.PI * 2 / 45.0), posY, posZ + range * Math.sin(i * Math.PI * 2 / 45.0));
            }
            GL11.glEnd();
            GL11.glPopMatrix();

            GL11.glDepthMask(true);
            GL11.glDisable(2848);
            GL11.glEnable(2929);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            mc.entityRenderer.enableLightmap();
            GL11.glPopMatrix();
        }
    }

    public void drawExhibitionCircle(EntityLivingBase entity, float partialTicks, float range, int color) {
        if (entity != mc.thePlayer && !entity.isInvisible() && mc.thePlayer.canEntityBeSeen(entity) && entity.isEntityAlive()) {
            GL11.glPushMatrix();
            mc.entityRenderer.disableLightmap();
            GL11.glDisable(3553);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glDepthMask(false);

            final double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
            final double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
            final double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

            GL11.glPushMatrix();
            GL11.glLineWidth(4.0f);
            GL11.glBegin(1);
            for (int i = 0; i <= 90; ++i) {
                RenderUtil.color(color);
                GL11.glVertex3d(posX + range * Math.cos(i * Math.PI * 2 / 45.0), posY, posZ + range * Math.sin(i * Math.PI * 2 / 45.0));
            }
            GL11.glEnd();
            GL11.glPopMatrix();

            GL11.glDepthMask(true);
            GL11.glDisable(2848);
            GL11.glEnable(2929);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            mc.entityRenderer.enableLightmap();
            GL11.glPopMatrix();
        }
    }


    enum EspMode {
        Point, Circle, Polygon
    }
}
