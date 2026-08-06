package dev.just.modules;

import dev.just.modules.combat.AntiBot;
import dev.just.modules.combat.AttackAura;
import dev.just.modules.combat.AttackExtend;
import dev.just.modules.combat.AutoExplosion;
import dev.just.modules.combat.AutoPotion;
import dev.just.modules.combat.AutoSwap;
import dev.just.modules.combat.AutoTotem;
import dev.just.modules.combat.Criticals;
import dev.just.modules.combat.CrystalAura;
import dev.just.modules.combat.HitBox;
import dev.just.modules.combat.NoFriendDamage;
import dev.just.modules.combat.SelfTrap;
import dev.just.modules.combat.SuperBow;
import dev.just.modules.combat.TargetStrafe;
import dev.just.modules.combat.Velocity;
import dev.just.modules.misc.AutoDuel;
import dev.just.modules.misc.AutoDuelBot;
import dev.just.modules.misc.ClientSounds;
import dev.just.modules.misc.DeathCoords;
import dev.just.modules.misc.DiscordRCP;
import dev.just.modules.misc.ElytraHelper;
import dev.just.modules.misc.FTHelper;
import dev.just.modules.misc.Globals;
import dev.just.modules.misc.HWHelper;
import dev.just.modules.misc.IRC;
import dev.just.modules.misc.NameProtect;
import dev.just.modules.misc.NoCommands;
import dev.just.modules.misc.Optimizer;
import dev.just.modules.misc.RWHelper;
import dev.just.modules.misc.ServerRPSpoff;
import dev.just.modules.misc.TPLoot;
import dev.just.modules.misc.UnHook;
import dev.just.modules.misc.Xray;
import dev.just.modules.movement.AirStuck;
import dev.just.modules.movement.AutoSprint;
import dev.just.modules.movement.Blink;
import dev.just.modules.movement.ElytraMotion;
import dev.just.modules.movement.ElytraRecast;
import dev.just.modules.movement.ElytraTarget;
import dev.just.modules.movement.Flight;
import dev.just.modules.movement.FreeLook;
import dev.just.modules.movement.NoSlow;
import dev.just.modules.movement.NoWeb;
import dev.just.modules.movement.Phase;
import dev.just.modules.movement.Speed;
import dev.just.modules.movement.Spider;
import dev.just.modules.movement.Strafe;
import dev.just.modules.movement.SuperFirework;
import dev.just.modules.movement.Timer;
import dev.just.modules.player.AutoAccept;
import dev.just.modules.player.AutoLeave;
import dev.just.modules.player.AutoMessage;
import dev.just.modules.player.AutoRespawn;
import dev.just.modules.player.AutoTool;
import dev.just.modules.player.ChestStealer;
import dev.just.modules.player.ClickAction;
import dev.just.modules.player.CustomCoolDown;
import dev.just.modules.player.EnderChestExploit;
import dev.just.modules.player.FreeCamera;
import dev.just.modules.player.GuiWalk;
import dev.just.modules.player.HighJump;
import dev.just.modules.player.InvseeExploit;
import dev.just.modules.player.ItemFixSwap;
import dev.just.modules.player.ItemScroller;
import dev.just.modules.player.MiddleClickFriend;
import dev.just.modules.player.MiddleClickPearl;
import dev.just.modules.player.NoDelay;
import dev.just.modules.player.NoInteract;
import dev.just.modules.player.NoPush;
import dev.just.modules.player.NoRayTrace;
import dev.just.modules.player.PerfectTime;
import dev.just.modules.player.RegionExploit;
import dev.just.modules.render.Arrows;
import dev.just.modules.render.AspectRatio;
import dev.just.modules.render.BlockESP;
import dev.just.modules.render.BlockHighLight;
import dev.just.modules.render.Breadcrumbs;
import dev.just.modules.render.ClickGUI;
import dev.just.modules.render.CrossHair;
import dev.just.modules.render.ESP;
import dev.just.modules.render.ExtraTab;
import dev.just.modules.render.FullBright;
import dev.just.modules.render.HUD;
import dev.just.modules.render.ItemPhysic;
import dev.just.modules.render.JumpCircles;
import dev.just.modules.render.LittleSnickers;
import dev.just.modules.render.NameTags;
import dev.just.modules.render.NoRender;
import dev.just.modules.render.Particles;
import dev.just.modules.render.Prediction;
import dev.just.modules.render.SwingAnimations;
import dev.just.modules.render.TargetESP;
import dev.just.modules.render.TargetHUD;
import dev.just.modules.render.Trails;
import dev.just.modules.render.ViewModel;
import dev.just.modules.render.World;
import dev.just.modules.render.Zoom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FunctionManager {
   public static final List<Function> functions = new CopyOnWriteArrayList<>();
   public final ClickGUI clickGUI;
   public final Optimizer optimizer;
   public final ClientSounds clientSounds;
   public final ElytraTarget elytraTarget;
   public final SuperFirework superFirework;
   public final AttackAura attackAura;
   public final CrystalAura crystalAura;
   public final NoFriendDamage noFriendDamage;
   public final HitBox xbox;
   public final NoCommands noCommands;
   public final SwingAnimations swingAnimations;
   public final ViewModel viewModel;
   public final NoPush noPush;
   public final FreeCamera freeCamera;
   public final HUD hud;
   public final TargetESP targetESP;
   public final TargetHUD targetHUD;
   public final NoRender noRender;
   public final NameProtect nameProtect;
   public final NoInteract noInteract;
   public final ItemScroller itemScroller;
   public final NoSlow noSlow;
   public final LittleSnickers littleSnickers;
   public final UnHook unHook;
   public final FullBright fullBright;
   public final ItemPhysic itemPhysic;
   public final AutoPotion autoPotion;
   public final GuiWalk guiWalk;
   public final ExtraTab extraTab;
   public final FreeLook freeLook;
   public final AntiBot antiBot;
   public final AutoExplosion autoExplosion;
   public final Arrows arrows;
   public final CustomCoolDown customCoolDown;
   public final Blink blink;
   public final NameTags nameTags;
   public final BlockESP blockESP;
   public final ChestStealer chestStealer;
   public final AspectRatio aspectRatio;
   public final World customWorld;
   public final NoRayTrace noRayTrace;
   public final IRC irc;
   public final ClickAction clickAction;
   public final Phase phase;
   public final TargetStrafe targetStrafe;
   public final Globals globals;
   public final AutoSprint autoSprint;
   public final Speed speed;
   public final CrossHair crossHair;
   public final SelfTrap selfTrap;
   public final AutoTotem autoTotem;
   public final BlockHighLight blockHighLight;

   public FunctionManager() {
      functions.addAll(
         Arrays.asList(
            new Criticals(),
            this.targetStrafe = new TargetStrafe(),
            this.noFriendDamage = new NoFriendDamage(),
            this.autoExplosion = new AutoExplosion(),
            new AttackExtend(),
            this.attackAura = new AttackAura(),
            this.crystalAura = new CrystalAura(),
            this.selfTrap = new SelfTrap(),
            this.autoPotion = new AutoPotion(),
            this.autoTotem = new AutoTotem(),
            new AutoSwap(),
            new SuperBow(),
            this.xbox = new HitBox(),
            this.antiBot = new AntiBot(),
            new Velocity(),
            this.unHook = new UnHook(),
            this.optimizer = new Optimizer(),
            this.clientSounds = new ClientSounds(),
            new DeathCoords(),
            new ServerRPSpoff(),
            new Xray(),
            new ElytraHelper(),
            new FTHelper(),
            new HWHelper(),
            new RWHelper(),
            new AutoDuel(),
            new AutoDuelBot(),
            new DiscordRCP(),
            this.globals = new Globals(),
            this.irc = new IRC(),
            this.nameProtect = new NameProtect(),
            this.noCommands = new NoCommands(),
            this.blink = new Blink(),
            this.phase = new Phase(),
            this.autoSprint = new AutoSprint(),
            new HighJump(),
            new Flight(),
            this.elytraTarget = new ElytraTarget(),
            new ElytraRecast(),
            new ElytraMotion(),
            this.superFirework = new SuperFirework(),
            this.freeLook = new FreeLook(),
            this.speed = new Speed(),
            new Strafe(),
            new Spider(),
            new AirStuck(),
            this.noSlow = new NoSlow(),
            new Timer(),
            new NoWeb(),
            this.guiWalk = new GuiWalk(),
            new NoDelay(),
            new AutoLeave(),
            new AutoMessage(),
            this.clickAction = new ClickAction(),
            this.itemScroller = new ItemScroller(),
            new ItemFixSwap(),
            new PerfectTime(),
            this.noRayTrace = new NoRayTrace(),
            this.noPush = new NoPush(),
            new AutoRespawn(),
            new AutoTool(),
            this.freeCamera = new FreeCamera(),
            this.customCoolDown = new CustomCoolDown(),
            new MiddleClickFriend(),
            new MiddleClickPearl(),
            this.noInteract = new NoInteract(),
            this.chestStealer = new ChestStealer(),
            new EnderChestExploit(),
            new InvseeExploit(),
            new RegionExploit(),
            this.clickGUI = new ClickGUI(),
            this.hud = new HUD(),
            this.swingAnimations = new SwingAnimations(),
            this.viewModel = new ViewModel(),
            this.aspectRatio = new AspectRatio(),
            this.crossHair = new CrossHair(),
            this.fullBright = new FullBright(),
            this.customWorld = new World(),
            this.noRender = new NoRender(),
            this.blockESP = new BlockESP(),
            this.itemPhysic = new ItemPhysic(),
            this.extraTab = new ExtraTab(),
            this.arrows = new Arrows(),
            new ESP(),
            this.nameTags = new NameTags(),
            new Prediction(),
            this.blockHighLight = new BlockHighLight(),
            new AutoAccept(),
            new JumpCircles(),
            new Breadcrumbs(),
            new Trails(),
            new Particles(),
            this.targetESP = new TargetESP(),
            this.targetHUD = new TargetHUD(),
            new TPLoot(),
            this.littleSnickers = new LittleSnickers(),
            new Zoom()
         )
      );
   }

   public List<Function> getFunctions() {
      return functions;
   }

   public List<Function> getFunctions(Type category) {
      List<Function> functions = new ArrayList<>();

      for (Function function : this.getFunctions()) {
         if (function.getCategory() == category) {
            functions.add(function);
         }
      }

      return functions;
   }

   public static Function get(String name) {
      for (Function function : functions) {
         if (function != null && function.name.equalsIgnoreCase(name)) {
            return function;
         }
      }

      return null;
   }
}
