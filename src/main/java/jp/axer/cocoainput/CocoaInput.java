package jp.axer.cocoainput;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import jp.axer.cocoainput.arch.wayland.WaylandController;
import jp.axer.cocoainput.plugin.IMEReceiver;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jp.axer.cocoainput.arch.darwin.DarwinController;
import jp.axer.cocoainput.arch.dummy.DummyController;
import jp.axer.cocoainput.arch.win.WinController;
import jp.axer.cocoainput.arch.x11.X11Controller;
import jp.axer.cocoainput.plugin.CocoaInputController;
import jp.axer.cocoainput.util.ConfigPack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFW;

public class CocoaInput {
	private static CocoaInputController controller;
	public static ConfigPack config = ConfigPack.DEFAULT_CONFIG;
    public static Logger LOGGER = LogManager.getLogger("CocoaInput:Mod");
	
	public static void setup() {
		try {
			switch (GLFW.glfwGetPlatform()) {
				case GLFW.GLFW_PLATFORM_COCOA -> CocoaInput.applyController(new DarwinController());
				case GLFW.GLFW_PLATFORM_WIN32 -> CocoaInput.applyController(new WinController());
				case GLFW.GLFW_PLATFORM_X11 -> CocoaInput.applyController(new X11Controller());
				case GLFW.GLFW_PLATFORM_WAYLAND -> CocoaInput.applyController(new WaylandController());
				default -> {
					CocoaInput.LOGGER.info("CocoaInput cannot find appropriate Controller in current environment.");
					CocoaInput.applyController(new DummyController());
				}
			}
			CocoaInput.LOGGER.info("CocoaInput has been initialized.");
		} catch (IOException e) {
			CocoaInput.applyController(new DummyController());
			CocoaInput.LOGGER.error("IO Exception occurs during copying.", e);
		}
	}

	public static double getScreenScaledFactor() {
		return Minecraft.getInstance().getWindow().getGuiScale();
	}

	public static void applyController(CocoaInputController controller) {
		CocoaInput.controller = controller;
		CocoaInput.LOGGER.info("CocoaInput is now using controller:" + controller.getClass());
	}

	public static CocoaInputController getController() {
		return CocoaInput.controller;
	}

	public static void distributeScreen(Screen sc) {
		if (CocoaInput.getController() != null) {
			try {
				Field wrapper = sc.getClass().getField("cocoainput$wrapper");
				wrapper.setAccessible(true);
				if (wrapper.get(sc) instanceof IMEReceiver)
					return;
			} catch (Exception ignored) {}
			CocoaInput.getController().screenOpenNotify(sc);
		}
	}

	public static void copyLibrary(String libraryName, String libraryPath) throws IOException {
		InputStream libFile;
		libFile = CocoaInput.class.getResourceAsStream("/" + libraryPath);
		File nativeDir = new File(Minecraft.getInstance().gameDirectory.getAbsolutePath().concat("/native"));
		File copyLibFile = new File(
				Minecraft.getInstance().gameDirectory.getAbsolutePath().concat("/native/" + libraryName));
		try {
			nativeDir.mkdir();
			FileOutputStream fos = new FileOutputStream(copyLibFile);
			copyLibFile.createNewFile();
			IOUtils.copy(libFile, fos);
			fos.close();
		} catch (IOException e1) {
			CocoaInput.LOGGER.error("Attempted to copy library to ./native/" + libraryName + " but failed.");
			throw e1;
		}
		System.setProperty("jna.library.path", nativeDir.getAbsolutePath());
		CocoaInput.LOGGER.info("CocoaInput has copied library to native directory.");
	}
}
