package jp.axer.cocoainput.mixin;

import jp.axer.cocoainput.CocoaInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	 @Inject(method="setScreen",at=@At("HEAD"))
	 private void setScreen(Screen screen,CallbackInfo ci) {
		CocoaInput.openScreen(screen);
	 }
}
