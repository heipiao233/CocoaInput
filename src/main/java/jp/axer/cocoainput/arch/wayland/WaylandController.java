package jp.axer.cocoainput.arch.wayland;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.CocoaInputController;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWNativeWayland;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

public class WaylandController implements CocoaInputController {

	static WaylandIMEOperator focusedOperator = null;
	String toBePreedit = "";
	int preeditBefore = 0, preeditAfter = 0;
	String toBeCommit = "";

	Handle.PreeditCallback preedit_callback = (String str, int before, int after) -> {
        if (WaylandController.focusedOperator != null) {
			toBePreedit = Objects.requireNonNullElse(str, "");
			preeditBefore = before;
			preeditAfter = after;
        }
    };

	Handle.CommitCallback commit_callback = (str) -> {
		if (WaylandController.focusedOperator != null) {
			toBeCommit = Objects.requireNonNullElse(str, "");
		}
	};

	Handle.DoneCallback done_callback = () -> {
        if (WaylandController.focusedOperator != null) {
			if(!toBePreedit.isEmpty()) focusedOperator.owner.setMarkedText(toBePreedit, preeditBefore, preeditAfter);
			if(!toBeCommit.isEmpty() || toBePreedit.isEmpty()) focusedOperator.owner.insertText(toBeCommit);
			preeditBefore = 0;
			preeditAfter = 0;
			toBePreedit = "";
			toBeCommit = "";
        }
    };

	private static final long window = Minecraft.getInstance().getWindow().getWindow();

	public WaylandController() throws IOException {
		Logger.log("This is Wayland Controller");
		CocoaInput.copyLibrary("libwaylandcocoainput.so", "wayland/libwaylandcocoainput.so");
		Logger.log("Call clang initializer");
		Handle.INSTANCE.initialize(this.done_callback, this.preedit_callback, this.commit_callback,
				GLFWNativeWayland.glfwGetWaylandDisplay(),
				Logger.clangLog, Logger.clangError, Logger.clangDebug);
		Handle.INSTANCE.unfocus();
		Logger.log("Finished clang initializer");
		Logger.log("WaylandController finished initialize");

	}

	@Override
	public IMEOperator generateIMEOperator(IMEReceiver arg0) {
		return new WaylandIMEOperator(arg0);
	}

	@Override
	public void screenOpenNotify(Screen gui) {
		if (WaylandController.focusedOperator != null) {
			WaylandController.focusedOperator.setFocused(false);
			WaylandController.focusedOperator = null;
		}
	}

}
