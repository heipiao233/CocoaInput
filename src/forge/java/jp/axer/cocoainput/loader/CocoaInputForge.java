package jp.axer.cocoainput.loader;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.util.FCConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.ConfigScreenHandler;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod("cocoainput")
public class CocoaInputForge {

	public CocoaInputForge(){
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);

	}

	private void setup(final FMLCommonSetupEvent event) {
			CocoaInput.setup();
			CocoaInput.LOGGER.info("Forge config setup");
			var config = FMLPaths.CONFIGDIR.get().resolve("cocoainput.json");
			CocoaInput.LOGGER.info("Config path:" + config);
			FCConfig.init("cocoainput", config, FCConfig.class);
			ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, ()->new ConfigScreenHandler.ConfigScreenFactory((mc,modListScreen)->new FCConfig().getScreen(modListScreen)));
			CocoaInput.config=new FCConfig();
			CocoaInput.LOGGER.info("ConfigPack:"+CocoaInput.config.isAdvancedPreeditDraw()+" "+CocoaInput.config.isNativeCharTyped());
	}
}
