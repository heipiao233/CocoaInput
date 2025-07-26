package jp.axer.cocoainput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import jp.axer.cocoainput.arch.wayland.WaylandController;
import jp.axer.cocoainput.plugin.IMEReceiver;
import org.apache.commons.io.IOUtils;

import jp.axer.cocoainput.arch.darwin.DarwinController;
import jp.axer.cocoainput.arch.dummy.DummyController;
import jp.axer.cocoainput.arch.win.WinController;
import jp.axer.cocoainput.arch.x11.X11Controller;
import jp.axer.cocoainput.plugin.CocoaInputController;
import jp.axer.cocoainput.util.ConfigPack;
import jp.axer.cocoainput.util.ModLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFW;

public class CocoaInput {
	private static CocoaInputController controller;
	private static String zipsource;
	public static ConfigPack config = ConfigPack.DEFAULT_CONFIG;
	
	public CocoaInput(String loader, String zipfile) {
		ModLogger.log("Modloader:" + loader);
		CocoaInput.zipsource = zipfile;
		try {
			switch (GLFW.glfwGetPlatform()) {
				case GLFW.GLFW_PLATFORM_COCOA -> CocoaInput.applyController(new DarwinController());
				case GLFW.GLFW_PLATFORM_WIN32 -> CocoaInput.applyController(new WinController());
				case GLFW.GLFW_PLATFORM_X11 -> CocoaInput.applyController(new X11Controller());
				case GLFW.GLFW_PLATFORM_WAYLAND -> CocoaInput.applyController(new WaylandController());
				default -> {
					ModLogger.log("CocoaInput cannot find appropriate Controller in current environment.");
					CocoaInput.applyController(new DummyController());
				}
			}
			ModLogger.log("CocoaInput has been initialized.");
		} catch (IOException e) {
			CocoaInput.applyController(new DummyController());
			ModLogger.error("IO Exception occurs during copying ");
		}
	}

	public static double getScreenScaledFactor() {
		return Minecraft.getInstance().getWindow().getGuiScale();
	}

	public static void applyController(CocoaInputController controller) {
		CocoaInput.controller = controller;
		ModLogger.log("CocoaInput is now using controller:" + controller.getClass());
	}

	public static CocoaInputController getController() {
		return CocoaInput.controller;
	}

	public void distributeScreen(Screen sc) {
		if (CocoaInput.getController() != null) {
			try {
				Field wrapper = sc.getClass().getField("wrapper");
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
			ModLogger.error("Attempted to copy library to ./native/" + libraryName + " but failed.");
			throw e1;
		}
		System.setProperty("jna.library.path", nativeDir.getAbsolutePath());
		ModLogger.log("CocoaInput has copied library to native directory.");
	}
}
