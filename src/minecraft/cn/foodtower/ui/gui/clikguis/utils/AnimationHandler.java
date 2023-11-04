package cn.foodtower.ui.gui.clikguis.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;

import java.util.WeakHashMap;

public class AnimationHandler {
    final WeakHashMap<RenderChunk, AnimationData> timeStamps;

    public AnimationHandler() {
        timeStamps = new WeakHashMap<>();
    }

    public void preRender(RenderChunk renderChunk) {
        if (timeStamps.containsKey(renderChunk)) {
            AnimationData animationData = timeStamps.get(renderChunk);
            long time = animationData.timeStamp;

            if (time == -1L) {
                time = System.currentTimeMillis();

                animationData.timeStamp = time;

                BlockPos zeroedPlayerPosition = Minecraft.getMinecraft().thePlayer.getPosition();
                zeroedPlayerPosition = zeroedPlayerPosition.add(0, -zeroedPlayerPosition.getY(), 0);

                BlockPos zeroedCenteredChunkPos = renderChunk.getPosition().add(8, -renderChunk.getPosition().getY(),
                        8);

                Vec3i dif = zeroedPlayerPosition.subtract(zeroedCenteredChunkPos);

                int difX = Math.abs(dif.getX());
                int difZ = Math.abs(dif.getZ());

                EnumFacing chunkFacing;

                if (difX > difZ) {
                    if (dif.getX() > 0) {
                        chunkFacing = EnumFacing.EAST;
                    } else {
                        chunkFacing = EnumFacing.WEST;
                    }
                } else {
                    if (dif.getZ() > 0) {
                        chunkFacing = EnumFacing.SOUTH;
                    } else {
                        chunkFacing = EnumFacing.NORTH;
                    }
                }

                animationData.chunkFacing = chunkFacing;
            }

            long timeDif = System.currentTimeMillis() - time;

            int animationDuration = 1000;

            if (timeDif < animationDuration) {
                double chunkY = renderChunk.getPosition().getY();

                EnumFacing chunkFacing = animationData.chunkFacing;

                if (chunkFacing != null) {
                    Vec3i vec = chunkFacing.getDirectionVec();
                    double mod = -(200D - (200D / animationDuration * timeDif));

                    GlStateManager.translate(vec.getX() * mod, 0, vec.getZ() * mod);
                }
            } else {
                timeStamps.remove(renderChunk);
            }
        }
    }

    public void setPosition(RenderChunk renderChunk, BlockPos position) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            BlockPos zeroedPlayerPosition = Minecraft.getMinecraft().thePlayer.getPosition();
            zeroedPlayerPosition = zeroedPlayerPosition.add(0, -zeroedPlayerPosition.getY(), 0);
            BlockPos zeroedCenteredChunkPos = position.add(8, -position.getY(), 8);

            EnumFacing chunkFacing = null;

            AnimationData animationData = new AnimationData(-1L, null);
            timeStamps.put(renderChunk, animationData);
        }
    }

    private class AnimationData {
        public long timeStamp;

        public EnumFacing chunkFacing;

        public AnimationData(long timeStamp, EnumFacing chunkFacing) {
            this.timeStamp = timeStamp;
            this.chunkFacing = chunkFacing;
        }
    }
}
