package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.events.impl.player.EventAttack;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

@FunctionAnnotation(name = "Criticals", type = Type.Combat)
public class I0l1OI0l extends Function {

    private static final double CRIT_OFFSET = 0.0125;

    @Override
    public void onEvent(Event event) {
        if (!(event instanceof EventAttack)) return;
        if (mc == null || mc.player == null || mc.world == null) return;
        if (!canCrit()) return;

        double x = mc.player.getX();
        double y = mc.player.getY();
        double z = mc.player.getZ();

        mc.player.networkHandler.sendPacket(
            new PlayerMoveC2SPacket.PositionAndOnGround(x, y + CRIT_OFFSET, z, false, true));
        mc.player.networkHandler.sendPacket(
            new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false, true));
    }

    private boolean canCrit() {
        return mc.player.isOnGround()
            && !mc.player.getAbilities().flying
            && !mc.player.isTouchingWater()
            && !mc.player.isInLava();
    }
}
