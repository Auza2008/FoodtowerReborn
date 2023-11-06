package cn.foodtower.module.modules.combat;

import cn.foodtower.Client;
import cn.foodtower.api.EventBus;
import cn.foodtower.api.EventHandler;
import cn.foodtower.api.events.Render.EventRender3D;
import cn.foodtower.api.events.World.EventAttack;
import cn.foodtower.api.events.World.EventPacket;
import cn.foodtower.api.events.World.EventPreUpdate;
import cn.foodtower.api.events.World.EventWorldChanged;
import cn.foodtower.api.value.Mode;
import cn.foodtower.api.value.Numbers;
import cn.foodtower.api.value.Option;
import cn.foodtower.manager.FriendManager;
import cn.foodtower.module.Module;
import cn.foodtower.module.ModuleType;
import cn.foodtower.module.modules.world.Teams;
import cn.foodtower.ui.notifications.user.Notifications;
import cn.foodtower.util.misc.Helper;
import cn.foodtower.util.render.RenderUtil;
import cn.foodtower.util.rotations.RotationUtils;
import cn.foodtower.util.time.TimeHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.concurrent.CopyOnWriteArrayList;

public class VanillaAura extends Module {
    public static boolean blockingStatus = false;
    public static EntityLivingBase target = null;
    private final Option rotation = new Option("Rotation", true);
    private final Numbers<Double> maxCPS = new Numbers<>("CPSMax", 20.0, 1.0, 30.0, 1.0);
    private final Numbers<Double> minCPS = new Numbers<>("CPSMin", 20.0, 1.0, 30.0, 1.0);
    private final Numbers<Double> range = new Numbers<>("Range", 10.0, 1.0, 10.0, 1.0);
    private final Numbers<Double> blockRange = new Numbers<>("BlockRange", 5.0, 1.0, 5.0, 1.0);
    private final Option autoblock = new Option("AutoBlock", true);
    private final Mode blockMode = new Mode("AutoBlockMode", BlockMode.values(), BlockMode.Both);
    private final Option esp = new Option("ESP", true);
    public CopyOnWriteArrayList<EntityLivingBase> targets = new CopyOnWriteArrayList<>();
    public Option attackPlayers = new Option("Players", true);
    public Option attackAnimals = new Option("Animals", false);
    public Option attackMobs = new Option("Mobs", false);
    public Option invisible = new Option("Invisibles", false);
    public TimeHelper attacktimer = new TimeHelper();
    private Option smart = new Option("SmartMode", false);

    public VanillaAura() {
        super("VanillaAura", new String[]{"vaura", "hvhaura", "haura"}, ModuleType.Combat);
        addValues(maxCPS, minCPS, range, blockRange, autoblock, blockMode, rotation, smart, attackPlayers, attackAnimals, attackMobs, invisible, esp);
    }

    @Override
    public void onDisable() {
        targets.clear();
        target = null;
        sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        blockingStatus = false;
    }

