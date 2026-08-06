package dev.just.modules.player;

import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.manager.ClientManager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "CustomCoolDown",
   type = Type.Player,
   desc = "RcWfeWFsYXIgacOnaW4gw7Z6ZWwgYmVrbGVtZSBzw7xyZXNpIGF5YXJsYXI="
)
public class CustomCoolDown extends Function {
   private final MultiSetting items = new MultiSetting(
      I0O1l0I1.b("RcWfeWFsYXI="), Arrays.asList(I0O1l0I1.b("QWx0xLFuIEVsbWE="), I0O1l0I1.b("S29ybyBNZXl2ZXNp"), I0O1l0I1.b("RW5kZXIgxLBuY2lzaQ==")), new String[]{I0O1l0I1.b("QWx0xLFuIEVsbWE="), I0O1l0I1.b("S29ybyBNZXl2ZXNp"), I0O1l0I1.b("RW5kZXIgxLBuY2lzaQ==")}
   );
   private final SliderSetting appleTime = new SliderSetting(I0O1l0I1.b("QWx0xLFuIEVsbWEgU8O8cmVzaQ=="), 4.6F, 1.0, 16.0, 0.1F, () -> this.items.get(I0O1l0I1.b("QWx0xLFuIEVsbWE=")));
   private final SliderSetting pearlTime = new SliderSetting(I0O1l0I1.b("RW5kZXIgxLBuY2lzaSBTw7xyZXNp"), 13.5, 1.0, 16.0, 0.1F, () -> this.items.get(I0O1l0I1.b("RW5kZXIgxLBuY2lzaQ==")));
   private final SliderSetting horusTime = new SliderSetting(I0O1l0I1.b("S29ybyBNZXl2ZXNpIFPDvHJlc2k="), 3.5, 1.0, 16.0, 0.1F, () -> this.items.get(I0O1l0I1.b("S29ybyBNZXl2ZXNp")));
   public BooleanSetting PVPonly = new BooleanSetting(I0O1l0I1.b("U2FkZWNlIFBWUCBTxLFyYXPEsW5kYQ=="), false);
   public final Map<Item, Long> lastUseMap = new HashMap<>();

   public CustomCoolDown() {
      this.addSettings(new Setting[]{this.items, this.appleTime, this.pearlTime, this.horusTime, this.PVPonly});
   }

   @Override
   public void onEvent(Event event) {
      if (event instanceof EventUpdate && mc.player != null) {
         if (!this.PVPonly.get() || ClientManager.playerIsPVP()) {
            Iterator<Entry<Item, Long>> iterator = this.lastUseMap.entrySet().iterator();

            while (iterator.hasNext()) {
               Entry<Item, Long> entry = iterator.next();
               float delay = this.getCooldownForItem(entry.getKey()) * 1000.0F;
               if ((float)(System.currentTimeMillis() - entry.getValue()) >= delay) {
                  iterator.remove();
               }
            }

            if (mc.player.isUsingItem()) {
               ItemStack activeStack = mc.player.getActiveItem();
               Item item = activeStack.getItem();
               if (this.isItemEnabled(item) && this.lastUseMap.containsKey(item)) {
                  mc.player.clearActiveItem();
               }
            }
         }
      }
   }

   public float getCooldownForItem(Item item) {
      if (item == Items.GOLDEN_APPLE) {
         return this.appleTime.get().floatValue();
      } else if (item == Items.ENDER_PEARL) {
         return this.pearlTime.get().floatValue();
      } else {
         return item == Items.CHORUS_FRUIT ? this.horusTime.get().floatValue() : 0.0F;
      }
   }

   public boolean isItemEnabled(Item item) {
      if (item == Items.GOLDEN_APPLE) {
         return this.items.get(I0O1l0I1.b("QWx0xLFuIEVsbWE="));
      } else if (item == Items.ENDER_PEARL) {
         return this.items.get(I0O1l0I1.b("RW5kZXIgxLBuY2lzaQ=="));
      } else {
         return item == Items.CHORUS_FRUIT ? this.items.get(I0O1l0I1.b("S29ybyBNZXl2ZXNp")) : false;
      }
   }

   public void setCooldown(Item item) {
      this.lastUseMap.put(item, System.currentTimeMillis());
   }
}
