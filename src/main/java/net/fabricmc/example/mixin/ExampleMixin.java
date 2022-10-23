package net.fabricmc.example.mixin;

import net.fabricmc.example.ExampleMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(ClientPlayerEntity.class)
public class ExampleMixin {
	
	int i = 0;
	int timesPressed = 0;
	int targetPresses = 50;
	
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		ExampleMod.LOGGER.info("This line is printed by an example mod mixin!");
	}

	@Inject(at = @At("TAIL"), method = "tick")
	private void tick(CallbackInfo ci) {
		MinecraftClient mc = MinecraftClient.getInstance();
		ClientPlayerEntity player = mc.player;
		assert player != null;


		mc.keyboard.onKey(mc.getWindow().getHandle(), 87, 0, 2, 2);
	}

}


// @Mixin(Keyboard.class)
// public class ExampleMixin {

// 	@Inject(at = @At("HEAD"), method = "onKey(JIIII)V")
// 	private void init(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
		
// 		MinecraftClient mc = MinecraftClient.getInstance();
// 		ClientPlayerEntity player = mc.player;
// 		assert player != null;

// 		player.sendChatMessage(key + " is being pressed with " + action + " and " + modifiers, null);

// 		//key:
// 		/*
// 		 * W=87
// 		 * A=65
// 		 * S=83
// 		 * D=68
// 		 * 
// 		 * E=69
// 		 * 
// 		 * Space=32
// 		 * Ctrl=341
// 		 * Shift=340
// 		 * Esc=256
// 		 * 0-9=48-57
// 		 */

// 		//action:
// 		/*
// 		 * Release=0
// 		 * Press=1
// 		 * Hold=2
// 		 * 
// 		 * (so movement goes 122222222222220)
// 		 * (switching to another key while holding keeps 1st key at 2 while deals with other)
// 		 */

// 		//modifiers:
// 		/*
// 		 * None=+0
// 		 * Shift=+1
// 		 * Ctrl=+2
// 		 * Alt=+4
// 		 * Meta=+8
// 		 * 
// 		 * e.g.
// 		 * Shift+Ctrl=3
// 		 * Alt+Meta+Shift=13
// 		 * 
// 		 */
// 	}
// }
