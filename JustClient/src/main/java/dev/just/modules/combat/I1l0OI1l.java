package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@FunctionAnnotation(name = "AimAssist", desc = "Hedefe nisanlamanizi kolaylastirir.", type = Type.Combat)
public class I1l0OI1l extends Function {

    private final SliderSetting range = new SliderSetting("Menzil", 4.5, 1.0, 8.0, 0.1F);
    private final SliderSetting fov = new SliderSetting("FOV", 90.0, 10.0, 180.0, 5.0F);
    private final SliderSetting speed = new SliderSetting("Hiz", 5.0, 1.0, 20.0, 0.5F);
    private final SliderSetting aimHeight = new SliderSetting("Hedef Yuksekligi", 0.5, 0.0, 1.0, 0.05F);
    private final BooleanSetting playersOnly = new BooleanSetting("Sadece Oyuncular", true);
    private final BooleanSetting ignoreFriends = new BooleanSetting("Dostlari Atla", true);
    private final BooleanSetting onlyOnClick = new BooleanSetting("Sadece Tiklaninca", false);
    private final BooleanSetting verticalAim = new BooleanSetting("Dikey Yardim", true);
    private final BooleanSetting smoothing = new BooleanSetting("Yumusak", true);

    public I1l0OI1l() {
        this.addSettings(new Setting[]{
            this.range,
            this.fov,
            this.speed,
            this.aimHeight,
            this.playersOnly,
            this.ignoreFriends,
            this.onlyOnClick,
            this.verticalAim,
            this.smoothing
        });
    }

    @Override
    public void onEvent(Event event) {
        if (!(event instanceof EventUpdate)) return;
        if (mc == null || mc.player == null || mc.world == null) return;
        if (mc.currentScreen != null) return;

        if (this.onlyOnClick.get() && !mc.options.attackKey.isPressed()) {
            return;
        }

        LivingEntity target = findTarget();
        if (target == null) return;

        Vec3d targetPos = target.getPos().add(0, target.getHeight() * this.aimHeight.get().doubleValue(), 0);
        Vec3d eyePos = mc.player.getEyePos();

        double dx = targetPos.x - eyePos.x;
        double dy = targetPos.y - eyePos.y;
        double dz = targetPos.z - eyePos.z;

        double horizontalDist = Math.sqrt(dx * dx + dz * dz);
        float targetYaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
        float targetPitch = (float) -Math.toDegrees(Math.atan2(dy, horizontalDist));

        float yawDiff = MathHelper.wrapDegrees(targetYaw - mc.player.getYaw());
        if (Math.abs(yawDiff) > this.fov.get().floatValue() / 2.0F) {
            return;
        }

        float speedFactor = this.speed.get().floatValue() / 20.0F;

        if (this.smoothing.get()) {
            float yawStep = yawDiff * speedFactor;
            mc.player.setYaw(mc.player.getYaw() + yawStep);

            if (this.verticalAim.get()) {
                float pitchDiff = targetPitch - mc.player.getPitch();
                float pitchStep = pitchDiff * speedFactor;
                mc.player.setPitch(MathHelper.clamp(mc.player.getPitch() + pitchStep, -90.0F, 90.0F));
            }
        } else {
            float maxYawStep = this.speed.get().floatValue();
            float yawStep = MathHelper.clamp(yawDiff, -maxYawStep, maxYawStep);
            mc.player.setYaw(mc.player.getYaw() + yawStep);

            if (this.verticalAim.get()) {
                float pitchDiff = targetPitch - mc.player.getPitch();
                float pitchStep = MathHelper.clamp(pitchDiff, -maxYawStep, maxYawStep);
                mc.player.setPitch(MathHelper.clamp(mc.player.getPitch() + pitchStep, -90.0F, 90.0F));
            }
        }
    }

    private LivingEntity findTarget() {
        double maxRange = this.range.get().doubleValue();

        List<LivingEntity> targets = StreamSupport.stream(mc.world.getEntities().spliterator(), false)
            .filter(e -> e instanceof LivingEntity)
            .map(e -> (LivingEntity) e)
            .filter(this::isValidTarget)
            .filter(e -> mc.player.distanceTo(e) <= maxRange)
            .sorted(Comparator.comparingDouble(e -> getAngleDifference(e)))
            .collect(Collectors.toList());

        return targets.isEmpty() ? null : targets.get(0);
    }

    private boolean isValidTarget(LivingEntity entity) {
        if (entity == null) return false;
        if (entity instanceof ArmorStandEntity) return false;
        if (!entity.isAlive()) return false;
        if (entity == mc.player) return false;

        if (this.playersOnly.get() && !(entity instanceof PlayerEntity)) {
            return false;
        }

        if (this.ignoreFriends.get() && entity instanceof PlayerEntity) {
            if (Manager.FRIEND_MANAGER.isFriend(entity.getName().getString())) {
                return false;
            }
        }

        if (Manager.FUNCTION_MANAGER.antiBot.check(entity)) {
            return false;
        }

        return true;
    }

    private double getAngleDifference(LivingEntity entity) {
        Vec3d targetPos = entity.getPos().add(0, entity.getHeight() * 0.5, 0);
        Vec3d eyePos = mc.player.getEyePos();

        double dx = targetPos.x - eyePos.x;
        double dz = targetPos.z - eyePos.z;

        float targetYaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
        float yawDiff = MathHelper.wrapDegrees(targetYaw - mc.player.getYaw());

        return Math.abs(yawDiff);
    }
}
