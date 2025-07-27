package jp.axer.cocoainput.loader;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.util.FCConfig;
import net.fabricmc.loader.api.FabricLoader;

public class CocoaInputFabric implements ClientModInitializer {
	public static CocoaInputFabric instance;

	@Override
	public void onInitializeClient() {
		CocoaInputFabric.instance=this;
		ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
			CocoaInput.setup();
			CocoaInput.LOGGER.info("Fabric config setup");
			var config = FabricLoader.getInstance().getConfigDir().resolve("cocoainput.json");
			CocoaInput.LOGGER.info("Config path:"+config.toString());
			FCConfig.init("cocoainput", config, FCConfig.class);
			CocoaInput.config=new FCConfig();
			CocoaInput.LOGGER.info("ConfigPack:"+CocoaInput.config.isAdvancedPreeditDraw()+" "+CocoaInput.config.isNativeCharTyped());
		});
	}
}
