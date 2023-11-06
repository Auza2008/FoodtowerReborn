package cn.foodtower.util.rotations;

import net.minecraft.util.Vec3;

public class VecRotation {
    final Vec3 vec3;
    final float[] rotation;

    public VecRotation(Vec3 Vec3, float[] Rotation) {
        vec3 = Vec3;
        rotation = Rotation;
    }

    public Vec3 getVec3() {
        return vec3;
    }

    public float[] getRotation() {
        return rotation;
    }

    public Vec3 getVec() {
        return vec3;
    }
}
