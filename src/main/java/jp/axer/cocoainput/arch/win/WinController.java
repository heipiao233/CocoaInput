package jp.axer.cocoainput.arch.win;
import java.io.IOException;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.arch.win.Handle.DoneCallback;
import jp.axer.cocoainput.arch.win.Handle.PreeditCallback;
import jp.axer.cocoainput.arch.win.Handle.RectCallback;
import jp.axer.cocoainput.plugin.CocoaInputController;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.NativeLogger;
import jp.axer.cocoainput.util.Rect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFWNativeWin32;

public class WinController implements CocoaInputController {

	static WinIMEOperator focusedOperator = null;

	PreeditCallback pc = (str, cursor, length) -> {
        if(focusedOperator!=null) {
            CocoaInput.LOGGER.info("marked "+str.toString()+" "+cursor+" "+length);
            focusedOperator.owner.setMarkedText(str.toString(), cursor, length);
        }
    };
	DoneCallback dc = str -> {
        if(focusedOperator!=null) {
            CocoaInput.LOGGER.info("done ("+str.toString()+")");
            focusedOperator.owner.insertText(str.toString());
        }
    };

	RectCallback rc = ret -> {
        if(focusedOperator!=null) {
            CocoaInput.LOGGER.info("Rect callback");
            Rect point = focusedOperator.owner.getRect();
			float[] buff;
			if (point == null) {
				buff = new float[]{0, 0, 0, 0};
			} else {
				buff = new float[]{point.getX(), point.getY(), point.getWidth(), point.getHeight()};
			}
			double factor = CocoaInput.getScreenScaledFactor();
			buff[0] *= factor;
			buff[1] *= factor;
			buff[2] *= factor;
			buff[3] *= factor;

			ret.write(0, buff, 0, 4);
			return 0;
        }
        return 1;
    };

	public WinController() {
		CocoaInput.LOGGER.info("This is Windows Controller");
		try {
			CocoaInput.copyLibrary("libwincocoainput.dll", "win/libwincocoainput.dll");
		} catch (IOException e) {
			CocoaInput.LOGGER.error(e.getMessage(), e);
		}
		Handle.INSTANCE.initialize(GLFWNativeWin32.glfwGetWin32Window(Minecraft.getInstance().getWindow().getWindow()), pc, dc,rc, NativeLogger.info, NativeLogger.error, NativeLogger.debug);

	}


	@Override
	public IMEOperator generateIMEOperator(IMEReceiver arg0) {
		return new WinIMEOperator(arg0);
	}
	@Override
	public void screenOpenNotify(Screen gui) {
		if (WinController.focusedOperator != null) {
			//WinIMEOperator old=WinController.focusedOperator;
			//WinController.focusedOperator=null;
			WinController.focusedOperator.setFocused(false);
		}
	}

}
