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
import dev.just.util.player.TimerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

@FunctionAnnotation(name = "TriggerBot", desc = "Hedefe baktiginda otomatik vurur.", type = Type.Combat)
public class l0OI1l0I extends Function {

    private final SliderSetting cps = new SliderSetting("CPS", 12.0, 1.0, 20.0, 0.5F);
    private final SliderSetting range = new SliderSetting("Menzil", 3.5, 3.0, 6.0, 0.1F);
    private final BooleanSetting playersOnly = new BooleanSetting("Sadece Oyuncular", true);
    private final BooleanSetting weaponOnly = new BooleanSetting("Sadece Silahla", false);
    private final BooleanSetting ignoreFriends = new BooleanSetting("Dostlari Atla", true);

    private final TimerUtil attackTimer = new TimerUtil();
    private long lastAttackTime = 0L;

    public l0OI1l0I() {
        this.addSettings(new Setting[]{
            this.cps,
            this.range,
            this.playersOnly,
            this.weaponOnly,
            this.ignoreFriends
        });
    }

    @Override
    public void onEvent(Event event) {
        if (!(event instanceof EventUpdate)) return;
        if (mc == null || mc.player == null || mc.world == null) return;
        if (mc.currentScreen != null) return;

        HitResult hitResult = mc.crosshairTarget;
        if (hitResult == null || hitResult.getType() != HitResult.Type.ENTITY) return;

        EntityHitResult entityHit = (EntityHitResult) hitResult;
        Entity target = entityHit.getEntity();

        if (!isValidTarget(target)) return;

        double distance = mc.player.distanceTo(target);
        if (distance > this.range.get().doubleValue()) return;

        long currentTime = System.currentTimeMillis();
        long delay = (long) (1000.0 / this.cps.get().doubleValue());
        if (currentTime - lastAttackTime < delay) return;

        float cooldown = mc.player.getAttackCooldownProgress(0.0F);
        if (cooldown < 0.9F) return;

        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
        lastAttackTime = currentTime;
    }

    private boolean isValidTarget(Entity entity) {
        if (entity == null) return false;
        if (!(entity instanceof LivingEntity)) return false;
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

        if (Manager.FUNCTION_MANAGER.antiBot.check((LivingEntity) entity)) {
            return false;
        }

        return true;
    }
}
