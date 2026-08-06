package dev.just.modules.movement;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.move.EventMotion;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.util.move.MoveUtil;
import dev.just.util.player.InventoryUtil;
import dev.just.util.player.TimerUtil;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

@FunctionAnnotation(name = "Flight", desc = "Havada ucmanizi saglar.", type = Type.Move)
public class Flight extends Function {

    private final ModeSetting mode = new ModeSetting("Tur", "Hareket", "Hareket", "ElytraRW-Eski");
    private final SliderSetting xspeed = new SliderSetting("X - Hizi", 1.0, 0.0, 5.0, 0.1F);
    private final SliderSetting yspeed = new SliderSetting("Y - Hizi", 1.0, 0.0, 5.0, 0.1F);
    private final TimerUtil timerUtil = new TimerUtil();
    private final TimerUtil swapTimer = new TimerUtil();
    private int item = -1;

    private static final int SLOT_CHEST = 6;
    private static final int SWAP_DELAY = 520;

    public Flight() {
        this.addSettings(new Setting[]{this.mode, this.xspeed, this.yspeed});
    }

    @Override
    public void onEvent(Event event) {
        if (mc == null || mc.player == null || mc.world == null) return;

        if (this.mode.is("Hareket") && event instanceof EventMotion) {
            handleMotionFlight();
        } else if (this.mode.is("ElytraRW-Eski") && event instanceof EventUpdate) {
            handleElytraFlight();
        }
    }

    private void handleMotionFlight() {
        // Y velocity (up/down)
        double yVel = 0.0;
        if (mc.options.jumpKey.isPressed()) {
            yVel = this.yspeed.get().floatValue();
        } else if (mc.options.sneakKey.isPressed()) {
            yVel = -this.yspeed.get().floatValue();
        }

        // Apply velocity
        mc.player.setVelocity(0.0, yVel, 0.0);

        // Horizontal motion - apply when moving (W/A/S/D keys)
        if (MoveUtil.isMoving()) {
            double speed = this.xspeed.get().floatValue();
            MoveUtil.setMotion(speed);
        }
    }

    private void handleElytraFlight() {
        int elytraSlot = findElytraSlot();

        if (elytraSlot >= 0 && canStartElytra()) {
            executeElytraSequence(elytraSlot);
        }

        // Auto firework
        if (mc.player.isGliding()) {
            InventoryUtil.inventorySwapClick2(Items.FIREWORK_ROCKET, true, false);
        }
    }

    private int findElytraSlot() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).isOf(Items.ELYTRA)) {
                return i;
            }
        }
        return -1;
    }

    private boolean canStartElytra() {
        return !mc.player.isOnGround()
            && !mc.player.isSubmergedInWater()
            && !mc.player.isInLava()
            && !mc.player.isGliding();
    }

    private void executeElytraSequence(int slot) {
        if (!this.timerUtil.hasTimeElapsed(SWAP_DELAY)) return;

        this.swapTimer.reset();
        InventoryUtil.swapSlotsUniversal(SLOT_CHEST, slot, false, false);

        mc.getNetworkHandler().sendPacket(
            new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        mc.player.startGliding();

        InventoryUtil.swapSlotsUniversal(SLOT_CHEST, slot, false, false);
        this.item = slot;
        this.timerUtil.reset();
    }
}
