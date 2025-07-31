package jp.axer.cocoainput.loader;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.util.FCConfig;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;

@Mod("cocoainput")
public class CocoaInputForge {
	private final ModContainer modContainer;

	public CocoaInputForge(IEventBus modEventBus, ModContainer modContainer){
		modEventBus.addListener(this::setup);
		this.modContainer = modContainer;
	}

	private void setup(final FMLCommonSetupEvent event) {
		CocoaInput.setup();
		CocoaInput.LOGGER.info("NeoForge config setup");
		var config = FMLPaths.CONFIGDIR.get().resolve("cocoainput.json");
		CocoaInput.LOGGER.info("Config path:" + config);
		FCConfig.init("cocoainput", config, FCConfig.class);
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, (mc,modListScreen)->new FCConfig().getScreen(modListScreen));
		CocoaInput.config=new FCConfig();
		CocoaInput.LOGGER.info("ConfigPack:"+CocoaInput.config.isAdvancedPreeditDraw()+" "+CocoaInput.config.isNativeCharTyped());
	}
}
