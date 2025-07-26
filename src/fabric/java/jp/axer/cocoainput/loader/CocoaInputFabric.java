package jp.axer.cocoainput.loader;

import net.fabricmc.api.ClientModInitializer;
import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.util.FCConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;

public class CocoaInputFabric implements ClientModInitializer {
	public static CocoaInputFabric instance;
	public CocoaInput cocoainput;
	@Override
	public void onInitializeClient() {
		CocoaInputFabric.instance=this;
	}
	public void onWindowLaunched(){
		this.cocoainput=new CocoaInput();
		CocoaInput.LOGGER.info("Fabric config setup");
		var config = FabricLoader.getInstance().getConfigDir().resolve("cocoainput.json");
		CocoaInput.LOGGER.info("Config path:"+config.toString());
		FCConfig.init("cocoainput", config, FCConfig.class);
		CocoaInput.config=new FCConfig();
		CocoaInput.LOGGER.info("ConfigPack:"+CocoaInput.config.isAdvancedPreeditDraw()+" "+CocoaInput.config.isNativeCharTyped());
	}

	public void onChangeScreen(Screen sc){
		if(this.cocoainput==null){
			this.onWindowLaunched();
			return;
		}
		this.cocoainput.distributeScreen(sc);
	}
}
