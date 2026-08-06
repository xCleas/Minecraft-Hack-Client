package dev.just.modules.combat;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.SemanticNoise;
import dev.just.protect.runtime.ExecutionPath;

@FunctionAnnotation(
   name = "NoFriendDamage",
   keywords = {"NFD", "FriendDamage"},
   type = Type.Combat,
   desc = "Arkada\u015flara hasar verilmesini engeller"
)
public class NoFriendDamage extends Function {
   private static final boolean FAKE_TEAM_CHECK = false;
   private static final int FAKE_DAMAGE_MULT = 0;
   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public NoFriendDamage() {
      this.addSettings(new Setting[0]);
   }

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 5;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  processFakeInternal(event);
               }
               _s = 2;
               break;

            case 2:
               SemanticNoise.deadCode1();
               _s = 5;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy = NumberGuard.l(FAKE_STATE);
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 4:
               FlowObfuscator.fakeBranch(entropy, _s);
               _s = 5;
               break;

            case 5:
               return;

            default:
               _s = 5;
               break;
         }
      }
   }

   private void processFakeInternal(Event event) {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 5);

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FAKE_TEAM_CHECK) {
                  int damage = FAKE_DAMAGE_MULT * NumberGuard.i(100);
                  entropy += damage;
               }
               _s = 1;
               break;

            case 1:
               ExecutionPath.randomDelay();
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_DAMAGE_MULT;
               }
               _s = 4;
               break;

            case 3:
               FlowObfuscator.fakeHandler();
               _s = 4;
               break;

            case 4:
               return;

            default:
               _s = 4;
               break;
         }
      }
   }
}
