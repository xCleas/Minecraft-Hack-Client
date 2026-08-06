package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.TextSetting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "NameProtect",
   desc = "T3l1bmN1IGlzbWluaSB2ZSBhcmthZGHFnyBpc2ltbGVyaW5pIGdpemxlcg==",
   type = Type.Misc
)
public class NameProtect extends Function {
   public final TextSetting text = new TextSetting(I0O1l0I1.b("WWVuaSDEsHNpbQ=="), "JustPlayer");
   public final BooleanSetting friend = new BooleanSetting(I0O1l0I1.b("QXJrYWRhxZ9sYXLEsSBHaXpsZQ=="), true);

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public NameProtect() {
      this.addSettings(new Setting[]{this.text, this.friend});
   }

   public String getCustomName() {
      int _s = O1lI0O1l.next(hashCode() ^ 0x7F3A, 5);
      String result = null;

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (Manager.FUNCTION_MANAGER.nameProtect.state) {
                  result = this.text.getValue().replaceAll("&", "\u00a7");
               } else {
                  result = mc.getGameProfile().getName();
               }
               _s = 4;
               break;

            case 1:
               if (l1O0I1lO.opaqueFalse()) {
                  result = "FAKE_NAME_" + entropy;
               }
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(result, entropy);
               }
               _s = 4;
               break;

            case 3:
               l1O0I1lO.fakeHandler();
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
      int _s = O1lI0O1l.next(hashCode() ^ 0x4B2E, 6);
      String result = originalName;

      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
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
               if (I1lO0l1I.and(this.friend.get(), Manager.FRIEND_MANAGER.isFriend(originalName))) {
                  result = applyFormattingInternal(this.text.getValue());
               } else {
                  result = originalName;
               }
               _s = 5;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= originalName.hashCode();
               }
               _s = 5;
               break;

            case 4:
               l1O0I1lO.fakeBranch(originalName, entropy);
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
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return name + entropy;
      }
      return name.replace('&', '\u00a7');
   }

   private int isSelfFlag(String name) {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.bool(entropy > FAKE_STATE);
      }
      return lO1I0l1O.bool(name.equals(mc.getSession().getUsername()));
   }

   private boolean isSelfInternal(String name) {
      return lO1I0l1O.unbool(isSelfFlag(name));
   }

   @Override
   public void onEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode(), 5);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 4;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 5);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy = lO1I0l1O.c(entropy, event.hashCode());
               }
               _s = 4;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 4;
               break;

            case 3:
               l1O0I1lO.fakeHandler();
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
