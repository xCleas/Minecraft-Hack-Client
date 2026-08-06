package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.I1lO0l1I;
import java.util.Arrays;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "ClientSounds",
   desc = "Q2xpZW50IGnDp2kgw7Z6ZWwgc2VzIGVmZWt0bGVyaS4=",
   type = Type.Misc
)
public class ClientSounds extends Function {
   public final MultiSetting check = new MultiSetting(
      I0O1l0I1.b("RXRraW4gT2xheWxhcg=="), Arrays.asList(I0O1l0I1.b("Q2xpZW50IEdpcmnFn2k="), I0O1l0I1.b("TW9kw7xsIEHDpy9LYXBh")), new String[]{I0O1l0I1.b("Q2xpZW50IEdpcmnFn2k="), I0O1l0I1.b("TW9kw7xsIEHDpy9LYXBh")}
   );
   public final ModeSetting mode = new ModeSetting(I0O1l0I1.b("U2VzIFRpcGk="), "Tip-1", "Tip-1", "Tip-2", "Tip-3", "Tip-4");
   public final SliderSetting volume = new SliderSetting(I0O1l0I1.b("U2VzIFNldml5ZXNp"), 100.0, 1.0, 100.0, 1.0);
   private boolean hasPlayedLoginSound = false;
   private boolean wasEnabled = false;

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public ClientSounds() {
      this.addSettings(new Setting[]{this.check, this.mode, this.volume});
   }

   @Override
   public void onEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 5;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (shouldPlayLoginSoundInternal()) {
                  if (l1O0I1lO.opaqueTrue()) {
                     this.playClientSound("login");
                     this.hasPlayedLoginSound = true;
                  }
               }
               _s = 5;
               break;

            case 2:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 3:
               l1O0I1lO.fakeHandler();
               _s = 5;
               break;

            case 4:
               l1O0I1lO.fakeBranch(entropy, _s);
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

   private int shouldPlayLoginSoundFlag() {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.bool(entropy > FAKE_STATE);
      }
      return lO1I0l1O.bool(I1lO0l1I.all(
         this.check.get(I0O1l0I1.b("Q2xpZW50IEdpcmnFn2k=")),
         I1lO0l1I.not(this.hasPlayedLoginSound),
         mc != null,
         mc.player != null,
         mc.world != null
      ));
   }

   private boolean shouldPlayLoginSoundInternal() {
      return lO1I0l1O.unbool(shouldPlayLoginSoundFlag());
   }

   @Override
   public void onEnable() {
      l1O0I1lO.fakeHandler();
      if (this.check.get(I0O1l0I1.b("TW9kw7xsIEHDpy9LYXBh"))) {
         if (l1O0I1lO.opaqueTrue()) {
            this.playClientSound("enable");
         }
      }
      this.wasEnabled = true;
      super.onEnable();
   }

   @Override
   public void onDisable() {
      l1O0I1lO.fakeHandler();
      if (I1lO0l1I.and(this.wasEnabled, this.check.get(I0O1l0I1.b("TW9kw7xsIEHDpy9LYXBh")))) {
         if (l1O0I1lO.opaqueTrue()) {
            this.playClientSound("disable");
         }
      }
      this.wasEnabled = false;
      super.onDisable();
   }

   private void playClientSound(String type) {
      l1O0I1lO.fakeHandler();
      if (l1O0I1lO.opaqueTrue()) {
         System.out.println(I0O1l0I1.b("U2VzIMOnYWzEsW7EsXlvcjogVGlwOiA=") + this.mode.get() + I0O1l0I1.b("LCBTZXMgdMO8csO8OiA=") + type + I0O1l0I1.b("LCBTZXMgZMO8emV5aTogIA==") + this.volume.get());
      }
   }
}
