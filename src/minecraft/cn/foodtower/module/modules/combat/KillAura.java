package cn.foodtower.module.modules.combat;


import cn.foodtower.Client;
import cn.foodtower.api.EventBus;
import cn.foodtower.api.EventHandler;
import cn.foodtower.api.events.Render.EventRender2D;
import cn.foodtower.api.events.World.*;
import cn.foodtower.api.value.Mode;
import cn.foodtower.api.value.Numbers;
import cn.foodtower.api.value.Option;
import cn.foodtower.manager.FriendManager;
import cn.foodtower.manager.ModuleManager;
import cn.foodtower.module.Module;
import cn.foodtower.module.ModuleType;
import cn.foodtower.module.modules.world.Teams;
import cn.foodtower.ui.notifications.user.Notifications;
import cn.foodtower.util.RayTraceUtil2;
import cn.foodtower.util.math.RotationUtil;
import cn.foodtower.util.rotations.RotationUtils;
import cn.foodtower.util.time.TimeHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import viamcp.utils.AttackOrder;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class KillAura extends Module {
    public static Option autoBlock = new Option("AutoBlock", true);
    public static Numbers<Double> reach = new Numbers<>("Range", 4.2, 3.0, 10.0, 0.1);
    public static Numbers<Double> switchsize = new Numbers<>("MaxTargets", 1.0, 1.0, 5.0, 1.0);
    public static Option attackPlayers = new Option("Players", true);
    public static Option attackAnimals = new Option("Animals", false);
    public static Option attackMobs = new Option("Mobs", false);
    public static Option invisible = new Option("Invisibles", false);
    public static boolean blockingStatus = false;
    public static boolean fakeblockstatus = false;
    // Utils
    public static CopyOnWriteArrayList<EntityLivingBase> targets = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<EntityLivingBase> attacked = new CopyOnWriteArrayList<>();
    public static EntityLivingBase curBot = null;
    public static EntityLivingBase currentTarget;
    public static EntityLivingBase target = null;
    public static EntityLivingBase needHitBot = null;
    public final Option toggleWhenDeadValue = new Option("DisableOnDeath", true);
    private final Mode attackMode = new Mode("AttackTiming", AttackMode.values(), AttackMode.Pre);
    private final Mode blockTiming = new Mode("BlockTiming", BlockTime.values(), BlockTime.Pre);
    public Mode priority = new Mode("Priority", Prioritymode.values(), Prioritymode.Range);
    public Mode rotMode = new Mode("RotationMode", Rotationmode.values(), Rotationmode.Hypixel);
    public Numbers<Double> hurttime = new Numbers<>("HurtTime", 10.0, 1.0, 10.0, 1.0);
    public Numbers<Double> mistake = new Numbers<>("Mistakes", 0.0, 0.0, 100.0, 1d);
    public Mode blockMode = new Mode("BlockMode", BlockMode.values(), BlockMode.Packet);
    public Numbers<Double> blockReach = new Numbers<>("BlockRange", 0.5, 0.0, 3.0, 0.1);
    public Numbers<Double> cpsMax = new Numbers<>("CPSMax", 10.0, 1.0, 20.0, 1.0);
    public Numbers<Double> cpsMin = new Numbers<>("CPSMin", 8.0, 1.0, 20.0, 1.0);
    public Numbers<Double> switchDelay = new Numbers<>("SwitchDelay", 50d, 0d, 2000d, 10d);
    public Numbers<Double> yawDiff = new Numbers<>("YawDifference", 15.0, 5.0, 90.0, 1.0);
    public Option throughblock = new Option("ThroughBlock", true);
    public Option rotations = new Option("HeadRotations", true);
    public Option RayCast = new Option("RayCast", true);
    public Random random = new Random();
    public boolean needBlock = false;
    public int index;
    // TimeHelper
    public TimeHelper switchTimer = new TimeHelper();
    public TimeHelper attacktimer = new TimeHelper();
    // תͷ
    AxisAlignedBB axisAlignedBB;
    float shouldAddYaw;
    float[] lastRotation = new float[]{0f, 0f};
    float curY = ScaledResolution.getScaledHeight();
    boolean cantickblock = false;
    private final Option forceUpdate = new Option("ForceUpdate", false);
    private final TimeHelper updateTimer = new TimeHelper();
    private float rotationYawHead;
    private float yaw;
    private float pitch;

    public KillAura() {
        super("KillAura", new String[]{"ka"}, ModuleType.Combat);
        addValues(rotMode, priority, blockMode, cpsMax, cpsMin, attackMode, blockTiming, switchDelay, reach, blockReach, hurttime, mistake, yawDiff, switchsize, rotations, RayCast, autoBlock, throughblock, forceUpdate, attackPlayers, attackAnimals, attackMobs, invisible, toggleWhenDeadValue);
        attacked = new CopyOnWriteArrayList<>();
    }

    public static float[] getLoserRotation(Entity target) {
        double xDiff = target.posX - mc.thePlayer.posX;
        double yDiff = target.posY - mc.thePlayer.posY - 0.4;
        double zDiff = target.posZ - mc.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) ((-Math.atan2(yDiff, dist)) * 180.0 / 3.141592653589793);
        float[] array = new float[2];
        int n = 0;
        float rotationYaw = mc.thePlayer.rotationYaw;
        array[n] = rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);
        int n3 = 1;
        float rotationPitch = mc.thePlayer.rotationPitch;
        array[n3] = rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch);
        return array;
    }

    public static float[] getRotations(EntityLivingBase currentTarget2) {
        if (currentTarget2 == null) {
            return null;
        }
        double diffX = currentTarget2.posX - mc.thePlayer.posX;
        double diffZ = currentTarget2.posZ - mc.thePlayer.posZ;
        double diffY = currentTarget2.posY - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) ((-Math.atan2(diffY, dist)) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    @EventHandler
    public void targetHud(EventRender2D event) {
        String str;
        if (switchsize.get() <= 1) {
            str = "Single";
        } else {
            str = "Switch";
        }
        setSuffix(str);
    }

    @EventHandler
    public void Event(EventWorldChanged e) {
        if (toggleWhenDeadValue.get()) {
            Notifications.getManager().post("KillAura", "检测到世界变更！已自动关闭KillAura");
            this.setEnabled(false);
        }
    }

    public boolean getBlockingStatus() {
        return blockingStatus;
    }

//    @EventHandler
//    public void onRender(EventRender3D render) { // Copy
//        if (target == null || !esp.get()) {
//            return;
//        }
//
//        if (KillAura.currentTarget == null) return;
//        double x = KillAura.currentTarget.lastTickPosX + (KillAura.currentTarget.posX - KillAura.currentTarget.lastTickPosX) * (double) render.getPartialTicks() - RenderManager.renderPosX;
//        double y = KillAura.currentTarget.lastTickPosY + (KillAura.currentTarget.posY - KillAura.currentTarget.lastTickPosY) * (double) render.getPartialTicks() - RenderManager.renderPosY;
//        double z = KillAura.currentTarget.lastTickPosZ + (KillAura.currentTarget.posZ - KillAura.currentTarget.lastTickPosZ) * (double) render.getPartialTicks() - RenderManager.renderPosZ;
//        double width = KillAura.currentTarget.getEntityBoundingBox().maxX - KillAura.currentTarget.getEntityBoundingBox().minX - 0.2;
//        double height = KillAura.currentTarget.getEntityBoundingBox().maxY - KillAura.currentTarget.getEntityBoundingBox().minY + 0.05;
//        if (KillAura.currentTarget.hurtTime > 4) {
//            float red = 1.0f;
//            float green = 0.0f;
//            float blue = 0.0f;
//            RenderUtil.drawEntityESP(x, y, z, width, height, red, green, blue, 0.3f, red, green, blue, 0.3f, 1);
//        } else {
//            float red = 0.0f;
//            float green = 1.0f;
//            float blue = 0.0f;
//            RenderUtil.drawEntityESP(x, y, z, width, height, red, green, blue, 0.3f, red, green, blue, 0.3f, 1);
//        }
//    }

    @EventHandler
    public void onPre(EventPreUpdate event) {
        if (target == null) {
            Client.Pitch = 0;
        }

        RayTraceUtil2 rayCastUtil;
        rotationYawHead = mc.thePlayer.rotationYawHead;
        needHitBot = null;

        if (!targets.isEmpty() && index >= targets.size()) index = 0; // ����Switch����

        for (EntityLivingBase ent : targets) {
            if (isValidEntity(ent)) continue;
            targets.remove(ent);
        }
        // Switch����

        getTarget(event); // ��ʵ��

        if (targets.size() == 0) { // ʵ������Ϊ0ֹͣ����
            target = null;
        } else {
            try {
                target = targets.get(index);// ���ù�����Target
                axisAlignedBB = null;
                if (mc.thePlayer.getDistanceToEntity(target) > reach.get()) {
                    target = targets.get(0);
                }
            } catch (Exception ignored) {

            }
        }
        if (ModuleManager.getModuleByName("Scaffold").isEnabled()) {
            target = null;
            return;
        }
        if (this.RayCast.get() && target != null && (rayCastUtil = new RayTraceUtil2(target)).getEntity() != target) {
            curBot = rayCastUtil.getEntity();
        }
        if (target != null) {
            // Switch��ʼ
            if (target.hurtTime == 10 && switchTimer.isDelayComplete(switchDelay.get().longValue()) && targets.size() > 1) {
                switchTimer.reset();
                ++index;
            }

            if (rotations.get()) { // Ťͷ
                switch ((Rotationmode) rotMode.get()) {
                    case Hypixel: {
                        float[] rotation = RotationUtils.getFluxRotations(target, reach.get() + blockReach.get());
                        event.setYaw(rotation[0]);
                        event.setPitch(rotation[1]);
                        rotationYawHead = event.getYaw();
                        Client.RenderRotate(rotation[0], rotation[1]);
                        break;
                    }
                    case Viro: {
                        float[] rotations = getRotations(target);
                        event.setYaw(rotations[0]);
                        event.setPitch(rotations[1]);
                        rotationYawHead = event.getYaw();
                        Client.RenderRotate(rotations[0], rotations[1]);
                        break;
                    }
                    case FootClick: {
                        float[] rotations = getLoserRotation(target);
                        event.setYaw(rotations[0]);
                        event.setPitch(rotations[1]);
                        rotationYawHead = event.getYaw();
                        Client.RenderRotate(rotations[0], rotations[1]);
                        break;
                    }
                }
            }

            if (forceUpdate.get()) {
                if (updateTimer.isDelayComplete(56L) && this.forceUpdate.getValue() && !mc.thePlayer.isMoving()) {
                    mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
                    updateTimer.reset();
                }
            }

            if (attackMode.get().equals(AttackMode.Pre)) {
                doAttack();
            }

            if (blockTiming.get().equals(BlockTime.Pre)) {
                if ((mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && autoBlock.get() || mc.thePlayer.isBlocking()) && (!blockingStatus || blockMode.get().equals(BlockMode.DCJ) || blockMode.get().equals(BlockMode.Vanilla))) { // ��
                    doBlock();
                }
            }
        } else { // ûʵ��
            lastRotation[0] = mc.thePlayer.rotationYaw;
            targets.clear();
            if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && autoBlock.get() && (blockingStatus || fakeblockstatus)) {
                unBlock();
            }
        }
    }

    @EventHandler
    private void onPost(EventPostUpdate e) {
        if (target != null) {
            if (attackMode.get().equals(AttackMode.Post)) {
                doAttack();
            }

            if (blockTiming.get().equals(BlockTime.Post)) {
                if ((mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && autoBlock.get() || mc.thePlayer.isBlocking()) && (!blockingStatus || blockMode.get().equals(BlockMode.DCJ) || blockMode.get().equals(BlockMode.Vanilla))) { // ��
                    doBlock();
                }
            }
        }
    }

    @EventHandler
    private void onUpdate(EventMotionUpdate e) {
        if (target != null) {
            if (attackMode.get().equals(AttackMode.All)) {
                doAttack();

            }

            if (blockTiming.get().equals(BlockTime.All)) {
                if ((mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && autoBlock.get() || mc.thePlayer.isBlocking()) && (!blockingStatus || blockMode.get().equals(BlockMode.DCJ) || blockMode.get().equals(BlockMode.Vanilla))) { // ��
                    doBlock();
                }
            }
        }
    }

    private void doBlock() {
        if (!blockMode.get().equals(BlockMode.Fake)) {
            switch ((BlockMode) blockMode.get()) {
                case Vanilla:
                    sendPacket(new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 255, mc.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
                    blockingStatus = true;
                    break;
                case DCJ:
                    final EntityPlayerSP thePlayer = mc.thePlayer;
                    final ItemStack item = thePlayer.inventory.getCurrentItem();
                    thePlayer.setItemInUse(item, item.getMaxItemUseDuration());
                    sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, item, 0, 0, 0));
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                    blockingStatus = true;
                    break;
                case Tick:
                    if (cantickblock && currentTarget != null) {
                        blockingStatus = true;
                        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                        cantickblock = false;
                    }
                    break;
                case Packet:
                    sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                    blockingStatus = true;
                    break;
            }
        }

        // needUnBlock = true;
        if (blockMode.get() == BlockMode.Fake) {
            fakeblockstatus = true;
        }
    }

    private void unBlock() {
        if (!blockMode.get().equals(BlockMode.Fake)) {
            switch ((BlockMode) blockMode.get()) {
                case DCJ:
                    sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    mc.thePlayer.stopUsingItem();
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                    blockingStatus = false;
                    break;
                case Tick:
                    sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    blockingStatus = false;
                    cantickblock = false;
                    break;
                default:
                    sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    blockingStatus = false;
                    break;
            }
        }

        // needUnBlock = false;
        if (blockMode.get() == BlockMode.Fake) {
            fakeblockstatus = false;
        }
    }

    private int randomNumber(int max, int min) {
        return (int) (Math.random() * (double) (max - min)) + min;
    }

    private void doAttack() {
        int aps = randomNumber(cpsMax.get().intValue(), cpsMin.get().intValue());
        int delayValue = 1000 / aps;

        if (attacktimer.isDelayComplete(delayValue)) { // ����Timer
            boolean miss = false;
            boolean isInRange = mc.thePlayer.getDistanceToEntity(target) <= reach.get();

            if (isInRange) {
                attacktimer.reset();
                if (target.hurtTime > hurttime.get() || // Hurttime
                        random.nextInt(100) < mistake.get().intValue() // ���Mistakes
                ) miss = true;

                float diff = Math.abs(Math.abs(MathHelper.wrapAngleTo180_float(rotationYawHead)) - Math.abs(MathHelper.wrapAngleTo180_float(RotationUtil.getRotations(target)[0])));

                if (diff > yawDiff.get() && !ModuleManager.getModuleByName("Scaffold").isEnabled()) {
                    miss = true;
                }
            }

            if (mc.thePlayer.isBlocking() || mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && autoBlock.get()) { // ��
                if (!blockMode.get().equals(BlockMode.Vanilla) && !blockMode.get().equals(BlockMode.DCJ)) {
                    unBlock();
                }
            }

            if (isInRange) {
                attack(miss); // ��������miss
            }
            // needBlattackock = true;
        }
    }

    private void attack(boolean mistake) {
        currentTarget = ((KillAura.curBot != null) ? KillAura.curBot : KillAura.target);
        if (!mistake) {
            needBlock = true; // ȷ����
            CopyOnWriteArrayList<EntityLivingBase> list = new CopyOnWriteArrayList<>();
            for (Entity entity : mc.theWorld.loadedEntityList) {
                float diff = Math.abs(Math.abs(MathHelper.wrapAngleTo180_float(rotationYawHead)) - Math.abs(MathHelper.wrapAngleTo180_float(RotationUtil.getRotations(entity)[0])));

                if (entity instanceof EntityZombie && entity.isInvisible() && (diff < yawDiff.get() || mc.thePlayer.getDistanceToEntity(target) < 1) && mc.thePlayer.getDistanceToEntity(entity) < reach.get()) {
                    list.add((EntityLivingBase) entity);
                }
            }
            if (list.size() == 0) list.add(target);
            needHitBot = list.get(random.nextInt(list.size()));

            EventAttack ej = new EventAttack(currentTarget, true);
            EventBus.getInstance().register(ej);
            AttackOrder.sendFixedAttack(mc.thePlayer, currentTarget);

            if (!attacked.contains(target) && target instanceof EntityPlayer) {
                attacked.add(target);
            }
            needHitBot = null;
            curBot = null;
        } else {
            mc.thePlayer.swingItem();
        }
    }

    private void getTarget(EventPreUpdate event) {
        int maxSize = switchsize.get().intValue();
        for (Entity o3 : mc.theWorld.loadedEntityList) {
            EntityLivingBase curEnt;

            if (o3 instanceof EntityLivingBase && isValidEntity(curEnt = (EntityLivingBase) o3) && !targets.contains(curEnt))
                targets.add(curEnt);

            if (targets.size() >= maxSize) break;
        }

        if (priority.get().equals(Prioritymode.Range))
            targets.sort((o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer)));

        if (priority.get().equals(Prioritymode.Fov))
            targets.sort(Comparator.comparingDouble(o -> RotationUtil.getDistanceBetweenAngles(mc.thePlayer.rotationPitch, RotationUtil.getRotations(o)[0])));

        if (priority.get().equals(Prioritymode.Angle)) {
            targets.sort((o1, o2) -> {
                float[] rot1 = RotationUtil.getRotations(o1);
                float[] rot2 = RotationUtil.getRotations(o2);
                return (int) (mc.thePlayer.rotationYaw - rot1[0] - (mc.thePlayer.rotationYaw - rot2[0]));
            });
        }

    }

    @EventHandler
    private void onPacket(EventPacket e) { // No Rotate
        // if (e.getPacket() instanceof S08PacketPlayerPosLook) {
        //       S08PacketPlayerPosLook look = (S08PacketPlayerPosLook) e.getPacket();
        //    look.yaw = (mc.thePlayer.rotationYaw);
        //    look.pitch = (mc.thePlayer.rotationPitch);
        //}
        if (e.getPacket() instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity) e.getPacket()).getEntityID() == mc.thePlayer.getEntityId()) {
            cantickblock = true;
        }

    }

    private boolean isValidEntity(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            if (entity.isDead || ((EntityLivingBase) entity).getHealth() <= 0f) {
                return false;
            }
            if (FriendManager.isFriend(entity.getName())) {
                return false;
            }
            if (mc.thePlayer.getDistanceToEntity(entity) < (reach.get() + blockReach.get())) {
                if (entity != mc.thePlayer && !mc.thePlayer.isDead && !(entity instanceof EntityArmorStand || entity instanceof EntitySnowman)) {

                    if (entity instanceof EntityPlayer && attackPlayers.get()) {
                        if (entity.ticksExisted < 30) return false;

                        if (!mc.thePlayer.canEntityBeSeen(entity) && !throughblock.get()) return false;

                        if (entity.isInvisible() && !invisible.get()) return false;

                        return !isBot(entity) && !Teams.isOnSameTeam(entity);
                    }

                    if (entity instanceof EntityMob && attackMobs.get()) {
                        return !isBot(entity);
                    }

                    if ((entity instanceof EntityAnimal || entity instanceof EntityVillager) && attackAnimals.get()) {
                        return !isBot(entity);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onEnable() {
        cantickblock = false;
        curY = ScaledResolution.getScaledHeight();
        shouldAddYaw = 0;
        attacked = new CopyOnWriteArrayList<>();
        axisAlignedBB = null;
        index = 0; // Switch Targetָ��
        super.onEnable();
    }

    @Override
    public void onDisable() {
//        if(mc.thePlayer.isBlocking() && (Boolean) autoBlock.get() && blockMode.get().equals(BlockMode.Legit)) {
//            this.stopBlocking();
//        }
        cantickblock = false;
        curY = ScaledResolution.getScaledHeight();
        axisAlignedBB = null;
        if (mc.thePlayer != null) {
            lastRotation[0] = mc.thePlayer.rotationYaw;
        }

        targets.clear();
        target = null;
        currentTarget = null;

        unBlock();

        super.onDisable();
    }

    private boolean isBot(Entity e) {
        if (AntiBot.isServerBot(e)) {
            return true;
        } else return AntiBot.isServerBot(e);
    }

    enum Rotationmode {
        Hypixel, Viro, FootClick
    }

    enum Prioritymode {
        Range, Angle, Fov
    }

    enum BlockMode {
        Fake, Vanilla, DCJ, Tick, Packet
    }

    enum AttackMode {
        Pre, Post, All
    }

    enum BlockTime {
        Pre, Post, All
    }
}