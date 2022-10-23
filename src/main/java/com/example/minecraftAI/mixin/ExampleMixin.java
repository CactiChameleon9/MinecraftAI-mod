package com.example.minecraftAI.mixin;

import com.example.minecraftAI.ExampleMod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(ClientPlayerEntity.class)
public class ExampleMixin {

	
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		ExampleMod.LOGGER.info("This line is printed by an example mod mixin!");
	}

	@Inject(at = @At("TAIL"), method = "tick")
	private void tick(CallbackInfo ci) {
		MinecraftClient mc = MinecraftClient.getInstance();
		ClientPlayerEntity player = mc.player;
		assert player != null;

		int wWidth = mc.getWindow().getWidth();
		int wHeight = mc.getWindow().getHeight();

		// ByteBuffer bb = ByteBuffer.allocateDirect(wWidth * wHeight * 3);
		// GL11.glReadPixels(0, 0, wWidth, wHeight, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, bb);

		// ExampleMod.LOGGER.info(bb.capacity() + "");

		// byte[] inputs = new byte[bb.capacity()];
		// for(int i = 0; i < bb.capacity(); i++){
		// 	inputs[i] = bb.get(i);
		// }

		ByteBuffer buffer = BufferUtils.createByteBuffer(wWidth * wHeight * 4);
		
		GL11.glReadPixels(0, 0, wWidth, wHeight, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);
	  
		double[] inputs = new double[buffer.capacity()];

		for(int i = 0; i < buffer.capacity(); i++){
  			inputs[i] = buffer.get(i);
		}
		
		double outputs[] = ExampleMod.net.runNetwork(inputs);
	  

		// double[] inputs = new double[4];
		// if (player.isInSneakingPose()) {inputs[0] = 1;} else {inputs[0] = -1;}
		// if (player.isCreative()) {inputs[1] = 1;} else {inputs[1] = -1;}
		// if (player.isOnFire()) {inputs[2] = 1;} else {inputs[2] = -1;}
		// if (player.isSprinting()) {inputs[3] = 1;} else {inputs[3] = -1;}


		// double[] outputs = ExampleMod.net.runNetwork(inputs);
		for (int i = 0; i < outputs.length; i++) {
			ExampleMod.LOGGER.info(outputs[i] + "");
		}

		
		if (outputs[0] > outputs[1]) {
			mc.keyboard.onKey(mc.getWindow().getHandle(), 87, 0, 1, 0);
			mc.keyboard.onKey(mc.getWindow().getHandle(), 65, 0, 0, 0);
		} else {
			mc.keyboard.onKey(mc.getWindow().getHandle(), 65, 0, 1, 0);
			mc.keyboard.onKey(mc.getWindow().getHandle(), 87, 0, 0, 0);
		}
	}

}


//This is a 2nd mod that prints out the keys pressed along with the action and modifiers

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
