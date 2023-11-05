package cn.foodtower.module.modules.combat;


import cn.foodtower.api.EventHandler;
import cn.foodtower.api.events.World.EventAttack;
import cn.foodtower.api.events.World.EventMotionUpdate;
import cn.foodtower.api.events.World.EventMove;
import cn.foodtower.api.events.World.EventPacketSend;
import cn.foodtower.api.value.Mode;
import cn.foodtower.api.value.Numbers;
import cn.foodtower.api.value.Option;
import cn.foodtower.api.value.Value;
import cn.foodtower.manager.ModuleManager;
import cn.foodtower.module.Module;
import cn.foodtower.module.ModuleType;
import cn.foodtower.module.modules.move.Fly;
import cn.foodtower.module.modules.move.Speed;
import cn.foodtower.module.modules.world.Scaffold;
import cn.foodtower.util.entity.MoveUtils;
import cn.foodtower.util.math.MathUtil;
import cn.foodtower.util.time.TimerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;


public class Criticals extends Module {
    private static final Numbers<Double> Delay = new Numbers<>("Delay", "Delay", 0.0, 0.0, 1000.0, 1.0);
    public static Mode mode = new Mode("Mode", "mode", CritMode.values(), CritMode.Packet);
    public static Option Always = new Option("Always", "Always", false);
    public static Option C06 = new Option("C06", "C06", false);
    public static Option AutoSet = new Option("AutoSet", "AutoSet", false);
    public static Numbers<Double> motionYvalue = new Numbers<>("MotionY", 0.42, 0.01, 1.0, 0.01);
    private final TimerUtil timer = new TimerUtil();
    private final Numbers<Double> HurtTime = new Numbers<>("HurtTime", "HurtTime", 20.0D, 1.0D, 20.0D, 1.0D);
    boolean readycrit = false;
    int attacks = 0;

    public Criticals() {
        super("Criticals", new String[]{"Criticals", "crit"}, ModuleType.Combat);
        this.addValues(mode, motionYvalue, HurtTime, Delay, Always, C06, AutoSet);
        setValueDisplayable(new Value<?>[]{motionYvalue}, mode, CritMode.Motion);
    }

    public static void sendCrit(double[] value) {
        double curX = mc.thePlayer.posX;
        double curY = mc.thePlayer.posY;
        double curZ = mc.thePlayer.posZ;
        for (double offset : value) {
            if (!C06.getValue()) {
                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY + offset, curZ, false));
            } else {
                sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(curX, curY + offset, curZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
            }
        }
    }

    private boolean canCrit(EventMotionUpdate event, Entity e) {
        return (!ModuleManager.getModuleByClass(Scaffold.class).isEnabled() && (AutoSet.getValue() ? e.hurtResistantTime <= MathUtil.randomNumber(20, 12) : e.hurtResistantTime <= HurtTime.getValue()) && (event.isOnGround() || MoveUtils.isOnGround(0.001)) && !ModuleManager.getModuleByClass(Speed.class).isEnabled() && !ModuleManager.getModuleByClass(Fly.class).isEnabled()) || Always.getValue();
    }

    @EventHandler
    public void onPacketsend(EventPacketSend e) {
        if (mode.getValue().equals(CritMode.NoGround) && e.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();
            packet.onGround = false;
        }

        if (mode.getValue().equals(CritMode.Edit) && e.getPacket() instanceof C03PacketPlayer && readycrit) {
            C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();
            packet.onGround = false;
            readycrit = false;
        }

        if (mode.getValue().equals(CritMode.DCJLowHop) && e.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();

            if (mc.thePlayer.ticksExisted % 3 == 0) {
                packet.y = packet.getPositionY() + 0.001;
                packet.onGround = false;
            }
        }
    }

    @Override
    public void onEnable() {
        if (mode.getValue().equals(CritMode.NoGround)) {
            mc.thePlayer.jump();
        }
    }

    @EventHandler
    private void onAttack(EventAttack e10) {
        if (e10.isPreAttack()) {
            ++attacks;
        }
    }

    @EventHandler
    public void onMotion(EventMove e) {
    }

    @EventHandler
    private void onUpdate(EventMotionUpdate e) {
        setSuffix(mode.getValue());

        if (KillAura.currentTarget == null || KillAura.target == null) return;

        if (canCrit(e, KillAura.currentTarget)) {
            if (this.timer.hasReached(AutoSet.getValue() ? MathUtil.randomNumber(375, 50) : Delay.getValue())) {
                this.timer.reset();
                mc.thePlayer.onCriticalHit(KillAura.currentTarget);
                switch ((CritMode) mode.getValue()) {
                    case Packet:
                        sendCrit(new double[]{0.03, 0.00, 0.06, 0.00});
                        break;
                    case NCP:
                        if (attacks >= 3) {
                            sendCrit(new double[]{0.00001100134977413, 0.00000000013487744, 0.00000571003114589, 0.00000001578887744});
                            attacks = 0;
                        }
                        break;
                    case AAC4:
                        if (MoveUtils.isMoving()) return;

                        mc.thePlayer.motionX = 0;
                        mc.thePlayer.motionZ = 0;
                        mc.thePlayer.jumpMovementFactor = 0;
                        sendCrit(new double[]{3e-14, 8e-15});
                        break;
                    case DCJPacket:
                        sendCrit(new double[]{0.06 + (Math.random() * 0.001), 0.01 + (Math.random() * 0.001)});
                        break;
                    case DCJ:
                        mc.thePlayer.motionY = 0.10000000149011612;
                        mc.thePlayer.fallDistance = 0.1f;
                        mc.thePlayer.onGround = false;
                        break;
                    case DCJLowHop:
                        mc.thePlayer.jump();
                        break;
                    case Edit:
                        readycrit = true;
                        break;
                    case LowHop:
                        mc.thePlayer.motionY = 0.4001999986886975;
                        mc.thePlayer.fallDistance = 0.4f;
                        mc.thePlayer.onGround = false;
                        break;
                    case Motion:
                        if (mc.thePlayer.onGround) {
                            if (motionYvalue.get() == 0.42) {
                                mc.thePlayer.jump();
                            } else {
                                mc.thePlayer.motionY = motionYvalue.get();
                            }
                        }
                        break;
                }

            }
        }
    }

    public enum CritMode {
        Packet, NCP, AAC4, DCJ, DCJPacket, DCJLowHop, NoGround, Edit, LowHop, Motion
    }
}
