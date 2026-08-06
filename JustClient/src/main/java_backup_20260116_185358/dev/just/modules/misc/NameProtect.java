package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.TextSetting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.LogicSplit;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "NameProtect",
   desc = "T3l1bmN1IGlzbWluaSB2ZSBhcmthZGHFnyBpc2ltbGVyaW5pIGdpemxlcg==",
   type = Type.Misc
)
public class NameProtect extends Function {
   public final TextSetting text = new TextSetting(Strings.b("WWVuaSDEsHNpbQ=="), "JustPlayer");
   public final BooleanSetting friend = new BooleanSetting(Strings.b("QXJrYWRhxZ9sYXLEsSBHaXpsZQ=="), true);

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public NameProtect() {
      this.addSettings(new Setting[]{this.text, this.friend});
   }

   public String getCustomName() {
      int _s = ControlFlow.next(hashCode() ^ 0x7F3A, 5);
      String result = null;

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (Manager.FUNCTION_MANAGER.nameProtect.state) {
                  result = this.text.getValue().replaceAll("&", "\u00a7");
               } else {
                  result = mc.getGameProfile().getName();
               }
               _s = 4;
               break;

            case 1:
               if (FlowObfuscator.opaqueFalse()) {
                  result = "FAKE_NAME_" + entropy;
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(result, entropy);
               }
               _s = 4;
               break;

            case 3:
               FlowObfuscator.fakeHandler();
               _s = 4;
               break;

            case 4:
               return result;

            default:
               _s = 4;
               break;
         }
      }
   }

   public String getProtectedName(String originalName) {
      int _s = ControlFlow.next(hashCode() ^ 0x4B2E, 6);
      String result = originalName;

      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (!Manager.FUNCTION_MANAGER.nameProtect.state) {
                  result = originalName;
                  _s = 5;
                  break;
               }
               _s = 1;
               break;

            case 1:
               if (isSelfInternal(originalName)) {
                  result = applyFormattingInternal(this.text.getValue());
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (LogicSplit.and(this.friend.get(), Manager.FRIEND_MANAGER.isFriend(originalName))) {
                  result = applyFormattingInternal(this.text.getValue());
               } else {
                  result = originalName;
               }
               _s = 5;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= originalName.hashCode();
               }
               _s = 5;
               break;

            case 4:
               FlowObfuscator.fakeBranch(originalName, entropy);
               _s = 5;
               break;

            case 5:
               return result;

            default:
               _s = 5;
               break;
         }
      }
   }

   private String applyFormattingInternal(String name) {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return name + entropy;
      }
      return name.replace('&', '\u00a7');
   }

   private int isSelfFlag(String name) {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.bool(entropy > FAKE_STATE);
      }
      return NumberGuard.bool(name.equals(mc.getSession().getUsername()));
   }

   private boolean isSelfInternal(String name) {
      return NumberGuard.unbool(isSelfFlag(name));
   }

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 5);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 4;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 5);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy = NumberGuard.c(entropy, event.hashCode());
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(event, entropy);
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
