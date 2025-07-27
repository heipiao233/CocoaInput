package jp.axer.cocoainput.loader;

import net.fabricmc.api.ClientModInitializer;
import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.util.FCConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;

public class CocoaInputFabric implements ClientModInitializer {
	public static CocoaInputFabric instance;
	private boolean up = false;

	@Override
	public void onInitializeClient() {
		CocoaInputFabric.instance=this;
	}

	private void onWindowLaunched(){
		this.up = true;
		CocoaInput.setup();
		CocoaInput.LOGGER.info("Fabric config setup");
		var config = FabricLoader.getInstance().getConfigDir().resolve("cocoainput.json");
		CocoaInput.LOGGER.info("Config path:"+config.toString());
		FCConfig.init("cocoainput", config, FCConfig.class);
		CocoaInput.config=new FCConfig();
		CocoaInput.LOGGER.info("ConfigPack:"+CocoaInput.config.isAdvancedPreeditDraw()+" "+CocoaInput.config.isNativeCharTyped());
	}

	public void onChangeScreen(Screen sc){
		if (!this.up){
			this.onWindowLaunched();
			return;
		}
		CocoaInput.openScreen(sc);
	}
}