    @EventHandler
    private void onPacket(EventPacket e) {
        if (target == null) return;

        if (!smart.get()) return;

        if (e.getTypes() == EventPacket.Type.RECEIVE) {
            if (e.getPacket() instanceof S08PacketPlayerPosLook) {
                if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                    Helper.sendMessage("ReBlock -> Receive S08");
                    sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                }
            }
        } else if (e.getTypes() == EventPacket.Type.SEND) {
            if (e.getPacket() instanceof C07PacketPlayerDigging) {
                Helper.sendMessage("ReBlock -> Send C07");
                e.cancel();
                if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                    sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                }
            }
        }
    }

    @EventHandler
    private void onLoop(EventRender3D e) {
        if (target != null) {
            doAttack();

            if (esp.get()) {
                double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * (double) e.getPartialTicks() - RenderManager.renderPosX;
                double y = target.lastTickPosY + (target.posY - target.lastTickPosY) * (double) e.getPartialTicks() - RenderManager.renderPosY;
                double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * (double) e.getPartialTicks() - RenderManager.renderPosZ;
                double width = target.getEntityBoundingBox().maxX - target.getEntityBoundingBox().minX - 0.2;
                double height = target.getEntityBoundingBox().maxY - target.getEntityBoundingBox().minY + 0.05;
                if (target.hurtResistantTime > 0) {
                    float red = 1.0f;
                    float green = 0.0f;
                    float blue = 0.0f;
                    RenderUtil.drawEntityESP(x, y, z, width, height, red, green, blue, 0.3f);
                } else {
                    float red = 0.0f;
                    float green = 1.0f;
                    float blue = 0.0f;
                    RenderUtil.drawEntityESP(x, y, z, width, height, red, green, blue, 0.3f);
                }
            }
        }
    }

    @EventHandler
    private void onPre(EventPreUpdate e) {
        for (EntityLivingBase ent : targets) {
            if (isValidEntity(ent)) continue;
            targets.remove(ent);
        }

        getTargets();

        if (targets.size() == 0) {
            target = null;
        } else {
            try {
                target = targets.get(0);
            } catch (Exception ignored) {

            }
        }

        if (target == null && blockingStatus) {
            sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            blockingStatus = false;
        }

        if (target != null && rotation.get()) {
            float[] rotation = RotationUtils.getCustomRotation(RotationUtils.getLocation(target.getEntityBoundingBox()));
            e.setYaw(rotation[0]);
            e.setPitch(rotation[1]);
            Client.RenderRotate(rotation[0], rotation[1]);
        }
    }

    @EventHandler
    private void onWorld(EventWorldChanged e) {
        Notifications.getManager().post("KillAura", "检测到世界变更！已自动关闭KillAura");
        this.setEnabled(false);
    }

    private int randomNumber(int max, int min) {
        return (int) (Math.random() * (double) (max - min)) + min;
    }

    private void doAttack() {
        int aps = randomNumber(maxCPS.get().intValue(), minCPS.get().intValue());
        int delayValue = 1000 / aps;

        if (attacktimer.isDelayComplete(delayValue)) {
            boolean isInRange = mc.thePlayer.getDistanceToEntity(target) <= range.get();

            if (autoblock.get() && !blockMode.getValue().equals(BlockMode.AfterAttack)) {
                if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                    sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                    blockingStatus = true;
                }
            }

            if (isInRange) {
                attack();
                attacktimer.reset();
            }

            if (autoblock.get() && !blockMode.getValue().equals(BlockMode.BeforeAttack)) {
                if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                    sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                    blockingStatus = true;
                }
            }
        }
    }

    private void attack() {
        EventAttack ej = new EventAttack(target, true);
        EventBus.getInstance().register(ej);
        mc.thePlayer.swingItem();
        sendPacket(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
    }

    private void getTargets() {
        for (Entity o3 : mc.theWorld.loadedEntityList) {
            EntityLivingBase curEnt;

            if (o3 instanceof EntityLivingBase && isValidEntity(curEnt = (EntityLivingBase) o3) && !targets.contains(curEnt))
                targets.add(curEnt);

            if (targets.size() >= 1) break;
        }

        targets.sort((o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer)));
    }

    private boolean isValidEntity(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            if (entity.isDead || ((EntityLivingBase) entity).getHealth() <= 0f) {
                return false;
            }
            if (FriendManager.isFriend(entity.getName())) {
                return false;
            }
            if (mc.thePlayer.getDistanceToEntity(entity) < (range.get() + blockRange.get())) {
                if (entity != mc.thePlayer && !mc.thePlayer.isDead && !(entity instanceof EntityArmorStand || entity instanceof EntitySnowman)) {
                    if (entity instanceof EntityPlayer && attackPlayers.get()) {
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

    private boolean isBot(Entity e) {
        if (AntiBot.isServerBot(e)) {
            return true;
        } else return AntiBot.isServerBot(e);
    }

    enum BlockMode {
        BeforeAttack, AfterAttack, Both
    }
}
