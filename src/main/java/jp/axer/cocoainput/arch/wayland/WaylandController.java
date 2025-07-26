package jp.axer.cocoainput.arch.wayland;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.CocoaInputController;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.NativeLogger;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFWNativeWayland;

import java.io.IOException;
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

	public WaylandController() throws IOException {
		CocoaInput.LOGGER.info("This is Wayland Controller");
		CocoaInput.copyLibrary("libwaylandcocoainput.so", "wayland/libwaylandcocoainput.so");
		CocoaInput.LOGGER.info("Call clang initializer");
		Handle.INSTANCE.initialize(this.done_callback, this.preedit_callback, this.commit_callback,
				GLFWNativeWayland.glfwGetWaylandDisplay(),
				NativeLogger.info, NativeLogger.error, NativeLogger.debug);
		Handle.INSTANCE.unfocus();
		CocoaInput.LOGGER.info("Finished clang initializer");
		CocoaInput.LOGGER.info("WaylandController finished initialize");

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
