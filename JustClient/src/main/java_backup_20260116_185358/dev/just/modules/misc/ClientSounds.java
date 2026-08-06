package dev.just.modules.misc;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.protect.runtime.LogicSplit;
import java.util.Arrays;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "ClientSounds",
   desc = "Q2xpZW50IGnDp2kgw7Z6ZWwgc2VzIGVmZWt0bGVyaS4=",
   type = Type.Misc
)
public class ClientSounds extends Function {
   public final MultiSetting check = new MultiSetting(
      Strings.b("RXRraW4gT2xheWxhcg=="), Arrays.asList(Strings.b("Q2xpZW50IEdpcmnFn2k="), Strings.b("TW9kw7xsIEHDpy9LYXBh")), new String[]{Strings.b("Q2xpZW50IEdpcmnFn2k="), Strings.b("TW9kw7xsIEHDpy9LYXBh")}
   );
   public final ModeSetting mode = new ModeSetting(Strings.b("U2VzIFRpcGk="), "Tip-1", "Tip-1", "Tip-2", "Tip-3", "Tip-4");
   public final SliderSetting volume = new SliderSetting(Strings.b("U2VzIFNldml5ZXNp"), 100.0, 1.0, 100.0, 1.0);
   private boolean hasPlayedLoginSound = false;
   private boolean wasEnabled = false;

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public ClientSounds() {
      this.addSettings(new Setting[]{this.check, this.mode, this.volume});
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
               if (shouldPlayLoginSoundInternal()) {
                  if (FlowObfuscator.opaqueTrue()) {
                     this.playClientSound("login");
                     this.hasPlayedLoginSound = true;
                  }
               }
               _s = 5;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 3:
               FlowObfuscator.fakeHandler();
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

   private int shouldPlayLoginSoundFlag() {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueFalse()) {
         return NumberGuard.bool(entropy > FAKE_STATE);
      }
      return NumberGuard.bool(LogicSplit.all(
         this.check.get(Strings.b("Q2xpZW50IEdpcmnFn2k=")),
         LogicSplit.not(this.hasPlayedLoginSound),
         mc != null,
         mc.player != null,
         mc.world != null
      ));
   }

   private boolean shouldPlayLoginSoundInternal() {
      return NumberGuard.unbool(shouldPlayLoginSoundFlag());
   }

   @Override
   public void onEnable() {
      FlowObfuscator.fakeHandler();
      if (this.check.get(Strings.b("TW9kw7xsIEHDpy9LYXBh"))) {
         if (FlowObfuscator.opaqueTrue()) {
            this.playClientSound("enable");
         }
      }
      this.wasEnabled = true;
      super.onEnable();
   }

   @Override
   public void onDisable() {
      FlowObfuscator.fakeHandler();
      if (LogicSplit.and(this.wasEnabled, this.check.get(Strings.b("TW9kw7xsIEHDpy9LYXBh")))) {
         if (FlowObfuscator.opaqueTrue()) {
            this.playClientSound("disable");
         }
      }
      this.wasEnabled = false;
      super.onDisable();
   }

   private void playClientSound(String type) {
      FlowObfuscator.fakeHandler();
      if (FlowObfuscator.opaqueTrue()) {
         System.out.println(Strings.b("U2VzIMOnYWzEsW7EsXlvcjogVGlwOiA=") + this.mode.get() + Strings.b("LCBTZXMgdMO8csO8OiA=") + type + Strings.b("LCBTZXMgZMO8emV5aTogIA==") + this.volume.get());
      }
   }
}
