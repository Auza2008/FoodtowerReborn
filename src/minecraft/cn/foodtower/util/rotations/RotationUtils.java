package cn.foodtower.util.rotations;

import cn.foodtower.util.misc.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class RotationUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static float[] getFluxRotations(Entity entity, double maxRange) {
        if (entity == null) {
            return null;
        } else {
            double diffX = entity.posX - mc.thePlayer.posX;
            double diffZ = entity.posZ - mc.thePlayer.posZ;
            Location BestPos = new Location(entity.posX, entity.posY, entity.posZ);
            Location myEyePos = new Location(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + mc.thePlayer.getEyeHeight(), Minecraft.getMinecraft().thePlayer.posZ);

            double diffY;
            for (diffY = entity.boundingBox.minY + 0.7D; diffY < entity.boundingBox.maxY - 0.1D; diffY += 0.1D) {
                if (myEyePos.distanceTo(new Location(entity.posX, diffY, entity.posZ)) < myEyePos.distanceTo(BestPos)) {
                    BestPos = new Location(entity.posX, diffY, entity.posZ);
                }
            }

            if (myEyePos.distanceTo(BestPos) > maxRange) {
                return null;
            } else {
                diffY = BestPos.getY() - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
                double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
                float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
                float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
                return new float[]{yaw, pitch};
            }
        }
    }

    public static float[] getAngles(Entity entity) {
        if (entity == null) return null;
        final EntityPlayerSP thePlayer = mc.thePlayer;

        final double diffX = entity.posX - thePlayer.posX, diffY = entity.posY + entity.getEyeHeight() * 0.9 - (thePlayer.posY + thePlayer.getEyeHeight()), diffZ = entity.posZ - thePlayer.posZ, dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ); // @on

        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F, pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

        return new float[]{thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - thePlayer.rotationYaw), thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - thePlayer.rotationPitch)};
    }
}
